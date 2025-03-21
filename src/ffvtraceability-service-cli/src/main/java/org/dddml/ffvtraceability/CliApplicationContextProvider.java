package org.dddml.ffvtraceability;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.dddml.ffvtraceability.specialization.ApplicationContext;
import org.dddml.ffvtraceability.specialization.spring.SpringApplicationContext;

@Component
public class CliApplicationContextProvider implements ApplicationContextAware {
    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext ctx) throws BeansException {
        ApplicationContext.current = new SpringApplicationContext(ctx);
    }
}
