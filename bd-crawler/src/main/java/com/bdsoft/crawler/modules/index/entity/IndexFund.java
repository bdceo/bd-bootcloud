package com.bdsoft.crawler.modules.index.entity;

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
 * 跟踪指数的基金产品
 * </p>
 *
 * @author bdceo
 * @since 2021-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_index_fund")
public class IndexFund implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 指数编码
     */
    private String indexCode;

    /**
     * 基金代码
     */
    private String code;

    /**
     * 基金名称
     */
    private String name;

    /**
     * 基金类型：其他、股票型、混合型
     */
    private String type1;

    /**
     * 产品类型：指数基金、LOF、联接基金
     */
    private String type2;

    /**
     * 基金公司
     */
    private String company;

    /**
     * 基金成立日期
     */
    private Date setupDate;

    /**
     * 同步时间
     */
    private Date synTime;


}
