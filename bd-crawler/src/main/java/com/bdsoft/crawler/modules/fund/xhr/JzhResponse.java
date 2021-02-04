package com.bdsoft.crawler.modules.fund.xhr;

import lombok.Data;

/**
 * 基金净值响应对象
 */
@Data
public class JzhResponse {

    private String code;
    private String msg;

    private JzhData data;

    private int pageIndex;
    private int pageSize;
    private int totalCount;

}
