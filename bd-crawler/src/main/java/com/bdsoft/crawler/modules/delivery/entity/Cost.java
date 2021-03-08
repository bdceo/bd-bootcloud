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
 * 持仓成本
 * </p>
 *
 * @author bdceo
 * @since 2021-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_cost")
public class Cost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
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
     * 成本价
     */
    private Float costPrice;

    /**
     * 创建日期
     */
    private Date createTime;

}
