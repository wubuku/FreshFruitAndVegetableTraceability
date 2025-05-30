package org.dddml.ffvtraceability.tool.hibernate;

// import org.dddml.ffvtraceability.sui.contract.MoveObjectIdGeneratorObject;
// import org.dddml.ffvtraceability.sui.contract.SuiPackage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.query.NativeQuery;
import org.hibernate.tool.schema.spi.SchemaManagementTool;
import org.hibernate.tool.schema.spi.ScriptSourceInput;
import org.hibernate.tool.schema.spi.ScriptTargetOutput;
import org.hibernate.tool.schema.internal.ExceptionHandlerHaltImpl;
import org.hibernate.tool.schema.internal.HibernateSchemaManagementTool;
import org.hibernate.tool.schema.internal.SchemaCreatorImpl;
import org.hibernate.tool.schema.internal.SchemaDropperImpl;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.hibernate.tool.schema.spi.ExecutionOptions;
import org.hibernate.tool.schema.spi.SchemaFilter;
import org.hibernate.tool.schema.spi.SourceDescriptor;
import org.hibernate.tool.schema.spi.TargetDescriptor;
import org.hibernate.tool.schema.spi.ContributableMatcher;
import org.hibernate.tool.schema.spi.ExceptionHandler;
import org.hibernate.tool.schema.SourceType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.nio.file.StandardOpenOption;

/**
 * Created by yangjiefeng on 2018/2/4.
 */
public class SchemaTool {

    static final String ADD_FK_PATTERN = "alter table(\\s+)(\\w+)(\\s+)add constraint(\\s+)(\\w+)(\\s+)foreign key";
    static final String OLD_DROP_FK_PATTERN = "alter table(\\s+)(\\w+)(\\s+)drop foreign key(\\s+)(\\w+)";
    static final String NEW_DROP_FK_REPLACEMENT = "set @fkConstraintName = (SELECT CONSTRAINT_NAME FROM information_schema.TABLE_CONSTRAINTS WHERE"
            + "\r\n"
            + "            CONSTRAINT_SCHEMA = DATABASE() AND" + "\r\n"
            + "            TABLE_NAME        = '$2' AND" + "\r\n"
            + "            CONSTRAINT_TYPE   = 'FOREIGN KEY');" + "\r\n"
            + "set @sqlVar = if(@fkConstraintName is not null, " + "\r\n"
            + "			concat('ALTER TABLE $2 drop foreign key ', @fkConstraintName)," + "\r\n"
            + "            'select 1');" + "\r\n"
            + "prepare stmt from @sqlVar;" + "\r\n"
            + "execute stmt;" + "\r\n"
            + "deallocate prepare stmt";

    static final String FILENAME_DROP_RVIEWS = "DropRViews.sql";
    static final String FILENAME_DROP_STATE_ID_FK_CONSTRAINTS = "DropStateIdForeignKeyConstraints.sql";
    static final String FILENAME_HBM2DDL_CREATE_FIX = "hbm2ddl_create_fix.sql";
    static final String FILENAME_HBM2DDL_CREATE = "hbm2ddl_create.sql";
    static final String FILENAME_HBM2DDL_UPDATE = "hbm2ddl_update.sql";
    static final String FILENAME_DROP_RVIEW_NAME_CONFLICTED_TABLES = "DropRViewNameConflictedTables.sql";
    static final String FILENAME_CREATE_RVIEWS = "CreateRViews.sql";
    static final String FILENAME_ADD_STATE_ID_FK_CONSTRAINTS = "AddStateIdForeignKeyConstraints.sql";
    private static final String SQL_DELIMITER = ";";
    /**
     * SQL script input/output directory.
     */
    private String sqlDirectory;
    /**
     * Database username.
     */
    private String connectionUsername = "root";
    /**
     * Database password.
     */
    private String connectionPassword = "123456";
    /**
     * Database connection JDBC URL.
     */
    private String connectionUrl;

    private Properties hibernateProperties = new Properties();

    private static boolean isSqlEmpty(String sql) {
        String[] lines = sql.split("\\r?\\n", 100);
        for (String line : lines) {
            if (!line.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static Properties getDefaultHibernateProperties() {
        Properties props = new Properties();
        // props.setProperty("hibernate.dialect",
        // "org.hibernate.dialect.MySQL5InnoDBDialect");
        // props.setProperty("hibernate.connection.driver_class",
        // "com.mysql.cj.jdbc.Driver");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.EhCacheProvider");
        props.setProperty("hibernate.cache.use_second_level_cache", "false");
        return props;
    }

    private Properties getHibernateProperties() {
        if (hibernateProperties == null) {
            hibernateProperties = getDefaultHibernateProperties();
        }
        return hibernateProperties;
    }

    public final String getSqlDirectory() {
        return sqlDirectory;
    }

    public final void setSqlDirectory(String value) {
        sqlDirectory = value;
    }

    public String getConnectionUsername() {
        return connectionUsername;
    }

    public void setConnectionUsername(String connnectionUsername) {
        this.connectionUsername = connnectionUsername;
    }

    public String getConnectionPassword() {
        return connectionPassword;
    }

    public void setConnectionPassword(String value) {
        connectionPassword = value;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connStr) {
        connectionUrl = connStr;
    }

    public void setUp() {
        if (hibernateProperties == null) {
            hibernateProperties = getDefaultHibernateProperties();
        } else {
            Properties defaultProperties = getDefaultHibernateProperties();
            for (Map.Entry<Object, Object> entry : defaultProperties.entrySet()) {
                if (!hibernateProperties.containsKey(entry.getKey())) {
                    hibernateProperties.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void dropCreateDatabaseAndSeed() {
        dropCreateDatabase();
        seedDatabase();
    }

    public void seedDatabase() {
        // todo seed
    }

    public void dropCreateDatabase() {
        org.hibernate.cfg.Configuration cfg = getHibernateConfiguration();

        String[] fns = getDropCreateFileNames();

        SessionFactory sf = cfg.buildSessionFactory();
        for (String fn : fns) {
            String filePath = Path.combine(getSqlDirectory(), fn);
            if (!FileUtils.fileExists(filePath)) {
                continue;
            }
            String sql = FileUtils.readUtf8TextFile(filePath);
            if (isSqlEmpty(sql)) {
                continue;
            }
            Session session = sf.openSession();
            try {
                session.beginTransaction();
                NativeQuery<?> query = session.createNativeQuery(sql);
                query.executeUpdate();
                session.getTransaction().commit();

                System.out.println("execute sql [" + filePath + "], ok.");
            } finally {
                session.close();
            }
        }
    }

    private String[] getDropCreateFileNames() {
        return new String[] {
                FILENAME_DROP_RVIEWS,
                FILENAME_DROP_STATE_ID_FK_CONSTRAINTS,
                FILENAME_HBM2DDL_CREATE_FIX,
                FILENAME_DROP_RVIEW_NAME_CONFLICTED_TABLES,
                FILENAME_CREATE_RVIEWS,
                FILENAME_ADD_STATE_ID_FK_CONSTRAINTS
        };
    }

    public final void hbm2DdlOutput() {
        org.hibernate.cfg.Configuration cfg = getHibernateConfiguration();

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties())
                .build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);

        for (String s : getHbmResourceNames()) {
            metadataSources.addResource(s);
        }
        for (Class<?> c : getAnnotationClasses()) {
            metadataSources.addAnnotatedClass(c);
        }

        MetadataImplementor metadata = (MetadataImplementor) metadataSources.getMetadataBuilder()
                .applyImplicitNamingStrategy(new SpringImplicitNamingStrategy())
                .applyPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy())
                .build();

        // Generate ddl update script
        hbm2DdlOutputUpdate(metadata);

        SchemaManagementTool tool = serviceRegistry.getService(SchemaManagementTool.class);

        String dropFilePath = Path.combine(getSqlDirectory(), "hbm2ddl_drop.sql");
        FileUtils.deleteIfExists(dropFilePath);
        TargetDescriptor dropTarget = new ScriptTargetOutputImpl(dropFilePath);
        tool.getSchemaDropper(null).doDrop(
                metadata,
                createExecutionOptions(),
                ContributableMatcher.ALL,
                createSourceDescriptor(),
                dropTarget);

        String createFilePath = Path.combine(getSqlDirectory(), FILENAME_HBM2DDL_CREATE);
        FileUtils.deleteIfExists(createFilePath);
        TargetDescriptor createTarget = new ScriptTargetOutputImpl(createFilePath);
        tool.getSchemaCreator(null).doCreation(
                metadata,
                createExecutionOptions(),
                ContributableMatcher.ALL,
                createSourceDescriptor(),
                createTarget);
    }

    private void hbm2DdlOutputUpdate(MetadataImplementor metadata) {
        ServiceRegistry serviceRegistry = metadata.getMetadataBuildingOptions().getServiceRegistry();
        SchemaManagementTool tool = serviceRegistry.getService(SchemaManagementTool.class);

        String fileName = (new SimpleDateFormat("yyyyMMddHHmm").format(new Date())) +
                FILENAME_HBM2DDL_UPDATE;
        String updateFilePath = Path.combine(getSqlDirectory(), fileName);
        FileUtils.deleteIfExists(updateFilePath);

        TargetDescriptor updateTarget = new ScriptTargetOutputImpl(updateFilePath);

        tool.getSchemaMigrator(null).doMigration(
                metadata,
                createExecutionOptions(),
                ContributableMatcher.ALL,
                updateTarget
        );
    }

    public final void copyAndFixHbm2DdlCreateSql() {
        String srcFilePath = Path.combine(getSqlDirectory(), FILENAME_HBM2DDL_CREATE);
        String dstFilePath = Path.combine(getSqlDirectory(), FILENAME_HBM2DDL_CREATE_FIX);

        String sql = FileUtils.readUtf8TextFile(srcFilePath);

        List<String> tableNames = getFKTableNames(sql);

        StringBuilder sb = new StringBuilder();
        for (String t : tableNames) {
            sb.append(NEW_DROP_FK_REPLACEMENT.replace("$2", t));
            sb.append(SQL_DELIMITER);
            sb.append("\r\n");
        }

        String newSql = sql.replaceAll(OLD_DROP_FK_PATTERN, NEW_DROP_FK_REPLACEMENT);// rgx.Replace(sql,
                                                                                     // NEW_DROP_FK_REPLACEMENT);
        newSql = newSql.replaceAll(" type=MyISAM", "");
        // System.out.println(newSql);

        sb.append(newSql);

        FileUtils.writeUtf8TextFile(dstFilePath, sb.toString());
    }

    private List<String> getFKTableNames(String sql) {
        Pattern pattern = Pattern.compile(ADD_FK_PATTERN);
        Matcher m = pattern.matcher(sql);

        List<String> tableNames = new ArrayList<String>();
        // System.out.println("============================");
        // Find all matches
        while (m.find()) {
            // Get the matching string
            String tableName = m.group(2);
            tableNames.add(tableName);
            // System.out.println(tableName);
        }
        // System.out.println("============================");
        return tableNames;
    }

    private org.hibernate.cfg.Configuration getHibernateConfiguration() {
        String connString = getConnectionUrl();
        //
        // For MySQL (jdbc:mysql). If using PostgreSQL, comment out the following lines
        // String connAllowMQOpt = "allowMultiQueries=true";
        // if (!connString.toLowerCase().contains((connAllowMQOpt).toLowerCase())) {
        //     connString = connString + (connString.endsWith("&") ? "" : "&") + connAllowMQOpt;
        // }
        //
        org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration();
        cfg.setProperty("hibernate.connection.url", connString);
        cfg.setProperty("hibernate.connection.username", getConnectionUsername());
        cfg.setProperty("hibernate.connection.password", getConnectionPassword());
        for (String key : getHibernateProperties().stringPropertyNames()) {
            cfg.setProperty(key, getHibernateProperties().getProperty(key));
        }

        List<String> resourceNames = getHbmResourceNames();
        for (String s : resourceNames) {
            cfg.addResource(s);
        }
        for (Class<?> c : getAnnotationClasses()) {
            cfg.addAnnotatedClass(c);
        }
        return cfg;
    }

    private List<String> getHbmResourceNames() {
        String[] locationPatterns = new String[] {
                // "classpath:/org/dddml/ffvtraceability/domain/hbm/*.hbm.xml",
                // "classpath:/org/dddml/ffvtraceability/tool/hibernate/hbm/*.hbm.xml"
                "classpath:/hibernate/*.hbm.xml" };
        List<String> resourceNames = new ArrayList<String>();
        for (String locationPattern : locationPatterns) {
            resourceNames.addAll(getHbmResourceNames(locationPattern));
        }
        return resourceNames;
    }

    private List<Class<?>> getAnnotationClasses() {
        return Arrays.stream(new Class<?>[] {
                // SuiPackage.class,
                // MoveObjectIdGeneratorObject.class
        }).collect(Collectors.toList());
    }

    private List<String> getHbmResourceNames(String locationPattern) {
        List<String> resourceNames = new ArrayList<String>();
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = new Resource[0];
        try {
            resources = resourcePatternResolver.getResources(locationPattern);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // //////////////////////////
        String classpathDirPath = locationPattern.substring("classpath:/".length(),
                locationPattern.lastIndexOf("*.hbm.xml"));
        // System.out.println(classpathDirPath);

        for (Resource m : resources) {
            try {
                String urlPath = m.getURL().getPath();
                int i = urlPath.lastIndexOf(classpathDirPath);
                resourceNames.add(urlPath.substring(i));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return resourceNames;
    }

    static class FileUtils {

        static final String UTF8_BOM = "\uFEFF";

        static void deleteIfExists(String path) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }

        static boolean fileExists(String path) {
            File file = new File(path);
            return file.exists();
        }

        static String readUtf8TextFile(String path) {

            StringBuilder sb = new StringBuilder();
            try {
                File fileDir = new File(path);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(new FileInputStream(fileDir), StandardCharsets.UTF_8));

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
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return sb.toString();
        }

        static void writeUtf8TextFile(String path, String str) {
            File file = new File(path);

            Writer out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                out.write(str);
                out.flush();
                out.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    static class Path {
        static String combine(String... paths) {
            File file = new File(paths[0]);

            for (int i = 1; i < paths.length; i++) {
                file = new File(file, paths[i]);
            }

            return file.getPath();
        }
    }

    // Add these new methods to the SchemaTool class
    private ExecutionOptions createExecutionOptions() {
        return new ExecutionOptions() {
            @Override
            public boolean shouldManageNamespaces() {
                return true;
            }

            @Override
            public Map<String, Object> getConfigurationValues() {
                return Collections.emptyMap();
            }

            @Override
            public ExceptionHandler getExceptionHandler() {
                return ExceptionHandlerHaltImpl.INSTANCE;
            }

            @Override
            public SchemaFilter getSchemaFilter() {
                return SchemaFilter.ALL;
            }
        };
    }

    private SourceDescriptor createSourceDescriptor() {
        return new SourceDescriptor() {
            @Override
            public SourceType getSourceType() {
                return SourceType.METADATA;
            }

            @Override
            public ScriptSourceInput getScriptSourceInput() {
                return null;
            }
        };
    }

}

// Update ScriptTargetOutputImpl to implement TargetDescriptor instead of
// GenerationTarget
class ScriptTargetOutputImpl implements TargetDescriptor {
    private final String filePath;
    private final ScriptTargetOutput scriptTargetOutput;

    public ScriptTargetOutputImpl(String filePath) {
        this.filePath = filePath;
        this.scriptTargetOutput = new FileScriptTargetOutput(filePath);
    }

    @Override
    public EnumSet<TargetType> getTargetTypes() {
        return EnumSet.of(TargetType.SCRIPT);
    }

    @Override
    public ScriptTargetOutput getScriptTargetOutput() {
        return scriptTargetOutput;
    }

    private static class FileScriptTargetOutput implements ScriptTargetOutput {
        private final String filePath;
        private Writer writer;

        public FileScriptTargetOutput(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void prepare() {
            try {
                this.writer = new FileWriter(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create file: " + filePath, e);
            }
        }

        @Override
        public void accept(String command) {
            if (writer == null) {
                throw new IllegalStateException("prepare() must be called before accept()");
            }
            try {
                writer.write(command);
                writer.write(System.lineSeparator());
            } catch (IOException e) {
                throw new RuntimeException("Unable to write to file", e);
            }
        }

        @Override
        public void release() {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // Log the exception or handle it as appropriate for your application
                    e.printStackTrace();
                }
            }
        }
    }
}

