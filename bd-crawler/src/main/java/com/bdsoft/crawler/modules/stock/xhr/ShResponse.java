package com.bdsoft.crawler.modules.stock.xhr;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 流通股东信息响应
 */
@Data
public class ShResponse {

    @JSONField(name = "pages")
    private int pageTotal;

    @JSONField(name = "data")
    private List<ShItem> data;
}
