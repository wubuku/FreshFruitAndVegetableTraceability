package org.dddml.ffvtraceability.domain;

import java.time.ZoneId;
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
    }

}