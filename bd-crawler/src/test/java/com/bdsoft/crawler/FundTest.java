package com.bdsoft.crawler;

import com.bdsoft.crawler.modules.fund.entity.FundInfo;
import com.bdsoft.crawler.modules.fund.entity.FundValue;
import com.bdsoft.crawler.modules.fund.mapper.FundInfoMapper;
import com.bdsoft.crawler.modules.fund.mapper.FundValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FundTest {

    @Autowired
    private FundInfoMapper fundInfoMapper;
    @Autowired
    private FundValueMapper fundValueMapper;

    @Test
    public void testList() {
        List<FundInfo> infoList = fundInfoMapper.selectList(null);
        List<FundValue> valueList = fundValueMapper.selectList(null);
        log.info("info size={}, value size={}", infoList.size(), valueList.size());
    }

}
