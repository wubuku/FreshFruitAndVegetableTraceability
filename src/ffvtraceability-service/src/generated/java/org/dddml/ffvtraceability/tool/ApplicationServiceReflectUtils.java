package org.dddml.ffvtraceability.tool;

import org.dddml.ffvtraceability.domain.meta.M.BoundedContextMetadata;
import org.dddml.ffvtraceability.specialization.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ApplicationServiceReflectUtils {

    private ApplicationServiceReflectUtils() {
    }

    public static Object invokeApplicationServiceGetMethod(String entityName, Object id) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String aggregateName = BoundedContextMetadata.TYPE_NAME_TO_AGGREGATE_NAME_MAP.get(entityName);
        Object appSvr = getApplicationService(aggregateName);
        Class appSrvClass = appSvr.getClass();
        Method m = appSrvClass.getMethod("get", id.getClass());
        return m.invoke(appSvr, id);
    }

    public static void invokeApplicationServiceInitializeMethod(String entityName, Object e) {
        String aggregateName = BoundedContextMetadata.TYPE_NAME_TO_AGGREGATE_NAME_MAP.get(entityName);
        Object appSvr = getApplicationService(aggregateName);
        Class appSrvClass = appSvr.getClass();
        Method m;
        Object arg = e;
        if (e.getClass().getName().endsWith("Created")) { // NOTE: Hardcoded here!
            try {
                m = appSrvClass.getMethod("initialize", getApplicationServiceInitializeMethodParameterType(aggregateName, entityName));
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException("No exact initialize() method found for " + aggregateName + "." + entityName);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Class not found for " + aggregateName + "." + entityName);
            }
            if (!m.getParameterTypes()[0].isAssignableFrom(arg.getClass())) {
                throw new RuntimeException("No exact initialize() method found for " + aggregateName + "." + entityName);
            }
        } else {
            try {
                m = appSrvClass.getMethod("when", getApplicationServiceCreateMethodParameterType(aggregateName, entityName));
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException("No exact when() method found for " + aggregateName + "." + entityName);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Class not found for " + aggregateName + "." + entityName);
            }
            if (m.getParameters()[0].getType().isAssignableFrom(e.getClass())) {
                Method convMethod = null;
                try {
                    convMethod = e.getClass().getMethod(String.format("toCreate%1$s", entityName));
                } catch (NoSuchMethodException ex) {
                    throw new RuntimeException("No toCreate" + entityName + "() method found for " + aggregateName + "." + entityName);
                }
                try {
                    arg = convMethod.invoke(e);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException("Failed to invoke toCreate" + entityName + "() method for " + aggregateName + "." + entityName, ex);
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException("Failed to invoke toCreate" + entityName + "() method for " + aggregateName + "." + entityName, ex);
                }
            } else {
                throw new RuntimeException("No exact when() method found for " + aggregateName + "." + entityName
                        + " with argument type " + e.getClass().getName()
                        + " with parameter type " + m.getParameters()[0].getType().getName()
                );
            }
        }

        try {
            m.invoke(appSvr, arg);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to invoke '%1$s' method for %2$s", m.getName(), aggregateName), ex);
        }
    }

    private static Object getApplicationService(String aggregateName) {
        String appSrvName = aggregateName.substring(0, 1).toLowerCase() + aggregateName.substring(1) + "ApplicationService";
        Object appSrv = ApplicationContext.current.get(appSrvName);
        if (appSrv == null) {
            // Try with "Impl" suffix
            String implName = appSrvName + "Impl";
            appSrv = ApplicationContext.current.get(implName);
            
            if (appSrv == null) {
                throw new IllegalStateException(
                    String.format("Application service not found in context. Tried both '%s' and '%s'. " +
                                "Please ensure the service is properly registered in Spring context.", 
                                appSrvName, implName)
                );
            }
        }
        return appSrv;
    }

    private static Class getApplicationServiceInitializeMethodParameterType(String aggregateName, String typeName) throws ClassNotFoundException {
        String paramTypeName = String.format(
                "%1$s.domain.%2$s.%3$sEvent$%4$sStateCreated", // NOTE: Hardcoded here!
                //"%1$s.domain.%2$s.%3$sStateEvent$%4$sStateCreated",
                getBoundedContextPackageName(),
                aggregateName.toLowerCase(),
                typeName,
                typeName
        );
        return Class.forName(paramTypeName);
    }

    private static Class getApplicationServiceCreateMethodParameterType(String aggregateName, String typeName) throws ClassNotFoundException {
        String paramTypeName = String.format("%1$s.domain.%2$s.%3$sCommand$Create%4$s",
                getBoundedContextPackageName(), aggregateName.toLowerCase(), typeName, typeName);
        return Class.forName(paramTypeName);
    }

    private static String getBoundedContextPackageName() {
        String[] thisClassNames = ApplicationServiceReflectUtils.class.getName().split("\\.");
        return Arrays.stream(thisClassNames).limit(thisClassNames.length - 2).reduce((x, y) -> x + "." + y).get();
    }

}
