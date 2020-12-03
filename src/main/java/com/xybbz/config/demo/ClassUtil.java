package com.xybbz.config.demo;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

public class ClassUtil {
    /**
     *
     * @param object   旧的对象带值
     * @param addMap   动态需要添加的属性和属性类型
     * @param addValMap  动态需要添加的属性和属性值
     * @return  新的对象
     * @throws Exception
     */
    public Object dynamicClass(Object object, HashMap<String, Class<?>> addMap, HashMap<String, Object> addValMap) throws Exception {
        //属性和属性值
        HashMap<String, Object> returnMap = new HashMap<>();
        //属性和属性类型
        HashMap<String, Class<?>> typeMap = new HashMap<>();

        //获得Object自带的属性和值
        Class<?> type = object.getClass();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (!"class".equals(propertyName)) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(object);
                //可以判断为 NULL不赋值
                returnMap.put(propertyName, result);
                typeMap.put(propertyName, descriptor.getPropertyType());
            }
        }

        //添加新的属性和值
        returnMap.putAll(addValMap);
        typeMap.putAll(addMap);
        //map转换成实体对象
        DynamicBean bean = new DynamicBean(typeMap);
        //赋值
        Set<String> keys = typeMap.keySet();
        for (Object o : keys) {
            String key = (String) o;
            bean.setValue(key, returnMap.get(key));
        }
        Object obj = bean.getObject();
        return obj;
    }

}
