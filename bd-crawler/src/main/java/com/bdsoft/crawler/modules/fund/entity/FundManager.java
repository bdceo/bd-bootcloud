package com.bdsoft.crawler.modules.fund.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
* <p>
    * 基金的经理任职记录
    * </p>
*
* @author bdceo
* @since 2021-02-10
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("t_fund_manager")
    public class FundManager implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

            /**
            * 基金代码
            */
    private String code;

            /**
            * 基金经理编号
            */
    private String managerCode;

            /**
            * 基金经理名称
            */
    private String managerName;

            /**
            * 管理时间-开始
            */
    private LocalDate start;

            /**
            * 管理时间-截至
            */
    private LocalDate end;


}
