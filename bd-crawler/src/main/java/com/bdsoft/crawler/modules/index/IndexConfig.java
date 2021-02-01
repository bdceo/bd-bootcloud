package com.bdsoft.crawler.modules.index;

/**
 * 中证指数，抓取配置
 */
public class IndexConfig {

    // 官网地址
    public static final String HOST = "http://www.csindex.com.cn/zh-CN/";

    // 指数详情页：指数代码
    public static final String INDEX_PAGE = HOST + "indices/index-detail/{0}";

    // 指数相关产品：指数名称
    public static final String INDEX_FUND_PAGE = HOST + "search/index-derivatives?index_name={0}";

    // 搜索指数：页码，每页条数
    public static final String INDEX_SEARCH = HOST + "indices/index?page={0}&page_size=100&data_type=json";

}
