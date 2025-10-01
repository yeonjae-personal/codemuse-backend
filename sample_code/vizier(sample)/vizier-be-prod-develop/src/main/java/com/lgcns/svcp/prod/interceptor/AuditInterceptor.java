package com.lgcns.svcp.prod.interceptor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.lgcns.svcp.prod.context.UserContext;

/**
 * MyBatis Interceptor to automatically populate audit fields.
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class AuditInterceptor implements Interceptor {

    private static final String UPD_DTM = "updDtm";
    private static final String UPD_USER = "updUser";
    private static final String RGST_DTM = "rgstDtm";
    private static final String RGST_USER = "rgstUser";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1]; // Retrieve parameter object
        if (parameter != null) {
            String currentUser = UserContext.getCurrentUser(); // Retrieve current user from ThreadLocal
            String dtm = LocalDateTime.now().toString();
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

            // Automatically populate audit fields
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                setField(parameter, RGST_USER, currentUser);
                setField(parameter, RGST_DTM, dtm);
                setField(parameter, UPD_USER, currentUser);
                setField(parameter, UPD_DTM, dtm);
            } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                setField(parameter, UPD_USER, currentUser);
                setField(parameter, UPD_DTM, dtm);
            }
            // 25.07.14 임시처리
            // insert에는 save, create 단어도 포함
            else if (isInsertLike(mappedStatement.getId())) {
                setField(parameter, RGST_USER, currentUser);
                setField(parameter, RGST_DTM, dtm);
                setField(parameter, UPD_USER, currentUser);
                setField(parameter, UPD_DTM, dtm);
            }
            // update는 "update"만 포함
            else if (mappedStatement.getId().contains("update")) {
                setField(parameter, UPD_USER, currentUser);
                setField(parameter, UPD_DTM, dtm);
            }
        }
        return invocation.proceed();
    }
    // 25.07.14 임시처리
    private boolean isInsertLike(String sqlId) {
        return sqlId != null && (
                sqlId.contains("insert") ||
                        sqlId.contains("save") ||
                        sqlId.matches("create")
        );
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // No properties needed
    }

    /**
     * Sets the value of a field on a given object (including fields in parent classes).
     * If the target object is a Map<String, Object>, updates or adds the key-value pair.
     * Silently ignores errors when trying to add to the Map.
     *
     * @param targetObject the object whose field value needs to be set
     * @param fieldName    the exact name of the field or the key in the Map
     * @param fieldValue   the value to set on the field or the value to associate with the key in the Map
     */
    public static void setField(Object targetObject, String fieldName, Object fieldValue) {
        // Check if the targetObject is an instance of Map<String, Object>
        if (targetObject instanceof Map) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) targetObject;
                // Update the map with the key-value pair
                map.put(fieldName, fieldValue);
                return;
            } catch (ClassCastException e) {
                // Silently ignore if the targetObject is not a Map<String, Object>
                return;
            }
        }

        Field field = null;

        // Look for the field in the current class and its parent classes
        Class<?> targetClass = targetObject.getClass();
        while (targetClass != null) {
            try {
                // Try to get the field from the current class
                field = targetClass.getDeclaredField(fieldName);
                break; // Exit the loop if the field is found
            } catch (NoSuchFieldException e) {
                // If the field is not found, move up to the superclass
                targetClass = targetClass.getSuperclass();
            }
        }

        // If no field was found, do nothing
        if (field == null) {
            return;
        }

        // Allow access to private/protected fields
        field.setAccessible(true);

        try {
            // Set the value of the field on the target object
            field.set(targetObject, fieldValue);
        } catch (IllegalAccessException e) {
            // Handle the exception if the field cannot be accessed
            return;
        }
    }
}