package com.bdsoft.crawler.modules.fund.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>基金净值
 * </p>
 *
 * @author bdceo
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_fund_value")
public class FundValue implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private Date valueDate;

    private BigDecimal unitValue;

    private BigDecimal totalValue;

    private BigDecimal growthRate;

    private Integer growthType;

    private Date createTime;


}
