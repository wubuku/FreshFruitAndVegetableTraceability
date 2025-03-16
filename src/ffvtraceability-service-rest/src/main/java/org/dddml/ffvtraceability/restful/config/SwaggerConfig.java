package org.dddml.ffvtraceability.restful.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfig {

    private static final String HEADER_X_TENANT_ID = "X-TenantID";

    @Bean
    public OperationCustomizer addGlobalHeader() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            HeaderParameter headerParam = (HeaderParameter) new HeaderParameter()
                    .name(HEADER_X_TENANT_ID)
                    .description("租户Id")
                    .required(false)
                    .schema(new StringSchema().example("X"));
            operation.addParametersItem(headerParam);
            return operation;
        };
    }

//    @Bean //这种做法没起作用，有时间再研究
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components().addHeaders(HEADER_X_TENANT_ID,
//                        new Header().
//                                required(true).
//                                schema(new StringSchema().example("X")).
//                                description("租户Id"))
//                );
//    }
}
