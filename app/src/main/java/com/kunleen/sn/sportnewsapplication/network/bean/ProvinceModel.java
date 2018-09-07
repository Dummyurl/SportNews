package com.kunleen.sn.sportnewsapplication.network.bean;

import java.util.List;

/**
 * Created by lixingjun on 7/15/16.
 */
public class ProvinceModel {
    private String name;
    private List<CityModel> cityList;

    public ProvinceModel() {
        super();
    }

    public ProvinceModel(String name, List<CityModel> cityList) {
        super();
        this.name = name;
        this.cityList = cityList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityModel> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityModel> cityList) {
        this.cityList = cityList;
    }

    @Override
    public String toString() {
        return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
    }
}
