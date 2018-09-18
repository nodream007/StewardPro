package com.nodream.xskj.module.main.work.model;

import java.util.List;

public class WorkDetailResponse {
    private int totalRecords;
    private int totalPages;
    private int pageIndex;
    private int pageSize;
    private List<WorkBean> list;

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<WorkBean> getWorkBeanList() {
        return list;
    }

    public void setWorkBeanList(List<WorkBean> workBeanList) {
        this.list = workBeanList;
    }
}
