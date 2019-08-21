package com.bdsoft.crawler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bdsoft.crawler.common.BDFileUtil;
import com.bdsoft.crawler.common.BDHttpParam;
import com.bdsoft.crawler.common.BDHttpUtil;
import com.bdsoft.crawler.modules.gas.GasConfig;
import com.bdsoft.crawler.modules.weibo.WeiboConfig;
import com.bdsoft.crawler.modules.weibo.feed.MLoginRes;
import com.bdsoft.crawler.modules.weibo.feed.SinaRes;
import com.hshc.basetools.json.JSONUtil;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchWeiboTest extends SuperTest {

    @Test
    public void testLogin() {
        if (WeiboConfig.checkLogin()) {
            log.info("已登录");
            return;
        }

        // 登录地址
        String url = "https://passport.weibo.cn/sso/login";

        // 登录参数：保留响应cookie
        BDHttpParam hp = BDHttpParam.init();
        hp.addHeader("Host", "passport.weibo.cn");
        hp.addHeader("Referer", "https://passport.weibo.cn/signin/login?display=0&retcode=6102");

        hp.addCommon("username", "youknowceo");
        hp.addCommon("password", "bdceosina");
        hp.addCommon("savestate", "1");
        hp.addCommon("entry", "mweibo");
        hp.addCommon("mainpageflag", "1");

        hp.holdResCookie(true);

        // 模拟登录
        String res = BDHttpUtil.sendPost(url, hp);
        log.info("----------");
        SinaRes<MLoginRes> loginRes = JSONObject.parseObject(res, new TypeReference<SinaRes<MLoginRes>>() {
        });
        log.info(JSONUtil.json(loginRes));
        log.info("----------");
        log.info(JSONUtil.json(hp.getCookieParams()));
        log.info("----------");

        // 本地缓存cookie
        String path = MessageFormat.format(WeiboConfig.COOKIE_CACHE, loginRes.getData().getUid());
        BDFileUtil.writeFile(path, JSONUtil.json(hp.getCookieParams()), true);
        log.info("模拟登录并缓存cookie");
    }

    @Test
    public void testWeibo() {
        // 参数：博主，类型，页码
        String weiboId = "liucaishen";
        String type = "is_ori";
        int page = 1;
        String url = MessageFormat.format(WeiboConfig.WEIBO_LIST, weiboId, type, page);

        BDHttpParam hp = BDHttpParam.init().setCharset(BDHttpParam.CHARSET_GBK);
//        hp.addHeader("Host", "weibo.com");
        hp.addHeader("Sec-Fetch-Mode", "nested-navigate");
        hp.addHeader("Sec-Fetch-Site", "same-origin");
        hp.addHeader("Referer", "https://weibo.com/liucaishen?from=myfollow_group&is_all=1");

        hp.addCookie("YF-Page-G0", "afcf131cd4181c1cbdb744cd27663d8d|1566374334|1566374078");
        hp.addCookie("cross_origin_proto", "SSL");
        hp.addCookie("SSOLoginState", "1563357066");
//        hp.addCookie("","");

        String html = BDHttpUtil.sendGet(url, hp);
        log.info(html);

    }

    @Test
    public void testFetchWeibo() {
        Unirest.config().setDefaultResponseEncoding(GasConfig.ZHSHH_CHARSET);

        // 参数：博主，类型，页码
        String weiboId = "liucaishen";
        String type = "is_ori";
        int page = 1;
        String url = MessageFormat.format(WeiboConfig.WEIBO_LIST, weiboId, type, page);

        LocalDate now = LocalDate.now();
        StringBuilder tmp = new StringBuilder();
        tmp.append("d:/download/weibo/");
        tmp.append(weiboId).append("-");
        tmp.append(now.getYear()).append(now.getMonthValue()).append(now.getDayOfMonth()).append("-");
        tmp.append(page);
        tmp.append(".html");

        Map<String, String> headers = new HashMap<>();
//        headers.put("Host","weibo.com");
//        headers.put("Accept-Encoding","gzip");
        headers.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36");
//        headers.put("Referer","https://weibo.com/liucaishen?profile_ftype=1&is_pic=1");

        // 网络抓取
//        url = "https://weibo.com/liucaishen?pids=Pl_Official_MyProfileFeed__21&is_ori=1&profile_ftype=1&page=1&ajaxpagelet=1";
        HttpResponse<String> res = Unirest.get(url)
                .headers(headers)
                .asString();
        log.info("http status={}", res.getStatus());

//        Document html = super.getAndCache(url, tmp.toString());


    }

}
