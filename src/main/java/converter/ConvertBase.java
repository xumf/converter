package com.gf.jdp.jira.pms.convert;

public interface ConvertBase {

    /**
     *  类型值装换
     * @param <T> {@link T}
     * @return {@link T}
     */
    public <T> T convert();

    /**
     *  类型值装换
     * @param clz 需要转换的class
     * @param <T> {@link T}
     * @return {@link T}
     */
    public <T> T convert(Class<T> clz);

}
