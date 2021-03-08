package com.bdsoft.crawler.excel;

import com.alibaba.excel.EasyExcel;
import com.bdsoft.crawler.common.excel.DateConverter;
import com.bdsoft.crawler.modules.delivery.parser.DFCFDeliverParser;
import com.bdsoft.crawler.modules.delivery.parser.HuaTaiDeliverParser;
import com.bdsoft.crawler.modules.delivery.po.DFCFDeliveryPO;
import com.bdsoft.crawler.modules.delivery.po.HuaTaiDeliveryPO;
import com.bdsoft.crawler.modules.delivery.service.IDeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 交割单解析入库
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DeliverParseTest {

    @Resource(name = "deliveryServiceImpl")
    private IDeliveryService deliveryService;

    /**
     * 华泰证券-交割单解析
     */
    @Test
    public void testParseHuaTai() {
        String fileName = "E:\\download\\h5\\华泰交割单.xlsx";
        EasyExcel.read(fileName, HuaTaiDeliveryPO.class, new HuaTaiDeliverParser(deliveryService))
                .registerConverter(new DateConverter())
                .sheet().doRead();
    }

    /**
     * 东方财富-交割单解析
     */
    @Test
    public void testParseDFCF() {
        String fileName = "E:\\download\\h5\\东方财富交割单.xlsx";
        EasyExcel.read(fileName, DFCFDeliveryPO.class, new DFCFDeliverParser(deliveryService))
                .registerConverter(new DateConverter())
                .sheet().doRead();
    }

}
