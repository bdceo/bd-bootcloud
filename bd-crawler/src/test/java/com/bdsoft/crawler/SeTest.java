package com.bdsoft.crawler;

import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

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


    }
}
