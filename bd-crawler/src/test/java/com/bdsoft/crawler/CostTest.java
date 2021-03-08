package com.bdsoft.crawler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdsoft.crawler.modules.delivery.entity.Cost;
import com.bdsoft.crawler.modules.delivery.entity.Delivery;
import com.bdsoft.crawler.modules.delivery.enums.AccountTypeEnum;
import com.bdsoft.crawler.modules.delivery.enums.TradeTypeEnum;
import com.bdsoft.crawler.modules.delivery.mapper.CostMapper;
import com.bdsoft.crawler.modules.delivery.mapper.DeliveryMapper;
import com.bdsoft.crawler.modules.delivery.service.ICostService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计算成本
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CostTest {

    @Autowired
    private DeliveryMapper deliveryMapper;
    @Autowired
    private CostMapper costMapper;
    @Resource(name = "costServiceImpl")
    private ICostService costService;

    @Test
    public void calcCostHuaTai() {
        // 加载交割流水
        List<Delivery> data = deliveryMapper.selectList(new QueryWrapper<Delivery>()
                .eq("account", AccountTypeEnum.TP1.getName()).orderByAsc("dt"));
        if (CollectionUtils.isEmpty(data)) {
            log.info("加载交割单流水空");
            return;
        }

        // 按产品编码分组
        Map<String, List<Delivery>> stockMap = data.stream().collect(Collectors.groupingBy(Delivery::getCode));

        Date now = new Date();
        // 计算产品成本
        for (String code : stockMap.keySet()) {
            List<Delivery> deliveryList = stockMap.get(code);
            log.info("计算持仓成本：{}-{}", code, deliveryList.get(0).getName());
            int dealNum = 0;
            float total = 0;
            Map<Date, Cost> costMap = new LinkedHashMap<>();
            for (Delivery delivery : deliveryList) {
                if (delivery.getOp().equals(TradeTypeEnum.TP1.getCode())) {
                    dealNum += delivery.getDealNum();
                    total += delivery.getTotal() * -1;
                } else {
                    dealNum -= delivery.getDealNum();
                    total -= delivery.getTotal();
                }
                float cp = total / dealNum;

                Cost cost = new Cost();
                cost.setDt(delivery.getDt());
                cost.setCode(code);
                cost.setName(delivery.getName());
                cost.setCostPrice(cp);
                cost.setCreateTime(now);
                costMap.put(delivery.getDt(), cost);
            }

            // 刷新产品成本
            int rows = costMapper.delete(new QueryWrapper<Cost>().eq("code", code));
            log.info("清空历史成本结果：{} {}", code, rows);
            boolean dbRes = costService.saveBatch(costMap.values());
            log.info("产品成本计算入库结果：{}， {}", code, dbRes);
        }
    }


}
