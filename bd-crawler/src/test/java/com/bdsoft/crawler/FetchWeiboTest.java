package com.bdsoft.crawler;

import com.bdsoft.crawler.modules.gas.GasConfig;
import com.bdsoft.crawler.modules.weibo.WeiboConfig;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
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
        url = "https://weibo.com/liucaishen?pids=Pl_Official_MyProfileFeed__21&is_ori=1&profile_ftype=1&page=1&ajaxpagelet=1";
        HttpResponse<String> res = Unirest.get(url)
                .headers(headers)
                .asString();
        log.info("http status={}", res.getStatus());

//        Document html = super.getAndCache(url, tmp.toString());


    }

}
