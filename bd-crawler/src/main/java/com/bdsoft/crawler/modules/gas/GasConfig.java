package com.bdsoft.crawler.modules.gas;

import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 加油站：配置+工具
 */
public class GasConfig {

    public static Random RANDOM = new Random(System.currentTimeMillis());

    // 中石化-字符集
    public static String ZHSHH_CHARSET = "GBK";

    // 中石化-省份
    public static String ZHSHH_PROVINCES = "https://www.sinopecsales.com/website/html/service/jiayouzhan.html";

    // 中石化-省份编号规则
    public static String ZHSHH_PROVINCE_ID = ".*queryGasStationByCondition.action\\?province=([\\d]+)&page.*";
    public static Pattern PAT_ZHSHH_PROVINCE_ID = Pattern.compile(ZHSHH_PROVINCE_ID);

    // 中石化-加油站列表：省份，页码，售卡充值
    public static String ZHSHH_STATIONS = "https://www.sinopecsales.com/website/gasStationAction_queryGasStationByCondition.action?province={0}&page.pageNo={1}&stationCharge={2}";


    /**
     * 中石化-提取省份编号
     *
     * @param url 加油站列表
     * @return
     */
    public static String getZhshhProvinceId(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("url无效");
        }

        Matcher mat = PAT_ZHSHH_PROVINCE_ID.matcher(url);
        if (mat.find()) {
            return mat.group(1);
        }
        throw new RuntimeException("url无效");
    }
}
