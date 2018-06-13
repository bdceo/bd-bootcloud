package com.bdsoft.hshc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityStatFromFile {

    static Logger log = LoggerFactory.getLogger(ActivityStatFromFile.class);

    public static void stat() throws Exception {
        // 用户映射
        Map<String, String> userMapper = parseUser();

        // 解析参加活动的用户
        List<String> act1User = parseAct("hshc/act-1.txt");
        List<String> act11User = parseAct("hshc/act-1-1.txt");
        List<String> act2User = parseAct("hshc/act-2.txt");
        List<String> act3User = parseAct("hshc/act-3.txt");

        userMapper.forEach((k, v) -> {
            if (act1User.contains(v)) {
                log.info("手机号：{}，参加过砍价活动", k);
            } else if (act11User.contains(v)) {
                log.info("手机号：{}，领过砍价优惠券", k);
            } else if (act2User.contains(v)) {
                log.info("手机号：{}，参加过抽奖活动", k);
            } else if (act3User.contains(v)) {
                log.info("手机号：{}，参加过刮奖活动", k);
            } else {
                log.info("手机号：{}，未参加活动", k);
            }
        });
    }

    public static Map<String, String> parseUser() throws Exception {
        File idFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "hshc/prod-uid-mobile.txt");
        Map<String, String> userMapper = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(idFile));
            String s = null;
            while ((s = br.readLine()) != null) {
                log.info("id={}, mobile={}", s.split("\t")[0].trim(), s.split("\t")[1]);
                userMapper.put(s.split("\t")[1], s.split("\t")[0].trim());
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userMapper;
    }

    public static List<String> parseAct(String fileName) throws Exception {
        File idFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + fileName);
        List<String> userList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(idFile));
            String s = null;
            while ((s = br.readLine()) != null) {
                log.info("id={}", s.trim());
                userList.add(s.trim());
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
}
