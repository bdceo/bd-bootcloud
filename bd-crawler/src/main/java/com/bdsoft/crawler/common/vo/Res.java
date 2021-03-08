package com.bdsoft.crawler.common.vo;

import lombok.Data;

/**
 * 通用响应结构
 */
@Data
public class Res<T> {

    private int code;
    private String msg;
    private T data;

    private Res(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Res suc(T data) {
        Res res = new Res(1, "成功");
        res.setData(data);
        return res;
    }

    public static <T> Res err(String msg) {
        return new Res(0, msg);
    }

}
