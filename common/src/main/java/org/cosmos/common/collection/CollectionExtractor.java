package org.cosmos.common.collection;

import org.apache.commons.lang3.StringUtils;
import org.cosmos.common.reflect.ReflectionUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CollectionExtractor {

    /**
     * 提取集合中的对象的两个属性(通过Getter函数), 组合成Map.
     *
     * @param collection        来源集合.
     * @param keyPropertyName   要提取为Map中的Key值的属性名.
     * @param valuePropertyName 要提取为Map中的Value值的属性名.
     */
    public static Map extractToMap(final Collection<Object> collection, final String keyPropertyName,
                                   final String valuePropertyName) {
        Map map = new HashMap(collection.size());

        try {
            for (Object obj : collection) {
                map.put(ReflectionUtil.getProperty(obj, keyPropertyName),
                        ReflectionUtil.getProperty(obj, valuePropertyName));
            }
        } catch (Exception e) {
            throw ReflectionUtil.convertReflectionExceptionToUnchecked(e);
        }

        return map;
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     */
    public static List extractToList(final Collection collection, final String propertyName) {
        List list = new ArrayList(collection.size());

        try {
            for (Object obj : collection) {
                list.add(ReflectionUtil.getProperty(obj, propertyName));
            }
        } catch (Exception e) {
            throw ReflectionUtil.convertReflectionExceptionToUnchecked(e);
        }

        return list;
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     * @param separator    分隔符.
     */
    public static String extractToString(final Collection collection, final String propertyName,
                                         final String separator) {
        List list = extractToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
     */
    public static String convertToString(final Collection collection, final String separator) {
        return StringUtils.join(collection, separator);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div>。
     */
    public static String convertToString(final Collection collection, final String prefix, final String postfix) {
        StringBuilder builder = new StringBuilder();
        for (Object o : collection) {
            builder.append(prefix).append(o).append(postfix);
        }
        return builder.toString();
    }

    /**
     * 将对象的属性提取出来组成由属性为key的map
     */

    public static <T> Map<?, ?> extractBeanPropertyToMap(T bean) throws IntrospectionException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class<?> type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    /**
     * 将Map对应的key-value值转化成对应的对象
     */
    public static <T> T extractMapElementToBean(Map<?, ?> map, Class<T> bean) throws IntrospectionException,
            InstantiationException, IllegalAccessException {
        BeanInfo beanInfo = Introspector.getBeanInfo(bean);
        T obj = bean.newInstance();
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            Method method = descriptor.getWriteMethod();
            String name = descriptor.getName();
            if (map.containsKey(name)) {
                Class<?>[] types = method.getParameterTypes();
                for (int x = 0; x < types.length; x++) {
                    Object value = map.get(name);
                    try {
                        method.invoke(obj, value);
                    } catch (Exception e) {
                        System.out.println("type:" + types[x].getSimpleName() + "|name:" + name + "|value:" + value);
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }

}
