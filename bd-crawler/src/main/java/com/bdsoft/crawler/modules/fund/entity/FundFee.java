package com.bdsoft.crawler.modules.fund.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 基金手续费信息
 * </p>
 *
 * @author bdceo
 * @since 2021-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_fund_fee")
public class FundFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 基金代码
     */
    private String code;

    /**
     * 托管费
     */
    private Float glf;

    /**
     * 管理费
     */
    private Float tgf;

    /**
     * 销售服务费
     */
    private Float xsh;

    /**
     * 认购费
     */
    private Float rgf;

    /**
     * 申购费
     */
    private Float sgf;

    /**
     * 赎回费
     */
    private Float shf;


}
