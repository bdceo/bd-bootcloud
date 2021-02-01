package com.bdsoft.crawler.modules.index.xhr;

import lombok.Data;

/**
 * 通用响应
 */
@Data
public class IndexCommonResponse {

    private Integer total;
    private Integer pageSize;
    private Integer totalPage;

}
