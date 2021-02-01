package com.bdsoft.crawler;

import java.text.MessageFormat;

/**
 * se测试
 */
public class SeTest {

    public static void main(String[] args) throws  Exception{

        String msg = "abss{0}ddd{1}eee{2}";
        msg = MessageFormat.format(msg,1, 2, 3);
        System.out.println(msg);


    }
}
