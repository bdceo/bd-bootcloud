package com.bdsoft.crawler.modules.delivery.parser;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.bdsoft.crawler.modules.delivery.entity.Delivery;
import com.bdsoft.crawler.modules.delivery.enums.AccountTypeEnum;
import com.bdsoft.crawler.modules.delivery.enums.TradeTypeEnum;
import com.bdsoft.crawler.modules.delivery.po.HuaTaiDeliveryPO;
import com.bdsoft.crawler.modules.delivery.service.IDeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 华泰交割单Excel解析
 */
@Slf4j
public class HuaTaiDeliverParser extends AnalysisEventListener<HuaTaiDeliveryPO> {

    // 持久化
    private IDeliveryService deliveryService;

    // 解析的行数据
    private static final int BATCH_SIZE = 100;
    private List<Delivery> data;
    private Date now = new Date();

    public HuaTaiDeliverParser(IDeliveryService deliveryService) {
        this.deliveryService = deliveryService;
        this.data = new ArrayList<>();
    }

    @Override
    public void invoke(HuaTaiDeliveryPO row, AnalysisContext ctx) {
        log.info("华泰交割单记录：{}", JSONUtil.toJsonStr(row));
        Delivery tmp = new Delivery();
        BeanUtils.copyProperties(row, tmp);

        tmp.setAccount(AccountTypeEnum.TP1.getName());
        tmp.setOp(TradeTypeEnum.getTypeByName(row.getOpType()));
        tmp.setCreateTime(now);
        data.add(tmp);

        if (data.size() >= BATCH_SIZE) {
            this.save();
            data.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext ctx) {
        log.info("华泰交割单解析完毕");
        this.save();
    }

    /**
     * 入库
     */
    private void save() {
        boolean res = deliveryService.saveBatch(data);
        log.info("批量插入：{} {}", data.size(), res);
    }

}
