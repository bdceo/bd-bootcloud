package com.bdsoft.crawler.modules.fund.service.impl;

import com.bdsoft.crawler.modules.fund.entity.Fund;
import com.bdsoft.crawler.modules.fund.mapper.FundMapper;
import com.bdsoft.crawler.modules.fund.service.IFundService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基金简要信息 服务实现类
 * </p>
 *
 * @author bdceo
 * @since 2021-02-10
 */
@Service
public class FundServiceImpl extends ServiceImpl<FundMapper, Fund> implements IFundService {

}
