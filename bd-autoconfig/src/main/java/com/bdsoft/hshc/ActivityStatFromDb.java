package com.bdsoft.hshc;

import com.alibaba.fastjson.JSONObject;
import com.bdsoft.utils.BDExcelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityStatFromDb {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private RestTemplate rest;

    @GetMapping("/stat")
    public void stat() {
        // 查询砍价记录
        List<Integer> kanjiaUids = listKanJiaUsers("36");
        // 查询抽奖记录
        List<Integer> chouJiangUids = listJiangUsers("31");
        // 查询刮奖记录
        List<Integer> guaJiangUids = listJiangUsers("32,37");

        // 解析手机号
        String mobiles = parseExcel();
        String mbs = mobiles.replaceAll("\t\n",",");

        // 换取用户ID
        Map<String, Integer> userMobile = new HashMap<>();
        for (String mobile : mobiles.split("\t\n")) {
            int uid = getUser(mobile);
            if (uid == -1) {
                continue;
            }
            userMobile.put(mobile, uid);
        }

        // 比对是否参加过活动
        userMobile.forEach((k, v) -> {
            if (kanjiaUids.contains(v)) {
                log.info("手机号：{}，参加过砍价活动", k);
            } else if (chouJiangUids.contains(v)) {
                log.info("手机号：{}，参加过抽奖活动", k);
            } else if (guaJiangUids.contains(v)) {
                log.info("手机号：{}，参加过刮奖活动", k);
            } else {
                log.info("手机号：{}，未参加过嘉年华活动", k);
            }
        });

    }

    public String parseExcel() {
        try {
            File cfgFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "hshc/act-mobile.xls");
            InputStream in = new FileInputStream(cfgFile);
            return new BDExcelParser().parseExcelData(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Integer getUser(String phone) {
        String api = "unirest://www.huashenghaoche.com/personalcenter/user/userInfoByPhone?phone={1}&type={2}";
        ResponseEntity<String> data = rest.getForEntity(api, String.class, phone, "app");
        JSONObject json = JSONObject.parseObject(data.getBody());
        log.info("手机号：{}， 查询结果：{}", phone, data.getBody());
        if (json.getInteger("code").equals(0)) {
            JSONObject user = json.getJSONObject("data");
            if (user != null) {
                return user.getInteger("id");
            }
        }
        return -1;
    }

    public List<Integer> listJiangUsers(String actIds) {
        String sql = "select distinct(user_id) fromUid from activiti_participation_record where activiti_id in(" + actIds + ")";
        List<Integer> data = jdbc.query(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(1);
            }
        });
        return data;
    }

    public List<Integer> listKanJiaUsers(String actIds) {
        // 查询砍价记录
        String sql = "select distinct(assistant_user_id) from activiti_assistant where activiti_id in(" + actIds + ")";
        List<Integer> data = jdbc.query(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(1);
            }
        });
        return data;
    }
}
