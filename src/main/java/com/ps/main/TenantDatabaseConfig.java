package com.ps.main;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ps.config.tenant.CurrentTenantIdentifierResolverImpl;
import com.ps.config.tenant.DataSourceBasedMultiTenantConnectionProviderImpl;


@Configuration
@AutoConfigureAfter(MasterDBConfiguration.class)
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.ps.services.dao.repository.tenant","com.ps.entities.tenant" })
@EnableJpaRepositories(basePackages = { "com.ps.services.dao.repository.tenant" },
						entityManagerFactoryRef = "tenantEntityManagerFactory", 
						transactionManagerRef = "tenantTransactionManager")
public class TenantDatabaseConfig {

    private static final Logger LOG = Logger.getLogger(TenantDatabaseConfig.class);


    @Bean(name = "tenantJpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }
    
    /**
     * The multi tenant connection provider
     * 
     * @return
     */
    @Bean(name = "datasourceBasedMultitenantConnectionProvider")
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        // Autowires the multi connection provider
        return new DataSourceBasedMultiTenantConnectionProviderImpl();
    }

    /**
     * The current tenant identifier resolver
     * 
     * @return
     */
    @Bean(name = "currentTenantIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolverImpl();
    }

    /**
     * Creates the entity manager factory bean which is required to access the
     * JPA functionalities provided by the JPA persistence provider, i.e.
     * Hibernate in this case.
     * 
     * @param connectionProvider
     * @param tenantResolver
     * @return
     */
    @Bean(name = "tenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("datasourceBasedMultitenantConnectionProvider") 
            MultiTenantConnectionProvider connectionProvider,
            @Qualifier("currentTenantIdentifierResolver") 
            CurrentTenantIdentifierResolver tenantResolver) {

        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        //All tenant related entities, repositories and service classes must be scanned
        emfBean.setPackagesToScan("com.ps.entities.tenant");
        emfBean.setJpaVendorAdapter(jpaVendorAdapter());
        emfBean.setPersistenceUnitName("tenantdb-persistence-unit");
        Map<String, Object> properties = new HashMap<>();
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT,
                MultiTenancyStrategy.SCHEMA);
        properties.put(
                org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER,
                connectionProvider);
        properties.put(
                org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,
                tenantResolver);
        // ImprovedNamingStrategy is deprecated and unsupported in Hibernate 5
        // properties.put("hibernate.ejb.naming_strategy",
        // "org.hibernate.cfg.ImprovedNamingStrategy");
        properties.put(org.hibernate.cfg.Environment.DIALECT,
                "org.hibernate.dialect.SQLServerDialect");
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
        properties.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "none");

        emfBean.setJpaPropertyMap(properties);
        LOG.info("tenantEntityManagerFactory set up successfully!");
        return emfBean;
    }
    
    @Bean(name = "tenantTransactionManager")
	@PersistenceContext(unitName="tenantdb-persistence-unit")
    public JpaTransactionManager transactionManager(
    		@Qualifier("tenantEntityManagerFactory")
            EntityManagerFactory tenantEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tenantEntityManager);
        return transactionManager;
    }

}