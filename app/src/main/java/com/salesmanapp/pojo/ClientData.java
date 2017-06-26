package com.salesmanapp.pojo;

/**
 * Created by SATHISH on 19-Jun-17.
 */

public class ClientData {

    String clientid, clientname, moibleno1, bussiness, address, note, email, moibleno2, landline, companyname, devicetype,clienttype,lattitude,longtitude,visitingcardfront,visitingcardback;


    public ClientData(String clientid, String clientname, String moibleno, String bussiness, String address, String note, String email, String mobile2, String landline, String compname, String devicetype,String clienttype,String lattitude,String longtitude) {
        this.clientid = clientid;
        this.clientname = clientname;
        this.moibleno1 = moibleno;
        this.bussiness = bussiness;
        this.address = address;
        this.note = note;
        this.email = email;
        this.moibleno2 = mobile2;
        this.landline = landline;
        this.companyname = compname;
        this.devicetype = devicetype;
        this.clienttype = clienttype;
        this.lattitude = lattitude;
        this.longtitude = longtitude;


    }


    public String getVisitingcardfront() {
        return visitingcardfront;
    }

    public void setVisitingcardfront(String visitingcardfront) {
        this.visitingcardfront = visitingcardfront;
    }

    public String getVisitingcardback() {
        return visitingcardback;
    }

    public void setVisitingcardback(String visitingcardback) {
        this.visitingcardback = visitingcardback;
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

    public String getClienttype() {
        return clienttype;
    }

    public void setClienttype(String clienttype) {
        this.clienttype = clienttype;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
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

    public void setMoibleno1(String moibleno) {
        this.moibleno1 = moibleno;
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
}
