package com.bdsoft.crawler.modules.stock;

/**
 * 股票数据抓取配置
 */
public class StockConfig {

    // 沪深港股交易日排行
    public static String hsgTop10 = "http://data.eastmoney.com/hsgt/top10/{0}.html";

    // 沪深港股资金流向-jquery-callback、get
    public static String hsgMoneyFlow = "http://push2.eastmoney.com/api/qt/kamt/get?fields1=f1,f2,f3,f4&fields2=f51,f52,f53,f54&cb={0}";

    public static String STOCK_HOLDER_REFER="http://data.eastmoney.com/";
    public static String STOCK_HOLDER_HOST="dcfm.eastmoney.com";

    // 十大流通股东：jquery，页码
    //public static String STOCK_HOLDER = "http://dcfm.eastmoney.com/em_mutisvcexpandinterface/api/js/get?callback={0}&st=BDSUM&sr=-1&ps=50&p={1}&js=%7Bpages%3A(tp)%2Cdata%3A(x)%7D&token=70f12f2f4f091e459a279469fe49eca5&type=NSCHGSTALA";
    public static String STOCK_HOLDER = "http://dcfm.eastmoney.com/em_mutisvcexpandinterface/api/js/get?callback={0}&st=BDSUM&sr=-1&ps=50&p={1}&js=%7Bpages%3A(tp)%2Cdata%3A(x)%7D&token=70f12f2f4f091e459a279469fe49eca5&type=NSCHGSTALA";
    public static String STOCK_INDEX="http://data.eastmoney.com/gdfx/shareholder/{0}.html";
}
