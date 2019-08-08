package com.example.myproject;

import android.widget.EditText;

public class Customer {
    private String cus_fname;
    private String cus_lname;
    private String number;
    private String drom;
    private String roomnum;
    private String floor;
    private String group;
    private String road;
    private String alley;
    private String subdistrict;
    private String district;
    private String province;
    private String code;

    public Customer(){}

    public Customer(String cus_fname, String cus_lname, String number, String drom, String roomnum, String floor, String group, String road, String alley, String subdistrict, String district, String province, String code) {
        this.cus_fname = cus_fname;
        this.cus_lname = cus_lname;
        this.number = number;
        this.drom = drom;
        this.roomnum = roomnum;
        this.floor = floor;
        this.group = group;
        this.road = road;
        this.alley = alley;
        this.subdistrict = subdistrict;
        this.district = district;
        this.province = province;
        this.code = code;
    }

    public String getCus_fname() {
        return cus_fname;
    }

    public void setCus_fname(String cus_fname) {
        this.cus_fname = cus_fname;
    }

    public String getCus_lname() {
        return cus_lname;
    }

    public void setCus_lname(String cus_lname) {
        this.cus_lname = cus_lname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDrom() {
        return drom;
    }

    public void setDrom(String drom) {
        this.drom = drom;
    }

    public String getRoomnum() {
        return roomnum;
    }

    public void setRoomnum(String roomnum) {
        this.roomnum = roomnum;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getAlley() {
        return alley;
    }

    public void setAlley(String alley) {
        this.alley = alley;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String toString(){
        return cus_fname + " " + cus_lname;
    }
}
