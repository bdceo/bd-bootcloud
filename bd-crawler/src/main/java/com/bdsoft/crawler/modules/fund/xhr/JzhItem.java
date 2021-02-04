package com.bdsoft.crawler.modules.fund.xhr;

import lombok.Data;

import java.util.Date;

/**
 * Created by Administrator on 2021/2/4.
 */
@Data
public class JzhItem {

    private String dateStr;

    private Date dt;

    /**
     * 单位净值、累计净值
     */
    private float dwjz;
    private float ljjz;

    /**
     * 日涨幅
     */
    private float jjzzl;

}
