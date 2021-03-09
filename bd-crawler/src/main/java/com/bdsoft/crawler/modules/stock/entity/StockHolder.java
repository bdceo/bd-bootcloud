package com.bdsoft.crawler.modules.stock.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 股东信息
 * </p>
 *
 * @author bdceo
 * @since 2021-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_stock_holder")
public class StockHolder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 股东类型：个人、基金、社保
     */
    private String type;

    /**
     * 编号-东方财富网
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 流通市值
     */
    private BigDecimal mvTotal;

    /**
     * 入库时间
     */
    private Date createTime;


}
