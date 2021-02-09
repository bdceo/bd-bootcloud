package com.bdsoft.crawler.modules.index.service.impl;

import com.bdsoft.crawler.modules.index.entity.IndexStock;
import com.bdsoft.crawler.modules.index.mapper.IndexStockMapper;
import com.bdsoft.crawler.modules.index.service.IIndexStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 指数十大权重股票、债券 服务实现类
 * </p>
 *
 * @author bdceo
 * @since 2021-02-09
 */
@Service
public class IndexStockServiceImpl extends ServiceImpl<IndexStockMapper, IndexStock> implements IIndexStockService {

}
