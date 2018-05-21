package com.gf.jdp.jira.pms.convert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class ConvertAbstract implements ConvertBase {

    /**
     * 集合转集合
     *
     * @param list 集合
     * @param <T>  返回值的类型
     * @param <P>  方法参数类型
     * @return {@link T}
     */
    public static <T, P extends ConvertBase> List<T> convertList(List<P> list) {

        return convertList(list, null);
    }

    /**
     * 集合转集合
     *
     * @param list 集合
     * @param clz  转换类的class
     * @param <T>  返回值的类型
     * @param <P>  方法参数类型
     * @return {@link T}
     */
    public static <T, P> List<T> convertListTo(List<P> list, Class<T> clz) {

        List<T> dtoList = new ArrayList<>();
        T dto;
        for (P p : list) {
            dto = convert(clz, p);
            if (dto == null) continue;
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * 集合转集合
     *
     * @param list 集合
     * @param clz  转换类的class
     * @param <T>  返回值的类型
     * @param <P>  方法参数类型
     * @return {@link T}
     */
    public static <T, P extends ConvertBase> List<T> convertList(List<P> list, Class<T> clz) {

        List<T> dtoList = new ArrayList<>();
        T dto;
        for (P p : list) {
            if (clz == null) {
                dto = p.convert();
            } else {
                dto = p.convert(clz);
            }
            if (dto == null) continue;
            dtoList.add(dto);
        }
        return dtoList;
    }

    public <T> T convert() {
        return null;
    }

    /**
     * 对象转换对象
     *
     * @param clz 需要转换的类型
     * @param o   原对象
     * @param <T> {@link T}
     * @param <O> {@link O}
     * @return {@link T}
     */
    public static <T, O> T convert(Class<T> clz, O o) {

        T t = null;
        try {
            t = clz.newInstance();
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field f : fields) {
                //获取字段中包含fieldMeta的注解
                ConvertAnnotation an = f.getAnnotation(ConvertAnnotation.class);
                // 设置可以访问私有属性
                f.setAccessible(true);
                if (an != null) {
                    // 是否忽略赋值
                    boolean ingore = an.ingore();
                    if (ingore) continue;
                    matchProperty(t, an.value(), f.getName(), f.get(o));
                } else {
                    matchProperty(t, "", f.getName(), f.get(o));
                }
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return t;
    }

    public <T> T convert(Class<T> clz) {
        return convert(clz, this);
    }

    /**
     * 给对应的属性赋值
     *
     * @param obj               转换的对象
     * @param fieldName         注解值
     * @param originalFieldName 需要转换对象的属性名
     * @param value             属性值
     */
    private static void matchProperty(Object obj, String fieldName, String originalFieldName, Object value) throws IllegalAccessException {
        Field[] tFields = obj.getClass().getDeclaredFields();
        for (Field tf :
                tFields) {
            tf.setAccessible(true);
            if (fieldName.equals(tf.getName())) { // 注解值与转换类的属性是否有相等的
                setConvertValue(obj, value, tf);
            } else {
                if (originalFieldName.equals(tf.getName())) { // 原类与转换类的属性是否有相等的
                    setConvertValue(obj, value, tf);
                }
            }
        }
    }


    /**
     * 给转换对象赋值
     *
     * @param obj   转换对象
     * @param value 需要赋值的值
     * @param tf    转换类Field
     * @throws IllegalAccessException
     */
    private static void setConvertValue(Object obj, Object value, Field tf) throws IllegalAccessException {
        if (String.class == tf.getType()) {
            tf.set(obj, value.toString());
        } else if (Integer.class == tf.getType() || int.class == tf.getType()) {
            tf.setInt(obj, (Integer) value);
        } else if (Long.class == tf.getType() || long.class == tf.getType()) {
            tf.setLong(obj, (Long) value);
        } else if (Float.class == tf.getType() || float.class == tf.getType()) {
            tf.setFloat(obj, (Float) value);
        } else if (Double.class == tf.getType() || double.class == tf.getType()) {
            tf.setDouble(obj, (Double) value);
        } else if (Boolean.class == tf.getType() || boolean.class == tf.getType()) {
            tf.setBoolean(obj, (Boolean) value);
        } else if (byte.class == tf.getType()) {
            tf.setByte(obj, (byte) value);
        } else if (char.class == tf.getType()) {
            tf.setChar(obj, (char) value);
        } else {
            tf.set(obj, value);
            System.out.println(tf.getType() + "\t");
        }
    }

}
