package com.bdsoft.crawler.modules.index.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 指数信息
 * </p>
 *
 * @author bdceo
 * @since 2021-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_index")
public class Index implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 指数代码
     */
    private String code;

    /**
     * 指数名称
     */
    private String name;

    /**
     * 指数简介
     */
    private String info;

    /**
     * 成分股数量
     */
    private Integer stockNum;

    /**
     * 指数系列：中证，上证，深证，新三板
     */
    private String serial;

    /**
     * 指数类型：综合，规模，行业，风格，主题，策略
     */
    private String type;

    /**
     * 资产类别：股票，债券，基金，期货
     */
    private String assets;

    /**
     * 地区：境内，境外
     */
    private String region;

    /**
     * 币种：人民币，港元，美元
     */
    private String currency;

    /**
     * 同步时间
     */
    private Date synTime;


}
