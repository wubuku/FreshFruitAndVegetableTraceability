package org.dddml.ffvtraceability.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.dddml.ffvtraceability.domain.attributesetinstance.*;
import org.dddml.ffvtraceability.specialization.IdGenerator;

@Configuration
public class IdGeneratorConfig {

    @Bean
    public IdGenerator<String, AttributeSetInstanceCommand.CreateAttributeSetInstance, AttributeSetInstanceState> attributeSetInstanceIdGenerator() {
        return new AttributeSetInstanceIdGenerator();
    }
}
