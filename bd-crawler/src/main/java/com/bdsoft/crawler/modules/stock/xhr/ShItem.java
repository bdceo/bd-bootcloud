package com.bdsoft.crawler.modules.stock.xhr;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 流通股东响应-股东信息
 */
@Data
public class ShItem {

    @JSONField(name = "SHAREHDCODE")
    private String code;
    @JSONField(name = "SHAREHDNAME")
    private String name;
    @JSONField(name = "SHAREHDTYPE")
    private String type;
    @JSONField(name = "LTAG")
    private Float mvTotal;

}
