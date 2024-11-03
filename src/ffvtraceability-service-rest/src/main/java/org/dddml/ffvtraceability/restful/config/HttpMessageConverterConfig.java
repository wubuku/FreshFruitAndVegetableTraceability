package org.dddml.ffvtraceability.restful.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class HttpMessageConverterConfig implements WebMvcConfigurer {
    @Override
    public void extendMessageConverters(@NonNull List<HttpMessageConverter<?>> converters) {
        // Remove StringHttpMessageConverter to ensure all String responses are
        // JSON-formatted.
        // By default, Spring MVC uses StringHttpMessageConverter for String returns,
        // after removal it falls back to JSON converter (Jackson).
        converters.removeIf(StringHttpMessageConverter.class::isInstance);
    }
}