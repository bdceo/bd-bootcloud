package com.bdsoft.crawler.modules.stock;

/**
 * 股票数据抓取配置
 */
public class StockConfig {

    // 沪深港股交易日排行
    public static String hsgTop10 = "http://data.eastmoney.com/hsgt/top10/{0}.html";

    // 沪深港股资金流向-jquery-callback、get
    public static String hsgMoneyFlow = "http://push2.eastmoney.com/api/qt/kamt/get?fields1=f1,f2,f3,f4&fields2=f51,f52,f53,f54&cb={0}";


}
