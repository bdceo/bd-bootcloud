package com.bdsoft.crawler.modules.fund.po;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 基金特色数据：风险、指数跟踪
 */
@Data
public class FundDataPO {

    /**
     * 基金标识
     */
    private String code;

    /**
     * 风险等级：全部、同类
     * 1-低，2-中低，3-中，4-中高，5-高
     */
    private int wholeRiskLevel;
    private int sameRiskLevel;

    /**
     * 风险指标
     */
    // 标准差：反映基金收益率的波动程度。标准差越小，基金的历史阶段收益越稳定。
    private float stdDev1;
    private float stdDev2;
    private float stdDev3;
    // 夏普比率：反映基金承担单位风险，所能获得的超过无风险收益的超额收益。夏普比率越大，基金的历史阶段绩效表现越佳。
    private float sharpeRate1;
    private float sharpeRate2;
    private float sharpeRate3;
    // 信息比率：表示单位主动风险所带来的超额收益，比率高说明超额收益高。
    private float infoRate1;
    private float infoRate2;
    private float infoRate3;

    /**
     * 指数基金指标：跟踪指数、跟踪误差、平类平均跟踪误差
     */
    private String trackIndex;
    private float trackDiff;
    private float sameDiff;

    public FundDataPO() {
    }

    public FundDataPO(String code) {
        this.code = code;
    }

    public void setWholeRiskLevel(String riskName) {
        this.wholeRiskLevel = this.getRiskLevel(riskName);
    }

    public void setSameRiskLevel(String riskName) {
        this.sameRiskLevel = this.getRiskLevel(riskName);
    }

    /**
     * 风险等级映射
     *
     * @param riskName
     * @return
     */
    private int getRiskLevel(String riskName) {
        if (StringUtils.isEmpty(riskName)) {
            return -1;
        } else if ("低".equals(riskName)) {
            return 1;
        } else if ("中低".equals(riskName)) {
            return 2;
        } else if ("中".equals(riskName)) {
            return 3;
        } else if ("中高".equals(riskName)) {
            return 4;
        } else if ("高".equals(riskName)) {
            return 5;
        } else {
            return -1;
        }
    }

    /**
     * 风险指标解析
     *
     * @param name  指标名称
     * @param year1 最近一年数据
     * @param year2 最近两年数据
     * @param year3 最近三年数据
     */
    public void setRiskIndex(String name, String year1, String year2, String year3) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(year1) || "--".equals(year1)) {
            return;
        }
        if ("标准差".equals(name)) {
            this.stdDev1 = Float.valueOf(year1.replace("%", ""));
            this.stdDev2 = (!"--".equals(year2)) ? Float.valueOf(year2.replace("%", "")) : 0;
            this.stdDev3 = (!"--".equals(year3)) ? Float.valueOf(year3.replace("%", "")) : 0;
        } else if ("夏普比率".equals(name)) {
            this.sharpeRate1 = Float.valueOf(year1);
            this.sharpeRate2 = (!"--".equals(year2)) ? Float.valueOf(year2) : 0;
            this.sharpeRate3 = (!"--".equals(year3)) ? Float.valueOf(year3) : 0;
        } else if ("信息比率".equals(name)) {
            this.infoRate1 = Float.valueOf(year1);
            this.infoRate2 = (!"--".equals(year2)) ? Float.valueOf(year2) : 0;
            this.infoRate3 = (!"--".equals(year3)) ? Float.valueOf(year3) : 0;
        }
    }
}
