package com.bdsoft.crawler.modules.jd.feed;

import lombok.Data;

/**
 * sku-评论统计
 */
@Data
public class JdCommentState {

    private String SkuId;
    private String ProductId;

    // 全部评价
    private String CommentCount;

    // 好评
    private String GoodCount;

    // 中评
    private String GeneralCount;

    // 差评
    private String PoorCount;

    // 追评
    private String AfterCount;

    // 视频晒单
    private String VideoCount;


}
