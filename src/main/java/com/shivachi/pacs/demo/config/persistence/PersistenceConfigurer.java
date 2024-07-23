package com.shivachi.pacs.demo.config.persistence;

import com.shivachi.pacs.demo.config.properties.PersistenceProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.logging.Level;

@Log
@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
public class PersistenceConfigurer implements TransactionManagementConfigurer {
    private final PersistenceProperties persistenceProperties;
    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return null;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties hibernateAdditionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.jdbc.time_zone", "Africa/Nairobi");
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.use_sql_comments", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");

        properties.setProperty("hibernate.generate_statistics", "false");
        properties.setProperty("hibernate.id.new_generator_mappings", String.valueOf(this.persistenceProperties.isGenerateMappings()));
        properties.setProperty("hibernate.jdbc.lob.non_contextual_creation", String.valueOf(this.persistenceProperties.isLobNonContextualCreation()));

        properties.setProperty("hibernate.connection.driver_class", this.persistenceProperties.getDriverClass());
        properties.setProperty("hibernate.dialect", this.persistenceProperties.getDialect());
        return properties;
    }

    @Bean(name = "datasource")
    @Primary
    public DataSource dataSource() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", this.persistenceProperties.getHost().trim(), this.persistenceProperties.getPort(), this.persistenceProperties.getDatabase().trim()));
            config.setSchema(this.persistenceProperties.getSchema());
            config.setUsername(this.persistenceProperties.getUsername().trim());
            config.setPassword(this.persistenceProperties.getPassword().trim());

            config.setMaximumPoolSize(this.persistenceProperties.getHikariPoolMaxConnections());
            config.setPoolName(this.persistenceProperties.getHikariPoolName());
            config.setConnectionTimeout(this.persistenceProperties.getHikariPoolConnectionTimeout());
            config.setIdleTimeout(this.persistenceProperties.getHikariPoolIdleTimeout());
            config.setMaxLifetime(this.persistenceProperties.getHikariPoolMaxLifetime());
            config.setValidationTimeout(this.persistenceProperties.getHikariPoolValidationTimeout());
            config.setLeakDetectionThreshold(this.persistenceProperties.getHikariPoolLeakDetectionThreshold());

            config.addDataSourceProperty("cachePrepStmts", this.persistenceProperties.isCachePrepareStatements());
            config.addDataSourceProperty("prepStmtCacheSize", this.persistenceProperties.getCachePrepareStatementsSize());
            config.addDataSourceProperty("prepStmtCacheSqlLimit", this.persistenceProperties.getCachePrepareStatementsSqlLimit());
            config.setMinimumIdle(1);

            config.validate();
            return new HikariDataSource(config);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Datasource initialize error", e);
        }
        return null;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        try {
            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            vendorAdapter.setGenerateDdl(true);
            vendorAdapter.setDatabase(Database.MYSQL);
            vendorAdapter.setShowSql(true);

            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setJpaProperties(this.hibernateAdditionalProperties());
            em.setPackagesToScan(this.persistenceProperties.getPackagesToScan());
            em.setDataSource(dataSource());
            em.setPersistenceUnitName("pacs-db");
            em.setJpaVendorAdapter(vendorAdapter);
            return em;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Entity manager factory error", e);
            return new LocalContainerEntityManagerFactoryBean();
        }
    }

}
