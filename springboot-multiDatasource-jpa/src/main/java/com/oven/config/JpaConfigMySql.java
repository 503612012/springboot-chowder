package com.oven.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.oven.dao.mysql", entityManagerFactoryRef = "mysqlEntityManagerFactoryRef", transactionManagerRef = "mysqlTransactionManagerRef")
public class JpaConfigMySql {

    @Resource
    @Qualifier(value = "mysqlDataSource")
    private DataSource mysqlDataSource;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactoryRef(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(mysqlDataSource)
                .packages("com.oven.entity")
                .build();
    }

    @Bean
    @SuppressWarnings("ConstantConditions")
    public PlatformTransactionManager mysqlTransactionManagerRef(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactoryBean = mysqlEntityManagerFactoryRef(builder);
        return new JpaTransactionManager(mysqlEntityManagerFactoryBean.getObject());
    }

}
