package com.bdsoft.crawler.modules.delivery.service.impl;

import com.bdsoft.crawler.modules.delivery.entity.Delivery;
import com.bdsoft.crawler.modules.delivery.mapper.DeliveryMapper;
import com.bdsoft.crawler.modules.delivery.service.IDeliveryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交割单、交易流水 服务实现类
 * </p>
 *
 * @author bdceo
 * @since 2021-03-08
 */
@Service
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements IDeliveryService {

}
