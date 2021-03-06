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

    // 基金排行：页码、随机串
    public static final String FUND_RANK_HOST = "fund.eastmoney.com";
    public static final String FUND_RANK_REFER="http://fund.eastmoney.com/data/fundranking.html";
    // 开放基金排名
    public static final String FUND_KF_RANK="http://fund.eastmoney.com/data/rankhandler.aspx?op=ph&dt=kf&ft=all&rs=&gs=0&sc=lnzf&st=desc&pi={0}&pn=50&dx=0&v={1}";
    // 场内基金排名
    public static final String FUND_FB_RANK="http://fund.eastmoney.com/data/rankhandler.aspx?op=ph&dt=fb&ft=ct&rs=&gs=0&sc=zzf&st=desc&pi={0}&pn=50&dx=0&v={1}";

    // 基金公司：代码
    public static String COMPANY_INDEX = HOST + "company/{0}.html";
    public static String COMPANY_GK = HOST + "Company/f10/jbgk_{0}.html";

    // 基金主页：代码
    public static String FUND_INDEX = HOST + "{0}.html";

    // 基金概况：代码
    public static String FUND_GK = HOST_INFO + "jbgk_{0}.html";

    // 基金经理：代码
    public static String FUND_JL = HOST_INFO + "jjjl_{0}.html";
    public static String MANAGER_REG = ".*fund.eastmoney.com/manager/([\\d]+).html.*";

    // 特色数据：代码
    public static String FUND_TS = HOST_INFO + "tsdata_{0}.html";

    // 历史净值：代码
    public static String FUND_JZ = HOST_INFO + "jjjz_{0}.html";
    // 历史净值：随机串、代码、页码、时间戳
    public static String FUND_JZ_XHR = "http://api.fund.eastmoney.com/f10/lsjz?callback={0}&fundCode={1}&pageIndex={2}&pageSize=20&startDate=&endDate=&_={3}";

    // 基金持仓历史：代码，年度
    public static String FUND_STOCK = HOST_INFO + "FundArchivesDatas.aspx?type=jjcc&code={0}&year={1}&topline=10";
    public static String FUND_BOND = HOST_INFO + "FundArchivesDatas.aspx?type=zqcc&code={0}&year={1}&rt={2}";

    // 基金公司主页地址
    public static String COMPANY_REG = ".*fund.eastmoney.com/company/([\\d]+).html.*";

    /**
     * 提取js响应中的json体
     *
     * @param jsRes js内容
     * @return
     */
    public static String pickJsJson(String jsRes) {
        if (StringUtils.isEmpty(jsRes)) {
            return null;
        }
        return jsRes.substring(jsRes.indexOf("{"), jsRes.length() - 1);
    }

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
