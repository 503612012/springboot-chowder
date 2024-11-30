// package com.oven.sqlite.utils;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.BeanUtils;
// import org.springframework.beans.BeanWrapper;
// import org.springframework.beans.NotWritablePropertyException;
// import org.springframework.beans.PropertyAccessorFactory;
// import org.springframework.dao.DataRetrievalFailureException;
// import org.springframework.dao.InvalidDataAccessApiUsageException;
// import org.springframework.jdbc.core.RowMapper;
// import org.springframework.jdbc.support.JdbcUtils;
// import org.springframework.util.Assert;
// import org.springframework.util.StringUtils;
//
// import javax.persistence.Column;
// import java.beans.PropertyDescriptor;
// import java.lang.reflect.Field;
// import java.lang.reflect.Method;
// import java.sql.ResultSet;
// import java.sql.ResultSetMetaData;
// import java.sql.SQLException;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.Map;
// import java.util.Set;
//
// /**
//  * JDBC关系映射工具类
//  */
// @Slf4j
// public class PropertyRowMapper<T> implements RowMapper<T> {
//
//     private Class<T> mappedClass;
//     private Map<String, PropertyDescriptor> mappedFields;
//     private Set<String> mappedProperties;
//
//     public PropertyRowMapper(Class<T> mappedClass) {
//         this.initialize(mappedClass);
//     }
//
//     public static <T> PropertyRowMapper<T> build(Class<T> mappedClass) {
//         return new PropertyRowMapper<>(mappedClass);
//     }
//
//     private void initialize(Class<T> mappedClass) {
//         this.mappedClass = mappedClass;
//         this.mappedFields = new HashMap<>();
//         this.mappedProperties = new HashSet<>();
//         PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
//
//         for (PropertyDescriptor pd : pds) {
//             String propertyName = pd.getName();
//             Method method = pd.getWriteMethod();
//             if (method != null) {
//                 Column column = this.getClassFieldColumnInfo(mappedClass, propertyName);
//                 String underscoredName;
//                 if (column != null) {
//                     underscoredName = column.name();
//                     this.mappedFields.put(underscoredName.toLowerCase(), pd);
//                 } else {
//                     this.mappedFields.put(pd.getName().toLowerCase(), pd);
//                     underscoredName = this.underscoreName(pd.getName());
//                     if (!pd.getName().toLowerCase().equals(underscoredName)) {
//                         this.mappedFields.put(underscoredName, pd);
//                     }
//                 }
//
//                 this.mappedProperties.add(pd.getName());
//             }
//         }
//     }
//
//     private Column getClassFieldColumnInfo(Class<T> mappedClass, String propertyName) {
//         Column column = null;
//         Field[] fields = mappedClass.getDeclaredFields();
//
//         for (Field f : fields) {
//             if (f.getName().equals(propertyName)) {
//                 column = f.getAnnotation(Column.class);
//                 break;
//             }
//         }
//
//         return column;
//     }
//
//     private String underscoreName(String name) {
//         if (!StringUtils.hasLength(name)) {
//             return "";
//         } else {
//             StringBuilder result = new StringBuilder();
//             result.append(name.substring(0, 1).toLowerCase());
//
//             for (int i = 1; i < name.length(); ++i) {
//                 String s = name.substring(i, i + 1);
//                 String slc = s.toLowerCase();
//                 if (!s.equals(slc)) {
//                     result.append("_").append(slc);
//                 } else {
//                     result.append(s);
//                 }
//             }
//
//             return result.toString();
//         }
//     }
//
//     private boolean isCheckFullyPopulated() {
//         return false;
//     }
//
//     @Override
//     public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
//         Assert.state(this.mappedClass != null, "Mapped class was not specified");
//         T mappedObject = BeanUtils.instantiateClass(this.mappedClass);
//         BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
//         this.initBeanWrapper();
//         ResultSetMetaData rsmd = rs.getMetaData();
//         int columnCount = rsmd.getColumnCount();
//         Set<String> populatedProperties = this.isCheckFullyPopulated() ? new HashSet<>() : null;
//
//         for (int index = 1; index <= columnCount; ++index) {
//             String column = JdbcUtils.lookupColumnName(rsmd, index);
//             PropertyDescriptor pd = this.mappedFields.get(column.replaceAll(" ", "").toLowerCase());
//             if (pd != null) {
//                 try {
//                     Object value = this.getColumnValue(rs, index, pd);
//                     if (log.isDebugEnabled() && rowNumber == 0) {
//                         log.debug("Mapping column '" + column + "' to property '" + pd.getName() + "' of type " + pd.getPropertyType());
//                     }
//                     bw.setPropertyValue(pd.getName(), value);
//                     if (populatedProperties != null) {
//                         populatedProperties.add(pd.getName());
//                     }
//                 } catch (NotWritablePropertyException var14) {
//                     throw new DataRetrievalFailureException("Unable to map column " + column + " to property " + pd.getName(), var14);
//                 }
//             }
//         }
//
//         if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
//             throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields necessary to populate object of class [" + this.mappedClass + "]: " + this.mappedProperties);
//         } else {
//             return mappedObject;
//         }
//     }
//
//     private void initBeanWrapper() {
//     }
//
//     private Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
//         return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
//     }
//
// }