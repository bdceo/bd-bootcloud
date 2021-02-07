package com.bdsoft.crawler.modules.fund.xhr;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 基金净值响应-数据对象
 */
@Data
public class JzhData {

    @JSONField(name = "LSJZList")
    private List<JzhItem> list;

    @JSONField(name = "FundType")
    private String fundType;

    @JSONField(name = "Feature")
    private String feature;


}
