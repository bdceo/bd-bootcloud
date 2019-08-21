package com.bdsoft.crawler.modules.weibo.feed;

import lombok.Data;

/**
 * 新浪api响应结构
 */
@Data
public class SinaRes<T> {

    private int retcode;

    private String msg;

    private T data;

    /**
     * 是否成功
     */
    public boolean isSuc() {
        return retcode == 20000000;
    }

}
