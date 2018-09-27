package com.nodream.xskj.module.main.inqusisition.model;

import com.nodream.xskj.module.main.information.model.PatientBean;
import com.nodream.xskj.module.main.work.model.LocationBean;

public class OrderBean {
    private String orderId;
    private String stationName;
    private String stationAddress;
    private PatientBean patient;
    private int orderStatus;
    private String inquiryTime;
    private String illnessDesc;
    private String linkman;
    private String linkMobile;
    private LocationBean serveLocation;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public PatientBean getPatient() {
        return patient;
    }

    public void setPatient(PatientBean patient) {
        this.patient = patient;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getInquiryTime() {
        return inquiryTime;
    }

    public void setInquiryTime(String inquiryTime) {
        this.inquiryTime = inquiryTime;
    }

    public String getIllnessDesc() {
        return illnessDesc;
    }

    public void setIllnessDesc(String illnessDesc) {
        this.illnessDesc = illnessDesc;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkMobile() {
        return linkMobile;
    }

    public void setLinkMobile(String linkMobile) {
        this.linkMobile = linkMobile;
    }

    public LocationBean getServeLocation() {
        return serveLocation;
    }

    public void setServeLocation(LocationBean serveLocation) {
        this.serveLocation = serveLocation;
    }
}
