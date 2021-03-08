package com.bdsoft.crawler.modules.delivery.po;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 华泰交割单导出Excel字段映射
 */
@Data
public class HuaTaiDeliveryPO {

    @ExcelProperty(value = "发生日期", index = 0)
    private Date dt;

    @ExcelProperty(value = "证券代码", index = 2)
    private String code;
    @ExcelProperty(value = "证券名称", index = 3)
    private String name;

    @ExcelProperty(value = "买卖标志", index = 4)
    private String opType;

    @ExcelProperty(value = "成交数量", index = 5)
    private Integer dealNum;
    @ExcelProperty(value = "成交价格", index = 6)
    private Float dealPrice;
    @ExcelProperty(value = "成交金额", index = 7)
    private Float dealAmount;

    @ExcelProperty(value = "佣金", index = 8)
    private Float feeYj;
    @ExcelProperty(value = "印花税", index = 9)
    private Float feeYhs;
    @ExcelProperty(value = "过户费", index = 10)
    private Float feeGhf;

    @ExcelProperty(value = "发生金额", index = 11)
    private Float total;

}
