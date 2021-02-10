package com.bdsoft.crawler.modules.fund.service.impl;

import com.bdsoft.crawler.modules.fund.entity.FundStock;
import com.bdsoft.crawler.modules.fund.mapper.FundStockMapper;
import com.bdsoft.crawler.modules.fund.service.IFundStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基金股票持仓 服务实现类
 * </p>
 *
 * @author bdceo
 * @since 2021-02-10
 */
@Service
public class FundStockServiceImpl extends ServiceImpl<FundStockMapper, FundStock> implements IFundStockService {

}
