package com.bdsoft.crawler;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2019/7/3.
 */
public class SeTest {

    public static void main(String[] args) throws  Exception{

        String msg = "abss{0}ddd{1}eee{2}";
        msg = MessageFormat.format(msg,1, 2, 3);
        System.out.println(msg);


    }
}
