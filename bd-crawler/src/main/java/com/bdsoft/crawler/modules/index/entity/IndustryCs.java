package com.bdsoft.crawler.modules.index.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 中证行业分类表
 * </p>
 *
 * @author bdceo
 * @since 2021-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_industry_cs")
public class IndustryCs implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 行业代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String info;

    /**
     * 所属一级编码
     */
    private String code1;

    /**
     * 所属二级编码
     */
    private String code2;

    /**
     * 所属三级编码
     */
    private String code3;


}
