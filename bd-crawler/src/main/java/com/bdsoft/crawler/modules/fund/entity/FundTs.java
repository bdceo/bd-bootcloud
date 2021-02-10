package com.bdsoft.crawler.modules.fund.entity;

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
 * 特色数据：风险、跟踪误差
 * </p>
 *
 * @author bdceo
 * @since 2021-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_fund_ts")
public class FundTs implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 基金代码
     */
    private String code;

    /**
     * 在所有基金中的风险等级：1-低，2-中低，3-中，4-中高，5-高
     */
    private Integer wholeRiskLevel;

    /**
     * 在同类基金中的风险等级：1-低，2-中低，3-中，4-中高，5-高
     */
    private Integer sameRiskLevel;

    /**
     * 标准差：近1年
     */
    private Float stdDev1;

    /**
     * 标准差：近2年
     */
    private Float stdDev2;

    /**
     * 标准差：近3年
     */
    private Float stdDev3;

    /**
     * 夏普比率：近1年
     */
    private Float sharpeRate1;

    /**
     * 夏普比率：近2年
     */
    private Float sharpeRate2;

    /**
     * 夏普比率：近3年
     */
    private Float sharpeRate3;

    /**
     * 信息比率：近1年
     */
    private Float infoRate1;

    /**
     * 信息比率：近2年
     */
    private Float infoRate2;

    /**
     * 信息比率：近3年
     */
    private Float infoRate3;

    /**
     * 跟踪指数
     */
    private String trackIndex;

    /**
     * 跟踪误差
     */
    private Float trackDiff;

    /**
     * 平类平均跟踪误差
     */
    private Float sameDiff;

    /**
     * 同步时间
     */
    private Date synTime;


}
