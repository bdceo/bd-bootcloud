package com.bdsoft.crawler;

import com.bdsoft.crawler.common.BDFileUtil;
import com.bdsoft.crawler.common.WebUtils;
import com.bdsoft.crawler.common.unirest.CommonConfig;
import com.bdsoft.crawler.modules.jd.JdConfig;
import com.bdsoft.crawler.modules.jd.feed.JdCommentState;
import com.bdsoft.crawler.modules.jd.feed.JdPrice;
import com.bdsoft.crawler.modules.jd.feed.JdStock;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * unirest ： http://kong.github.io/unirest-java/
 * <p>
 * jsoup：https://www.cnblogs.com/zhangyinhua/p/8037599.html
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JsoupTest {

    // 全局配置
    static {
        Unirest.config().connectTimeout(3_000).socketTimeout(5_000)
                .setDefaultHeader("Accept-Encoding", "gzip")
                .setDefaultHeader("User-Agent", CommonConfig.USER_AGENT);
    }

    /**
     * 从网络获取
     *
     * @param url 地址
     * @return
     */
    private Document getDocumentFromWeb(String url) {
        HttpResponse<String> res = Unirest.get(url).asString();
        log.info("http status={}", res.getStatus());

        if (res.isSuccess()) {
            String file = "d:/download/jd/25118865051.txt";
            BDFileUtil.writeFile(file, res.getBody(), true);
            log.info("fetch suc url={}", url);
            return Jsoup.parse(res.getBody());
        } else {
            log.error("get from web error {}", url);
            return null;
        }
    }

    /**
     * 从本地获取
     *
     * @param file 缓存路径
     * @return
     */
    private Document getDocumentFromLocal(String file) {
        try {
            return Jsoup.parse(new File(file), "utf-8");
        } catch (IOException e) {
            log.error("local file not exists {}", file);
            return null;
        }
    }

    @Test
    public void testFetchJdSku() throws Exception {
        String url = "https://item.jd.com/25118865051.html";
        String file = "d:/download/jd/25118865051.txt";

//        Document html = this.getDocumentFromWeb(url);
        Document html = this.getDocumentFromLocal(file);

        // 商品名称
        Element ele = html.selectFirst("div.sku-name");
        String skuName = ele.text().trim();

        // 价格 - 异步获取
        JdPrice jdPrice = this.getPrice(url, html);
        String price = jdPrice.getP();

        // 评论数 - 异步获取
        JdCommentState jdCommentState = this.getCommentState(url);
        String commentCount = jdCommentState.getCommentCount();

        log.info("name={}, comments={}, price={}", skuName, commentCount, price);
    }

    /**
     * 获取商品评论统计
     *
     * @param url 详情页地址
     * @return
     */
    public JdCommentState getCommentState(String url) {
        // 准备参数
        String skuId = JdConfig.getSkuId(url);
        String ccUrl = MessageFormat.format(JdConfig.SKU_COMMENT_COUNT, skuId);
        log.info("get sku comment stat url={}", ccUrl);

        // 调用Api
        HttpResponse<JsonNode> res = Unirest.get(ccUrl).asJson();
        String json = res.getBody().getObject().getJSONArray("CommentsCount").getJSONObject(0).toString();
        JdCommentState jdCommentState = new Gson().fromJson(json, JdCommentState.class);

        return jdCommentState;
    }

    /**
     * 获取商品价格
     *
     * @param url  详情页地址
     * @param html 商品详情页
     * @return
     */
    public JdPrice getPrice(String url, Document html) {
        // 准备参数
        String skuId = JdConfig.getSkuId(url);
        String cats = this.getCat3(html);
        String priceUrl = MessageFormat.format(JdConfig.SKU_PRICE, skuId, cats, JdConfig.DEFAULT_PARAM_AREA);
        log.info("get sku price url={}", priceUrl);

        // 调用Api
        HttpResponse<JsonNode> res = Unirest.get(priceUrl).asJson();
        String json = res.getBody().getObject().getJSONObject("stock").toString();
        JdStock jdStock = new Gson().fromJson(json, JdStock.class);

        return jdStock.getJdPrice();
    }

    /**
     * 获取三级分类
     *
     * @param html 商品详情页
     * @return
     */
    public String getCat3(Document html) {
        // div > div > a
        Element ele = html.select("div#crumb-wrap div.item").get(4).child(0);
        // 分类链接， 参数名=cat
        String url = ele.attr("href");

        return WebUtils.getUrlParam(url, "cat");
    }

}
