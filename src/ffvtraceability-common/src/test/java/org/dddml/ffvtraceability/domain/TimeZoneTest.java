package org.dddml.ffvtraceability.domain;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Set;


public class TimeZoneTest {

    public static void main(String[] args) {
        // 1. 获取所有可用的时区ID
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();

        // 2. 检查 Vancouver 是否在其中
        boolean hasVancouver = availableZoneIds.contains("America/Vancouver");
        System.out.println("Supports America/Vancouver: " + hasVancouver);

        try {
            // 3. 尝试创建 Vancouver 时区
            ZoneId vancouverZone = ZoneId.of("America/Vancouver");
            System.out.println("Vancouver ZoneId: " + vancouverZone);
        } catch (Exception e) {
            System.out.println("Error creating Vancouver zone: " + e.getMessage());
        }

        // 创建一个 OffsetDateTime
        OffsetDateTime dateTime = OffsetDateTime.now();

        // 方式1：使用预定义的格式化器
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/M/d HH:mm:ss");
        String formatted1 = dateTime.format(formatter1);  // 2023/6/1 00:00:00

        // 方式2：自定义格式化器并指定时区
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss")
                .withZone(ZoneId.of("Asia/Shanghai"));  // 指定上海时区
        String formatted2 = dateTime.format(formatter2);  // 2023-6-1 00:00:00

        // 方式3：先转换时区再格式化
        OffsetDateTime shanghaiTime = dateTime.atZoneSameInstant(ZoneId.of("Asia/Shanghai"))
                .toOffsetDateTime();
        String formatted3 = shanghaiTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        System.out.println("Format 1: " + formatted1);
        System.out.println("Format 2: " + formatted2);
        System.out.println("Format 3: " + formatted3);

        // 创建两个表示相同时刻但时区不同的 OffsetDateTime
        OffsetDateTime time1 = OffsetDateTime.of(2024, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime time2 = OffsetDateTime.of(2024, 1, 1, 20, 0, 0, 0, ZoneOffset.ofHours(8));

        // 打印两个时间
        System.out.println("time1: " + time1);
        System.out.println("time2: " + time2);

        // 比较是否相等
        System.out.println("equals result: " + time1.equals(time2));

        System.out.println("isEquals result: " + time1.isEqual(time2));

        // 额外检查它们是否表示相同的时刻
        System.out.println("Same instant (toInstant comparison): " +
                time1.toInstant().equals(time2.toInstant()));

        System.out.println("Test truncate");
        time1 = OffsetDateTime.now();
        time2 = OffsetDateTime.now();

        boolean isEqual = time1.truncatedTo(ChronoUnit.SECONDS)
                .isEqual(time2.truncatedTo(ChronoUnit.SECONDS));
        System.out.println("isEquals result: " + isEqual);
        boolean isExactlyEqual = time1.truncatedTo(ChronoUnit.SECONDS)
                .equals(time2.truncatedTo(ChronoUnit.SECONDS));
        System.out.println("equals result: " + isExactlyEqual);


        OffsetTime ot1 = OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC);
        OffsetTime ot2 = OffsetTime.of(20, 0, 0, 0, ZoneOffset.ofHours(8));
        // equals() 会返回 false，因为偏移量不同
        System.out.println("equals result: " + ot1.equals(ot2));
        // isEqual() 会返回 true，因为表示相同时刻
        System.out.println("isEqual result: " + ot1.isEqual(ot2));
    }

}