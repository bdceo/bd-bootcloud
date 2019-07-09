package com.bdsoft.crawler.modules.gas.service.impl;

import com.bdsoft.crawler.modules.gas.entity.StationDb;
import com.bdsoft.crawler.modules.gas.mapper.StationDbMapper;
import com.bdsoft.crawler.modules.gas.service.IStationDbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 站点数据 服务实现类
 * </p>
 *
 * @author bdceo
 * @since 2019-07-08
 */
@Service
public class StationDbServiceImpl extends ServiceImpl<StationDbMapper, StationDb> implements IStationDbService {

}
