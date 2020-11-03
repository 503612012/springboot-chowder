package com.oven.utils;

/**
 * 自定义返回类
 */
public class ResultInfo<T> {

    private Integer code; // 返回代码(200:成功)
    private T data; // 返回的数据,正确的信息或错误描述信息

    public ResultInfo() {
        super();
    }

    public ResultInfo(Integer code, T data) {
        super();
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }

}
