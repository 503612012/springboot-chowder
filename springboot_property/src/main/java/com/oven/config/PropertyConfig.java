package com.oven.config;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

@Slf4j
public class PropertyConfig {

    /**
     * 生成Properties对象
     */
    public static Properties loadProperties() {
        return loadPropertiesFromDb();
    }

    /**
     * 从数据库中加载配置信息
     */
    private static Properties loadPropertiesFromDb() {
        Properties properties = new Properties();
        InputStream in = PropertyConfig.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(in);
        } catch (Exception e) {
            log.error("加载配置文件异常，异常信息：", e);
            return null;
        }
        String driverClassName = properties.getProperty("driver");
        String url = properties.getProperty("url");
        String userName = properties.getProperty("username");
        String password = properties.getProperty("password");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName(driverClassName);
            String sql = "select * from t_properties";
            conn = DriverManager.getConnection(url, userName, password);
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            log.info("从数据库中加载配置信息...");
            while (rs.next()) {
                String key = rs.getString("key");
                String value = rs.getString("value");
                log.info(" {} --- {}", key, value);
                properties.put(key, value);
            }
            return properties;
        } catch (Exception e) {
            log.error("加载系统配置表异常，异常信息：", e);
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
