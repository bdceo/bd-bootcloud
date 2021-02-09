package com.bdsoft.crawler.modules.index.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 指数十大权重股票、债券
 * </p>
 *
 * @author bdceo
 * @since 2021-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_index_stock")
public class IndexStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 指数编码
     */
    private String indexCode;

    /**
     * 股票、债券代码
     */
    private String code;

    /**
     * 股票、债券名称
     */
    private String name;

    /**
     * 行业名称
     */
    private String industryName;

    /**
     * 中证-行业编码
     */
    private String industryCode;

    /**
     * 权重
     */
    private float weight;

    /**
     * 截止日期
     */
    private Date endDate;

    /**
     * 同步时间
     */
    private Date synTime;


}
