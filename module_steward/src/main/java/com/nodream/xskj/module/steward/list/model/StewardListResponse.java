package com.nodream.xskj.module.steward.list.model;

import com.nodream.xskj.module.steward.bean.StewardBean;

import java.util.List;

/**
 * Created by nodream on 2017/12/8.
 */

public class StewardListResponse {

    private int totalRecords;
    private int pageIndex;
    private int pageSize;
    private int totalPages;
    private List<StewardBean> mStewardList;
}
