package com.bdsoft.crawler.modules.gas.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站点数据
 * </p>
 *
 * @author bdceo
 * @since 2019-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_station_db")
public class StationDb implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类型：1-中石化，2-中石油，3-私人
     */
    private Integer type;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 电话
     */
    private String phone;

    /**
     * 省编码
     */
    private String provinceCode;

    /**
     * 市编码
     */
    private String cityCode;

    /**
     * 区县编码
     */
    private String districtCode;

    /**
     * 数据来源
     */
    private String source;

    /**
     * 入库时间
     */
    private Date createTime;


}
