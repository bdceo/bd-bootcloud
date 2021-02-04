package com.bdsoft.crawler.modules.fund.xhr;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2021/2/4.
 */
@Data
public class JzhData {

    private List<JzhItem> list;

    private String fundType;

    private String feature;

}
