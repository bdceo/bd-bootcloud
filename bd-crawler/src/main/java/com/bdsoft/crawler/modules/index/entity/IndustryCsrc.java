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
 * 证监会行业分类表
 * </p>
 *
 * @author bdceo
 * @since 2021-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_industry_csrc")
public class IndustryCsrc implements Serializable {

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
     * 所属一级编码
     */
    private String code1;


}
