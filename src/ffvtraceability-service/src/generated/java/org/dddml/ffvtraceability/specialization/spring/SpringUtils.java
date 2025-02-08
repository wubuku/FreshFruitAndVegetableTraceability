package org.dddml.ffvtraceability.specialization.spring;

import org.springframework.aop.framework.Advised;

/**
 * Created by yangjiefeng on 2018/2/1.
 */
public class SpringUtils {

    public static Object getAopTarget(Object obj) {
        if (obj instanceof Advised) {
            Object target;
            try {
                target = ((Advised) obj).getTargetSource().getTarget();
            } catch (Exception e) {
                return null;
            }
            return target;
        }
        //throw new IllegalArgumentException("The argument is NOT Advised object.");
        return null;
    }

}
