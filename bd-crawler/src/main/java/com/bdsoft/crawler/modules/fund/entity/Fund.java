package com.bdsoft.crawler.modules.fund.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bdsoft.crawler.modules.fund.po.FundPO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 基金简要信息
 * </p>
 *
 * @author bdceo
 * @since 2021-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_fund")
public class Fund implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 基金代码
     */
    private String code;

    /**
     * 基金名称
     */
    private String name;

    /**
     * 基金全称
     */
    private String fullName;

    /**
     * 基金名称-拼音
     */
    private String py;

    /**
     * 天天基金类型：股票、债券、指数、混合
     */
    private String type;

    /**
     * 基金规模（亿）
     */
    private Float gm;

    /**
     * 基金公司
     */
    private String company;

    /**
     * 基金公司编码
     */
    private String companyCode;

    /**
     * 成立日期
     */
    private Date setupDate;

    /**
     * 同步时间
     */
    private Date synTime;

    /**
     * 同步基金概况信息
     */
    public void setGk(FundPO po) {
        this.fullName = po.getFullName();
        this.type = po.getType();
        this.setupDate = po.getFxDate();
        this.gm = po.getGm();
        this.company = po.getCompany();
        this.companyCode = po.getCompanyCode();
    }


}
