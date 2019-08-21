package com.bdsoft.crawler.modules.weibo.feed;

import lombok.Data;

import java.util.Map;

/**
 * wap版微博登录响应
 */
@Data
public class MLoginRes {

    /**
     * keys： weibo.com，sina.com.cn，weibo.cn
     */
    private Map<String, String> crossdomainlist;

    private String loginresulturl;

    private String uid;

}
