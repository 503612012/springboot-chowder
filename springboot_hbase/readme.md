# springboot炖hbase
### 1. 先睹为快
### 2. 实现原理
#### 2.1 docker中安装hbase环境
##### 2.1.1 拉取镜像
```shell script
docker pull harisekhon/hbase
```
##### 2.1.2 启动容器
```shell script
docker run -d -h docker-hbase \
        -p 2181:2181 \
        -p 8080:8080 \
        -p 8085:8085 \
        -p 9090:9090 \
        -p 9000:9000 \
        -p 9095:9095 \
        -p 16000:16000 \
        -p 16010:16010 \
        -p 16201:16201 \
        -p 16301:16301 \
        -p 16020:16020\
        --name hbase \
        harisekhon/hbase
```
#### 2.2 新建项目
#### 2.2.1 创建maven目录结构，以及pom.xml文件
#### 2.2.2 pom.xml文件中加入依赖
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.5.RELEASE</version>
    <relativePath/>
</parent>
```
#### 2.2.3 pom.xml文件中加入springboot-starter依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
    </dependency>

    <dependency>
        <groupId>org.apache.hbase</groupId>
        <artifactId>hbase-client</artifactId>
        <version>2.2.4</version>
    </dependency>
</dependencies>
```
#### 2.2.4 pom.xml文件中加入maven-springboot打包插件
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```
#### 2.2.5 开发启动类
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
#### 2.2.6 开发配置文件适配类
```java
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "hbase")
public class HbaseProperties {

    private Map<String, String> config;

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

}
```
#### 2.2.7 开发hbase配置类
```java
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

@Configuration
@EnableConfigurationProperties(HbaseProperties.class)
public class HbaseConfig {

    private final HbaseProperties properties;

    public HbaseConfig(HbaseProperties properties) {
        this.properties = properties;
    }

    public org.apache.hadoop.conf.Configuration configuration() {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        Map<String, String> config = properties.getConfig();
        Set<String> keySet = config.keySet();
        for (String key : keySet) {
            configuration.set(key, config.get(key));
        }
        return configuration;
    }

}
```
#### 2.2.8 开发hbase客户端
```java
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@DependsOn("hbaseConfig")
public class HBaseClient {

    @Resource
    private HbaseConfig config;

    private static Admin admin = null;
    private static Connection connection = null;

    @PostConstruct
    private void init() {
        if (connection != null) {
            return;
        }
        try {
            connection = ConnectionFactory.createConnection(config.configuration());
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String tableName, String... columnFamilies) throws IOException {
        TableName name = TableName.valueOf(tableName);
        boolean isExists = this.tableExists(tableName);
        if (isExists) {
            throw new TableExistsException(tableName + "is exists!");
        }
        TableDescriptorBuilder descriptorBuilder = TableDescriptorBuilder.newBuilder(name);
        List<ColumnFamilyDescriptor> columnFamilyList = new ArrayList<>();
        for (String columnFamily : columnFamilies) {
            ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder
                    .newBuilder(columnFamily.getBytes()).build();
            columnFamilyList.add(columnFamilyDescriptor);
        }
        descriptorBuilder.setColumnFamilies(columnFamilyList);
        TableDescriptor tableDescriptor = descriptorBuilder.build();
        admin.createTable(tableDescriptor);
    }

    public void insertOrUpdate(String tableName, String rowKey, String columnFamily, String column, String value) throws IOException {
        this.insertOrUpdate(tableName, rowKey, columnFamily, new String[]{column}, new String[]{value});
    }

    public void insertOrUpdate(String tableName, String rowKey, String columnFamily, String[] columns, String[] values)
            throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        for (int i = 0; i < columns.length; i++) {
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columns[i]), Bytes.toBytes(values[i]));
            table.put(put);
        }
    }

    public void deleteRow(String tableName, String rowKey) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(rowKey.getBytes());
        table.delete(delete);
    }

    public void deleteColumnFamily(String tableName, String rowKey, String columnFamily) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(rowKey.getBytes());
        delete.addFamily(Bytes.toBytes(columnFamily));
        table.delete(delete);
    }

    public void deleteColumn(String tableName, String rowKey, String columnFamily, String column) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(rowKey.getBytes());
        delete.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
        table.delete(delete);
    }

    public void deleteTable(String tableName) throws IOException {
        boolean isExists = this.tableExists(tableName);
        if (!isExists) {
            return;
        }
        TableName name = TableName.valueOf(tableName);
        admin.disableTable(name);
        admin.deleteTable(name);
    }

    public String getValue(String tableName, String rowkey, String family, String column) {
        Table table;
        String value = "";
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(family) || StringUtils.isBlank(rowkey) || StringUtils.isBlank(column)) {
            return null;
        }
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            Get g = new Get(rowkey.getBytes());
            g.addColumn(family.getBytes(), column.getBytes());
            Result result = table.get(g);
            List<Cell> ceList = result.listCells();
            if (ceList != null && ceList.size() > 0) {
                for (Cell cell : ceList) {
                    value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public String selectOneRow(String tableName, String rowKey, String colFamily) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes());
        get.addColumn(colFamily.getBytes(), "name".getBytes());
        get.addColumn(colFamily.getBytes(), "age".getBytes());
        Result result = table.get(get);
        table.close();
        if (result == null) {
            return "";
        }
        return new String(result.getValue(colFamily.getBytes(), "name".getBytes())) + "-" + new String(result.getValue(colFamily.getBytes(), "age".getBytes()));
    }

    public boolean tableExists(String tableName) throws IOException {
        TableName[] tableNames = admin.listTableNames();
        if (tableNames != null && tableNames.length > 0) {
            for (TableName name : tableNames) {
                if (tableName.equals(name.getNameAsString())) {
                    return true;
                }
            }
        }
        return false;
    }

}
```
#### 2.2.9 开发测试控制层
```java
import com.oven.config.HBaseClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    private static final String TABLE_NAME = "user";
    private static final String TABLE_FAMILY_1 = "pet";
    private static final String TABLE_FAMILY_2 = "car";

    @Resource
    private HBaseClient hBaseClient;

    @RequestMapping("/createTable")
    public String createTable() {
        try {
            hBaseClient.createTable(TABLE_NAME, TABLE_FAMILY_1, TABLE_FAMILY_2);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/deleteTable")
    public String deleteTable() {
        try {
            hBaseClient.deleteTable(TABLE_NAME);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/add")
    public String add() {
        try {
            hBaseClient.insertOrUpdate(TABLE_NAME, "1", TABLE_FAMILY_1, "name", "金毛");
            hBaseClient.insertOrUpdate(TABLE_NAME, "1", TABLE_FAMILY_1, "age", "2");
            hBaseClient.insertOrUpdate(TABLE_NAME, "1", TABLE_FAMILY_1, "sex", "男");
            hBaseClient.insertOrUpdate(TABLE_NAME, "1", TABLE_FAMILY_2, "brand", "宝马");
            hBaseClient.insertOrUpdate(TABLE_NAME, "1", TABLE_FAMILY_2, "price", "100W");

            hBaseClient.insertOrUpdate(TABLE_NAME, "2", TABLE_FAMILY_1, "name", "英短");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/delete")
    public String delete(String id) {
        try {
            hBaseClient.deleteRow(TABLE_NAME, id);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/deleteColumnFamily")
    public String deleteColumnFamily(String id) {
        try {
            hBaseClient.deleteColumnFamily(TABLE_NAME, id, TABLE_FAMILY_2);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/deleteColumn")
    public String deleteColumn(String id) {
        try {
            hBaseClient.deleteColumn(TABLE_NAME, id, TABLE_FAMILY_1, "age");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/getValue")
    public String getValue(String id) {
        try {
            return hBaseClient.getValue(TABLE_NAME, id, TABLE_FAMILY_1, "name");
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/getById")
    public String getById(String id) {
        try {
            return hBaseClient.selectOneRow(TABLE_NAME, id, TABLE_FAMILY_1);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}
```
#### 2.2.10 编写配置文件
```yaml
hbase:
  config:
    hbase.zookeeper.quorum: 172.16.188.194
    hbase.zookeeper.port: 2181
    hbase.zookeeper.znode: /hbase
    hbase.client.keyvalue.maxsize: 1572864000
```
#### 2.2.11 在本机配置hbase所在机器的host
```shell script
172.16.188.194 docker-hbase
```
#### 2.3 编译打包运行
### 3. 应用场景