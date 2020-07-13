package com.example.basictest.Class;

import java.io.Serializable;

public class bankEntity implements Serializable {


    /**
     * searchValue : null
     * createBy : null
     * createTime : null
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * id : 12
     * coName : 宁波银行
     * fStatus : 1
     * timeC : 2020-06-11 09:20:08
     * urlVerify : null
     * urlSync : null
     * rpcToken : null
     */

    private Object searchValue;
    private Object createBy;
    private Object createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private ParamsBean params;
    private int id;
    private String coName;
    private String fStatus;
    private String timeC;
    private Object urlVerify;
    private Object urlSync;
    private Object rpcToken;

    public Object getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(Object searchValue) {
        this.searchValue = searchValue;
    }

    public Object getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Object createBy) {
        this.createBy = createBy;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Object updateBy) {
        this.updateBy = updateBy;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoName() {
        return coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }

    public String getFStatus() {
        return fStatus;
    }

    public void setFStatus(String fStatus) {
        this.fStatus = fStatus;
    }

    public String getTimeC() {
        return timeC;
    }

    public void setTimeC(String timeC) {
        this.timeC = timeC;
    }

    public Object getUrlVerify() {
        return urlVerify;
    }

    public void setUrlVerify(Object urlVerify) {
        this.urlVerify = urlVerify;
    }

    public Object getUrlSync() {
        return urlSync;
    }

    public void setUrlSync(Object urlSync) {
        this.urlSync = urlSync;
    }

    public Object getRpcToken() {
        return rpcToken;
    }

    public void setRpcToken(Object rpcToken) {
        this.rpcToken = rpcToken;
    }

    public static class ParamsBean {
    }
}
