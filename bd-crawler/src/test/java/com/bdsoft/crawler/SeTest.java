package com.bdsoft.crawler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.Date;

/**
 * se测试
 */
@Slf4j
public class SeTest {

    public static void main(String[] args) throws Exception {

        String msg = "abss{0}ddd{1}eee{2}";
        msg = MessageFormat.format(msg, 1, 2, 3);
        log.info("{}", msg);

        log.info("{} ， {}", Float.MIN_VALUE, Float.MAX_VALUE);

        long t = 1612661114532L;
        Date dt = new Date(t);
        log.info("dt={}", dt.toString());
        log.info("t={}, st={}", t, System.currentTimeMillis());

        log.info("len={}", "18306229061496483879".length());
        log.info("len={}", (System.currentTimeMillis() + "").length());
        log.info("len={}", (IdWorker.getIdStr()).length());

        Date dt1 = new Date();
        Date dt2 = new Date(dt1.getTime() + 3);
        log.info("dt1 before dt2 {}", dt1.before(dt2));
        log.info("dt1 before dt2 {}", dt1.after(dt2));


    }
}
