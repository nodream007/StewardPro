package com.nodream.xskj.module.main.work.model;

import com.nodream.xskj.module.main.information.model.PatientBean;

public class TaskBean {
    private String id;
    private String treatmentId;
    private String orderId;
    private String linkman;
    private String linkMobile;
    private int medicalStaffId;
    private LocationBean serveLocation;
    private ProductBean product;
    private PatientBean patient;
    private boolean receive;
    private int taskType;
    private String createTime;
    private String planedStartTime;
    private String startTime;
    private String endTime;
    private int taskStatus;
    private String description;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getMedicalStaffId() {
        return medicalStaffId;
    }

    public void setMedicalStaffId(int medicalStaffId) {
        this.medicalStaffId = medicalStaffId;
    }

    public LocationBean getServeLocation() {
        return serveLocation;
    }

    public void setServeLocation(LocationBean serveLocation) {
        this.serveLocation = serveLocation;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public boolean isReceive() {
        return receive;
    }

    public void setReceive(boolean receive) {
        this.receive = receive;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPlanedStartTime() {
        return planedStartTime;
    }

    public void setPlanedStartTime(String planedStartTime) {
        this.planedStartTime = planedStartTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public PatientBean getPatient() {
        return patient;
    }

    public void setPatient(PatientBean patient) {
        this.patient = patient;
    }
}
