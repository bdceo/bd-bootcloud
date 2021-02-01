package com.bdsoft.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdsoft.crawler.modules.index.IndexConfig;
import com.bdsoft.crawler.modules.index.po.IndexPO;
import com.bdsoft.crawler.modules.index.xhr.IndexCommonResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试：抓取指数数据
 */
@Slf4j
public class FetchIndexTest extends SuperTest {

    @Test
    public void testSearchIndex() {
        log.info("测试：搜索指数");

        // 获取数据总页数
        IndexCommonResponse firstPage = this.getPageTotal();
        if (firstPage == null) {
            log.info("获取首页数据出错");
            return;
        }

        // 分页抓取
        List<IndexPO> data = new ArrayList<>(firstPage.getTotal());
        for (int page = 1; page <= firstPage.getTotalPage(); page++) {
            String url = MessageFormat.format(IndexConfig.INDEX_SEARCH, page);
            log.info("获取分页数据：{}-{}", page, url);

            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                JSONObject resObj = JSONObject.parseObject(res.getBody());
                JSONArray resList = resObj.getJSONArray("list");
                log.info("解析第{}页数据：{}", page, resList.size());
                List<IndexPO> pageData = this.parseSearch(resList);
                data.addAll(pageData);
                this.viewData(pageData);
            } else {
                log.info("分页数据获取失败：{}", page);
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
