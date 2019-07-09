package com.bdsoft.crawler;


import com.bdsoft.crawler.modules.gas.FetchZhongShihua;
import com.bdsoft.crawler.modules.gas.FetchZhongShiyou;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchGasTest extends SuperTest {

    @Autowired
    private FetchZhongShihua fetchZhongShihua;
    @Autowired
    private FetchZhongShiyou fetchZhongShiyou;


    @Test
    public void testZhshhFetch() throws Exception {
        fetchZhongShihua.fetch();
    }

    @Test
    public void testZhshyFetch() throws Exception {
        fetchZhongShiyou.fetch();
    }


}
