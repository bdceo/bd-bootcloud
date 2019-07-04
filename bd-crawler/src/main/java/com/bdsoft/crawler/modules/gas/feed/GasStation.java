package com.bdsoft.crawler.modules.gas.feed;

import lombok.Data;

/**
 * 加油站
 */
@Data
public class GasStation {

    private String name;

    private String province;
    private String city;
    private String district;

    private String address;

    private String phone;


    @Override
    public String toString() {
        return "GasStation{" +
                "name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
