package com.zxj.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zxj.common.convert.Convert;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;

/**
 * 将beancopier做成静态类，方便拷贝
 */
public class BeanUtils {

    /**
     * copier cache
     */
    private static ConcurrentHashMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<>();

    /**
     * exec bean map
     *
     * @param obj
     * @return
     */
    public static BeanMap getBeanMap(Object obj) {
        return BeanMap.create(obj);
    }

    /**
     * 将source的同名属性复制到target.适用于两个不同类型的对象
     *
     * @param a 资源类
     * @param b 目标类
     */
    public static void copyA2B(Object a, Object b) {
        BeanCopier copier = getBeanCopier(a, b);
        copier.copy(a, b, null);
    }

    /**
     * exec beanCopies from source and target
     *
     * @param source source
     * @param target target
     * @return BeanCopies
     */
    private static BeanCopier getBeanCopier(Object source, Object target) {
        String key = source.getClass().toString() + target.getClass().toString();
        BeanCopier beanCopier = beanCopierMap.get(key);
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.put(key, beanCopier);
        }
        return beanCopier;
    }

    /**
     * 给target赋上source的非空属性值
     *
     * @param a
     * @param b
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDiffProperties(T a, T b) throws IllegalAccessException, InstantiationException {
        if (a == null || b == null) {
            return b;
        }
        BeanMap beanMap = BeanMap.create(a);
        Object result = null;
        for (Object key : beanMap.keySet()) {
            Object targetValue = beanMap.get(b, key); //获取Target原值
            Object sourceValue = beanMap.get(a, key); //获取Source原值
            if (sourceValue != null && !sourceValue.equals(targetValue)) { //sourceValue不为空,两个值不相等
                if (targetValue == null || sourceValue.getClass().equals(targetValue.getClass())) {
                    if (null == result) {
                        result = a.getClass().newInstance();
                    }
                    beanMap.put(result, key, targetValue);
                }
            }
        }
        return (T) result;
    }


    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        if (map == null)
            return null;
        T t = clazz.newInstance();
        BeanMap beanMap = BeanMap.create(t);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            //获取属性类型
            Class propertyType = beanMap.getPropertyType(entry.getKey());
            //如果类型为空,continue
            if (propertyType == null)
                continue;
            //如果value 为空, continue
            Object val = map.get(entry.getKey());
            if (val == null)
                continue;
            //同类型,不需要做转换
            if (propertyType.equals(val.getClass()))
                continue;
            //属性为日期类型
            if (Date.class.equals(propertyType)) {
                if (val instanceof Long || long.class.equals(val.getClass())) {
                    val = new Date((long) val); //long转换成date
                } else if (val instanceof String) {
                    val = DateUtils.parse((String) val); //字符串转成date
                }
            } else if (BigDecimal.class.equals(propertyType)) { // To BigDecimal
                val = Convert.toBigDecimal(val);
            } else if (Boolean.class.equals(propertyType) || boolean.class.equals(propertyType)) { // To Boolean
                val = Convert.toBool(val);
            } else if (String.class.equals(propertyType)) {     //To string
                val = Convert.toStr(val);
            } else if (Integer.class.equals(propertyType) || int.class.equals(propertyType)) { //To Long
                val = Convert.toInt(val);
            } else if (Long.class.equals(propertyType) || long.class.equals(propertyType)) { //To Long
                val = Convert.toLong(val);
            } else if (Float.class.equals(propertyType) || float.class.equals(propertyType)) { //To Float
                val = Convert.toFloat(val);
            } else if (Double.class.equals(propertyType) || double.class.equals(propertyType)) { //To double
                val = Convert.toDouble(val);
            } else if (Byte.class.equals(propertyType) || byte.class.equals(propertyType)) { //To byte
                val = Convert.toByte(val);
            } else if (Character.class.equals(propertyType) || char.class.equals(propertyType)) { //To char
                val = Convert.toChar(val);
            } else if (Short.class.equals(propertyType) || short.class.equals(propertyType)) { //To short
                val = Convert.toShort(val);
            } else if (Number.class.equals(propertyType)) { //To Number
                val = Convert.toNumber(val);
            } else if (BigInteger.class.equals(propertyType)) { //To BigInteger
                val = Convert.toBigInteger(val);
            }
            map.put(entry.getKey(), val);
        }
        beanMap.putAll(map);
        return (T) beanMap.getBean();
    }

}