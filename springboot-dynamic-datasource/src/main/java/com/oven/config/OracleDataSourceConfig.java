package com.oven.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Oracle数据源配置
 *
 * @author Oven
 */
@Configuration
@MapperScan(basePackages = "com.oven.mapper.oracle", sqlSessionFactoryRef = "oracleSqlSessionFactory")
public class OracleDataSourceConfig {

    @Resource
    @Qualifier("oracleDataSource")
    private DataSource oracleDataSource;

    @Bean
    @Primary
    public SqlSessionFactory oracleSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(oracleDataSource);
        return factoryBean.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate oracleSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(oracleSqlSessionFactory());
    }

}
