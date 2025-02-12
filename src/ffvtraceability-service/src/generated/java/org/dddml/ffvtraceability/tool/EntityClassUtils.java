package org.dddml.ffvtraceability.tool;

import org.dddml.ffvtraceability.domain.meta.M.BoundedContextMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EntityClassUtils {
    private final static String BOUNDED_CONTEXT_DOMAIN_PACKAGE;

    static {
        BOUNDED_CONTEXT_DOMAIN_PACKAGE = getBoundedContextDomainPackage();
    }

    static Class<?> getEntityInitializationClass(String entityName, boolean preferCommandClass) throws ClassNotFoundException {
        String[] names = getEntityInitializationClassNames(entityName, preferCommandClass);
        Class<?> entityClass = null;
        for (String n : names) {
            try {
                entityClass = Class.forName(n);
            } catch (ClassNotFoundException e) {
                System.out.printf("Info: Class not found: %s%n", n);
            }
            if (entityClass != null) {
                break;
            }
        }
        if (entityClass == null) {
            throw new ClassNotFoundException(String.format(
                    "No initialization class found for entity '%s'. Tried: %s",
                    entityName, String.join(", ", names)));
        }
        return entityClass;
    }

    static List<Class<?>> getEntityInitializationClasses(String entityName, boolean preferCommandClass) {
        String[] names = getEntityInitializationClassNames(entityName, preferCommandClass);
        List<Class<?>> entityClasses = new ArrayList<>();
        for (String n : names) {
            try {
                Class<?> entityClass = Class.forName(n);
                entityClasses.add(entityClass);
            } catch (ClassNotFoundException e) {
                System.out.printf("Info: Class not found: %s%n", n);
            }
        }
        return entityClasses;
    }

    static Class<?> getEntityStateClass(String entityName) throws ClassNotFoundException {
        String name = getEntityStateClassName(entityName);
        return Class.forName(name);
    }

    static String getEntityStateClassName(String entityName) {
        String aggregateName = BoundedContextMetadata.TYPE_NAME_TO_AGGREGATE_NAME_MAP
                .get(entityName);
        if (aggregateName == null) {
            throw new IllegalArgumentException(String.format("Aggregate name not found for entity name: %s", entityName));
        }
        String packageClassPath = aggregateName.toLowerCase();
        return String.format(
                "%1$s.%2$s.Abstract%3$sState$Simple%4$sState",
                BOUNDED_CONTEXT_DOMAIN_PACKAGE, packageClassPath, entityName, entityName);
    }

    static String[] getEntityInitializationClassNames(String entityName, boolean preferCommandClass) {
        String aggregateName = BoundedContextMetadata.TYPE_NAME_TO_AGGREGATE_NAME_MAP
                .get(entityName);
        if (aggregateName == null) {
            throw new IllegalArgumentException(String.format("Aggregate name not found for entity name: %s", entityName));
        }
        String packageClassPath = aggregateName.toLowerCase();
        ArrayList<String> names = new ArrayList<>();
        names.add(String.format(
                "%1$s.%2$s.Abstract%3$sEvent$Simple%4$sStateCreated",
                BOUNDED_CONTEXT_DOMAIN_PACKAGE, packageClassPath, entityName, entityName));
        names.add(String.format(
                "%1$s.%2$s.CreateOrMergePatch%3$sDto$Create%4$sDto",
                BOUNDED_CONTEXT_DOMAIN_PACKAGE, packageClassPath, entityName, entityName));
        if (preferCommandClass) {
            Collections.reverse(names);
        }
        return names.toArray(new String[0]);
    }

    private static String getBoundedContextDomainPackage() {
        String[] thisClassNames = EntityClassUtils.class.getName().split("\\.");
        return Arrays.stream(thisClassNames).limit(thisClassNames.length - 2).reduce((x, y) -> x + "." + y)
                .get() + ".domain";
    }

}
