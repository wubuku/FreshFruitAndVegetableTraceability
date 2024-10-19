package org.dddml.ffvtraceability.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class ConsistentEqualityMapTest {

    @Test
    void testEqualsAndHashCode() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key1", "value1");
        map1.put("key2", Arrays.asList(1, 2, 3));
        map1.put("key3", new HashMap<String, Object>() {{
            put("nestedKey", "nestedValue");
        }});

        Map<String, Object> map2 = new HashMap<>(map1);

        ConsistentEqualityMap cem1 = new ConsistentEqualityMap(map1);
        ConsistentEqualityMap cem2 = new ConsistentEqualityMap(map2);

        assertEquals(cem1, cem2);
        assertEquals(cem1.hashCode(), cem2.hashCode());

        // Test with different order
        Map<String, Object> map3 = new LinkedHashMap<>();
        map3.put("key3", new HashMap<String, Object>() {{
            put("nestedKey", "nestedValue");
        }});
        map3.put("key1", "value1");
        map3.put("key2", Arrays.asList(1, 2, 3));

        ConsistentEqualityMap cem3 = new ConsistentEqualityMap(map3);
        assertEquals(cem1, cem3);
        assertEquals(cem1.hashCode(), cem3.hashCode());
    }

    @Test
    void testDeepEquality() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key1", Arrays.asList(1, 2, 3));
        map1.put("key2", new int[]{4, 5, 6});

        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put("key1", Arrays.asList(1, 2, 3));
        map2.put("key2", new int[]{4, 5, 6});

        ConsistentEqualityMap cem1 = new ConsistentEqualityMap(map1);
        ConsistentEqualityMap cem2 = new ConsistentEqualityMap(map2);

        assertEquals(cem1, cem2);
        assertEquals(cem1.hashCode(), cem2.hashCode());
    }

    @Test
    void testNotEqual() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key1", "value1");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("key1", "value2");

        ConsistentEqualityMap cem1 = new ConsistentEqualityMap(map1);
        ConsistentEqualityMap cem2 = new ConsistentEqualityMap(map2);

        assertNotEquals(cem1, cem2);
        assertNotEquals(cem1.hashCode(), cem2.hashCode());
    }

    @Test
    void testBasicOperations() {
        ConsistentEqualityMap cem = new ConsistentEqualityMap();
        assertTrue(cem.isEmpty());
        assertEquals(0, cem.size());

        cem.put("key1", "value1");
        assertFalse(cem.isEmpty());
        assertEquals(1, cem.size());
        assertTrue(cem.containsKey("key1"));
        assertTrue(cem.containsValue("value1"));
        assertEquals("value1", cem.get("key1"));

        cem.remove("key1");
        assertTrue(cem.isEmpty());
        assertEquals(0, cem.size());
    }
}
