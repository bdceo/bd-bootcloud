package com.bdsoft.crawler;

import com.bdsoft.crawler.common.unirest.CommonConfig;
import kong.unirest.Unirest;

/**
 * Created by Administrator on 2019/7/4.
 */
public class SuperTest {

    // 全局配置
    static {
        Unirest.config().connectTimeout(3_000).socketTimeout(5_000)
                .setDefaultHeader("Accept-Encoding", "gzip")
                .setDefaultHeader("User-Agent", CommonConfig.USER_AGENT);
    }

}
