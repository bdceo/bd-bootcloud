package com.bdsoft.crawler;

import com.alibaba.fastjson.JSONObject;
import com.bdsoft.crawler.modules.fund.FundConfig;
import com.bdsoft.crawler.modules.fund.entity.FundInfo;
import com.bdsoft.crawler.modules.fund.entity.FundValue;
import com.bdsoft.crawler.modules.fund.mapper.FundInfoMapper;
import com.bdsoft.crawler.modules.fund.mapper.FundValueMapper;
import com.bdsoft.crawler.modules.fund.po.FundFeePO;
import com.bdsoft.crawler.modules.fund.po.FundJzhPO;
import com.bdsoft.crawler.modules.fund.po.FundPO;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

        String code = "00596";

        int pageIndex = 1;
        int pageTotal = 1;
        Random random = new Random(System.currentTimeMillis());
        Unirest.config().addDefaultHeader("Referer", FundConfig.HOST_INFO);
        Unirest.config().addDefaultHeader("Host", FundConfig.HOST_API);

        List<FundJzhPO> jzhList = new ArrayList<>();
        for (; pageIndex <= pageTotal; pageIndex++) {
            // 分页抓取
            String url = MessageFormat.format(FundConfig.FUND_JZ_XHR, random.nextLong(), code, pageIndex, System.currentTimeMillis());
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
                            log.info("净值：{}", JSONUtil.json(jzh));
                            jzhList.add(jzh);
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

}
