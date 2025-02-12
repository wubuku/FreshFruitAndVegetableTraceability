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
        Method m = findMethod(appSrvClass, "get", id.getClass());
        if (m == null) {
            throw new NoSuchMethodException(String.format("Method 'get(%s)' not found in class hierarchy of %s",
                    id.getClass().getSimpleName(), appSrvClass.getName()));
        }
        return m.invoke(appSvr, id);
    }

    private static Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            // 如果在当前类中没找到，并且父类不是null，就在父类中查找
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return findMethod(superClass, methodName, parameterTypes);
            }
            return null;
        }
    }

    public static void invokeApplicationServiceInitializeMethod(String entityName, Object e) {
        String aggregateName = BoundedContextMetadata.TYPE_NAME_TO_AGGREGATE_NAME_MAP.get(entityName);
        Object appSvr = getApplicationService(aggregateName);
        Class appSrvClass = appSvr.getClass();
        Method m;
        Object arg = e;
        if (e.getClass().getName().endsWith("Created")) { // NOTE: Hardcoded here!
            try {
                m = findMethod(appSrvClass, "initialize", getApplicationServiceInitializeMethodParameterType(aggregateName, entityName));
                if (m == null) {
                    throw new NoSuchMethodException();
                }
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(String.format("Method 'initialize(%s)' not found in class hierarchy of %s for aggregate %s",
                        entityName, appSrvClass.getName(), aggregateName));
            }
            if (!m.getParameterTypes()[0].isAssignableFrom(arg.getClass())) {
                throw new RuntimeException(String.format("Method 'initialize' found but parameter type mismatch. Expected: %s, Found: %s",
                        m.getParameterTypes()[0].getName(), arg.getClass().getName()));
            }
        } else {
            try {
                m = findMethod(appSrvClass, "when", getApplicationServiceCreateMethodParameterType(aggregateName, entityName));
                if (m == null) {
                    throw new NoSuchMethodException();
                }
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(String.format("Method 'when(%s)' not found in class hierarchy of %s for aggregate %s",
                        entityName, appSrvClass.getName(), aggregateName));
            }
            if (m.getParameters()[0].getType().isAssignableFrom(e.getClass())) {
                Method convMethod = null;
                try {
                    convMethod = e.getClass().getDeclaredMethod(String.format("toCreate%1$s", entityName));
                    convMethod.setAccessible(true);
                } catch (NoSuchMethodException ex) {
                    throw new RuntimeException(String.format("Method 'toCreate%s()' not found in %s",
                            entityName, e.getClass().getName()));
                }
                try {
                    arg = convMethod.invoke(e);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException(String.format("Failed to invoke 'toCreate%s()' method: %s",
                            entityName, ex.getMessage()), ex);
                }
            } else {
                throw new RuntimeException(String.format(
                        "Parameter type mismatch for method 'when'. Expected: %s, Found: %s",
                        m.getParameters()[0].getType().getName(),
                        e.getClass().getName()
                ));
            }
        }

        try {
            m.invoke(appSvr, arg);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to invoke '%s' method for aggregate %s: %s",
                    m.getName(), aggregateName, ex.getMessage()), ex);
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

    private static Class getApplicationServiceInitializeMethodParameterType(String aggregateName, String typeName) {
        String paramTypeName = String.format(
                "%1$s.domain.%2$s.%3$sEvent$%4$sStateCreated", // NOTE: Hardcoded here!
                //"%1$s.domain.%2$s.%3$sStateEvent$%4$sStateCreated",
                getBoundedContextPackageName(),
                aggregateName.toLowerCase(),
                typeName,
                typeName
        );
        try {
            return Class.forName(paramTypeName);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(String.format("Parameter type class not found: %s", paramTypeName));
        }
    }

    private static Class getApplicationServiceCreateMethodParameterType(String aggregateName, String typeName) {
        String paramTypeName = String.format("%1$s.domain.%2$s.%3$sCommand$Create%4$s",
                getBoundedContextPackageName(),
                aggregateName.toLowerCase(),
                typeName,
                typeName);
        try {
            return Class.forName(paramTypeName);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(String.format("Parameter type class not found: %s", paramTypeName));
        }
    }

    private static String getBoundedContextPackageName() {
        String[] thisClassNames = ApplicationServiceReflectUtils.class.getName().split("\\.");
        return Arrays.stream(thisClassNames).limit(thisClassNames.length - 2).reduce((x, y) -> x + "." + y).get();
    }

}
