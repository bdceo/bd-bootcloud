package com.bdsoft.crawler.modules.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdsoft.crawler.common.vo.Res;
import com.bdsoft.crawler.modules.delivery.entity.Cost;
import com.bdsoft.crawler.modules.delivery.entity.Delivery;
import com.bdsoft.crawler.modules.delivery.enums.TradeTypeEnum;
import com.bdsoft.crawler.modules.delivery.mapper.CostMapper;
import com.bdsoft.crawler.modules.delivery.mapper.DeliveryMapper;
import com.bdsoft.crawler.modules.delivery.service.ICostService;
import com.bdsoft.crawler.modules.delivery.vo.FundCostViewVO;
import com.bdsoft.crawler.modules.fund.entity.FundVal;
import com.bdsoft.crawler.modules.fund.mapper.FundValMapper;
import com.hshc.basetools.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 持仓成本 服务实现类
 * </p>
 *
 * @author bdceo
 * @since 2021-03-08
 */
@Slf4j
@Service
public class CostServiceImpl extends ServiceImpl<CostMapper, Cost> implements ICostService {

    @Autowired
    private FundValMapper fundValMapper;
    @Autowired
    private DeliveryMapper deliveryMapper;
    @Autowired
    private CostMapper costMapper;

    @Override
    public Res<FundCostViewVO> getCostViewData(String code) {
        // 加载交割记录：买入
        List<Delivery> deliveryList = deliveryMapper.selectList(new QueryWrapper<Delivery>()
                .eq("code", code).eq("op", TradeTypeEnum.TP1.getCode()).orderByAsc("dt"));
        if (CollectionUtils.isEmpty(deliveryList)) {
            log.info("基金无交割记录：{}", code);
            return Res.err("基金无交割记录");
        }

        // 获取基金建仓日期
        Delivery first = deliveryList.get(0);
        log.info("加载基金交割流水：{}({}) 共{}次买入记录", first.getName(), code, deliveryList.size());

        // 加载净值历史
        List<FundVal> valList = fundValMapper.selectList(new QueryWrapper<FundVal>()
                .eq("code", code).ge("dt", first.getDt()).orderByAsc("dt"));
        if (CollectionUtils.isEmpty(valList)) {
            log.info("基金净值历史空：{}", code);
            return Res.err("基金净值历史获取失败");
        }

        // 加载持仓成本
        List<Cost> costList = costMapper.selectList(new QueryWrapper<Cost>()
                .eq("code", code).orderByAsc("dt"));
        if (CollectionUtils.isEmpty(costList)) {
            log.info("基金持仓成本未计算：{}", code);
            return Res.err("基金持仓成本未计算");
        }

        // 生成视图数据
        FundCostViewVO vo = new FundCostViewVO(code, first.getName());
        this.genLineVal(vo, valList);
        this.genLineBuy(vo, valList, deliveryList);
        this.genLineCost(vo, valList, costList);
        log.info("视图数据：{}({}) = {}", first.getName(), code, JSONUtil.json(vo));

        return Res.suc(vo);
    }

    /**
     * 生成基金净值线数据
     */
    private void genLineVal(FundCostViewVO vo, List<FundVal> valList) {
        List<String> valLabel = new ArrayList<>();
        List<String> valData = new ArrayList<>();
        for (FundVal v : valList) {
            valData.add(String.format("%.4f", v.getUnitVal()));
            valLabel.add((DateFormatUtils.format(v.getDt(), "yyyy-MM-dd")));
        }
        log.info("生成净值线数据");
        log.info("labels: {},", valLabel);
        log.info("data: {},", valData);
        vo.setLineValData(valData);
        vo.setLineValLabel(valLabel);
    }

    /**
     * 生成买入线数据
     */
    private void genLineBuy(FundCostViewVO vo, List<FundVal> valList, List<Delivery> deliveryList) {
        List<String> buyData = new ArrayList<>();
        Map<Date, List<Delivery>> doMap = deliveryList.stream().collect(Collectors.groupingBy(Delivery::getDt));
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
                    buyData.add(null);
                } else {
                    float price = total / dealNum;
                    buyData.add(String.format("%.4f", price));
                }
            } else {
                buyData.add(null);
            }
        }
        log.info("生成买入点位数据");
        log.info("data: {},", buyData);
        vo.setLineBuyData(buyData);
    }

    /**
     * 生成成本线
     */
    private void genLineCost(FundCostViewVO vo, List<FundVal> valList, List<Cost> costList) {
        List<String> costData = new ArrayList<>();
        Map<Date, Cost> doMap = costList.stream().collect(Collectors.toMap(Cost::getDt, Function.identity(), (o1, o2) -> o1));
        String last = "";
        for (FundVal d : valList) {
            Cost cost = doMap.get(d.getDt());
            if (cost != null) {
                last = String.format("%.4f", cost.getCostPrice());
                costData.add(last);
            } else {
                costData.add(last);
            }
        }
        log.info("生成成本线数据");
        log.info("data: {},", costData);
        vo.setLineCostData(costData);
    }

}
