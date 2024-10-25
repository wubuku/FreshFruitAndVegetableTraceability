package org.dddml.ffvtraceability.config;

import org.dddml.ffvtraceability.domain.hibernate.TenantInterceptor;
import org.dddml.ffvtraceability.specialization.NullReadOnlyProxyGenerator;
import org.dddml.ffvtraceability.specialization.ReadOnlyProxyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

// Enables Spring's annotation-driven transaction management capability, similar to the support found in Spring's <tx:*> XML namespace.
@EnableTransactionManagement
@Configuration
public class DatabaseConfig {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DataSource dataSource;


//    @Bean
//    public JpaTransactionManager transactionManager() throws IOException {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(hibernateSessionFactory().getObject());
//        return transactionManager;
//    }

    @Bean(name = {
            "hibernateSessionFactory",
            "entityManagerFactory"
    })
    public LocalSessionFactoryBean hibernateSessionFactory() throws IOException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMappingLocations(
                ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                        .getResources("classpath:/hibernate/*.hbm.xml")
        );
        sessionFactory.setPackagesToScan("org.dddml.ffvtraceability.*"); //add annotation mappings

        //hibernate.physical_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
        sessionFactory.setPhysicalNamingStrategy(new org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy());

        Properties hibernateProperties = org.hibernate.cfg.Environment.getProperties();
        hibernateProperties.put("hibernate.session_factory.interceptor", entityInterceptor());
        sessionFactory.setHibernateProperties(hibernateProperties);
        
        return sessionFactory;
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
