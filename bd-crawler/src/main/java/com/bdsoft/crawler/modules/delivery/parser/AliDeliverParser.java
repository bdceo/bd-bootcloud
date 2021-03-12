package com.bdsoft.crawler.modules.delivery.parser;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.bdsoft.crawler.modules.delivery.po.AliDeliveryPO;

/**
 * 支付宝交易流水解析
 */
public class AliDeliverParser extends AnalysisEventListener<AliDeliveryPO> {

    @Override
    public void invoke(AliDeliveryPO dfcfDeliveryPO, AnalysisContext analysisContext) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
