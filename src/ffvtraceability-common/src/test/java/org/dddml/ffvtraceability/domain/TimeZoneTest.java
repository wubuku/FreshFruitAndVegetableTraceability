package org.dddml.ffvtraceability.domain;

import java.time.ZoneId;
import java.util.Set;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;


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
    }

}