package com.oven.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.oven.dao.oracle", entityManagerFactoryRef = "oracleEntityManagerFactoryRef", transactionManagerRef = "oracleTransactionManagerRef")
public class JpaConfigOracle {

    @Resource
    @Qualifier(value = "oracleDataSource")
    private DataSource oracleDataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactoryRef(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(oracleDataSource)
                .packages("com.oven.vo")
                .build();
    }

    @Bean
    @SuppressWarnings("ConstantConditions")
    public PlatformTransactionManager oracleTransactionManagerRef(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean oracleEntityManagerFactoryBean = oracleEntityManagerFactoryRef(builder);
        return new JpaTransactionManager(oracleEntityManagerFactoryBean.getObject());
    }

}
