package com.bdsoft.crawler.modules.fund.xhr;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 基金净值响应对象
 */
@Data
public class JzhResponse {

    @JSONField(name = "ErrCode")
    private int code;
    @JSONField(name = "ErrMsg")
    private String msg;

    @JSONField(name = "Data")
    private JzhData data;

    @JSONField(name = "PageIndex")
    private int pageIndex;
    @JSONField(name = "PageSize")
    private int pageSize;
    @JSONField(name = "TotalCount")
    private int totalCount;

    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return code == 0;
    }

    /**
     * 计算总页数
     */
    public int getPageTotal() {
        int pt = totalCount / pageSize;
        return (totalCount % pageSize == 0) ? pt : pt + 1;
    }

}
