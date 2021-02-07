package com.bdsoft.crawler.modules.fund.xhr;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * 基金净值响应-数据对象-每日数据
 */
@Data
public class JzhItem {

    /**
     * 净值归属日
     */
    @JSONField(name = "FSRQ")
    private String dateStr;
    private Date dt;

    /**
     * 单位净值、累计净值
     */
    @JSONField(name = "DWJZ")
    private float dwjz;
    @JSONField(name = "LJJZ")
    private float ljjz;

    /**
     * 净值增长率
     */
    @JSONField(name = "JZZZL")
    private float jjzzl;

    public Date getDt() {
        try {
            return StringUtils.isEmpty(dateStr) ? null : DateUtils.parseDate(dateStr, "yyyy-MM-dd");
        } catch (ParseException e) {
            return null;
        }
    }
}
