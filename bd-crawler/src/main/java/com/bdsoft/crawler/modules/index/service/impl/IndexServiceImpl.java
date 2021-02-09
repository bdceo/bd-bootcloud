package com.bdsoft.crawler.modules.index.service.impl;

import com.bdsoft.crawler.modules.index.entity.Index;
import com.bdsoft.crawler.modules.index.mapper.IndexMapper;
import com.bdsoft.crawler.modules.index.service.IIndexService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 指数信息 服务实现类
 * </p>
 *
 * @author bdceo
 * @since 2021-02-09
 */
@Service
public class IndexServiceImpl extends ServiceImpl<IndexMapper, Index> implements IIndexService {

}
