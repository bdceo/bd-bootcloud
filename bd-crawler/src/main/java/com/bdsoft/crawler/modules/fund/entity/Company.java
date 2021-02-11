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
import java.util.Date;

/**
* <p>
* 基金公司
* </p>
*
* @author bdceo
* @since 2021-02-10
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 公司编码
    */
    private String code;

    /**
    * 公司全称
    */
    private String name;

    /**
    * 注册地址
    */
    private String regAddress;

    /**
    * 成立日期
    */
    private Date setupDate;

    /**
    * 管理基金数量
    */
    private Integer fundTotal;

    /**
    * 基金经理人数
    */
    private Integer managerTotal;

    /**
    * 管理规模：亿元
    */
    private Float fundGm;

    /**
    * 同步时间
    */
    private Date synTime;

}
