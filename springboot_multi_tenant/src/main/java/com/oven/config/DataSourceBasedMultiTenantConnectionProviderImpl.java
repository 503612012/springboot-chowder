package com.oven.config;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    @Resource(name = "defaultDataSource")
    private DataSource defaultDataSource;

    @Resource
    private ApplicationContext context;

    static Map<String, DataSource> map = new HashMap<>();

    public static boolean init = false;

    @PostConstruct
    public void load() {
        map.put("defaultDataSource", defaultDataSource);
    }

    @Override
    public DataSource selectAnyDataSource() {
        return map.get("defaultDataSource");
    }

    @Override
    public DataSource selectDataSource(String tenant) {
        if (!init) {
            init = true;
            TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
            map.putAll(tenantDataSource.getAll());
        }
        return map.get(tenant);

//        // 懒加载模式
//        DataSource dataSource = map.get(tenantIdentifier);
//        if (dataSource == null) {
//            TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
//            dataSource = tenantDataSource.getDataSource(tenantIdentifier);
//            map.put(tenantIdentifier, dataSource);
//        }
//        return dataSource;
    }

}
