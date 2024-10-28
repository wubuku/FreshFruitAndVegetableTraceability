package org.dddml.ffvtraceability;

import org.dddml.ffvtraceability.specialization.DomainError;
import org.dddml.ffvtraceability.tool.ApplicationServiceReflectUtils;
import org.dddml.ffvtraceability.tool.JsonEntityDataTool;
import org.dddml.ffvtraceability.tool.hibernate.SchemaTool;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import picocli.CommandLine;
import picocli.CommandLine.Mixin;

import java.util.*;

@SpringBootApplication
public class ServiceCli {
    private static ConfigurableApplicationContext applicationContext;

    public static void main(final String[] args) throws Exception {
        // Parse command line arguments using Picocli
        CommandLine commandLine = new CommandLine(new TopLevelCommand());
        CommandLine.ParseResult parseResult = commandLine.parseArgs(args);

        // Create Spring ApplicationContext with parsed arguments
        if (parseResult.hasSubcommand()) {
            Object subcommand = parseResult.subcommands().get(0).commandSpec().userObject();
            if (subcommand instanceof DatabaseOptionsAware) {
                applicationContext = createApplicationContext(((DatabaseOptionsAware) subcommand).getDatabaseOptions());
            } else {
                applicationContext = createApplicationContext(null);
            }
        } else {
            applicationContext = createApplicationContext(null);
        }

        // Call 'ddl' subcommand or 'initData' subcommand
        if (parseResult.hasSubcommand()) {
            Object subcommand = parseResult.subcommands().get(0).commandSpec().userObject();
            if (subcommand instanceof DdlSubcommand) {
                ddl((DdlSubcommand) subcommand);
            } else if (subcommand instanceof InitDataSubcommand) {
                initData((InitDataSubcommand) subcommand);
            }
        } else {
            // Print usage information if no subcommand is provided
            commandLine.usage(System.out);
        }

        // Close the ApplicationContext
        applicationContext.close();

        System.exit(0);
    }

    private static ConfigurableApplicationContext createApplicationContext(DatabaseOptions databaseOptions) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ServiceCli.class)
                .bannerMode(Banner.Mode.OFF);

        if (databaseOptions != null) {
            ConfigurableEnvironment environment = new StandardEnvironment();
            Map<String, Object> overrides = new HashMap<>();

            if (databaseOptions.connectionUrl != null) {
                overrides.put("spring.datasource.url", databaseOptions.connectionUrl);
            }
            if (databaseOptions.username != null) {
                overrides.put("spring.datasource.username", databaseOptions.username);
            }
            if (databaseOptions.password != null) {
                overrides.put("spring.datasource.password", databaseOptions.password);
            }

            environment.getPropertySources().addFirst(new MapPropertySource("commandLineArgs", overrides));
            builder.environment(environment);
        }

        return builder.run();
    }

    // ddl method definition
    public static void ddl(DdlSubcommand ddlSubcommand) {
        SchemaTool t = new SchemaTool();

        t.setSqlDirectory(ddlSubcommand.sqlDirectory);
        DatabaseOptions databaseOptions = ddlSubcommand.getDatabaseOptions();
        t.setConnectionUrl(databaseOptions.connectionUrl);
        if (databaseOptions.username != null && !databaseOptions.username.isEmpty()) {
            t.setConnectionUsername(databaseOptions.username);
        }
        if (databaseOptions.password != null && !databaseOptions.password.isEmpty()) {
            t.setConnectionPassword(databaseOptions.password);
        }
        t.setUp();

        t.hbm2DdlOutput();
        System.out.println("DDL output generated successfully.");
        t.copyAndFixHbm2DdlCreateSql();
        System.out.println("DDL copied and fixed successfully.");

        if (!ddlSubcommand.onlyOutputDdl) {
            t.dropCreateDatabaseAndSeed();
            System.out.println("Database dropped, recreated, and seeded successfully.");
        }
    }

    public static void initData(InitDataSubcommand initDataSubcommand) {
        if (initDataSubcommand.xml) {
            // TODO: createEntitiesFromXmlData(initDataSubcommand.dataLocationPattern);
        }
        if (initDataSubcommand.json) {
            createEntitiesFromJsonData(initDataSubcommand.dataLocationPattern);
        }
    }

    public static void createEntitiesFromJsonData(String jsonDataLocationPattern) {
        String pathPattern = JsonEntityDataTool.DEFAULT_JSON_DATA_LOCATION_PATTERN;
        if (jsonDataLocationPattern != null) {
            pathPattern = jsonDataLocationPattern;
        }
        Map<String, List<Object>> allData = JsonEntityDataTool.deserializeAllGroupByEntityName(pathPattern);
        for (Map.Entry<String, List<Object>> kv : allData.entrySet()) {
            for (Object e : kv.getValue()) {
                try {
                    ApplicationServiceReflectUtils.invokeApplicationServiceInitializeMethod(kv.getKey(), e);
                } catch (Exception ex) {
                    if (isCausedByConstraintViolation(ex)) {
                        System.err.println("Constraint violation occurred while initializing entity " + kv.getKey()
                                + ". Continuing with next entity.");
                        ex.printStackTrace(System.err);
                    } else {
                        System.err.println("Error occurred while initializing entity " + kv.getKey() + ".");
                        ex.printStackTrace(System.err);
                        throw new RuntimeException("Failed to initialize entity " + kv.getKey(), ex);
                    }
                }
            }
        }
    }

    // Create a top-level command with subcommands 'ddl' and 'initData'
    @CommandLine.Command(subcommands = {
            DdlSubcommand.class,
            InitDataSubcommand.class
    })
    static class TopLevelCommand {
    }

    // DdlSubcommand class definition
    @CommandLine.Command(name = "ddl", mixinStandardHelpOptions = true, description = "Generate and execute DDL (Data Definition Language) scripts for database schema management.")
    static class DdlSubcommand implements DatabaseOptionsAware {
        @Mixin
        private DatabaseOptions databaseOptions = new DatabaseOptions();

        @CommandLine.Option(names = { "-d",
                "--sqlDirectory" }, required = true, description = "Directory path for input and output of SQL scripts. "
                        +
                        "Generated DDL scripts will be saved here, and existing scripts may be read from this location.")
        String sqlDirectory;

        @CommandLine.Option(names = {
                "--only-output" }, description = "Only output the DDL scripts, do not drop-create database and seed.")
        boolean onlyOutputDdl;

        @Override
        public DatabaseOptions getDatabaseOptions() {
            return databaseOptions;
        }
    }

    @CommandLine.Command(name = "initData", mixinStandardHelpOptions = true, description = "Initialize the database with seed data from XML or JSON files.")
    static class InitDataSubcommand implements DatabaseOptionsAware {
        @Mixin
        private DatabaseOptions databaseOptions = new DatabaseOptions();

        @CommandLine.Option(names = { "-d",
                "--dataLocationPattern" }, required = true, description = "File path pattern to locate data files. " +
                        "Use Spring Resource Patterns like 'file:{PATH_TO}/*.json' or 'classpath*:/data/*.json' to specify multiple files.")
        String dataLocationPattern;

        @CommandLine.Option(names = { "-x",
                "--xml" }, required = false, description = "Use XML files for data initialization. " +
                        "If specified, the tool will look for XML files matching the data location pattern.")
        boolean xml;

        @CommandLine.Option(names = { "-j",
                "--json" }, required = false, description = "Use JSON files for data initialization. " +
                        "If specified, the tool will look for JSON files matching the data location pattern. " +
                        "The JSON file name must match '{EntityName}Data.json'.")
        boolean json;

        @Override
        public DatabaseOptions getDatabaseOptions() {
            return databaseOptions;
        }
    }

    // DatabaseOptions mixin class
    static class DatabaseOptions {
        @CommandLine.Option(names = { "-c", "--connectionUrl" }, description = "JDBC connection URL for the database. "
                +
                "Example: jdbc:mysql://localhost:3306/mydatabase")
        String connectionUrl;

        @CommandLine.Option(names = { "-u", "--username" }, description = "Username for database authentication.")
        String username;

        @CommandLine.Option(names = { "-p", "--password" }, description = "Password for database authentication.")
        String password;
    }

    // Interface for commands that use DatabaseOptions
    interface DatabaseOptionsAware {
        DatabaseOptions getDatabaseOptions();
    }

    private static boolean isCausedByConstraintViolation(Exception ex) {
        boolean b = false;
        Throwable c = ex;
        while (c != null) {
            if (c.getClass().getName().endsWith("ConstraintViolationException")) {
                b = true;
                break;
            }
            if (c.getMessage() != null && c.getMessage().startsWith("Duplicate entry")) {
                b = true;
                break;
            }
            if (c.getMessage() != null && c.getMessage().startsWith("[rebirth]")) {
                b = true;
                break;
            }
            if (c instanceof DomainError) {
                DomainError domainError = (DomainError) c;
                if (domainError.getName().equalsIgnoreCase("rebirth")) {
                    b = true;
                    break;
                }
            }
            c = c.getCause();
        }
        return b;
    }

}
