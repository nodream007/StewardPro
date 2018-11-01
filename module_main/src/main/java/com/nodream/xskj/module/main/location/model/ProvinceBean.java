package com.nodream.xskj.module.main.location.model;

import java.util.List;

public class ProvinceBean {
    private int id;
    private int level;
    private String name;
    private List<CityBean> regions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getRegions() {
        return regions;
    }

    public void setRegions(List<CityBean> regions) {
        this.regions = regions;
    }
}
