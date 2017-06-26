package com.salesmanapp.pojo;

/**
 * Created by SATHISH on 24-Jun-17.
 */

public class OrderData {


    String orderid, serviceid,quantity,rate,discountamount,netamount,cleintid,employeeid,date,devicetype;

    public OrderData(String orderid, String serviceid, String quantity, String rate, String discountamount, String netamount, String cleintid, String employeeid, String date,String devType) {
        this.orderid = orderid;
        this.serviceid = serviceid;
        this.quantity = quantity;
        this.rate = rate;
        this.discountamount = discountamount;
        this.netamount = netamount;
        this.cleintid = cleintid;
        this.employeeid = employeeid;
        this.date = date;
        this.devicetype =devType;

    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(String discountamount) {
        this.discountamount = discountamount;
    }

    public String getNetamount() {
        return netamount;
    }

    public void setNetamount(String netamount) {
        this.netamount = netamount;
    }

    public String getCleintid() {
        return cleintid;
    }

    public void setCleintid(String cleintid) {
        this.cleintid = cleintid;
    }

    public String getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
