package com.bdsoft.crawler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bdsoft.crawler.common.CopyUtils;
import com.bdsoft.crawler.modules.fund.FundConfig;
import com.bdsoft.crawler.modules.stock.StockConfig;
import com.bdsoft.crawler.modules.stock.entity.StockHolder;
import com.bdsoft.crawler.modules.stock.mapper.StockHolderMapper;
import com.bdsoft.crawler.modules.stock.po.StockHolderPO;
import com.bdsoft.crawler.modules.stock.service.IStockHolderService;
import com.bdsoft.crawler.modules.stock.xhr.ShItem;
import com.bdsoft.crawler.modules.stock.xhr.ShResponse;
import com.hshc.basetools.json.JSONUtil;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 股东信息
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchStockHolderTest extends SuperTest {

    @Autowired
    private StockHolderMapper stockHolderMapper;
    @Resource(name = "stockHolderServiceImpl")
    private IStockHolderService stockHolderService;

    @Test
    public void testStockHolder() {
        log.info("测试：十大流通股东");
        Unirest.config().addDefaultHeader("Referer", StockConfig.STOCK_HOLDER_REFER);
        Unirest.config().addDefaultHeader("Host", StockConfig.STOCK_HOLDER_HOST);

        // 分页抓取
        int pageIndex = 1;
        int pageTotal = 550;
        String jq = new StringBuilder("jQuery").append(IdWorker.getIdStr()).append("1_")
                .append(String.valueOf(System.currentTimeMillis())).toString();
        List<StockHolderPO> poList = new ArrayList<>();
        for (; pageIndex <= pageTotal; pageIndex++) {
            String url = MessageFormat.format(StockConfig.STOCK_HOLDER, jq, pageIndex);
            HttpResponse<String> res = Unirest.get(url).asString();
            if (res.isSuccess()) {
                String json = FundConfig.pickXhrData(res.getBody());
                List<StockHolderPO> resObj = JSONObject.parseArray(json, StockHolderPO.class);
                if (!CollectionUtils.isEmpty(resObj)) {
                    for (StockHolderPO item : resObj) {
                        log.info("股东：{}/{} {}", pageIndex, pageTotal, JSONUtil.json(item));
                    }
                    poList.addAll(resObj);
                    try {
                        Thread.sleep(Math.min(2000, FundConfig.RANDOM.nextInt(10000)));
                    } catch (InterruptedException e) {
                        log.error("分页sleep异常：", e);
                    }
                } else {
                    log.error("抓取结果异常：{}, {}", pageIndex, url);
                    continue;
                }
            } else {
                log.error("分页抓取失败：{}, {}", pageIndex, url);
            }
        }

        // 入库
        Date now = new Date();
        List<StockHolder> shList = CopyUtils.copy(poList, StockHolder.class);
        shList.forEach(s -> s.setCreateTime(now));
        stockHolderService.saveBatch(shList);
    }

    @Test
    public void testStockHolder2() {
        log.info("测试：十大流通股东");
        Unirest.config().addDefaultHeader("Referer", StockConfig.STOCK_HOLDER_REFER);
        Unirest.config().addDefaultHeader("Host", StockConfig.STOCK_HOLDER_HOST);

        // 分页抓取
        int pageIndex = 1;
        int pageTotal = 550;
        String jq = new StringBuilder("jQuery").append(IdWorker.getIdStr()).append("1_")
                .append(String.valueOf(System.currentTimeMillis())).toString();
        List<ShItem> poList = new ArrayList<>();
        for (; pageIndex <= pageTotal; pageIndex++) {
            String url = MessageFormat.format(StockConfig.STOCK_HOLDER, jq, pageIndex);
            HttpResponse<String> res = null;
            try {
                res = Unirest.get(url).asString();
            } catch (UnirestException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e2) {
                    log.error("分页sleep异常：", e2);
                }
                pageIndex--;
                continue;
            }
            if (res.isSuccess()) {
                String json = FundConfig.pickXhrData(res.getBody());
                ShResponse resObj = JSONObject.parseObject(json, ShResponse.class);
                if (resObj != null && !CollectionUtils.isEmpty(resObj.getData())) {
                    for (ShItem item : resObj.getData()) {
                        log.info("股东：{}/{} {}", pageIndex, pageTotal, JSONUtil.json(item));
                    }
                    poList.addAll(resObj.getData());
                    try {
                        Thread.sleep(Math.min(500, FundConfig.RANDOM.nextInt(10000)));
                    } catch (InterruptedException e) {
                        log.error("分页sleep异常：", e);
                    }
                } else {
                    log.error("抓取结果异常：{}, {}", pageIndex, url);
                    continue;
                }
            } else {
                log.error("分页抓取失败：{}, {}", pageIndex, url);
            }
        }

        // 入库
        Date now = new Date();
        List<StockHolder> shList = CopyUtils.copy(poList, StockHolder.class);
        shList.forEach(s -> s.setCreateTime(now));
        stockHolderService.saveBatch(shList);
    }

}
