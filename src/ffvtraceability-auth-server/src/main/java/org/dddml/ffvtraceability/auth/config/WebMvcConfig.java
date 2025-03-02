package org.dddml.ffvtraceability.auth.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.dddml.ffvtraceability.auth.jackson.CustomJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    @Primary
    public ObjectMapper oauth2ObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.registerModule(new CustomJacksonModule());
        return objectMapper;
    }

    @Bean(name = "restApiObjectMapper")
    public ObjectMapper restApiObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册 Java 8 时间模块（支持 LocalDateTime 等）
        objectMapper.registerModule(new JavaTimeModule());
        // 禁用反序列化时未知属性的警告
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 禁用日期时间戳格式（使用 ISO-8601 格式）
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // 禁用空 Bean 序列化失败
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                // 禁用默认的类型信息（防止安全漏洞）
                .setDefaultTyping(null);  // 关键：禁用类型信息
        return objectMapper;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 移除所有现有的 JSON 转换器
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);

        // 添加我们的 REST API 转换器
        MappingJackson2HttpMessageConverter restApiConverter = new MappingJackson2HttpMessageConverter(restApiObjectMapper());
        converters.add(0, restApiConverter);  // 添加到第一位，确保优先使用
    }
}

