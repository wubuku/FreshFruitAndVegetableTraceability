package org.dddml.ffvtraceability.config;

import org.dddml.ffvtraceability.domain.hibernate.TenantInterceptor;
import org.dddml.ffvtraceability.specialization.NullReadOnlyProxyGenerator;
import org.dddml.ffvtraceability.specialization.ReadOnlyProxyGenerator;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

// Enables Spring's annotation-driven transaction management capability, similar to the support found in Spring's <tx:*> XML namespace.
@EnableTransactionManagement
@org.springframework.context.annotation.Configuration
public class DatabaseConfig {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DataSource dataSource;

    // 注入 Spring 配置
    @Autowired
    private org.springframework.core.env.Environment springEnvironment;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @Bean(name = {
            "hibernateSessionFactory",
            "entityManagerFactory"
    })
    public SessionFactory hibernateSessionFactory() throws IOException {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        // Load HBM XML mappings
        Arrays.stream(ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                        .getResources("classpath:/hibernate/*.hbm.xml"))
                .forEach(resource -> {
                    try {
                        configuration.addInputStream(resource.getInputStream());
                    } catch (IOException e) {
                        throw new RuntimeException("Error loading Hibernate mapping file: " + resource.getFilename(),
                                e);
                    }
                });

        // Scan for annotated entities
        configuration.addPackage("org.dddml.ffvtraceability.*");
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        // Read config from application-test.yml
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.dialect", hibernateDialect);
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto",
                springEnvironment.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.show_sql",
                springEnvironment.getProperty("spring.jpa.show-sql", "true"));
        hibernateProperties.setProperty("hibernate.connection.pool_size",
                springEnvironment.getProperty("spring.jpa.properties.hibernate.connection.pool_size", "1"));
        hibernateProperties.setProperty("hibernate.cache.provider_class",
                springEnvironment.getProperty("spring.jpa.properties.hibernate.cache.provider_class"));
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache",
                springEnvironment.getProperty("spring.jpa.properties.hibernate.cache.use_second_level_cache"));

        hibernateProperties.put(Environment.INTERCEPTOR, entityInterceptor());
        hibernateProperties.put(Environment.DATASOURCE, dataSource);
        configuration.setProperties(hibernateProperties);

        return configuration.buildSessionFactory();
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean
    public ReadOnlyProxyGenerator stateReadOnlyProxyGenerator() {
        return new NullReadOnlyProxyGenerator();
    }

    @Bean
    public org.hibernate.Interceptor entityInterceptor() {
        return new TenantInterceptor();
    }
}
