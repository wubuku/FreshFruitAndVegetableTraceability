package org.dddml.ffvtraceability;

import org.dddml.ffvtraceability.specialization.ApplicationContext;
import org.dddml.ffvtraceability.specialization.spring.SpringApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static final Object CONTEXT_LOCK = new Object();


    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext ctx) throws BeansException {
        synchronized (CONTEXT_LOCK) {
            ApplicationContext.current = new SpringApplicationContext(ctx);
        }
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        synchronized (CONTEXT_LOCK) {
            if (ApplicationContext.current == null ||
                    ((SpringApplicationContext) ApplicationContext.current).getInnerContext() != event.getApplicationContext()) {
                ApplicationContext.current = new SpringApplicationContext(event.getApplicationContext());
            }
        }
    }
}
