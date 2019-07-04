package com.bdsoft.crawler.modules.jd;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 京东：配置+工具
 */
public class JdConfig {

    // 商品价格 https://c0.3.cn/stock?skuId=25118865051&cat=12218,13591,13594&area=1_72_4137_0
    public static String SKU_PRICE = "https://c0.3.cn/stock?skuId={0}&cat={1}&area={2}";

    // 商品唯一码正则
    public static String REG_SKU_ID = "^http.*item.jd.com/([\\d]+).html.*";
    public static Pattern PAT_SKU_ID = Pattern.compile(REG_SKU_ID);

    // 默认地区：北京-朝阳区-管庄地区
    public static String DEFAULT_PARAM_AREA = "1_72_4137_0";

    // 商品评论数统计信息 https://club.jd.com/comment/productCommentSummaries.action?referenceIds=100001820569,25118865051
    public static String SKU_COMMENT_COUNT = "https://club.jd.com/comment/productCommentSummaries.action?referenceIds={0}";
    // 商品评论列表
    public static String SKU_COMMENT_LIST = "";

    /**
     * 提取skuId
     *
     * @param url 商品详情页
     * @return
     */
    public static String getSkuId(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("url无效");
        }

        Matcher mat = PAT_SKU_ID.matcher(url);
        if (mat.find()) {
            return mat.group(1);
        }
        throw new RuntimeException("url无效");
    }

}
