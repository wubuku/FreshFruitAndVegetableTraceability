package org.dddml.ffvtraceability.domain.util;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.UUID;

public class IdUtils {

    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
            'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z'
    };

    /**
     * Converts date and time to a 6-character base-34 string.
     * Format breakdown:
     * - 1 char for year (0 represents 2024, cycles back to 0 after 33)
     * - 1 char for month
     * - 1 char for day
     * - 1 char for hour
     * - 2 chars for minutes*seconds
     * Pads with zeros if necessary.
     */
    public static String base34DateTime6(int year, int month, int day, int hour, int minute, int second) {
        // Convert year (0 represents 2024)
        int yearOffset = year - 2024;
        while (yearOffset >= 34) {
            yearOffset -= 34;
        }
        String yearStr = toBase34(yearOffset);

        // Convert month, day, and hour to single base-34 chars, ensuring valid ranges
        String monthStr = toBase34(Math.min(month, 33));
        String dayStr = toBase34(Math.min(day, 33));
        String hourStr = toBase34(Math.min(hour, 33));

        // Convert minutes*seconds to two base-34 chars (max value: 1155)
        int minuteSeconds = (minute * 60 + second) % 1156;
        String minuteSecondsStr = toBase34(minuteSeconds);
        if (minuteSecondsStr.length() < 2) {
            minuteSecondsStr = "0" + minuteSecondsStr;
        } else if (minuteSecondsStr.length() > 2) {
            minuteSecondsStr = minuteSecondsStr.substring(0, 2); // should not happen
        }

        return yearStr + monthStr + dayStr + hourStr + minuteSecondsStr;
    }

    /**
     * Converts an integer to a base-34 string representation.
     * Uses digits 0-9 and A-Z (excluding I, O) to represent values 0-33.
     */
    public static String toBase34(int number) {
        if (number == 0) {
            return "0";
        }

        StringBuilder result = new StringBuilder();
        int num = Math.abs(number); // handle negative numbers

        while (num > 0) {
            result.insert(0, DIGITS[num % 34]);
            num = num / 34;
        }

        return number < 0 ? "-" + result.toString() : result.toString();
    }

    public static String randomId() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();
        // System.out.println("UUID: " + uuid);
        // Remove the "-" symbols from the UUID
        String hex = uuid.toString().replace("-", "");
        // Split the hex string into two substrings
        String hex1 = hex.substring(0, 16);
        String hex2 = hex.substring(16, 32);
        // Convert the substrings into long values
        BigInteger num1 = new BigInteger(hex1, 16);
        BigInteger num2 = new BigInteger(hex2, 16);
        // XOR the two values
        BigInteger xor = num1.xor(num2);
        // Convert BigInteger to base34
        StringBuilder result = new StringBuilder();
        BigInteger base = BigInteger.valueOf(34);
        BigInteger xorAbs = xor.abs(); // Handle negative values

        do {
            BigInteger[] divideAndRemainder = xorAbs.divideAndRemainder(base);
            result.insert(0, DIGITS[divideAndRemainder[1].intValue()]);
            xorAbs = divideAndRemainder[0];
        } while (!xorAbs.equals(BigInteger.ZERO));

        // Truncate to 12 digits if longer, pad with zeros if shorter
        if (result.length() > 12) {
            result = new StringBuilder(result.substring(0, 12));
        } else {
            while (result.length() < 12) {
                result.insert(0, '0');
            }
        }

        // Add current timestamp as prefix
        OffsetDateTime now = OffsetDateTime.now();
        String dateTimeStr = base34DateTime6(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(),
                now.getMinute(), now.getSecond());
        // System.out.println(dateTimeStr);
        return dateTimeStr + result.toString();
    }

//    // Only for test
//    public static void main(String[] args) {
//        String s1 = randomId();
//        System.out.println(s1);
//        String s2 = randomId();
//        System.out.println(s2);
//        // System.out.println(s1.compareTo(s2));
//        // 0BC0PGLUU73XVJDA8U
//        System.out.println(s1.length()); // 18
//        System.out.println(s2.length());
//    }
}
