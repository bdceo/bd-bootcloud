package com.bdsoft.crawler.modules.fund.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 基金净值
 * </p>
 *
 * @author bdceo
 * @since 2021-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_fund_val")
public class FundVal implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 基金代码
     */
    private String code;

    /**
     * 净值日期
     */
    private LocalDate dt;

    /**
     * 单位净值
     */
    private Float unitVal;

    /**
     * 累计净值
     */
    private Float totalVal;

    /**
     * 日涨幅
     */
    private Float dayGrowth;

    private LocalDateTime createTime;


}
