package com.bdsoft.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdsoft.crawler.modules.stock.StockConfig;
import com.bdsoft.crawler.modules.stock.feed.*;
import com.hshc.basetools.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchStockTest extends SuperTest {

    /**
     * 沪深港通资金流向
     */
    @Test
    public void testHSMoneyFlow() {
        // 模拟jquery-callback
        StringBuilder cb = new StringBuilder("jQuery");
        cb.append(System.currentTimeMillis());
        String url = MessageFormat.format(StockConfig.hsgMoneyFlow, cb);
        String file = "e:/download/stock/hsgt/money/20200608.txt";
        String html = super.getAndCacheAsString(url, file);

        String preTag = "(", endTag = ");";
        String data = html.substring(html.indexOf(preTag) + 1, html.indexOf(endTag));
        log.info(data);

        JSONObject json = JSONObject.parseObject(data);
        Map<String, Object> jsonData = json.getObject("data", Map.class);
        jsonData.forEach((k, v) -> {
            MoneyFlow mf = JSONObject.parseObject(JSONUtil.json(v), MoneyFlow.class);
            StringBuilder str = new StringBuilder();
            str.append("市场：").append(k).append("\t净流入：").append(mf.getDayIn());
            str.append("\t余额：").append(mf.getDayRemain()).append("\t限额：").append(mf.getDayLimit());
            log.info(str.toString());
        });
    }

    /**
     * 沪深港股通排行
     */
    @Test
    public void testHSgtTop10() {
        String url = "http://data.eastmoney.com/hsgt/top10/2020-06-08.html";
        String file = "e:/download/stock/hsgt/top10/20200608.txt";
        String html = super.getAndCacheAsString(url, file);

        // 抓取排名
        String jsPreTag = "var DATA1 =", jsEndTag = "function fillTable";
        html = html.substring(html.indexOf(jsPreTag), html.indexOf(jsEndTag));
        log.info("top10={}", html);

        // 提取沪股通top10
        html = html.substring(html.indexOf("var DATA1 ="));
        String data1 = html.substring(html.indexOf("["), html.indexOf("};"));
        List<HgtTop10> hgtList = JSONArray.parseArray(data1, HgtTop10.class);
        //log.info("data1={}", data1);
        log.info("沪股通十大成交股");
        for (HgtTop10 item : hgtList) {
            StringBuilder str = new StringBuilder();
            str.append("排名：").append(item.getRank()).append("\t代码：").append(item.getCode());
            str.append("\t名称：").append(item.getName()).append("\t收盘：").append(item.getClose());
            str.append("\t涨跌：").append(item.getChangePercent().setScale(2, BigDecimal.ROUND_HALF_UP)).append("%");
            str.append("\t买入：").append(item.getInAmount()).append("\t卖出：").append(item.getOutAmount()).append("\t成交额：").append(item.getDealAmount());
            log.info(str.toString());
        }

        // 提取深股通top10
        html = html.substring(html.indexOf("var DATA2 ="));
        String data2 = html.substring(html.indexOf("["), html.indexOf("};"));
        List<SgtTop10> sgtList = JSONArray.parseArray(data2, SgtTop10.class);
        //log.info("data2={}", data2);
        log.info("深股通十大成交股");
        for (SgtTop10 item : sgtList) {
            StringBuilder str = new StringBuilder();
            str.append("排名：").append(item.getRank()).append("\t代码：").append(item.getCode());
            str.append("\t名称：").append(item.getName()).append("\t收盘：").append(item.getClose());
            str.append("\t涨跌：").append(item.getChangePercent().setScale(2, BigDecimal.ROUND_HALF_UP)).append("%");
            str.append("\t买入：").append(item.getInAmount()).append("\t卖出：").append(item.getOutAmount()).append("\t成交额：").append(item.getRealAmount());
            log.info(str.toString());
        }

        // 提取港股通-沪top10
        html = html.substring(html.indexOf("var DATA3 ="));
        String data3 = html.substring(html.indexOf("["), html.indexOf("};"));
        List<GgtHTop10> ggtHList = JSONArray.parseArray(data3, GgtHTop10.class);
        //log.info("data3={}", data3);
        log.info("港股通(沪)十大成交股");
        for (GgtHTop10 item : ggtHList) {
            StringBuilder str = new StringBuilder();
            str.append("排名：").append(item.getRank()).append("\t代码：").append(item.getCode());
            str.append("\t名称：").append(item.getName()).append("\t收盘：").append(item.getClose());
            str.append("\t涨跌：").append(item.getChangePercent().setScale(2, BigDecimal.ROUND_HALF_UP)).append("%");
            str.append("\t买入：").append(item.getInAmount()).append("\t卖出：").append(item.getOutAmount()).append("\t成交额：").append(item.getDealAmount());
            log.info(str.toString());
        }

        // 提取港股通-深top10
        html = html.substring(html.indexOf("var DATA4 ="));
        String data4 = html.substring(html.indexOf("["), html.indexOf("};"));
        List<GgtSTop10> ggtSList = JSONArray.parseArray(data4, GgtSTop10.class);
        //log.info("data4={}", data4);
        log.info("港股通(深)十大成交股");
        for (GgtSTop10 item : ggtSList) {
            StringBuilder str = new StringBuilder();
            str.append("排名：").append(item.getRank()).append("\t代码：").append(item.getCode());
            str.append("\t名称：").append(item.getName()).append("\t收盘：").append(item.getClose());
            str.append("\t涨跌：").append(item.getChangePercent().setScale(2, BigDecimal.ROUND_HALF_UP)).append("%");
            str.append("\t买入：").append(item.getInAmount()).append("\t卖出：").append(item.getOutAmount()).append("\t成交额：").append(item.getRealAmount());
            log.info(str.toString());
        }
    }

}
