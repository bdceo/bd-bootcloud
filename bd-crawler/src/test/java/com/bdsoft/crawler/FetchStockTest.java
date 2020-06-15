package com.bdsoft.crawler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Documented;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchStockTest extends SuperTest {

    /**
     * 北向资金流入排行
     */
    @Test
    public void testNorthTop10() {
        String url = "http://data.eastmoney.com/hsgt/top10/2020-06-08.html";
        String file = "e:/download/stock/north/top10/20200608.txt";
        String html = super.getAndCacheAsString(url, file);

        // 抓取排名
        String jsPreTag = "var DATA1 =", jsEndTag = "function fillTable";
        html = html.substring(html.indexOf(jsPreTag), html.indexOf(jsEndTag));
        log.info("top10={}", html);

        // 提取沪股通top10
        html = html.substring(html.indexOf("var DATA1 ="));
        String data1 = html.substring(html.indexOf("["), html.indexOf("};"));
        log.info("data1={}", data1);

        // 提取深股通top10
        html = html.substring(html.indexOf("var DATA2 ="));
        String data2 = html.substring(html.indexOf("["), html.indexOf("};"));
        log.info("data2={}", data2);

        // 提取港股通-沪top10
        html = html.substring(html.indexOf("var DATA3 ="));
        String data3 = html.substring(html.indexOf("["), html.indexOf("};"));
        log.info("data3={}", data3);

        // 提取港股通-深top10
        html = html.substring(html.indexOf("var DATA4 ="));
        String data4 = html.substring(html.indexOf("["), html.indexOf("};"));
        log.info("data4={}", data4);
    }

}
