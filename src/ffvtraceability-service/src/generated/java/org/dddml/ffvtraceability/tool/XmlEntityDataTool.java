package org.dddml.ffvtraceability.tool;

import org.dddml.ffvtraceability.domain.meta.M.BoundedContextMetadata;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class XmlEntityDataTool {

    public final static String DEFAULT_XML_DATA_LOCATION_PATTERN = "file:../../data/*.xml";
    //"file:/C:/Users/yangjiefeng/Documents/GitHub/wms/data/*.xml";
    //"classpath*:/data/*.xml";

    private final static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private final static String BOUNDED_CONTEXT_DOMAIN_PACKAGE;
    private final static String XML_ROOT_NODE_NAME = "entity-engine-xml";
    private static DefaultConversionService defaultConversionService = new DefaultConversionService();

    static {
        BOUNDED_CONTEXT_DOMAIN_PACKAGE = getBoundedContextDomainPackage();

        // Converters
        defaultConversionService.addConverter(Long.class, Timestamp.class,
                new Converter() {

                    @Override
                    public Object convert(Object source) {
                        if (source != null) {
                            return new Timestamp((Long) source);
                        }
                        return null;
                    }
                });

        defaultConversionService.addConverter(Long.class, Date.class,
                new Converter() {
                    @Override
                    public Object convert(Object source) {
                        if (source != null) {
                            return new Date((Long) source);
                        }
                        return null;
                    }
                });

        defaultConversionService.addConverter(Long.class, OffsetDateTime.class,
                new Converter() {
                    @Override
                    public Object convert(Object source) {
                        if (source != null) {
                            return OffsetDateTime.ofInstant(Instant.ofEpochMilli((Long) source), ZoneOffset.UTC);
                        }
                        return null;
                    }
                });

        final Converter<String, LocalDateTime> localDateTimeConverter = new Converter() {
            @Override
            public Object convert(Object source) {
                if (source != null) {
                    String dateStr = (String) source;
                    // Try different datetime formats in sequence
                    DateTimeFormatter[] formatters = {
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    };

                    for (DateTimeFormatter formatter : formatters) {
                        try {
                            return LocalDateTime.parse(dateStr, formatter);
                        } catch (Exception ex) {
                            continue;
                        }
                    }
                    throw new IllegalArgumentException("Unable to parse datetime string: " + dateStr);
                }
                return null;
            }
        };
        defaultConversionService.addConverter(String.class, LocalDateTime.class, localDateTimeConverter);

        defaultConversionService.addConverter(String.class, OffsetDateTime.class,
                new Converter() {
                    @Override
                    public Object convert(Object source) {
                        if (source != null) {
                            String dateStr = (String) source;
                            try {
                                try {
                                    // First try to parse ISO format (e.g. "2024-03-21T15:30:00Z" or "2024-03-21T15:30:00+08:00")
                                    return OffsetDateTime.parse(dateStr);
                                } catch (Exception e) {
                                    // Convert to LocalDateTime first, then to OffsetDateTime
                                    LocalDateTime localDateTime = localDateTimeConverter.convert(dateStr);
                                    return localDateTime.atOffset(ZoneOffset.systemDefault().getRules().getOffset(Instant.now()));
                                }
                            } catch (Exception e) {
                                throw new IllegalArgumentException("Unable to parse datetime string: " + dateStr, e);
                            }
                        }
                        return null;
                    }
                });
        //
        // NOTE: More converters can be added here.
        //
    }

    private static String getBoundedContextDomainPackage() {
        String[] thisClassNames = XmlEntityDataTool.class.getName().split("\\.");
        return Arrays.stream(thisClassNames).limit(thisClassNames.length - 2).reduce((x, y) -> x + "." + y)
                .get() + ".domain";
    }

    // only for test.
    public static void _main(String[] args) {
        List<Object> entityInstList = null;
        Map<String, List<Object>> entityInstGroupByEntityName = null;
        try {
            entityInstList = deserializeAll(DEFAULT_XML_DATA_LOCATION_PATTERN);
            entityInstGroupByEntityName = deserializeGroupByEntityName(DEFAULT_XML_DATA_LOCATION_PATTERN);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        System.out.println(entityInstList.size());
        for (Object obj : entityInstList) {
            System.out.println(obj);
        }
        int count = 0;
        for (Map.Entry<String, List<Object>> kv : entityInstGroupByEntityName.entrySet()) {
            System.out.println(kv.getKey());
            List<Object> objList = kv.getValue();
            count += objList.size();
            System.out.println(objList.size());
        }
        System.out.println(count);
        System.out.println(count == entityInstList.size());
        if (count != entityInstList.size()) {
            throw new RuntimeException("sum(entityInstGroupByEntityName.size()) != entityInstList.size()");
        }
        System.exit(0);
    }

    /**
     * Groups deserialized objects by their entity names after deserializing XML data with type inference.
     *
     * @param xmlDataLocationPattern The pattern to locate XML data files
     * @return A map where keys are entity names and values are lists of corresponding objects
     */
    public static Map<String, List<Object>> deserializeGroupByEntityName(String xmlDataLocationPattern) {
        Map<String, List<Object>> entityObjListMap = new HashMap<>();
        deserializeAll(xmlDataLocationPattern, (e, d) -> {
            List<Object> objList = entityObjListMap.getOrDefault(e.getNodeName(), null);
            if (objList == null) {
                objList = new ArrayList<>();
                entityObjListMap.put(e.getNodeName(), objList);
            }
            objList.add(d);
        });
        return entityObjListMap;
    }

    /**
     * Deserializes XML data into objects by inferring their types from entity names.
     * For each entity in XML, it tries to find and use the most appropriate Java class
     * from available initialization classes.
     *
     * @param xmlDataLocationPattern The pattern to locate XML data files
     * @return A list of deserialized objects
     */
    public static List<Object> deserializeAll(String xmlDataLocationPattern) {
        List<Object> objList = new ArrayList<>();
        deserializeAll(xmlDataLocationPattern, (e, d) -> objList.add(d));
        return objList;
    }

    public static List<Object> deserializeAllState(String xmlDataLocationPattern) {
        List<Object> objList = new ArrayList<>();
        deserializeAllState(xmlDataLocationPattern, (e, d) -> objList.add(d));
        return objList;
    }

    public static void deserializeAllState(String xmlDataLocationPattern, BiConsumer<Node, Object> action) {
        XmlEntityDataTool deserializer = new XmlEntityDataTool();
        try {
            for (final Resource resource : resourcePatternResolver.getResources(xmlDataLocationPattern)) {
                deserializer.deserialize(resource.getInputStream(),
                        (entityName) -> {
                            try {
                                return Collections.singletonList(EntityClassUtils.getEntityStateClass(entityName));
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        action
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Deserializes XML data into objects by inferring their types, with custom handling through an action.
     * For each entity in XML, it attempts to find the most appropriate Java class for deserialization.
     *
     * @param xmlDataLocationPattern The pattern to locate XML data files
     * @param action                 BiConsumer to handle each deserialized object along with its XML node
     */
    public static void deserializeAll(String xmlDataLocationPattern, BiConsumer<Node, Object> action) {
        XmlEntityDataTool deserializer = new XmlEntityDataTool();
        try {
            for (final Resource resource : resourcePatternResolver.getResources(xmlDataLocationPattern)) {
                deserializer.deserialize(resource.getInputStream(), action);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Document parseXmlDocument(InputStream xmlInputStream) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(xmlInputStream);
    }

    private static void setProperty(String entityName, Object obj, Map<String, PropertySetter> setterMap, String attrName, Object attrVal) throws InvocationTargetException, IllegalAccessException {
        PropertySetter propertySetter = setterMap.get(attrName);
        Set<String> ignorableNames = Arrays.stream(new String[]{
                "createdDate", "createdBy", "createdAt"
        }).collect(Collectors.toSet());

        if (propertySetter == null) {
            if (ignorableNames.contains(attrName)) {
                return;
            }
            throw new NullPointerException(String.format(
                    "Property setter not found for entity '%s'. Property name: '%s', Object type: '%s'",
                    entityName, attrName, obj.getClass().getName()));
        }

        Class propertyType = propertySetter.getPropertyType();
        propertySetter.invoke(obj, convertAttributeValue(attrVal, propertyType));
    }

    private static Object convertAttributeValue(Object attributeVal, Class<?> type) {
        try {
            return defaultConversionService.convert(attributeVal, type);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(
                    "Failed to convert value '%s' to type '%s'. Error: %s",
                    attributeVal, type.getSimpleName(), e.getMessage()
            ), e);
        }
    }

    private static String getCreatedAtPropertyName(String entityName) {
        try {
            Class<?> metadataClazz = getAggregateMetadataClass(entityName);
            Object fieldVal = metadataClazz.getField("PROPERTY_NAME_CREATED_AT").get(null);
            return (String) fieldVal;
        } catch (Exception e) {
            System.out.printf("Warning: Failed to get PROPERTY_NAME_CREATED_AT for entity '%s': %s%n",
                    entityName, e.getMessage());
            return null;
        }
    }

    private static String getVersionPropertyName(String entityName) {
        try {
            Class<?> metadataClazz = getAggregateMetadataClass(entityName);
            Object fieldVal = metadataClazz.getField("PROPERTY_NAME_VERSION").get(null);
            return (String) fieldVal;
        } catch (Exception e) {
            System.out.printf("Warning: Failed to get PROPERTY_NAME_VERSION for entity '%s': %s%n",
                    entityName, e.getMessage());
            return null;
        }
    }

    private static Class<?> getAggregateMetadataClass(String entityName) throws ClassNotFoundException {
        String aggregateName = BoundedContextMetadata.TYPE_NAME_TO_AGGREGATE_NAME_MAP
                .get(entityName);
        String className = String.format("%1$s.meta.M$%2$sMetadata", BOUNDED_CONTEXT_DOMAIN_PACKAGE,
                aggregateName);
        return Class.forName(className);
    }

    private static Map<String, PropertySetter> buildPropertySetterMap(String entityName, Class beanClass)
            throws IntrospectionException {
        Map<String, PropertySetter> setterMap = new HashMap<>();
        BeanInfo info = Introspector.getBeanInfo(beanClass);
        PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();

        String superEntityName = BoundedContextMetadata.TYPE_NAME_TO_AGGREGATE_NAME_MAP.get(entityName);

        // Collect all ID property names that need special handling
        Set<String> specialIdPropertyNames = collectSpecialIdPropertyNames(superEntityName, propertyDescriptors);

        // Process all properties
        for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();

            // Basic property handling: add properties that have both getter and setter
            if (propertyDescriptor.getWriteMethod() != null && propertyDescriptor.getReadMethod() != null) {
                setterMap.put(propertyName, new PropertySetter() {
                    @Override
                    public void invoke(Object b, Object pVal) throws InvocationTargetException, IllegalAccessException {
                        propertyDescriptor.getWriteMethod().invoke(b, pVal);
                    }

                    @Override
                    public Class getPropertyType() {
                        return propertyDescriptor.getPropertyType();
                    }
                });
            }

            // Special ID property handling: add nested property setters for all special ID properties
            if (specialIdPropertyNames.contains(propertyName)) {
                addNestedIdPropertySetters(setterMap, propertyDescriptor);
            }
        }

        return setterMap;
    }

    private static Set<String> collectSpecialIdPropertyNames(String superEntityName, PropertyDescriptor[] propertyDescriptors) {
        Set<String> specialIdPropertyNames = new HashSet<>();

        // 1. Add the entity's primary ID property
        String primaryIdPropertyName = null;
        try {
            Class<?> metadataClazz = getAggregateMetadataClass(superEntityName);
            Field idPropertyNameField = metadataClazz.getField("ID_PROPERTY_NAME");
            primaryIdPropertyName = (String) idPropertyNameField.get(null);

            // Find the property descriptor for the ID property
            PropertyDescriptor idPropertyDescriptor = null;
            for (PropertyDescriptor pd : propertyDescriptors) {
                if (pd.getName().equals(primaryIdPropertyName)) {
                    idPropertyDescriptor = pd;
                    break;
                }
            }

            // Only add to specialIdPropertyNames if the ID property type is a complex type
            if (idPropertyDescriptor != null) {
                Class<?> idPropertyType = idPropertyDescriptor.getPropertyType();
                if (!isSimpleType(idPropertyType)) {
                    specialIdPropertyNames.add(primaryIdPropertyName);
                }
            }
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            System.out.printf("Info: Failed to get ID_PROPERTY_NAME for aggregate '%s': %s%n",
                    superEntityName, e.getMessage());
        }

        // 2. Add EntityId and EventId properties if they are complex types and not already added
        String[] potentialIdProps = {superEntityName + "Id", superEntityName + "EventId"};
        for (String propName : potentialIdProps) {
            // Skip if this property name is the same as the primary ID property
            if (propName.equals(primaryIdPropertyName)) {
                continue;
            }

            for (PropertyDescriptor pd : propertyDescriptors) {
                if (pd.getName().equals(propName)) {
                    Class<?> propType = pd.getPropertyType();
                    if (!isSimpleType(propType)) {
                        specialIdPropertyNames.add(propName);
                    }
                    break;
                }
            }
        }

        return specialIdPropertyNames;
    }

    private static boolean isSimpleType(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || type == Boolean.class
                || type == Character.class
                || Number.class.isAssignableFrom(type)
                || type == java.util.Date.class
                || type == java.sql.Date.class
                || type == java.sql.Timestamp.class
                || type.isEnum()
                // NOTE: Add more types as needed.
                ;
    }

    private static void addPropertySetter(
            String entityName, Class beanClass,
            String propertyName,
            Map<String, PropertySetter> setterMap
    ) {
        Map<String, String> aliasMap = getEntityAliasMap(entityName);
        if (aliasMap.containsKey(propertyName)) {
            String propertyPath = aliasMap.get(propertyName);
            if (!setterMap.containsKey(propertyPath)) { // Only add if not already exists
                String[] propertyNames = propertyPath.split("\\.");
                if (propertyNames.length == 1) {
                    try {
                        // Get property descriptor for the property path
                        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
                        PropertyDescriptor propertyDescriptor = null;
                        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                            if (pd.getName().equals(propertyPath)) {
                                propertyDescriptor = pd;
                                break;
                            }
                        }

                        if (propertyDescriptor == null || propertyDescriptor.getWriteMethod() == null) {
                            System.out.printf("Warning: Property descriptor or setter not found for '%s' in class '%s'%n",
                                    propertyPath, beanClass.getName());
                            return;
                        }

                        final PropertyDescriptor finalPropertyDescriptor = propertyDescriptor;
                        setterMap.put(propertyPath, new PropertySetter() {
                            @Override
                            public void invoke(Object beanObj, Object propertyVal)
                                    throws InvocationTargetException, IllegalAccessException {
                                finalPropertyDescriptor.getWriteMethod().invoke(beanObj, propertyVal);
                            }

                            @Override
                            public Class getPropertyType() {
                                return finalPropertyDescriptor.getPropertyType();
                            }
                        });
                    } catch (IntrospectionException e) {
                        System.out.printf("Warning: Failed to introspect property '%s' in class '%s': %s%n",
                                propertyPath, beanClass.getName(), e.getMessage());
                    }
                } else {
                    // For nested properties, keep runtime check due to complexity
                    setterMap.put(propertyPath, new PropertySetter() {
                        @Override
                        public void invoke(Object beanObj, Object propertyVal) {
                            try {
                                // Get the parent object of the last property
                                Object parentObj = PropertyPathUtils.getNestedPropertyParent(
                                        beanObj, propertyNames, propertyNames.length - 1);

                                // Set the value of the final property
                                String finalPropertyName = propertyNames[propertyNames.length - 1];
                                String setterMethodName = "set" + finalPropertyName.substring(0, 1).toUpperCase() +
                                        finalPropertyName.substring(1);
                                Method setter = parentObj.getClass().getMethod(setterMethodName, propertyVal.getClass());
                                setter.invoke(parentObj, propertyVal);
                            } catch (Exception e) {
                                throw new RuntimeException(String.format(
                                        "Failed to set nested property '%s': %s", propertyPath, e.getMessage()), e);
                            }
                        }

                        @Override
                        public Class getPropertyType() {
                            try {
                                return PropertyPathUtils.getPropertyType(beanClass, propertyNames);
                            } catch (Exception e) {
                                throw new RuntimeException(String.format(
                                        "Failed to determine property type for '%s': %s",
                                        propertyPath, e.getMessage()), e);
                            }
                        }
                    });
                }
            }
        }
    }

    private static void addNestedIdPropertySetters(Map<String, PropertySetter> setterMap, PropertyDescriptor idPropertyDescriptor) throws IntrospectionException {
        // First verify the id property has getter and setter
        if (idPropertyDescriptor.getReadMethod() == null || idPropertyDescriptor.getWriteMethod() == null) {
            return; // Skip if getter or setter is missing
        }

        Class idPropertyType = idPropertyDescriptor.getPropertyType();
        BeanInfo idPropertyTypeInfo = Introspector.getBeanInfo(idPropertyType);
        for (final PropertyDescriptor nestedPropertyDescriptor : idPropertyTypeInfo.getPropertyDescriptors()) {
            // Skip if property already exists or doesn't have both getter and setter
            if (setterMap.containsKey(nestedPropertyDescriptor.getName()) ||
                    nestedPropertyDescriptor.getWriteMethod() == null ||
                    nestedPropertyDescriptor.getReadMethod() == null) {
                continue;
            }

            setterMap.put(nestedPropertyDescriptor.getName(), new PropertySetter() {
                @Override
                public void invoke(Object b, Object pVal) throws InvocationTargetException, IllegalAccessException {
                    Object idObj = idPropertyDescriptor.getReadMethod().invoke(b);
                    if (idObj == null) {
                        try {
                            idObj = idPropertyType.newInstance();
                            idPropertyDescriptor.getWriteMethod().invoke(b, idObj);
                        } catch (InstantiationException e) {
                            throw new RuntimeException(String.format(
                                    "Failed to create instance of type '%s' for property '%s'. Error: %s",
                                    idPropertyType.getSimpleName(), idPropertyDescriptor.getName(), e.getMessage()
                            ), e);
                        }
                    }
                    nestedPropertyDescriptor.getWriteMethod().invoke(idObj, pVal);
                }

                @Override
                public Class getPropertyType() {
                    return nestedPropertyDescriptor.getPropertyType();
                }
            });
        }
    }

    private static Class<?> getEntityMetadataClass(String entityName) {
        String metadataClassName = String.format("%s.domain.meta.M$%sMetadata",
                getBoundedContextPackageName(),
                entityName);
        try {
            return Class.forName(metadataClassName);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(String.format("Entity metadata class not found: %s", metadataClassName));
        }
    }

    // Get aliasMap from Metadata class
    @SuppressWarnings("unchecked")
    private static Map<String, String> getEntityAliasMap(String entityName) {
        Class<?> metadataClass = getEntityMetadataClass(entityName);
        try {
            Field aliasMapField = metadataClass.getDeclaredField("aliasMap");
            aliasMapField.setAccessible(true);
            return (Map<String, String>) aliasMapField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new RuntimeException(String.format("Failed to get aliasMap from metadata class for entity %s: %s",
                    entityName, ex.getMessage()), ex);
        }
    }

    private static String getBoundedContextPackageName() {
        String[] thisClassNames = XmlEntityDataTool.class.getName().split("\\.");
        return Arrays.stream(thisClassNames).limit(thisClassNames.length - 2).reduce((x, y) -> x + "." + y).get();
    }

    /**
     * Core deserialization method that processes a single XML input stream with type inference.
     * For each entity in the XML, it:
     * 1. Determines possible Java classes based on the entity name
     * 2. Attempts deserialization with each class until successful
     * 3. Handles the result through the provided action
     *
     * @param xmlInputStream The input stream containing XML data
     * @param action         BiConsumer to handle each deserialized object along with its XML node
     */
    public void deserialize(InputStream xmlInputStream, BiConsumer<Node, Object> action) {
        deserialize(xmlInputStream, (entityName) ->
                EntityClassUtils.getEntityInitializationClasses(entityName, false), action);
    }

    void deserialize(
            InputStream xmlInputStream,
            Function<String, List<Class<?>>> beanClassProvider,
            BiConsumer<Node, Object> action
    ) {
        Map<Class<?>, Map<String, PropertySetter>> propSetterMapCache = new HashMap<>();
        try {
            Document doc = parseXmlDocument(xmlInputStream);
            Element docElement = doc.getDocumentElement();
            if (!XML_ROOT_NODE_NAME.equals(docElement.getNodeName())) {
                throw new IllegalArgumentException(String.format(
                        "Invalid XML root node. Expected '%s' but found '%s'",
                        XML_ROOT_NODE_NAME, docElement.getNodeName()));
            }
            NodeList childNodes = docElement.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                Node entityDataNode = childNodes.item(i);
                if (!entityDataNode.hasAttributes()) {
                    continue;
                }
                Map<String, Object> attrMap = new HashMap<>();

                String entityName = entityDataNode.getNodeName();
                NamedNodeMap entityDataAttributes = entityDataNode.getAttributes();
                for (int j = 0; j < entityDataAttributes.getLength(); j++) {
                    Node item = entityDataAttributes.item(j);
                    String attributeName = item.getNodeName();
                    String nodeValue = item.getNodeValue();
                    attrMap.put(attributeName, nodeValue);
                }
                List<Class<?>> beanClasses = beanClassProvider.apply(entityName);
                if (beanClasses.isEmpty()) {
                    throw new ClassNotFoundException(String.format("No suitable initialization class found for entity: %s", entityName));
                }

                RuntimeException lastException = null;
                boolean success = false;
                for (Class<?> beanClass : beanClasses) {
                    try {
                        Map<String, PropertySetter> setterMap = propSetterMapCache.getOrDefault(beanClass, null);
                        if (setterMap == null) {
                            setterMap = buildPropertySetterMap(entityName, beanClass);
                            propSetterMapCache.put(beanClass, setterMap);
                        }
                        for (String attrName : attrMap.keySet()) {
                            if (!setterMap.containsKey(attrName)) {
                                addPropertySetter(entityName, beanClass, attrName, setterMap);
                            }
                        }
                        action.accept(entityDataNode, convertEntityData(beanClass, setterMap, entityName, attrMap));
                        success = true;
                        break;
                    } catch (RuntimeException e) {
                        lastException = e;
                        System.out.printf("Info: Failed to initialize entity using class '%s': %s%n",
                                beanClass.getName(), e.getMessage());
                    }
                }

                if (!success) {
                    throw new RuntimeException(String.format(
                            "Failed to initialize entity '%s' with any available class. Last error: %s",
                            entityName, lastException.getMessage()), lastException);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Failed to deserialize XML data. Error: %s",
                    e.getMessage()
            ), e);
        }
    }

    Object convertEntityData(
            Class<?> beanClass,
            Map<String, PropertySetter> setterMap,
            String entityName,
            Map<String, Object> attrMap
    ) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        String createdAtPropName = getCreatedAtPropertyName(entityName);
        if (createdAtPropName != null && !attrMap.containsKey(createdAtPropName)) {
            attrMap.put(createdAtPropName, Long.valueOf(System.currentTimeMillis())); // NOTE: Is this OK?
        }
        if (autoSetVersionProperty()) {
            String versionPropName = getVersionPropertyName(entityName);
            if (versionPropName != null && !attrMap.containsKey(versionPropName)) {
                attrMap.put(versionPropName, -1L);
            }
        }
        Object beanInst = beanClass.getDeclaredConstructor().newInstance();
        for (Map.Entry<String, Object> kv : attrMap.entrySet()) {
            setProperty(entityName, beanInst, setterMap, kv.getKey(), kv.getValue());
        }
        return beanInst;
    }

    protected boolean autoSetVersionProperty() {
        return true;
    }

    //	private static String toCamelCase(String s) {
    //		if (Character.isLowerCase(s.charAt(0)))
    //			return s;
    //		else
    //			return (new StringBuilder())
    //					.append(Character.toLowerCase(s.charAt(0)))
    //					.append(s.substring(1)).toString();
    //	}

    interface PropertySetter {
        void invoke(Object beanObj, Object propertyVal) throws InvocationTargetException, IllegalAccessException;

        Class getPropertyType();
    }

    private static class PropertyPathUtils {
        static Object getNestedPropertyParent(Object rootObj, String[] propertyPath, int endIndex)
                throws ReflectiveOperationException {
            Object currentObj = rootObj;
            for (int i = 0; i < endIndex; i++) {
                String propertyName = propertyPath[i];
                String getterMethodName = "get" + propertyName.substring(0, 1).toUpperCase() +
                        propertyName.substring(1);
                Method getter;
                try {
                    getter = currentObj.getClass().getMethod(getterMethodName);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(String.format(
                            "Getter method '%s' not found in class '%s'",
                            getterMethodName, currentObj.getClass().getName()), e);
                }

                currentObj = getter.invoke(currentObj);
                if (currentObj == null) {
                    // If intermediate object is null, create a new instance
                    Class<?> propertyType = getter.getReturnType();
                    currentObj = propertyType.newInstance();
                    String setterMethodName = "set" + propertyName.substring(0, 1).toUpperCase() +
                            propertyName.substring(1);
                    Method setter = rootObj.getClass().getMethod(setterMethodName, propertyType);
                    setter.invoke(rootObj, currentObj);
                }
            }
            return currentObj;
        }

        static Class<?> getPropertyType(Class<?> rootClass, String[] propertyPath)
                throws NoSuchMethodException {
            Class<?> currentClass = rootClass;
            for (String propertyName : propertyPath) {
                String getterMethodName = "get" + propertyName.substring(0, 1).toUpperCase() +
                        propertyName.substring(1);
                Method getter = currentClass.getMethod(getterMethodName);
                currentClass = getter.getReturnType();
            }
            return currentClass;
        }
    }
}
