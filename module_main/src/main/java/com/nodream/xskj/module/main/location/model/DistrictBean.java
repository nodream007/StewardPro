package com.nodream.xskj.module.main.location.model;

import java.util.List;

public class DistrictBean {
    private int id;
    private int level;
    private String name;
    private List<StreetBean> regions;

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

    public List<StreetBean> getRegions() {
        return regions;
    }

    public void setRegions(List<StreetBean> regions) {
        this.regions = regions;
    }
}
