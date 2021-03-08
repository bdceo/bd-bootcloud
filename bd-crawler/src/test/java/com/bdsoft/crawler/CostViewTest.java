package com.bdsoft.crawler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdsoft.crawler.modules.delivery.entity.Cost;
import com.bdsoft.crawler.modules.delivery.entity.Delivery;
import com.bdsoft.crawler.modules.delivery.enums.TradeTypeEnum;
import com.bdsoft.crawler.modules.delivery.mapper.CostMapper;
import com.bdsoft.crawler.modules.delivery.mapper.DeliveryMapper;
import com.bdsoft.crawler.modules.fund.entity.FundVal;
import com.bdsoft.crawler.modules.fund.mapper.FundValMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 成本中心可视化数据准备
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CostViewTest {

    @Autowired
    private FundValMapper fundValMapper;
    @Autowired
    private DeliveryMapper deliveryMapper;
    @Autowired
    private CostMapper costMapper;

    @Test
    public void testLineVal() {
        String code = "512660";

        // 建仓日期
        Delivery first = deliveryMapper.selectOne(new QueryWrapper<Delivery>()
                .eq("code", code).orderByAsc("dt").last("limit 1"));
        log.info("加载建仓日以来的净值变化：{} {}", code, first.getDt().toLocaleString());

        // 加载净值历史
        List<FundVal> valList = fundValMapper.selectList(new QueryWrapper<FundVal>()
                .eq("code", code).ge("dt", first.getDt()).orderByAsc("dt"));

        StringBuilder label = new StringBuilder();
        StringBuilder data = new StringBuilder();
        for (FundVal v : valList) {
            label.append("'").append(DateFormatUtils.format(v.getDt(), "yyyy-MM-dd")).append("',");
            data.append("'").append(v.getUnitVal()).append("',");
        }
        label.replace(label.length() - 1, label.length(), "");
        data.replace(data.length() - 1, data.length(), "");
        log.info("净值线");
        log.info("labels: [{}],", label.toString());
        log.info("data: [{}],", data.toString());
    }

    @Test
    public void testLineDelivery() {
        String code = "512660";

        // 建仓日期
        Delivery first = deliveryMapper.selectOne(new QueryWrapper<Delivery>()
                .eq("code", code).orderByAsc("dt").last("limit 1"));
        log.info("加载建仓日以来的净值变化：{} {}", code, first.getDt().toLocaleString());

        // 加载净值历史
        List<FundVal> valList = fundValMapper.selectList(new QueryWrapper<FundVal>()
                .select("dt").eq("code", code).ge("dt", first.getDt()).orderByAsc("dt"));

        // 加载买入记录
        List<Delivery> delList = deliveryMapper.selectList(new QueryWrapper<Delivery>()
                .eq("code", code).eq("op", TradeTypeEnum.TP1.getCode()).orderByAsc("dt"));

        StringBuilder data = new StringBuilder();
        Map<Date, List<Delivery>> doMap = delList.stream().collect(Collectors.groupingBy(Delivery::getDt));
        for (FundVal d : valList) {
            List<Delivery> orders = doMap.get(d.getDt());
            if (orders != null) {
                float total = 0;
                int dealNum = 0;
                for (Delivery order : orders) {
                    total += order.getTotal() * -1;
                    dealNum += order.getDealNum();
                }
                if (dealNum == 0) {
                    data.append(",");
                } else {
                    float price = total / dealNum;
                    data.append("'").append(String.format("%.4f", price)).append("',");
                }
            } else {
                data.append(",");
            }
        }
        data.replace(data.length() - 1, data.length(), "");
        log.info("买入点位");
        log.info("data: [{}],", data.toString());
    }

    @Test
    public void testLineCost() {
        String code = "512660";

        // 建仓日期
        Delivery first = deliveryMapper.selectOne(new QueryWrapper<Delivery>()
                .eq("code", code).orderByAsc("dt").last("limit 1"));
        log.info("加载建仓日以来的净值变化：{} {}", code, first.getDt().toLocaleString());

        // 加载净值历史
        List<FundVal> valList = fundValMapper.selectList(new QueryWrapper<FundVal>()
                .select("dt").eq("code", code).ge("dt", first.getDt()).orderByAsc("dt"));

        // 加载持仓成本
        List<Cost> costList = costMapper.selectList(new QueryWrapper<Cost>()
                .eq("code", code).orderByAsc("dt"));
        StringBuilder data = new StringBuilder();
        Map<Date, Cost> doMap = costList.stream().collect(Collectors.toMap(Cost::getDt, Function.identity(), (o1, o2) -> o1));
        for (FundVal d : valList) {
            Cost cost = doMap.get(d.getDt());
            if (cost != null) {
                data.append("'").append(String.format("%.4f", cost.getCostPrice())).append("',");
            } else {
                data.append(",");
            }
        }
        data.replace(data.length() - 1, data.length(), "");
        log.info("成本线");
        log.info("data: [{}],", data.toString());
    }

}
