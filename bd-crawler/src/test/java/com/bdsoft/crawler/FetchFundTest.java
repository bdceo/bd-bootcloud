package com.bdsoft.crawler;

import com.alibaba.fastjson.JSONObject;
import com.bdsoft.crawler.modules.fund.FundConfig;
import com.bdsoft.crawler.modules.fund.entity.FundInfo;
import com.bdsoft.crawler.modules.fund.entity.FundValue;
import com.bdsoft.crawler.modules.fund.mapper.FundInfoMapper;
import com.bdsoft.crawler.modules.fund.mapper.FundValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.lang.model.util.Elements;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchFundTest extends SuperTest {

    @Autowired
    private FundInfoMapper fundInfoMapper;
    @Autowired
    private FundValueMapper fundValueMapper;

    @Test
    public void testList() {
        List<FundInfo> infoList = fundInfoMapper.selectList(null);
        List<FundValue> valueList = fundValueMapper.selectList(null);
        log.info("info size={}, value size={}", infoList.size(), valueList.size());
    }

    /**
     * 基金持仓方案
     */
    @Test
    public void testFetchJjcc() throws Exception {
        String code = "420003";
        int top = 20;

        String url = MessageFormat.format(FundConfig.JJCC, code, top);
        String file = "d:/download/fund/" + code + "-jjcc.txt";
        String html = super.getAndCacheAsString(url, file);

        // 提取html-js片段
        String jsPreTag = "var apidata=", jsEndTag = "};";
        html = html.replace(jsPreTag, "").replace(jsEndTag, "}");
        JSONObject json = JSONObject.parseObject(html);
        html = json.getString("content");

        // 获取最新一季度方案内容
        Document doc = Jsoup.parse(html);
        Element table = doc.select("div.box").get(0).select("tbody").first();
        for (Element tr : table.getElementsByTag("tr")) {
             String stockNo = tr.children().get(1).text();
             String stockName = tr.children().get(2).text();
             String fundRate = tr.children().get(6).text();
            log.info("股票代码{}\t{}\t{}", stockNo, stockName, fundRate);
        }


        System.out.println();

    }

}
