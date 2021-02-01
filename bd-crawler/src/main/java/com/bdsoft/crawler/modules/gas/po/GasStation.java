package com.bdsoft.crawler.modules.gas.po;

import lombok.Data;

/**
 * 加油站
 */
@Data
public class GasStation {

    private String name;

    private String provinceCode;
    private String cityCode;
    private String districtCode;

    private String address;

    private String phone;

    @Override
    public String toString() {
        return "GasStation{" +
                "name='" + name + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
