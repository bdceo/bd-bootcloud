package com.bdsoft.crawler.modules.fund.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * 基金债券持仓
 * </p>
 *
 * @author bdceo
 * @since 2021-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_fund_bond")
public class FundBond implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 基金代码
     */
    private String code;

    /**
     * 方案日期
     */
    private Date dt;

    /**
     * 债券代码
     */
    private String bondCode;

    /**
     * 债券名称
     */
    private String bondName;

    /**
     * 占净值比例
     */
    private Float valueRate;

    /**
     * 持仓市值（万元）
     */
    private Float values;


}
