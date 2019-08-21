package com.bdsoft.crawler;

import com.bdsoft.crawler.common.BDFileUtil;
import com.bdsoft.crawler.common.unirest.CommonConfig;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

@Slf4j
public class SuperTest {

    // 全局配置
    static {
        Unirest.config().connectTimeout(3_000).socketTimeout(5_000)
                .setDefaultHeader("Accept-Encoding", "gzip")
                .setDefaultHeader("User-Agent", CommonConfig.USER_AGENT);
    }

    /**
     * 网络抓取并缓存本地
     *
     * @param url  资源地址
     * @param file 本地缓存地址
     * @return
     */
    public Document getAndCache(String url, String file) {
        // 先从本地缓存读
        Document doc = this.getFromLocal(file);
        if (doc != null) {
            return doc;
        }

        // 网络抓取
        HttpResponse<String> res = Unirest.get(url).asString();
        log.info("http status={}", res.getStatus());

        if (res.isSuccess()) {
            // 缓存本地
            BDFileUtil.writeFile(file, res.getBody(), true);
            log.info("fetch suc url={}", url);
            return Jsoup.parse(res.getBody());
        } else {
            log.error("get from web error {}", url);
            return null;
        }
    }

    /**
     * 网络抓取并缓存本地
     *
     * @param url  资源地址
     * @param file 本地缓存地址
     * @return
     */
    public String getAndCacheAsString(String url, String file) {
        // 先从本地缓存读
        String doc = this.getFromLocalAsString(file);
        if (doc != null) {
            return doc;
        }

        // 网络抓取
        HttpResponse<String> res = Unirest.get(url).asString();
        log.info("http status={}", res.getStatus());

        if (res.isSuccess()) {
            // 缓存本地
            BDFileUtil.writeFile(file, res.getBody(), true);
            log.info("fetch suc url={}", url);
            return res.getBody();
        } else {
            log.error("get from web error {}", url);
            return null;
        }
    }

    /**
     * 从本地缓存获取文档对象
     *
     * @param file 文件地址
     * @return
     */
    public Document getFromLocal(String file) {
        try {
            return Jsoup.parse(new File(file), "utf-8");
        } catch (IOException e) {
            log.error("local file not exists {}", file);
            return null;
        }
    }

    /**
     * 从本地缓存获取文档对象
     *
     * @param file 文件地址
     * @return
     */
    public String getFromLocalAsString(String file) {
        try {
            return FileUtils.readFileToString(new File(file));
        } catch (IOException e) {
            log.error("local file not exists {}", file);
            return null;
        }
    }

}
