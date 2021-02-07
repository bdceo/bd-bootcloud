package com.bdsoft.crawler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bdsoft.crawler.modules.fund.FundConfig;
import com.bdsoft.crawler.modules.fund.entity.FundInfo;
import com.bdsoft.crawler.modules.fund.entity.FundValue;
import com.bdsoft.crawler.modules.fund.mapper.FundInfoMapper;
import com.bdsoft.crawler.modules.fund.mapper.FundValueMapper;
import com.bdsoft.crawler.modules.fund.po.*;
import com.bdsoft.crawler.modules.fund.xhr.JzhData;
import com.bdsoft.crawler.modules.fund.xhr.JzhItem;
import com.bdsoft.crawler.modules.fund.xhr.JzhResponse;
import com.hshc.basetools.json.JSONUtil;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest
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

    @Test
    public void testJjcc() throws Exception {
        log.info("测试：基金持仓");

        String code = "420003";

        // 先获取历史年度
        String[] years = null;
        String url = MessageFormat.format(FundConfig.FUND_CC, code, "");
        HttpResponse<String> res = Unirest.get(url).asString();
        if (res.isSuccess()) {
            String jsRes = res.getBody();
            // var apidata={ content:"<divh><th ",arryear:[2020,2019,2018],curyear:2020};
            years = jsRes.substring(jsRes.indexOf(":[") + 2, jsRes.lastIndexOf("],")).split(",");
        } else {
            log.error("获取基金持仓历史年度失败：{}", url);
            return;
        }

        // 按年度抓取
        if (years != null) {
            boolean isLatest = true;
            for (int i = 0; i < years.length; i++) {
                url = MessageFormat.format(FundConfig.FUND_CC, code, years[i]);
                res = Unirest.get(url).asString();
                if (res.isSuccess()) {
                    String jsRes = res.getBody();
                    // var apidata={ content:"<divh><th ",arryear:[2020,2019,2018],curyear:2020};
                    String content = jsRes.substring(jsRes.indexOf(":\"") + 2, jsRes.indexOf("\","));
                    Document html = Jsoup.parse(content);

                    Elements elements = html.getElementsByClass("boxitem");
                    for (Element ele : elements) {
                        // 持仓时间
                        String dt = ele.select("font.px12").get(0).text();
                        FundHoldPO po = new FundHoldPO(code, DateUtils.parseDate(dt, "yyyy-MM-dd"));

                        // 持仓股票
                        Element table = ele.select("table.w782").get(0).select("tbody").first();
                        for (Element tr : table.getElementsByTag("tr")) {
                            po.setStockCode(tr.children().get(1).text());
                            po.setStockName(tr.children().get(2).text());
                            if (isLatest) {
                                po.setJzhRate(Float.valueOf(tr.children().get(6).text().replace("%", "")));
                                po.setHoldStock(NumberFormat.getInstance().parse(tr.children().get(7).text()).floatValue());
                                po.setHoldValue(NumberFormat.getInstance().parse(tr.children().get(8).text()).floatValue());
                            } else {
                                po.setJzhRate(Float.valueOf(tr.children().get(4).text().replace("%", "")));
                                po.setHoldStock(NumberFormat.getInstance().parse(tr.children().get(5).text()).floatValue());
                                po.setHoldValue(NumberFormat.getInstance().parse(tr.children().get(6).text()).floatValue());
                            }
                            log.info("股票持仓：{}", JSONUtil.json(po));
                        }
                        isLatest = false;
                    }
                } else {
                    log.error("获取基金持仓历史失败：{}", url);
                }
            }
        }
    }

    @Test
    public void testJjgk() {
        log.info("测试：基金概况、手续费");

        String code = "006341";
        String url = MessageFormat.format(FundConfig.FUND_GK, code);
        FundPO fund = new FundPO(code);
        FundFeePO fee = new FundFeePO();

        HttpResponse<String> res = Unirest.get(url).asString();
        if (res.isSuccess()) {
            Document html = Jsoup.parse(res.getBody());

            // 基金概况
            Element tableEle = html.getElementsByClass("info").get(0);
            Elements trs = tableEle.getElementsByTag("tr");

            // 简称、全称
            Elements tr1 = trs.get(0).getElementsByTag("td");
            fund.setFullName(tr1.get(0).text());
            fund.setName(tr1.get(1).text());

            // 类型
            Elements tr2 = trs.get(1).getElementsByTag("td");
            fund.setType(tr2.get(1).text());

            // 发行日期
            Elements tr3 = trs.get(2).getElementsByTag("td");
            String tmp = tr3.get(0).text();
            try {
                Date tmpDate = DateUtils.parseDate(tmp, "yyyy年MM月dd日");
                fund.setFxDate(tmpDate);
            } catch (ParseException e) {
                log.error("发行日期解析失败：{}", tmp);
            }

            // 基金规模
            Elements tr4 = trs.get(3).getElementsByTag("td");
            tmp = tr4.get(0).text();
            String gm = tmp.split("亿")[0];
            fund.setGm(Float.parseFloat(gm));

            // 基金公司
            Elements tr5 = trs.get(4).getElementsByTag("td");
            Element td = tr5.get(0).getElementsByTag("a").get(0);
            fund.setCompany(td.text());
            tmp = td.attr("href");
            tmp = FundConfig.pickCode(tmp, FundConfig.COMPANY_REG);
            fund.setCompanyCode(tmp);
            log.info("基金概况：{}", JSONUtil.json(fund));

            // 管理费、托管费
            Elements tr7 = trs.get(6).getElementsByTag("td");
            tmp = tr7.get(0).text();
            String glf = tmp.split("%")[0];
            fee.setGlf(Float.valueOf(glf));
            tmp = tr7.get(1).text();
            String tgf = tmp.split("%")[0];
            fee.setTgf(Float.valueOf(tgf));

            // 销售服务费、认购费
            Elements tr8 = trs.get(7).getElementsByTag("td");
            tmp = tr8.get(0).text();
            String xsh = tmp.split("%")[0];
            fee.setXsh(Float.valueOf(xsh));
            tmp = tr8.get(1).getElementsByTag("span").get(0).text();
            String rgf = tmp.split("%")[0];
            fee.setRgf(Float.valueOf(rgf));

            // 申购、赎回费
            Elements tr9 = trs.get(8).getElementsByTag("td");
            tmp = tr9.get(0).getElementsByTag("span").get(0).text();
            String sgf = tmp.split("%")[0];
            fee.setSgf(Float.valueOf(sgf));
            tmp = tr9.get(1).text();
            String shf = tmp.split("%")[0];
            fee.setShf(Float.valueOf(shf));
            log.info("手续费：{}", JSONUtil.json(fee));

        } else {
            log.error("基金概况信息抓取失败：{}，{}", code, url);
        }
    }

    @Test
    public void testJzh() {
        log.info("测试：历史净值");
        Unirest.config().addDefaultHeader("Referer", FundConfig.HOST_INFO);
        Unirest.config().addDefaultHeader("Host", FundConfig.HOST_API);

        String code = "006341";

        int pageIndex = 1;
        int pageTotal = 1;
        String jq = new StringBuilder("jQuery").append(IdWorker.getIdStr()).append("1_")
                .append(String.valueOf(System.currentTimeMillis())).toString();
        List<FundJzhPO> jzhList = new ArrayList<>();
        for (; pageIndex <= pageTotal; pageIndex++) {
            // 分页抓取
            String url = MessageFormat.format(FundConfig.FUND_JZ_XHR, jq, code, pageIndex, String.valueOf(System.currentTimeMillis()));
            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                String json = FundConfig.pickXhrData(res.getBody());
                JzhResponse resObj = JSONObject.parseObject(json, JzhResponse.class);
                if (resObj.isSuccess()) {
                    // 计算总页数
                    pageTotal = resObj.getPageTotal();

                    JzhData resData = resObj.getData();
                    if (!CollectionUtils.isEmpty(resData.getList())) {
                        for (JzhItem item : resData.getList()) {
                            FundJzhPO jzh = new FundJzhPO(code, item);
                            log.info("净值-{}/{}：{}", pageIndex, pageTotal, JSONUtil.json(jzh));
                            jzhList.add(jzh);
                        }
                        try {
                            Thread.sleep(Math.min(100, FundConfig.RANDOM.nextInt(1000)));
                        } catch (InterruptedException e) {
                            log.error("分页sleep异常：", e);
                        }
                    }
                } else {
                    log.error("抓取结果异常：{}, {}, {}", code, pageIndex, resObj.getMsg());
                    continue;
                }
            } else {
                log.error("分页抓取失败：{}, {}", code, pageIndex);
            }
        }
    }

    @Test
    public void testTs() {
        log.info("测试：特色数据");

        String code = "008592";
        String url = MessageFormat.format(FundConfig.FUND_TS, code);
        FundDataPO tsData = new FundDataPO(code);

        HttpResponse<String> res = Unirest.get(url).asString();
        if (res.isSuccess()) {
            Document html = Jsoup.parse(res.getBody());

            // 基金风险-风险等级
            Elements tmpElements = html.getElementsByClass("allfxdj");
            Element tmpEle = tmpElements.get(0).getElementsByClass("chooseLow").get(0);
            tsData.setWholeRiskLevel(tmpEle.text());
            tmpEle = tmpElements.get(1).getElementsByClass("chooseLow").get(0);
            tsData.setSameRiskLevel(tmpEle.text());

            // 基金风险-风险指标
            tmpEle = html.getElementsByClass("fxtb").get(0);
            tmpElements = tmpEle.getElementsByTag("tr");
            for (int i = 1; i < tmpElements.size(); i++) {
                Elements tds = tmpElements.get(i).getElementsByTag("td");
                tsData.setRiskIndex(tds.get(0).text(), tds.get(1).text(), tds.get(2).text(), tds.get(3).text());
            }

            // 指数基金指标
            tmpEle = html.getElementById("jjzsfj");
            if (tmpEle != null) {
                tmpElements = tmpEle.getElementsByClass("fxtb").get(0).getElementsByTag("tr");
                tmpElements = tmpElements.get(1).getElementsByTag("td");
                tsData.setTrackIndex(tmpElements.get(0).text());
                tsData.setTrackDiff(Float.valueOf(tmpElements.get(1).text().replace("%", "")));
                tsData.setSameDiff(Float.valueOf(tmpElements.get(2).text().replace("%", "")));
            }
            log.info("特色数据：{}", JSONUtil.json(tsData));
        } else {
            log.error("基金特色数据抓取失败：{}，{}", code, url);
        }
    }

}
