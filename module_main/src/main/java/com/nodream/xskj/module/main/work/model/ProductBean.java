package com.nodream.xskj.module.main.work.model;

import java.util.List;

public class ProductBean {
    private String id;
    private String name;
    private String pice;
    private List<ConsumablesBean> consumables;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPice() {
        return pice;
    }

    public void setPice(String pice) {
        this.pice = pice;
    }

    public List<ConsumablesBean> getConsumables() {
        return consumables;
    }

    public void setConsumables(List<ConsumablesBean> consumables) {
        this.consumables = consumables;
    }
}
