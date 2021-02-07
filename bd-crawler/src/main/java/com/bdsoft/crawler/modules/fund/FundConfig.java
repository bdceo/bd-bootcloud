package com.bdsoft.crawler.modules.fund;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基金数据抓取配置
 */
public class FundConfig {

    // 随机
    public static final Random RANDOM = new Random(System.currentTimeMillis());

    // 官网地址
    public static final String HOST = "http://fund.eastmoney.com/";
    public static final String HOST_INFO = "http://fundf10.eastmoney.com/";
    public static final String HOST_API = "api.fund.eastmoney.com";


    // 基金公司：代码
    public static String COMPANY_INDEX = HOST + "company/{0}.html";

    // 基金主页：代码
    public static String FUND_INDEX = HOST + "{0}.html";

    // 基金概况：代码
    public static String FUND_GK = HOST_INFO + "jbgk_{0}.html";

    // 基金经理：代码
    public static String FUND_JL = HOST_INFO + "jjjl_{0}.html";

    // 特色数据：代码
    public static String FUND_TS = HOST_INFO + "tsdata_{0}.html";

    // 历史净值：代码
    public static String FUND_JZ = HOST_INFO + "jjjz_{0}.html";
    // 历史净值：随机串、代码、页码、时间戳
    public static String FUND_JZ_XHR = "http://api.fund.eastmoney.com/f10/lsjz?callback={0}&fundCode={1}&pageIndex={2}&pageSize=20&startDate=&endDate=&_={3}";

    // 基金持仓历史：代码，年度
    public static String FUND_CC = HOST_INFO + "FundArchivesDatas.aspx?type=jjcc&code={0}&year={1}&topline=10";

    // 基金公司主页地址
    public static String COMPANY_REG = ".*fund.eastmoney.com/company/([\\d]+).html.*";

    /**
     * 从url提取code
     *
     * @param url 地址
     * @param reg 正则
     * @return
     */
    public static String pickCode(String url, String reg) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(reg)) {
            throw new RuntimeException("少参数：url、reg");
        }
        Pattern pattern = Pattern.compile(reg);
        Matcher mat = pattern.matcher(url);
        if (mat.find()) {
            return mat.group(1);
        }
        return null;
    }

    /**
     * 提取xhr响应的json内容
     *
     * @param xhrRes 响应
     * @return
     */
    public static String pickXhrData(String xhrRes) {
        if (xhrRes.startsWith("jQuery")) {
            return xhrRes.substring(xhrRes.indexOf("(") + 1, xhrRes.length() - 1);
        }
        return null;
    }

}
