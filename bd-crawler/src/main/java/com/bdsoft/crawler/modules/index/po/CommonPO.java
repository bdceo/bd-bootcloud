package com.bdsoft.crawler.modules.index.po;

import lombok.Data;

/**
 * 通用属性
 */
@Data
public class CommonPO {

    /**
     * 编号，名称
     */
    private String code;
    private String name;

    public CommonPO() {
    }

    public CommonPO(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
