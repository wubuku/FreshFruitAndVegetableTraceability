package org.dddml.ffvtraceability.config;

import org.dddml.ffvtraceability.domain.hibernate.TenantInterceptor;
import org.dddml.ffvtraceability.specialization.NullReadOnlyProxyGenerator;
import org.dddml.ffvtraceability.specialization.ReadOnlyProxyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.SessionFactory;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.Arrays;

// Enables Spring's annotation-driven transaction management capability, similar to the support found in Spring's <tx:*> XML namespace.
@EnableTransactionManagement
@org.springframework.context.annotation.Configuration
public class DatabaseConfig {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DataSource dataSource;

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

        Properties hibernateProperties = Environment.getProperties();
        hibernateProperties.put(Environment.INTERCEPTOR, entityInterceptor());
        hibernateProperties.put(Environment.DATASOURCE, dataSource);
        configuration.setProperties(hibernateProperties);

        return configuration.buildSessionFactory();
    }

    // @Bean
    // public EntityManagerFactory entityManagerFactory(SessionFactory sessionFactory) {
    //     LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    //     em.setDataSource(dataSource);
    //     em.setPackagesToScan("org.dddml.ffvtraceability.*");
    //     em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    //     Properties jpaProperties = new Properties();
    //     jpaProperties.put("hibernate.session_factory", sessionFactory);
    //     em.setJpaProperties(jpaProperties);
    //     em.afterPropertiesSet();
    //     return em.getObject();
    // }

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
