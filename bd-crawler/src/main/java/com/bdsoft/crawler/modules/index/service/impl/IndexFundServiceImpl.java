package com.bdsoft.crawler.modules.index.service.impl;

import com.bdsoft.crawler.modules.index.entity.IndexFund;
import com.bdsoft.crawler.modules.index.mapper.IndexFundMapper;
import com.bdsoft.crawler.modules.index.service.IIndexFundService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 跟踪指数的基金产品 服务实现类
 * </p>
 *
 * @author bdceo
 * @since 2021-02-09
 */
@Service
public class IndexFundServiceImpl extends ServiceImpl<IndexFundMapper, IndexFund> implements IIndexFundService {

}
