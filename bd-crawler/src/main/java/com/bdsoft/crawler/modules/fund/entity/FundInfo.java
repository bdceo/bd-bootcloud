package com.bdsoft.crawler.modules.fund.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>基金信息
 * </p>
 *
 * @author bdceo
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_fund_info")
public class FundInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private Integer type;

    private BigDecimal unitValue;

    private BigDecimal growthRate;

    private Date valueDate;

    private String company;

    private Date setupDate;

    private Date createTime;

    private Date updateTime;


}
