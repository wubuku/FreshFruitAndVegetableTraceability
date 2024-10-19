package org.dddml.ffvtraceability.tool;

import java.util.*;
import java.util.stream.Collectors;

import org.dddml.ffvtraceability.domain.meta.M.BoundedContextMetadata;

public class EntityClassUtils {
    private final static String BOUNDED_CONTEXT_DOMAIN_PACKAGE;

    static {
        BOUNDED_CONTEXT_DOMAIN_PACKAGE = getBoundedContextDomainPackage();
    }

    static Class<?> getEntityClass(String entityName, boolean preferCommandClass) throws ClassNotFoundException {
        String[] names = getEntityClassNames(entityName, preferCommandClass);
        Class<?> entityClass = null;
        for (String n : names) {
            try {
                entityClass = Class.forName(n);
            } catch (ClassNotFoundException e) {
                // e.printStackTrace();
            }
            if (entityClass != null) {
                break;
            }
        }
        return entityClass;
    }

    static protected String[] getEntityClassNames(String entityName, boolean preferCommandClass) {
        String packageClassPath = BoundedContextMetadata.TYPE_NAME_TO_AGGREGATE_NAME_MAP
                .get(entityName).toLowerCase();
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
