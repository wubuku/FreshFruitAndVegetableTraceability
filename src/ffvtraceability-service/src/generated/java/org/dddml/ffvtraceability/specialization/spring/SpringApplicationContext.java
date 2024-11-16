package org.dddml.ffvtraceability.specialization.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.dddml.ffvtraceability.specialization.ApplicationContext;
import org.dddml.ffvtraceability.specialization.ClobConverter;
import org.dddml.ffvtraceability.specialization.TypeConverter;
import org.dddml.ffvtraceability.specialization.json.JacksonClobConverter;

/**
 * Created by Yang on 2016/7/28.
 */
public class SpringApplicationContext extends ApplicationContext {

    protected static final ClobConverter DEFAULT_CLOB_CONVERTER = new JacksonClobConverter();
    protected static final JacksonTypeConverter DEFAULT_JACKSON_TYPE_CONVERTER = new JacksonTypeConverter();
    private org.springframework.context.ApplicationContext innerApplicationContext;

    public SpringApplicationContext(org.springframework.context.ApplicationContext innerApplicationContext) {
        this.innerApplicationContext = innerApplicationContext;
    }

    @Override
    public Object get(String name) {
        int len = 1;
        if (name.startsWith("_")) {
            len = 2;
        }
        String camelName = name.substring(0, len).toLowerCase() + name.substring(len);
        if (innerApplicationContext.containsBean(camelName)) {
            Object obj = innerApplicationContext.getBean(camelName);
            return obj;
        } else {
            if (innerApplicationContext.containsBean(name)) {
                return innerApplicationContext.getBean(name);
            } else {
                return null;
            }
        }
    }

    @Override
    public <T> T get(final Class<T> type) {
        return innerApplicationContext.getBean(type);
    }

    @Override
    public ClobConverter getClobConverter() {
        ClobConverter clobConverter = (ClobConverter) get("clobConverter");
        if (clobConverter == null) {
            return DEFAULT_CLOB_CONVERTER;
        }
        return clobConverter;
    }

    @Override
    public TypeConverter getTypeConverter() {
        return DEFAULT_JACKSON_TYPE_CONVERTER;
    }

    public static class JacksonTypeConverter implements TypeConverter {
        private static final ObjectMapper objectMapper;

        static {
            objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule())
                    .setDateFormat(new StdDateFormat().withColonInTimeZone(true))
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                    .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        }

        @Override
        public Object convertFromString(Class<?> type, String text) {
            try {
                return objectMapper.readValue(text, type);
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert from JSON string to " + type.getName(), e);
            }
        }

        @Override
        public String convertToString(Class<?> type, Object value) {
            try {
                return objectMapper.writeValueAsString(value);
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert object to JSON string", e);
            }
        }

        @Override
        public String convertToString(Object value) {
            try {
                return objectMapper.writeValueAsString(value);
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert object to JSON string", e);
            }
        }

        @Override
        public String[] convertToStringArray(Object[] values) {
            if (values == null) {
                return null;
            }
            String[] result = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = convertToString(values[i]);
            }
            return result;
        }

        @Override
        public <T> T convertValue(Object fromValue, Class<T> toValueType) {
            return objectMapper.convertValue(fromValue, toValueType);
        }
    }

}
