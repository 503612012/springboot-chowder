package com.oven.config.dao;

import com.oven.config.entity.DataSourceConfig;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class DataSourceConfigDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<DataSourceConfig> getAlll() {
        String sql = "select * from t_datasource_config";
        return this.jdbcTemplate.query(sql, resultMap());
    }

    public DataSourceConfig getByName(String name) {
        String sql = "select * from t_datasource_config where _name = ?";
        List<DataSourceConfig> list = this.jdbcTemplate.query(sql, resultMap(), name);
        return list.size() == 0 ? null : list.get(0);
    }

    private RowMapper<DataSourceConfig> resultMap() {
        return (rs, rowNum) -> {
            DataSourceConfig dataSourceConfig = new DataSourceConfig();
            dataSourceConfig.setId(rs.getLong("id"));
            dataSourceConfig.setName(rs.getString("_name"));
            dataSourceConfig.setPassword(rs.getString("_password"));
            dataSourceConfig.setUsername(rs.getString("username"));
            dataSourceConfig.setUrl(rs.getString("url"));
            dataSourceConfig.setDriverClassName(rs.getString("driverclassname"));
            return dataSourceConfig;
        };
    }

}
