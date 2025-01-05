package org.dddml.ffvtraceability.domain.util;

import java.util.Arrays;
import java.util.Set;

public class TelecomNumberUtil {
    private TelecomNumberUtil() {
    }

    /**
     * 解析电话号码
     * 1. 对于 E.123 格式 (如 "+86 10 1234 5678"):
     * countryCode: "86"
     * areaCode: "10"
     * contactNumber: "12345678"
     * <p>
     * 2. 对于不带+号但有空格分隔的号码 (如 "010 12345678"):
     * countryCode: null
     * areaCode: "010"
     * contactNumber: "12345678"
     * <p>
     * 3. 对于无分隔符的号码，尝试按规则解析
     *
     * @param phoneNumber 电话号码字符串
     * @return 包含解析结果的 TelecomNumberDto
     */
    public static TelecomNumberDto parse(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return new TelecomNumberDto(null, null, null);
        }

        String cleanNumber = phoneNumber.trim();

        // 检查是否包含空格作为分隔符
        if (cleanNumber.contains(" ")) {
            return parseWithSpaces(cleanNumber);
        }

        // 对于带+号但无空格的号码
        if (cleanNumber.startsWith("+")) {
            return parseE123WithoutSpaces(cleanNumber);
        }

        // 对于不带+号且无空格的号码
        return parseAreaCodeAndNumber(null, cleanNumber);
    }

    private static TelecomNumberDto parseWithSpaces(String number) {
        if (number == null || number.isEmpty()) {
            return new TelecomNumberDto(null, null, null);
        }

        String[] parts = number.split("\\s+");
        if (parts.length == 0) {
            return new TelecomNumberDto(null, null, number);
        }

        // 处理带+号的格式
        if (parts[0].startsWith("+")) {
            String countryCode = parts[0].length() > 1 ? parts[0].substring(1) : "";

            // 特殊处理北美格式
            if ("1".equals(countryCode) && parts.length >= 2) {
                String areaCode = parts[1];
                // 确保区号是3位数字且首位不为0或1
                if (isValidNorthAmericanAreaCode(areaCode)) {
                    String contactNumber = parts.length > 2 ?
                            String.join("", Arrays.copyOfRange(parts, 2, parts.length)) : "";
                    if (isValidNorthAmericanContactNumber(contactNumber)) {
                        return new TelecomNumberDto(countryCode, areaCode, contactNumber);
                    }
                }
            }

            // 非北美格式或格式不正确时的默认处理
            String areaCode = parts.length > 1 ? parts[1] : null;
            String contactNumber = parts.length > 2 ?
                    String.join("", Arrays.copyOfRange(parts, 2, parts.length)) : null;
            return new TelecomNumberDto(countryCode, areaCode, contactNumber);
        }

        // 不带+号的格式
        String areaCode = parts[0];
        String contactNumber = parts.length > 1 ?
                String.join("", Arrays.copyOfRange(parts, 1, parts.length)) : null;
        return new TelecomNumberDto(null, areaCode, contactNumber);
    }

    private static TelecomNumberDto parseE123WithoutSpaces(String number) {
        if (number == null || number.length() <= 1) {
            return new TelecomNumberDto(null, null, number);
        }

        // 移除+号
        String cleanNumber = number.substring(1);
        if (cleanNumber.isEmpty()) {
            return new TelecomNumberDto(null, null, number);
        }

        // 处理北美格式
        if (cleanNumber.startsWith("1")) {
            return parseNorthAmericanNumber(cleanNumber);
        }

        // 处理其他格式
        return parseDefaultFormat(cleanNumber);
    }

    private static TelecomNumberDto parseNorthAmericanNumber(String cleanNumber) {
        String countryCode = "1";
        if (cleanNumber.length() == 1) {
            return new TelecomNumberDto(countryCode, null, null);
        }

        String remaining = cleanNumber.substring(1);
        // 标准北美格式：10位数字（3位区号 + 7位号码）
        if (remaining.length() == 10 && isValidNorthAmericanAreaCode(remaining.substring(0, 3))) {
            return new TelecomNumberDto(countryCode,
                    remaining.substring(0, 3),
                    remaining.substring(3));
        }

        // 非标准格式的基本解析
        return new TelecomNumberDto(countryCode,
                remaining.length() >= 3 ? remaining.substring(0, 3) : remaining,
                remaining.length() > 3 ? remaining.substring(3) : null);
    }

    private static TelecomNumberDto parseDefaultFormat(String cleanNumber) {
        if (cleanNumber.length() < 2) {
            return new TelecomNumberDto(cleanNumber, null, null);
        }

        // 尝试识别3位国家代码
        if (cleanNumber.length() >= 3 && isValid3DigitCountryCode(cleanNumber.substring(0, 3))) {
            String countryCode = cleanNumber.substring(0, 3);
            String remaining = cleanNumber.length() > 3 ? cleanNumber.substring(3) : "";
            return parseAreaCodeAndNumber(countryCode, remaining);
        }

        // 尝试识别2位国家代码
        String potentialCountryCode = cleanNumber.substring(0, 2);
        if (isValid2DigitCountryCode(potentialCountryCode)) {
            String remaining = cleanNumber.length() > 2 ? cleanNumber.substring(2) : "";
            return parseAreaCodeAndNumber(potentialCountryCode, remaining);
        }

        // 如果无法识别国家代码，按默认规则处理
        String countryCode = cleanNumber.substring(0, 2);
        String remaining = cleanNumber.length() > 2 ? cleanNumber.substring(2) : "";
        return parseAreaCodeAndNumber(countryCode, remaining);
    }

    private static TelecomNumberDto parseAreaCodeAndNumber(String countryCode, String remaining) {
        if (remaining == null || remaining.isEmpty()) {
            return new TelecomNumberDto(countryCode, null, null);
        }

        if (remaining.length() <= 3) {
            return new TelecomNumberDto(countryCode, remaining, null);
        }

        return new TelecomNumberDto(countryCode,
                remaining.substring(0, 3),
                remaining.substring(3));
    }

    private static boolean isValidNorthAmericanAreaCode(String areaCode) {
        return areaCode != null
                && areaCode.length() == 3
                && Character.isDigit(areaCode.charAt(0))
                && areaCode.charAt(0) != '0'
                && areaCode.charAt(0) != '1';
    }

    private static boolean isValidNorthAmericanContactNumber(String contactNumber) {
        return contactNumber != null && contactNumber.length() == 7;
    }

    // 验证3位国家代码
    private static boolean isValid3DigitCountryCode(String code) {
        // 根据ITU-T E.164标准验证3位国家代码
        Set<String> valid3DigitCodes = Set.of(
                "852", // 香港
                "853", // 澳门
                "855", // 柬埔寨
                "886", // 台湾
                "880", // 孟加拉
                "856", // 老挝
                "870"  // 国际海事卫星组织
                // ... 可以添加更多
        );
        return valid3DigitCodes.contains(code);
    }

    // 验证2位国家代码
    private static boolean isValid2DigitCountryCode(String code) {
        // 根据ITU-T E.164标准验证2位国家代码
        Set<String> valid2DigitCodes = Set.of(
                "33", // 法国
                "44", // 英国
                "49", // 德国
                "61", // 澳大利亚
                "81", // 日本
                "82", // 韩国
                "86", // 中国
                "91", // 印度
                "20", // 埃及
                "27", // 南非
                "30", // 希腊
                "31", // 荷兰
                "32", // 比利时
                "34", // 西班牙
                "36", // 匈牙利
                "39", // 意大利
                "41", // 瑞士
                "43", // 奥地利
                "45", // 丹麦
                "46", // 瑞典
                "47", // 挪威
                "48", // 波兰
                "51", // 秘鲁
                "52", // 墨西哥
                "54", // 阿根廷
                "55", // 巴西
                "56", // 智利
                "57", // 哥伦比亚
                "58", // 委内瑞拉
                "60", // 马来西亚
                "62", // 印度尼西亚
                "63", // 菲律宾
                "64", // 新西兰
                "65", // 新加坡
                "66", // 泰国
                "84", // 越南
                "90", // 土耳其
                "92", // 巴基斯坦
                "93", // 阿富汗
                "94", // 斯里兰卡
                "95", // 缅甸
                "98"  // 伊朗
                // ... 可以添加更多
        );
        return valid2DigitCodes.contains(code);
    }

    public static class TelecomNumberDto {
        private final String countryCode;
        private final String areaCode;
        private final String contactNumber;

        public TelecomNumberDto(String countryCode, String areaCode, String contactNumber) {
            this.countryCode = countryCode;
            this.areaCode = areaCode;
            this.contactNumber = contactNumber;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        /**
         * 将电话号码转换为标准格式的字符串
         * 如果有国家代码，添加"+"前缀
         * 各部分之间用空格分隔
         *
         * @return 格式化的电话号码字符串
         */
        public String format() {
            StringBuilder sb = new StringBuilder();

            // 添加国家代码（如果存在）
            if (countryCode != null && !countryCode.isEmpty()) {
                sb.append("+").append(countryCode).append(" ");
            }

            // 添加区号（如果存在）
            if (areaCode != null && !areaCode.isEmpty()) {
                sb.append(areaCode);
                // 只有在后面还有号码的情况下才添加空格
                if (contactNumber != null && !contactNumber.isEmpty()) {
                    sb.append(" ");
                }
            }

            // 添加电话号码（如果存在）
            if (contactNumber != null && !contactNumber.isEmpty()) {
                sb.append(contactNumber);
            }

            return sb.toString().trim();
        }
    }
}