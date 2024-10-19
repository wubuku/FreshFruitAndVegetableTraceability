package org.dddml.ffvtraceability.domain;

import java.util.*;

public class ConsistentEqualityMap implements Map<String, Object> {
    private final Map<String, Object> internalMap;

    public ConsistentEqualityMap(Map<String, Object> map) {
        this.internalMap = new HashMap<>(map);
    }

    public ConsistentEqualityMap() {
        this.internalMap = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Map))
            return false;
        Map<?, ?> that = (Map<?, ?>) o;
        if (this.size() != that.size())
            return false;
        for (Entry<String, Object> entry : this.entrySet()) {
            if (!that.containsKey(entry.getKey()))
                return false;
            if (!deepEquals(entry.getValue(), that.get(entry.getKey())))
                return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean deepEquals(Object o1, Object o2) {
        if (o1 == o2)
            return true;
        if (o1 == null || o2 == null)
            return false;

        if (o1 instanceof Map && o2 instanceof Map) {
            return new ConsistentEqualityMap((Map<String, Object>) o1).equals(o2);
        }
        if (o1 instanceof List && o2 instanceof List) {
            List<?> l1 = (List<?>) o1;
            List<?> l2 = (List<?>) o2;
            if (l1.size() != l2.size())
                return false;
            for (int i = 0; i < l1.size(); i++) {
                if (!deepEquals(l1.get(i), l2.get(i)))
                    return false;
            }
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return Arrays.deepEquals(new Object[] { o1 }, new Object[] { o2 });
        }
        return Objects.equals(o1, o2);
    }

    @Override
    public int hashCode() {
        return deepHashCode(this);
    }

    private int deepHashCode(Object o) {
        if (o instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) o;
            int result = 0;
            for (Entry<?, ?> entry : map.entrySet()) {
                result += deepHashCode(entry.getKey()) ^ deepHashCode(entry.getValue());
            }
            return result;
        }
        if (o instanceof List) {
            List<?> list = (List<?>) o;
            int result = 1;
            for (Object element : list) {
                result = 31 * result + deepHashCode(element);
            }
            return result;
        }
        if (o != null && o.getClass().isArray()) {
            return Arrays.deepHashCode(new Object[] { o });
        }
        return Objects.hashCode(o);
    }

    @Override
    public int size() {
        return internalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return internalMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (Object v : internalMap.values()) {
            if (deepEquals(v, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        return internalMap.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return internalMap.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return internalMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        internalMap.putAll(m);
    }

    @Override
    public void clear() {
        internalMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return internalMap.keySet();
    }

    @Override
    public Collection<Object> values() {
        return internalMap.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return internalMap.entrySet();
    }

}