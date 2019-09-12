package com.example.base.listener;


/**
 * @Author zhengdengyao
 * @Created 2018/12/12 0012
 * @Description
 */
public interface  CommonListener<T > {
    /**
     * 接口请求成功
     * @param obj 接口返回的数据
     */
    void onSuccess(T obj);

    /**
     * 接口请求失败
     * @param obj 错误数据
     */
    void onError(T obj);
}
