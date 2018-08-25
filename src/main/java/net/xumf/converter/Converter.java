package main.java.net.xumf.converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类型转换工具
 * 同名属性自动转换，非同名属性可以通过转换源（target）添加主键{@link ConvertAnnotation}完成映射实现转换
 */
public abstract class Converter {

    public static <T, S> List<T> toList(Class<T> clazz, List<S> list, String... ignoreProperties) {

        return toList(clazz, list, false, ignoreProperties);
    }

    /**
     * @param clazz              转换类的class
     * @param list               集合
     * @param ignoreNullProperty 是否忽略null的数据
     * @param ignoreProperties   忽略转换的属性名 默认为false
     * @param <T>                返回值的类型
     * @param <S>                方法参数类型
     * @return T类型集合
     */
    public static <T, S> List<T> toList(Class<T> clazz, List<S> list, boolean ignoreNullProperty, String... ignoreProperties) {

        List<T> dtoList = new ArrayList<>();
        T dto;
        for (S s : list) {
            dto = to(clazz, s, ignoreNullProperty, ignoreProperties);
            if (dto == null) continue;
            dtoList.add(dto);
        }
        return dtoList;
    }


    public static <T, O> T to(Class<T> clazz, O source, String... ignores) {
        return to(clazz, source, false, ignores);
    }

    /**
     * @param clazz              需要转换的class
     * @param source             数据源对象
     * @param ignoreNullProperty 是否忽略null的数据
     * @param ignoreProperties   忽略转换的属性名 默认为false
     * @param <T>                {@link T} 需要转换的类型
     * @param <S>                {@link S} 转换数据源
     * @return {@link T}
     */
    public static <T, S> T to(Class<T> clazz, S source, boolean ignoreNullProperty, String... ignoreProperties) {

        T target = null;
        try {
            target = clazz.newInstance();
            to(target, source, ignoreNullProperty, ignoreProperties);
        } catch (IllegalAccessException | InstantiationException ignored) {
        }
        return target;
    }

    public static <T, S> T to(T target, S source, String... ignoreProperties) {
        return to(target, source, false, ignoreProperties);
    }

    /**
     * @param source             数据源对象
     * @param ignoreNullProperty 是否忽略null的数据
     * @param ignoreProperties   忽略转换的属性名 默认为false
     * @param <T>                {@link T} 需要转换的类型
     * @param <S>                {@link S} 转换数据源
     * @return {@link T}
     */
    public static <T, S> T to(T target, S source, boolean ignoreNullProperty, String... ignoreProperties) {
        List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;
        try {
            Field[] fields = source.getClass().getDeclaredFields();
            for (Field f : fields) {
                // 跳过忽略属性
                if (ignoreList != null && ignoreList.contains(f.getName())) {
                    continue;
                }
                f.setAccessible(true);
                Object sourceValue = f.get(source);
                // 如果当前值为null，并且忽略参数ignoreNull为真，跳过
                if (ignoreNullProperty && sourceValue == null) {
                    continue;
                }
                //获取字段中包含fieldMeta的注解
                ConvertAnnotation an = f.getAnnotation(ConvertAnnotation.class);
                if (an == null) {
                    matchProperty(target, null, f.getType(), f.getName(), sourceValue);
                } else {
                    // 是否忽略赋值
                    if (an.ignore()) continue;
                    boolean success = matchProperty(target, an.value(), f.getType(), f.getName(), sourceValue);
                    if (!success) { // 赋值失败
                        matchProperty(target, null, f.getType(), f.getName(), sourceValue);
                    }
                }
            }
        } catch (IllegalAccessException ignored) {
        }
        return target;
    }

    /**
     * 匹配的属性赋值
     *
     * @param target           转换的对象
     * @param fieldName        注解值
     * @param sourceFieldName  需要转换对象的属性名
     * @param sourceFieldValue 属性值
     * @return 是否赋值成功
     */
    private static <T> boolean matchProperty(T target, String fieldName, Class<?> sourceFieldType, String sourceFieldName, Object sourceFieldValue) {

        try {
            if (fieldName == null) {
                Field tf = target.getClass().getDeclaredField(sourceFieldName);
                if (tf != null) {
                    tf.setAccessible(true);
                    copyProperty(target, sourceFieldValue, sourceFieldType, tf);
                }
            } else {
                Field tf = target.getClass().getDeclaredField(fieldName);
                tf.setAccessible(true);
                // 注解值与转换类的属性是否有相等的
                copyProperty(target, sourceFieldValue, sourceFieldType, tf);
            }
        } catch (NoSuchFieldException ignored) {
            return false;
        }
        return true;
    }

    /**
     * 给转换对象赋值
     *
     * @param obj   转换对象
     * @param value 需要赋值的值
     * @param tf    转换类Field
     */
    private static void copyProperty(Object obj, Object value, Class<?> type, Field tf) {


        try {
            if (type == tf.getType()) {
                tf.set(obj, value);
            } else {
                if (String.class == tf.getType()) {
                    tf.set(obj, value.toString());
                } else if (Integer.class == tf.getType() || int.class == tf.getType()) {
                    tf.set(obj, Integer.parseInt(value.toString()));
                } else if (Long.class == tf.getType() || long.class == tf.getType()) {
                    tf.set(obj, Long.parseLong(value.toString()));
                } else if (Float.class == tf.getType() || float.class == tf.getType()) {
                    tf.set(obj, Float.parseFloat(value.toString()));
                } else if (Double.class == tf.getType() || double.class == tf.getType()) {
                    tf.set(obj, Double.parseDouble(value.toString()));
                } else if (Boolean.class == tf.getType() || boolean.class == tf.getType()) {
                    tf.set(obj, Boolean.parseBoolean(value.toString()));
                } else if (Byte.class == tf.getType() || byte.class == tf.getType()) {
                    tf.set(obj, Byte.parseByte(value.toString()));
                } else if (Character.class == tf.getType() || char.class == tf.getType()) {
                    tf.set(obj, value);
                } else {
                    tf.set(obj, value);
                }
            }
        } catch (IllegalAccessException ignored) {
        }

    }

}
