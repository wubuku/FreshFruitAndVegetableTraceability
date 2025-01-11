package org.dddml.ffvtraceability.tool;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class JsonEntityDataTool {

    public static final String DEFAULT_JSON_DATA_LOCATION_PATTERN = "classpath*:/data/*.json";

    private final static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private final static String DATA_FILE_SUFFIX = "Data.json";

    private final static String CREATION_COOMMANDS_FILE_SUFFIX = "CreationCommands.json";

    public static Map<String, List<Object>> deserializeAllGroupByEntityName(String jsonDataLocationPattern) {
        Map<String, List<Object>> entityObjListMap = new HashMap<>();
        deserializeAll(jsonDataLocationPattern, (e, d) -> {
            List<Object> objList = entityObjListMap.getOrDefault(e, null);
            if (objList == null) {
                objList = new ArrayList<>();
                entityObjListMap.put(e, objList);
            }
            objList.add(d);
        });
        return entityObjListMap;
    }

    static void deserializeAll(String jsonDataLocationPattern, BiConsumer<String, Object> action) {
        List<Object> objList = new ArrayList<>();
        try {
            for (final Resource resource : resourcePatternResolver.getResources(jsonDataLocationPattern)) {
                String filename = resource.getFilename();
                String entityName = null;
                boolean isCommandData = false;
                if (filename.endsWith(DATA_FILE_SUFFIX)) {
                    entityName = filename.substring(0, filename.length() - DATA_FILE_SUFFIX.length());
                } else if (filename.endsWith(CREATION_COOMMANDS_FILE_SUFFIX)) {
                    entityName = filename.substring(0, filename.length() - CREATION_COOMMANDS_FILE_SUFFIX.length());
                    isCommandData = true;
                }
                if (entityName != null) {
                    String jsonString = readUtf8TextFile(resource.getInputStream());
                    String finalEntityName = entityName;
                    deserialize(entityName, isCommandData, jsonString, (item) -> action.accept(finalEntityName, item));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deserialize(String entityName, boolean isCommandData, String jsonString, Consumer<Object> action)
            throws ClassNotFoundException, IOException {
        Class itemClass = null;
        itemClass = EntityClassUtils.getEntityInitializationClass(entityName, true);
        readJsonArrayAndConsumeItem(jsonString, itemClass, action);
    }

    private static <T> void readJsonArrayAndConsumeItem(String jsonString, Class<T> itemClass, Consumer<T> consumer)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        Class arrayClass = Array.newInstance(itemClass, 0).getClass();
        Object arrayObject = objectMapper.readValue(jsonString, arrayClass);
        if (arrayObject.getClass().isArray()) {
            int length = Array.getLength(arrayObject);
            for (int i = 0; i < length; i++) {
                Object arrayElement = Array.get(arrayObject, i);
                consumer.accept((T) arrayElement);
            }
        }
    }

    static final String UTF8_BOM = "\uFEFF";

    static String readUtf8TextFile(String path) throws IOException {
        File fileDir = new File(path);
        InputStream inputStream = new FileInputStream(fileDir);
        return readUtf8TextFile(inputStream);
    }

    private static String readUtf8TextFile(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
        String str;
        boolean firstLine = true;
        while ((str = in.readLine()) != null) {
            // System.out.println(str);
            if (firstLine && str.startsWith(UTF8_BOM)) {
                str = str.substring(1);
            }
            sb.append(str);
            sb.append("\r\n");
            firstLine = false;
        }
        in.close();
        return sb.toString();
    }

}
