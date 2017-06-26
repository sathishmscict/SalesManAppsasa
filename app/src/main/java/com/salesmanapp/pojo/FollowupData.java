package com.salesmanapp.pojo;

/**
 * Created by SATHISH on 19-Jun-17.
 */

public class FollowupData {


    String followupid, followupdate,followuptime,followupnote,clientid,devicetype;
    String  clientname, moibleno1, bussiness, address, note, email, moibleno2, landline, companyname ,clienttype,lattitude,longtitude;

    public FollowupData(String followupid, String followupdate, String followuptime, String followupnote, String clientid, String devicetype, String clientname, String moibleno1, String bussiness, String address, String note, String email, String moibleno2, String landline, String companyname, String clienttype, String lattitude, String longtitude) {
        this.followupid = followupid;
        this.followupdate = followupdate;
        this.followuptime = followuptime;
        this.followupnote = followupnote;
        this.clientid = clientid;
        this.devicetype = devicetype;
        this.clientname = clientname;
        this.moibleno1 = moibleno1;
        this.bussiness = bussiness;
        this.address = address;
        this.note = note;
        this.email = email;
        this.moibleno2 = moibleno2;
        this.landline = landline;
        this.companyname = companyname;
        this.clienttype = clienttype;
        this.lattitude = lattitude;
        this.longtitude = longtitude;
    }

    public String getFollowupid() {
        return followupid;
    }

    public void setFollowupid(String followupid) {
        this.followupid = followupid;
    }

    public String getFollowupdate() {
        return followupdate;
    }

    public void setFollowupdate(String followupdate) {
        this.followupdate = followupdate;
    }

    public String getFollowuptime() {
        return followuptime;
    }

    public void setFollowuptime(String followuptime) {
        this.followuptime = followuptime;
    }

    public String getFollowupnote() {
        return followupnote;
    }

    public void setFollowupnote(String followupnote) {
        this.followupnote = followupnote;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public String getMoibleno1() {
        return moibleno1;
    }

    public void setMoibleno1(String moibleno1) {
        this.moibleno1 = moibleno1;
    }

    public String getBussiness() {
        return bussiness;
    }

    public void setBussiness(String bussiness) {
        this.bussiness = bussiness;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMoibleno2() {
        return moibleno2;
    }

    public void setMoibleno2(String moibleno2) {
        this.moibleno2 = moibleno2;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getClienttype() {
        return clienttype;
    }

    public void setClienttype(String clienttype) {
        this.clienttype = clienttype;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
}
