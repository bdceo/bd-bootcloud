package com.bdsoft.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bdsoft.crawler.common.CopyUtils;
import com.bdsoft.crawler.modules.fund.FundConfig;
import com.bdsoft.crawler.modules.fund.entity.*;
import com.bdsoft.crawler.modules.fund.mapper.*;
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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class

FetchFundTest extends SuperTest {

    @Autowired
    private FundMapper fundMapper;
    @Autowired
    private FundInfoMapper fundInfoMapper;
    @Autowired
    private FundFeeMapper fundFeeMapper;
    @Autowired
    private FundTsMapper fundTsMapper;
    @Autowired
    private FundValMapper fundValMapper;
    @Autowired
    private FundStockMapper fundStockMapper;
    @Autowired
    private FundBondMapper fundBondMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private FundManagerMapper fundManagerMapper;

    @Test
    public void testRank() {
        log.info("测试：基金排行");
        Unirest.config().addDefaultHeader("Referer", FundConfig.FUND_RANK_REFER);
        Unirest.config().addDefaultHeader("Host", FundConfig.FUND_RANK_HOST);

        // 抓取指定排行榜
//        String fetchRank = FundConfig.FUND_KF_RANK;
        String fetchRank = FundConfig.FUND_FB_RANK;

        // 获取总页数
        int pageTotal;
        String url = MessageFormat.format(fetchRank, 1, String.valueOf(FundConfig.RANDOM.nextDouble()));
        HttpResponse<String> res = Unirest.get(url).asString();
        if (res.isSuccess()) {
            JSONObject json = JSON.parseObject(FundConfig.pickJsJson(res.getBody()));
            pageTotal = json.getIntValue("allPages");
            log.info("suc： 基金总数={}, 总页数={}", json.getIntValue("allRecords"), pageTotal);
        } else {
            log.error("获取基金排行失败：{}", url);
            return;
        }

        Date now = new Date();
        // 分页获取
        for (int pageIndex = 1; pageIndex <= pageTotal; pageIndex++) {
            url = MessageFormat.format(fetchRank, pageIndex, String.valueOf(FundConfig.RANDOM.nextDouble()));
            res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                JSONObject json = JSON.parseObject(FundConfig.pickJsJson(res.getBody()));
                JSONArray jsonData = json.getJSONArray("datas");
                for (int i = 0; i < jsonData.size(); i++) {
                    String tmp = jsonData.getString(i);
                    String[] fundAtt = tmp.split(",");
                    FundPO po = new FundPO(fundAtt[0], fundAtt[1], fundAtt[2]);
                    log.info("基金base={}", JSONUtil.json(po));

                    // 入库
                    Fund dbFund = fundMapper.selectOne(new QueryWrapper<Fund>().eq("code", po.getCode()));
                    if(dbFund == null) {
                        Fund fund = CopyUtils.copy(po, Fund.class);
                        fund.setSynTime(now);
                        int rows = fundMapper.insert(fund);
                        log.info("insert fund {}", rows);
                    }
                }
            } else {
                log.error("获取基金排行失败：{}-{}", pageIndex, url);
                continue;
            }
        }
    }

    @Test
    public void testJjcc() {
        log.info("测试：基金持仓");

        // 加载所有基金
        List<Fund> fundList = fundMapper.selectList(null);

        List<FundStock> fsList = fundStockMapper.selectList(new QueryWrapper<FundStock>().select("code"));
        if(!CollectionUtils.isEmpty(fsList)){
            Set<String> fundCodes = fsList.parallelStream().map(FundStock::getCode).collect(Collectors.toSet());
            fundList= fundList.stream().filter(f->!fundCodes.contains(f.getCode())).collect(Collectors.toList());
        }

        // 遍历抓取
        for (Fund item : fundList) {
            String code = item.getCode();

            // 先获取历史年度
            JSONArray years;
            String url = MessageFormat.format(FundConfig.FUND_STOCK, code, "");
            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                JSONObject json = JSON.parseObject(FundConfig.pickJsJson(res.getBody()));
                years = json.getJSONArray("arryear");
            } else {
                log.error("获取基金持仓历史年度失败：{}", url);
                return;
            }

            if (years != null && years.size() > 0) {
                // 按年度抓取
                for (int i = 0; i < years.size(); i++) {
                    url = MessageFormat.format(FundConfig.FUND_STOCK, code, String.valueOf(years.getIntValue(i)));
                    res = Unirest.get(url).asString();
                    if (res.isSuccess()) {
                        log.info("基金股票持仓：{}", code);
                        JSONObject json = JSON.parseObject(FundConfig.pickJsJson(res.getBody()));
                        String content = json.getString("content");
                        Document html = Jsoup.parse(content);

                        // 解析每季度持仓
                        Elements elements = html.getElementsByClass("boxitem");
                        for (Element ele : elements) {
                            // 持仓方案时间
                            String dt = ele.select("font.px12").get(0).text();
                            Date planDate;
                            try {
                                planDate = DateUtils.parseDate(dt, "yyyy-MM-dd");
                            } catch (ParseException e) {
                                log.error("持仓方案时间解析出错：" + dt, e);
                                continue;
                            }
                            FundStockPO po = new FundStockPO(code, planDate);

                            // 持仓股票
                            Element table = ele.select("table.w782").get(0).select("tbody").first();
                            for (Element tr : table.getElementsByTag("tr")) {
                                po.setStockCode(tr.children().get(1).text());
                                po.setStockName(tr.children().get(2).text());
                                if (tr.children().size()>7) {
                                    String tmp = tr.children().get(6).text();
                                    if(tmp.contains("--")){
                                        po.setValueRate(0);
                                    }else{
                                        po.setValueRate(Float.valueOf(tmp.replace("%", "")));
                                    }
                                    try {
                                        po.setStocks(NumberFormat.getInstance().parse(tr.children().get(7).text()).floatValue());
                                        po.setMarketValue(NumberFormat.getInstance().parse(tr.children().get(8).text()).floatValue());
                                    } catch (ParseException e) {
                                        log.error("持股数、持仓市值解析出错：" + tr.text(), e);
                                        po.setStocks(0);
                                        po.setMarketValue(0);
                                    }
                                } else {
                                    String tmp = tr.children().get(4).text();
                                    if(tmp.contains("--")){
                                        po.setValueRate(0);
                                    }else{
                                        po.setValueRate(Float.valueOf(tmp.replace("%", "")));
                                    }
                                    try {
                                        po.setStocks(NumberFormat.getInstance().parse(tr.children().get(5).text()).floatValue());
                                        po.setMarketValue(NumberFormat.getInstance().parse(tr.children().get(6).text()).floatValue());
                                    } catch (ParseException e) {
                                        log.error("持股数、持仓市值解析出错：" + tr.text(), e);
                                        po.setStocks(0);
                                        po.setMarketValue(0);
                                    }
                                }
                                log.info("股票持仓：{}", JSONUtil.json(po));

                                // 入库
                                FundStock fs = CopyUtils.copy(po, FundStock.class);
                                int rows = fundStockMapper.insert(fs);
                                log.info("insert fund-stock {}", rows);
                            }
                        }
                    } else {
                        log.error("获取基金持仓历史失败：{}", url);
                    }
                }
            } else {
                log.info("基金无股票持仓：{}", code);
            }
        }
    }

    @Test
    public void testZqcc() {
        log.info("测试：债券持仓");

        // 加载所有基金
        List<Fund> fundList = fundMapper.selectList(null);

        List<FundBond> fsList = fundBondMapper.selectList(new QueryWrapper<FundBond>().select("code"));
        if(!CollectionUtils.isEmpty(fsList)){
            Set<String> fundCodes = fsList.parallelStream().map(FundBond::getCode).collect(Collectors.toSet());
            fundList= fundList.stream().filter(f->!fundCodes.contains(f.getCode())).collect(Collectors.toList());
        }

        // 遍历抓取
        for (Fund item : fundList) {
            String code = item.getCode();

            // 先获取历史年度
            JSONArray years;
            String url = MessageFormat.format(FundConfig.FUND_BOND, code, "", String.valueOf(FundConfig.RANDOM.nextDouble()));
            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                JSONObject json = JSON.parseObject(FundConfig.pickJsJson(res.getBody()));
                years = json.getJSONArray("arryear");
            } else {
                log.error("获取债券持仓历史年度失败：{}", url);
                return;
            }

            if (years != null && years.size() > 0) {
                // 按年度抓取
                for (int i = 0; i < years.size(); i++) {
                    url = MessageFormat.format(FundConfig.FUND_BOND, code, String.valueOf(years.getIntValue(i)), String.valueOf(FundConfig.RANDOM.nextDouble()));
                    res = Unirest.get(url).asString();
                    if (res.isSuccess()) {
                        log.info("基金债券持仓：{}", code);
                        JSONObject json = JSON.parseObject(FundConfig.pickJsJson(res.getBody()));
                        String content = json.getString("content");
                        Document html = Jsoup.parse(content);

                        // 解析每季度持仓
                        Elements elements = html.getElementsByClass("boxitem");
                        for (Element ele : elements) {
                            // 持仓方案时间
                            String dt = ele.select("font.px12").get(0).text();
                            Date planDate;
                            try {
                                planDate = DateUtils.parseDate(dt, "yyyy-MM-dd");
                            } catch (ParseException e) {
                                log.error("持仓方案时间解析出错：" + dt, e);
                                continue;
                            }
                            FundBondPO po = new FundBondPO(code, planDate);

                            // 持仓债券
                            Element table = ele.select("table.w782").get(0).select("tbody").first();
                            for (Element tr : table.getElementsByTag("tr")) {
                                po.setBondCode(tr.children().get(1).text());
                                po.setBondName(tr.children().get(2).text());
                                String tmp = tr.children().get(3).text();
                                if(tmp.contains("--")){
                                    po.setValueRate(0);
                                }else {
                                    po.setValueRate(Float.valueOf(tmp.replace("%", "")));
                                }

                                try {
                                    po.setMarketValue(NumberFormat.getInstance().parse(tr.children().get(4).text()).floatValue());
                                } catch (ParseException e) {
                                    log.error("持仓市值解析出错：" + tr.text(), e);
                                    po.setMarketValue(0);
                                }
                                log.info("债券持仓：{}", JSONUtil.json(po));

                                // 入库
                                FundBond fb = CopyUtils.copy(po, FundBond.class);
                                int rows = fundBondMapper.insert(fb);
                                log.info("insert fund-bond {}", rows);
                            }
                        }
                    } else {
                        log.error("获取债券持仓历史失败：{}", url);
                    }
                }
            } else {
                log.info("基金无债券持仓：{}", code);
            }
        }
    }

    @Test
    public void testJjgk() {
        log.info("测试：基金概况、手续费");

        Date now = new Date();
        // 加载所有基金
        List<Fund> fundList = fundMapper.selectList(null);

        // 过滤已抓取
        List<FundInfo> fiList = fundInfoMapper.selectList(new QueryWrapper<FundInfo>().select("code"));
        if(!CollectionUtils.isEmpty(fiList)) {
             List<String> dbCodes = fiList.parallelStream().map(FundInfo::getCode).collect(Collectors.toList());
             fundList = fundList.parallelStream().filter(f->!dbCodes.contains(f.getCode())).collect(Collectors.toList());
        }

        for (Fund item : fundList) {
            String code = item.getCode();
            String url = MessageFormat.format(FundConfig.FUND_GK, code);
            FundPO fund = new FundPO(code);
            FundFeePO fee = new FundFeePO(code);
            FundInfoPO info = new FundInfoPO(code);

            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                Document html = Jsoup.parse(res.getBody());

                // 基本概况
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
                if (tmp.contains("---")) {
                    fund.setGm(0);
                } else {
                    String gm = tmp.split("亿")[0];
                    fund.setGm(Float.parseFloat(gm));
                }

                // 基金公司
                Elements tr5 = trs.get(4).getElementsByTag("td");
                Element td = tr5.get(0).getElementsByTag("a").get(0);
                fund.setCompany(td.text());
                tmp = td.attr("href");
                tmp = FundConfig.pickCode(tmp, FundConfig.COMPANY_REG);
                fund.setCompanyCode(tmp);
                log.info("基金概况：{}", JSONUtil.json(fund));

                // 更新基金概况信息
                item.setGk(fund);
                item.setSynTime(now);
                int rows = fundMapper.updateById(item);
                log.info("更新基金概况信息：{}-{}", rows, JSONUtil.json(item));

                // 管理费、托管费
                Elements tr7 = trs.get(6).getElementsByTag("td");
                tmp = tr7.get(0).text();
                if (tmp.contains("%")) {
                    String glf = tmp.split("%")[0];
                    fee.setGlf(Float.valueOf(glf));
                } else {
                    fee.setGlf(0);
                }
                tmp = tr7.get(1).text();
                String tgf = tmp.split("%")[0];
                fee.setTgf(Float.valueOf(tgf));

                // 销售服务费、认购费
                Elements tr8 = trs.get(7).getElementsByTag("td");
                tmp = tr8.get(0).text();
                if (tmp.contains("---")) {
                    fee.setXsh(0);
                } else {
                    String xsh = tmp.split("%")[0];
                    fee.setXsh(Float.valueOf(xsh));
                }
                if (tr8.get(1).getElementsByTag("span").size() > 0) {
                    tmp = tr8.get(1).getElementsByTag("span").get(0).text();
                } else {
                    tmp = tr8.get(1).text();
                }
                if (tmp.contains("---")) {
                    fee.setRgf(0);
                } else {
                    String rgf = tmp.split("%")[0];
                    fee.setRgf(Float.valueOf(rgf));
                }

                // 申购、赎回费
                Elements tr9 = trs.get(8).getElementsByTag("td");
                if (tr9.get(0).getElementsByTag("span").size() > 0) {
                    tmp = tr9.get(0).getElementsByTag("span").get(0).text();
                } else {
                    tmp = tr9.get(0).text();
                }
                if (tmp.contains("---")) {
                    fee.setSgf(0);
                } else {
                    String sgf = tmp.split("%")[0];
                    fee.setSgf(Float.valueOf(sgf));
                }
                tmp = tr9.get(1).text();
                if (tmp.contains("---")) {
                    fee.setShf(0);
                } else {
                    String shf = tmp.split("%")[0];
                    fee.setShf(Float.valueOf(shf));
                }
                log.info("手续费：{}", JSONUtil.json(fee));

                // 入库：手续费
                FundFee fundFee = CopyUtils.copy(fee, FundFee.class);
                FundFee dbFee = fundFeeMapper.selectOne(new QueryWrapper<FundFee>().eq("code", code));
                if(dbFee!=null){
                    fundFee.setId(dbFee.getId());
                    rows = fundFeeMapper.updateById(fundFee);
                    log.info("update fee {}", rows);
                }else{
                    rows = fundFeeMapper.insert(fundFee);
                    log.info("insert fee {}", rows);
                }

                // 基金概况：投资目标、投资理念、投资范围、投资策略、分红政策
                Elements boxItems  = html.getElementsByClass("boxitem");
                for (Element ele : boxItems) {
                    String title = ele.selectFirst("h4.t").text();
                    if(ele.selectFirst("p")!=null){
                        String content = ele.selectFirst("p").text();
                        if("投资目标".equals(title)){
                            info.setTarget(content);
                        }else if ("投资理念".equals(title)){
                            info.setIdea(content);
                        }else if ("投资范围".equals(title)){
                            info.setScope(content);
                        }else if ("投资策略".equals(title)){
                            info.setStrategy(content);
                        }else if ("分红政策".equals(title)){
                            info.setBonus(content);
                        }
                    }
                }

                // 入库：基金概况
                FundInfo fundInfo = CopyUtils.copy(info, FundInfo.class);
                FundInfo dbFi = fundInfoMapper.selectOne(new QueryWrapper<FundInfo>().eq("code", code));
                if(dbFi!=null){
                    fundInfo.setId(dbFi.getId());
                    rows = fundInfoMapper.updateById(fundInfo);
                    log.info("update info {}", rows);
                }else{
                    rows = fundInfoMapper.insert(fundInfo);
                    log.info("insert info {}", rows);
                }
            } else {
                log.error("基金概况信息抓取失败：{}，{}", code, url);
            }
        }
    }

    @Test
    public void testJzh() {
        log.info("测试：历史净值");
        Unirest.config().addDefaultHeader("Referer", FundConfig.HOST_INFO);
        Unirest.config().addDefaultHeader("Host", FundConfig.HOST_API);

        // 加载所有基金
        List<Fund> fundList = fundMapper.selectList(new QueryWrapper<Fund>().select("code"));
        List<String> fundCodes = fundList.parallelStream().map(Fund::getCode).collect(Collectors.toList());

        // 遍历抓取
        for (String code : fundCodes) {
            int fvSize = fundValMapper.selectCount(new QueryWrapper<FundVal>().eq("code", code));
            if(fvSize>0){
                log.info("基金净值已抓取：{} {}", code, fvSize);
                continue;
            }

            // 分页抓取
            int pageIndex = 1;
            int pageTotal = 1;
            String jq = new StringBuilder("jQuery").append(IdWorker.getIdStr()).append("1_")
                    .append(String.valueOf(System.currentTimeMillis())).toString();
            List<FundJzhPO> jzhList = new ArrayList<>();
            for (; pageIndex <= pageTotal; pageIndex++) {
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

            // 入库
            List<FundVal> fvList = CopyUtils.copy(jzhList, FundVal.class);
            for (FundVal fv : fvList) {
                int rows = fundValMapper.insert(fv);
                log.info("insert fund-val {}", rows);
            }
        }
    }

    @Test
    public void testTs() {
        log.info("测试：特色数据");

        Date now = new Date();
        // 加载所有基金
        List<Fund> fundList = fundMapper.selectList(null);

        // 过滤已抓取
        List<FundTs> tsList = fundTsMapper.selectList(null);
        if (!CollectionUtils.isEmpty(tsList)) {
            List<String> codeList = tsList.stream().map(FundTs::getCode).collect(Collectors.toList());
            fundList = fundList.stream().filter(f -> !codeList.contains(f.getCode())).collect(Collectors.toList());
        }

        for (Fund item : fundList) {
            String code = item.getCode();
            String url = MessageFormat.format(FundConfig.FUND_TS, code);
            FundDataPO tsData = new FundDataPO(code);

            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                Document html = Jsoup.parse(res.getBody());

                // 基金风险-风险等级
                Elements tmpElements = html.getElementsByClass("allfxdj");
                if (tmpElements.get(0).getElementsByClass("chooseLow").size() > 0) {
                    Element tmpEle = tmpElements.get(0).getElementsByClass("chooseLow").get(0);
                    tsData.setWholeRiskLevel(tmpEle.text());
                } else {
                    tsData.setWholeRiskLevel(-1);
                }
                if (tmpElements.get(1).getElementsByClass("chooseLow").size() > 0) {
                    Element tmpEle = tmpElements.get(1).getElementsByClass("chooseLow").get(0);
                    tsData.setSameRiskLevel(tmpEle.text());
                } else {
                    tsData.setSameRiskLevel(-1);
                }

                // 基金风险-风险指标
                Element tmpEle = html.getElementsByClass("fxtb").get(0);
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
                    String tmp = tmpElements.get(1).text();
                    if (tmp.contains("--")) {
                        tsData.setTrackDiff(0);
                    } else {
                        tsData.setTrackDiff(Float.valueOf(tmp.replace("%", "")));
                    }
                    tmp = tmpElements.get(2).text();
                    if (tmp.contains("--")) {
                        tsData.setSameDiff(0);
                    } else {
                        tsData.setSameDiff(Float.valueOf(tmp.replace("%", "")));
                    }
                }
                log.info("特色数据：{}", JSONUtil.json(tsData));

                // 入库
                FundTs ts = CopyUtils.copy(tsData, FundTs.class);
                ts.setSynTime(now);
                int rows = fundTsMapper.insert(ts);
                log.info("insert {}", rows);
            } else {
                log.error("基金特色数据抓取失败：{}，{}", code, url);
            }
        }
    }

    @Test
    public void testJjjl() {
        log.info("测试：基金经理");

        // 加载基金列表
        List<Fund> fundList = fundMapper.selectList(null);

        // 过滤已抓取
        List<FundManager> fmList = fundManagerMapper.selectList(null);
        if(!CollectionUtils.isEmpty(fmList)){
            Set<String> dbFundCodes = fmList.stream().map(FundManager::getCode).collect(Collectors.toSet());
            fundList = fundList.stream().filter(f->!dbFundCodes.contains(f.getCode())).collect(Collectors.toList());
        }

        // 遍历基金
        for (Fund item : fundList) {
            String code = item.getCode();
            String url = MessageFormat.format(FundConfig.FUND_JL, code);
            log.info("抓取基金经理变动信息：{}", code);

            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                Document html = Jsoup.parse(res.getBody());
                Elements trs = html.selectFirst("div.boxitem").selectFirst("tbody").getElementsByTag("tr");
                List<FundManagerPO> poList = new ArrayList<>(trs.size());
                for (Element tr : trs) {
                    // 解析管理时间
                    Date start = null, end = null;
                    String tmp = tr.child(0).text();
                    try {
                        start = DateUtils.parseDate(tmp, "yyyy-MM-dd");
                    } catch (ParseException e) {
                        log.error("基金经理-起始期解析出错：" + tmp, e);
                    }
                    tmp = tr.child(1).text();
                    if (!"至今".equals(tmp)) {
                        try {
                            end = DateUtils.parseDate(tmp, "yyyy-MM-dd");
                        } catch (ParseException e) {
                            log.error("基金经理-截止期解析出错：" + tmp, e);
                        }
                    }

                    // 解析管理期基金经理
                    Elements as = tr.child(2).getElementsByTag("a");
                    for (Element a : as) {
                        FundManagerPO po = new FundManagerPO(code, start, end);
                        po.setManagerName(a.text());
                        po.setManagerCode(FundConfig.pickCode(a.attr("href"), FundConfig.MANAGER_REG));
                        log.info("基金经理：{}", JSONUtil.json(po));
                        poList.add(po);
                    }
                }

                // 入库
                List<FundManager> fmHisList = CopyUtils.copy(poList, FundManager.class);
                for (FundManager fm : fmHisList) {
                    int rows = fundManagerMapper.insert(fm);
                    log.info("insert fund-manager {}", rows);
                }
            } else {
                log.error("基金经理数据抓取失败：{}，{}", code, url);
            }
        }
    }

    @Test
    public void testJjgs() {
        log.info("测试：基金公司");

        // 加载基金公司列表
        List<Fund> fundList = fundMapper.selectList(null);
        Set<String> cmpCodes = fundList.stream().map(Fund::getCompanyCode).collect(Collectors.toSet());

        // 过滤已抓取
        List<Company> companyList = companyMapper.selectList(null);
        if(!CollectionUtils.isEmpty(companyList)) {
            List<String> dbCmpCodes = companyList.stream().map(Company::getCode).collect(Collectors.toList());
            cmpCodes.removeAll(dbCmpCodes);
        }
        Date now = new Date();

        // 遍历抓取
        for (String code : cmpCodes) {
            // String code = "80000229";
            String url = MessageFormat.format(FundConfig.COMPANY_GK, code);
            log.info("抓取基金公司：{}", code);
            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                Document html = Jsoup.parse(res.getBody());

                Elements trs = html.selectFirst("table.category-list").getElementsByTag("tr");
                // 公司名称
                String name = trs.get(0).child(1).text();
                FundCompanyPO po = new FundCompanyPO(code, name);

                // 成立日期
                String tmp = trs.get(3).child(1).text();
                try {
                    po.setSetupDate(DateUtils.parseDate(tmp, "yyyy-MM-dd"));
                } catch (ParseException e) {
                    log.error("基金公司-成立日期解析失败：" + code, e);
                }

                // 注册地址
                po.setRegAddress(trs.get(5).child(1).text());

                // 管理基金数量
                tmp = trs.get(11).child(1).getElementsByTag("a").first().text();
                po.setFundTotal(Integer.valueOf(tmp));

                // 基金经理人数
                tmp = trs.get(12).child(1).text();
                if(tmp.contains("--")){
                    po.setFundGm(0);
                }else {
                    po.setFundGm(Float.valueOf(tmp.replace("亿元", "")));
                }
                tmp = trs.get(12).child(3).text();
                po.setManagerTotal(Integer.valueOf(tmp.replace("人", "")));
                log.info("基金公司：{}", JSONUtil.json(po));

                // 入库
                Company cmp = CopyUtils.copy(po,Company.class);
                cmp.setSynTime(now);
                int rows = companyMapper.insert(cmp);
                log.info("insert company {}", rows);
            } else {
                log.error("基金公司数据抓取失败：{}，{}", code, url);
            }
        }
    }

}
