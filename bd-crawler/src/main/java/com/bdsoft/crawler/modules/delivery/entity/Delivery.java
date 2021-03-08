package com.bdsoft.crawler.modules.delivery.entity;

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
 * 交割单、交易流水
 * </p>
 *
 * @author bdceo
 * @since 2021-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_delivery")
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 交易账号
     */
    private String account;

    /**
     * 交易时间
     */
    private Date dt;

    /**
     * 证券代码
     */
    private String code;

    /**
     * 证券名称
     */
    private String name;

    /**
     * 操作类型：1-买入、2-卖出
     */
    private Integer op;

    /**
     * 成交价格
     */
    private Float dealPrice;

    /**
     * 成交数量
     */
    private Integer dealNum;

    /**
     * 成交金额
     */
    private Float dealAmount;

    /**
     * 费用-佣金
     */
    private Float feeYj;

    /**
     * 费用-交易规费
     */
    private Float feeJyg;

    /**
     * 费用-过户费
     */
    private Float feeGhf;

    /**
     * 费用-印花税
     */
    private Float feeYhs;

    /**
     * 发生金额合计
     */
    private Float total;

    /**
     * 导入日期
     */
    private Date createTime;


}
