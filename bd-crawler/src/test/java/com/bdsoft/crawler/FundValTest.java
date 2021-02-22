package com.bdsoft.crawler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdsoft.crawler.modules.fund.entity.FundVal;
import com.bdsoft.crawler.modules.fund.mapper.FundValMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 净值历史测试
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FundValTest {

    @Autowired
    private FundValMapper fundValMapper;

    @Test
    public void testDayGrowth() throws Exception {
        DayGrowthStat stat = new DayGrowthStat();

        int pageSize = 10000;
        int totalCount = fundValMapper.selectCount(null);
        int pageTotal = totalCount / pageSize + 1;



    }

}

@Slf4j
class StatMap implements Runnable {

    private CountDownLatch latch;

    private DayGrowthStat stat;

    private int pageStart;
    private int pageEnd;

    private FundValMapper mapper;

    public StatMap(DayGrowthStat stat, CountDownLatch latch, int pageStart, int pageEnd, FundValMapper mapper) {
        this.stat = stat;
        this.latch = latch;
        this.pageStart = pageStart;
        this.pageEnd = pageEnd;
        this.mapper = mapper;
    }

    @Override
    public void run() {
        ZoneId zoneId = ZoneId.systemDefault();
        for (int pageIndex = pageStart; pageIndex <= pageEnd; pageIndex++) {
            Page<FundVal> page = new Page<>(pageIndex, 10000);
            QueryWrapper<FundVal> query = new QueryWrapper<FundVal>().select("id", "dt", "day_growth").orderByAsc("id");
            IPage<FundVal> pageData = mapper.selectPage(page, query);

            log.info("基于历史净值数据统计每周涨跌: {}/{}", pageStart, pageEnd);
            for (FundVal val : pageData.getRecords()) {
                LocalDate dt = val.getDt().toInstant().atZone(zoneId).toLocalDate();
                this.stat(dt.getDayOfWeek().getValue(), val.getDayGrowth() >= 0);
            }
        }

        latch.countDown();
    }

    private void stat(int dayOfWeek, boolean isUp) {
        switch (dayOfWeek) {
            case 1:
                if (isUp) {
                    stat.getZh1Up().incrementAndGet();
                } else {
                    stat.getZh1Down().incrementAndGet();
                }
                break;
            case 2:
                if (isUp) {
                    stat.getZh2Up().incrementAndGet();
                } else {
                    stat.getZh2Down().incrementAndGet();
                }
                break;
            case 3:
                if (isUp) {
                    stat.getZh3Up().incrementAndGet();
                } else {
                    stat.getZh3Down().incrementAndGet();
                }
                break;
            case 4:
                if (isUp) {
                    stat.getZh4Up().incrementAndGet();
                } else {
                    stat.getZh4Down().incrementAndGet();
                }
                break;
            case 5:
                if (isUp) {
                    stat.getZh5Up().incrementAndGet();
                } else {
                    stat.getZh5Down().incrementAndGet();
                }
                break;
        }
    }


}


@Data
class DayGrowthStat {

    private AtomicInteger zh1Up;
    private AtomicInteger zh1Down;
    private AtomicInteger zh2Up;
    private AtomicInteger zh2Down;
    private AtomicInteger zh3Up;
    private AtomicInteger zh3Down;
    private AtomicInteger zh4Up;
    private AtomicInteger zh4Down;
    private AtomicInteger zh5Up;
    private AtomicInteger zh5Down;

    public String view() {
        int total = zh1Up.intValue() + zh1Down.intValue() + zh2Up.intValue() + zh2Down.intValue() + zh3Up.intValue()
                + zh3Down.intValue() + zh4Up.intValue() + zh4Down.intValue() + zh5Up.intValue() + zh5Down.intValue();
        StringBuilder str = new StringBuilder("统计结果：\n");
        str.append("\t周一上涨概率：").append(zh1Up.intValue() * 100 / total).append("%").append(", 下跌概率：").append(zh1Down.intValue() * 100 / total).append("%\n");
        str.append("\t周二上涨概率：").append(zh2Up.intValue() * 100 / total).append("%").append(", 下跌概率：").append(zh2Down.intValue() * 100 / total).append("%\n");
        str.append("\t周三上涨概率：").append(zh3Up.intValue() * 100 / total).append("%").append(", 下跌概率：").append(zh3Down.intValue() * 100 / total).append("%\n");
        str.append("\t周四上涨概率：").append(zh4Up.intValue() * 100 / total).append("%").append(", 下跌概率：").append(zh4Down.intValue() * 100 / total).append("%\n");
        str.append("\t周五上涨概率：").append(zh5Up.intValue() * 100 / total).append("%").append(", 下跌概率：").append(zh5Down.intValue() * 100 / total).append("%");
        return str.toString();
    }

}


