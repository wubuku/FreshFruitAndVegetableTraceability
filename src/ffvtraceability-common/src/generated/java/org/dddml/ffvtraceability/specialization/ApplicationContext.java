package org.dddml.ffvtraceability.specialization;


/**
 * Created by Yang on 2016/7/20.
 */
public class ApplicationContext {

    protected static final TypeConverter DEFAULT_TYPE_CONVERTER = new DefaultTypeConverter();

    protected static final TimestampService DEFAULT_TIMESTAMP_SERVICE = new TimestampService() {
        @Override
        public Object now(Class<?> type) {
            if (type.equals(java.sql.Timestamp.class)) {
                return new java.sql.Timestamp(new java.util.Date().getTime());
            } else if (type.equals(java.time.OffsetDateTime.class)) {
                return java.time.OffsetDateTime.now();
            } else if (type.equals(java.time.ZonedDateTime.class)) {
                return java.time.ZonedDateTime.now();
            } else if (type.equals(java.util.Date.class)) {
                return new java.util.Date();
            } else if (type.equals(Long.class)) {
                return System.currentTimeMillis();
            } else {
                throw new IllegalArgumentException("Unknown type: " + type);
            }
        }
    };

    public static volatile ApplicationContext current;

    private final ThreadLocal<String> requesterIdHolder = new ThreadLocal<>();

    public Object get(String name) {
        throw new UnsupportedOperationException();
    }

    public <T> T get(Class<T> type) {
        throw new UnsupportedOperationException();
    }

    public TypeConverter getTypeConverter() {
        return DEFAULT_TYPE_CONVERTER;
    }

    public TimestampService getTimestampService() {
        return DEFAULT_TIMESTAMP_SERVICE;
    }

    public String getRequesterId() {
        return requesterIdHolder.get();
    }

    public void setRequesterId(String requesterId) {
        if (requesterId == null) {
            requesterIdHolder.remove();
        } else {
            requesterIdHolder.set(requesterId);
        }
    }

    public void clearRequesterId() {
        requesterIdHolder.remove();
    }

    public ClobConverter getClobConverter() {
        throw new UnsupportedOperationException();//return (ClobConverter) get("clobConverter");
    }

    public static class DefaultTypeConverter implements TypeConverter {

        private org.dddml.support.criterion.DefaultTypeConverter innerTypeConverter = new org.dddml.support.criterion.DefaultTypeConverter();

        @Override
        public Object convertFromString(Class<?> type, String text) {
            if (type.equals(java.time.OffsetDateTime.class)) {
                return java.time.OffsetDateTime.parse(text);
            }
            if (type.equals(java.time.ZonedDateTime.class)) {
                return java.time.ZonedDateTime.parse(text);
            }
            if (type.equals(java.time.LocalDateTime.class)) {
                return java.time.LocalDateTime.parse(text);
            }
            if (type.equals(java.time.LocalDate.class)) {
                return java.time.LocalDate.parse(text);
            }
            if (type.equals(java.time.LocalTime.class)) {
                return java.time.LocalTime.parse(text);
            }
            if (type.equals(java.time.OffsetTime.class)) {
                return java.time.OffsetTime.parse(text);
            }
            return innerTypeConverter.convertFromString(type, text);
        }

        @Override
        public String convertToString(Object value) {
            return innerTypeConverter.convertToString(value);
        }

        @Override
        public String convertToString(Class<?> type, Object value) {
            return innerTypeConverter.convertToString(value);
        }

        @Override
        public String[] convertToStringArray(Object[] values) {
            return innerTypeConverter.convertToStringArray(values);
        }

        @Override
        public <T> T convertValue(Object fromValue, Class<T> toValueType) {
            throw new UnsupportedOperationException();
        }

    }

}
