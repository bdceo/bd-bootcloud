package com.bdsoft.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdsoft.crawler.common.CopyUtils;
import com.bdsoft.crawler.modules.index.IndexConfig;
import com.bdsoft.crawler.modules.index.entity.Index;
import com.bdsoft.crawler.modules.index.entity.IndexFund;
import com.bdsoft.crawler.modules.index.entity.IndexStock;
import com.bdsoft.crawler.modules.index.entity.IndustryCs;
import com.bdsoft.crawler.modules.index.mapper.IndexFundMapper;
import com.bdsoft.crawler.modules.index.mapper.IndexMapper;
import com.bdsoft.crawler.modules.index.mapper.IndexStockMapper;
import com.bdsoft.crawler.modules.index.mapper.IndustryCsMapper;
import com.bdsoft.crawler.modules.index.po.FundRePO;
import com.bdsoft.crawler.modules.index.po.IndexPO;
import com.bdsoft.crawler.modules.index.po.StockRePO;
import com.bdsoft.crawler.modules.index.xhr.IndexCommonResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 测试：抓取指数数据
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchIndexTest extends SuperTest {

    @Autowired
    private IndexMapper indexMapper;
    @Autowired
    private IndexFundMapper indexFundMapper;
    @Autowired
    private IndexStockMapper indexStockMapper;
    @Autowired
    private IndustryCsMapper industryCsMapper;

    @Test
    public void testSearchIndex() {
        log.info("测试：搜索指数");

        // 获取数据总页数
        IndexCommonResponse firstPage = this.getPageTotal();
        if (firstPage == null) {
            log.info("获取首页数据出错");
        }
        Date synTime = new Date();

        // 分页抓取
        for (int page = 1; page <= firstPage.getTotalPage(); page++) {
            String url = MessageFormat.format(IndexConfig.INDEX_SEARCH, page);
            log.info("获取分页数据：{}-{}", page, url);

            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                JSONObject resObj = JSONObject.parseObject(res.getBody());
                JSONArray resList = resObj.getJSONArray("list");
                log.info("解析第{}页数据：{}", page, resList.size());
                List<IndexPO> pageData = this.parseSearch(resList);
                this.viewData(pageData);

                // 入库
                List<Index> indexList = CopyUtils.copy(pageData, Index.class);
                for (Index index : indexList) {
                    index.setSynTime(synTime);
                    int rows = indexMapper.insert(index);
                    log.info("insert {}", rows);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                log.info("分页数据获取失败：{}", page);
            }
        }
    }

    @Test
    public void testIndexDetail() throws Exception {
        log.info("测试：指数详情（简介、十大权重股）");

        // 加载数据库指数信息
        List<Index> indexList = indexMapper.selectList(null);
        if (CollectionUtils.isEmpty(indexList)) {
            log.info("数据库指数信息加载失败");
            return;
        }

        Date now = new Date();

        // 遍历指数
        for (Index index : indexList) {
            String code = index.getCode();
            String url = MessageFormat.format(IndexConfig.INDEX_PAGE, code);

            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                Document html = Jsoup.parse(res.getBody());
                // 简介
                Element infoEle = html.getElementsByClass("js_txt_new").get(0);
                log.info("指数简介：{}", infoEle.text());

                // 更新指数描述信息
                index.setInfo(infoEle.text());
                int rows = indexMapper.updateById(index);
                log.info("update {}", rows);

                // 十大权重股：div > h2 > div
                Element h2Ele = html.getElementsByClass("details_r").get(0)
                        .getElementsByTag("h2").last();
                // 截止日期
                String dateTxt = h2Ele.getElementsByTag("p").get(0).text();
                dateTxt = dateTxt.split(":")[1];
                Date endDate = DateUtils.parseDate(dateTxt, "yyyy-MM-dd");
                log.info("截止日期：{}", dateTxt);

                // 权重股
                List<StockRePO> poList = new ArrayList<>();
                Element tableEle = h2Ele.nextElementSibling();
                Elements trs = tableEle.getElementsByTag("tbody").get(0).getElementsByTag("tr");
                for (Element tr : trs) {
                    Elements tds = tr.getElementsByTag("td");
                    String stockCode = tds.get(0).text();
                    String stockName = tds.get(1).text();
                    String industry = tds.get(2).text();
                    String tmp = tds.get(3).text();
                    float weight = StringUtils.isNotBlank(tmp) ? Float.valueOf(tmp) : 0;
                    log.info("指数={}，代码={}，名称={}，行业={}，权重={}", code, stockCode, stockName, industry, weight);
                    poList.add(new StockRePO(code, stockCode, stockName, industry, weight));
                }

                // 解析，填充行业
                List<IndexStock> isList = CopyUtils.copy(poList, IndexStock.class);
                for (IndexStock is : isList) {
                    is.setEndDate(endDate);
                    is.setSynTime(now);
                    if (StringUtils.isNotBlank(is.getIndustryName())) {
                        List<IndustryCs> indList = industryCsMapper.selectList(new QueryWrapper<IndustryCs>()
                                .eq("name", is.getIndustryName()).orderByAsc("level"));
                        if (!CollectionUtils.isEmpty(indList)) {
                            is.setIndustryCode(indList.get(0).getCode());
                        }
                    }
                    rows = indexStockMapper.insert(is);
                    log.info("insert {}", rows);
                }
            } else {
                log.error("指数详情抓取失败：{}, {}", code, url);
            }
        }
    }

    @Test
    public void testIndexFund() throws Exception {
        log.info("测试：指数产品");

        // 加载数据库指数信息
        List<Index> indexList = indexMapper.selectList(null);
        if (CollectionUtils.isEmpty(indexList)) {
            log.info("数据库指数信息加载失败");
            return;
        }

        Date now = new Date();

        // 遍历指数
        for (Index index : indexList) {
            String code = index.getCode();
            String name = index.getName().replaceAll(" ","");
            String url = MessageFormat.format(IndexConfig.INDEX_FUND_PAGE, name);

            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                Document html = Jsoup.parse(res.getBody());
                // 产品列表
                Element tableEle = html.getElementById("item");
                if (tableEle.hasText()) {
                    Elements trs = tableEle.getElementsByTag("tr");
                    for (Element tr : trs) {
                        Elements tds = tr.getElementsByTag("td");
                        // 证券代码
                        String fundCode = tds.get(0).text();
                        // 基金名称
                        String fundName = tds.get(1).text();
                        // 成立日期
                        String setup = tds.get(2).text();
                        Date setupDate = DateUtils.parseDate(setup, "yyyy-MM-dd");
                        // 基金类型：股票、混合
                        String fundType = tds.get(3).text();
                        // 产品类型：指数基金、连接基金、ETF、LOF
                        String productType = tds.get(4).text();
                        // 基金公司in
                        String company = tds.last().text();

                        FundRePO po = new FundRePO(code, fundCode, fundName, setupDate, fundType, productType, company);
                        log.info("{}\t{}\t{}\t{}\t{}", fundCode, fundName, setup, fundType, productType, company);

                        IndexFund ifd = CopyUtils.copy(po, IndexFund.class);
                        ifd.setSynTime(now);
                        int rows = indexFundMapper.insert(ifd);
                        log.info("insert {}", rows);
                    }
                } else {
                    log.info("指数：{} 无跟踪产品", name);
                }
            } else {
                log.error("指数产品抓取失败：{}，{}", name, url);
            }
        }
    }

    /**
     * 格式化输出
     *
     * @param data
     */
    private void viewData(List<IndexPO> data) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        data.forEach(i -> log.info("code={}\tname={}\tserial={}\ttype={}\tasset={}\tregion={}",
                i.getCode(), i.getName(), i.getSerial(), i.getType(), i.getAssets(), i.getRegion()));
    }

    /**
     * 解析指数搜索
     *
     * @param data
     * @return
     */
    private List<IndexPO> parseSearch(JSONArray data) {
        List<IndexPO> poList = new ArrayList<>(data.size());
        for (int i = 0; i < data.size(); i++) {
            JSONObject obj = data.getJSONObject(i);
            IndexPO po = new IndexPO();
            po.setCode(obj.getString("index_code"));
            po.setName(obj.getString("indx_sname"));
            po.setStockNum(obj.getInteger("num"));
            po.setSerial(obj.getString("class_series"));
            po.setType(obj.getString("class_classify"));
            po.setAssets(obj.getString("class_assets"));
            po.setRegion(obj.getString("class_region"));
            po.setCurrency(obj.getString("class_currency"));
            poList.add(po);
        }
        return poList;
    }

    /**
     * 获取数据总页数
     */
    private IndexCommonResponse getPageTotal() {
        String url = MessageFormat.format(IndexConfig.INDEX_SEARCH, 1);
        log.info("获取指数数据总页数：{}", url);

        HttpResponse<String> res = Unirest.get(url).asString();
        if (res.isSuccess()) {
            IndexCommonResponse resObj = JSON.parseObject(res.getBody(), IndexCommonResponse.class);
            return resObj;
        }

        return null;
    }

}
