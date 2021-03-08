package com.bdsoft.crawler.modules.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bdsoft.crawler.common.vo.Res;
import com.bdsoft.crawler.modules.delivery.entity.Cost;
import com.bdsoft.crawler.modules.delivery.vo.FundCostViewVO;

/**
 * <p>
 * 持仓成本 服务类
 * </p>
 *
 * @author bdceo
 * @since 2021-03-08
 */
public interface ICostService extends IService<Cost> {

    /**
     * 获取基金成本视图数据
     *
     * @param code 基金代码
     * @return
     */
    Res<FundCostViewVO> getCostViewData(String code);
}
