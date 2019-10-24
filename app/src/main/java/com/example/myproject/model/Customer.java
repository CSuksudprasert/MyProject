package com.example.myproject.model;

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
    private String latitude;
    private String longtitude;

    String add_num, add_drom, add_rnum, add_f, add_g, add_r, add_al, add_subdis, add_dis, add_pro, add_code;

    public Customer() {
    }

    public Customer(String cus_fname, String cus_lname, String number, String drom, String roomnum, String floor, String group, String road, String alley, String subdistrict, String district, String province, String code
            , String latitude, String longtitude) {
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
        this.latitude = latitude;
        this.longtitude = longtitude;

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String name() {
        return cus_fname + " " + cus_lname;
    }

    public String address() {
        if (number == null) {
            number = "-";
        }
//        else {
//            number = "เลขที่ " + number;
//        }
        if (drom == null) {
            drom = "-";
        }
//        else {
//            drom = " หอพัก " + drom;
//        }
        if (roomnum == null) {
            roomnum = "-";
        }
//        else {
//            roomnum = " ห้อง " + roomnum;
//        }
        if (floor == null) {
            floor = "-";
        }
//        else {
//            floor = " ชั้น " + floor;
//        }
        if (group == null) {
            group = "-";
        }
//        else {
//            group = " หมู่ " + group;
//        }
        if (road == null) {
            road = "-";
        }
//        else {
//            road = " ถนน " + road;
//        }
        if (alley == null) {
            alley = "-";
        }
//        else {
//            alley = " ซอย " + alley;
//        }
        if (subdistrict == null) {
            subdistrict = "-";
        }
//        else {
//            subdistrict = " ตำบล " + subdistrict;
//        }
        if (district == null) {
            district = "-";
        }
//        else {
//            district = " อำเภอ " + district;
//        }
        if (province == null) {
            province = "-";
        }
//        else {
//            province = " จังหวัด " + province;
//        }

        return "เลขที่ " + number + " หอพัก " + drom + " ห้อง " + roomnum + " ชั้น " + floor + " หมู่ " + group + " ถนน " + road +
                " ซอย " + alley + " ตำบล " + subdistrict + " อำเภอ " + district + " จังหวัด " + province + " รหัสไปรษณีย์ " + code;

//        return number +  drom +  roomnum +  floor +  group + road +
//                 alley +  subdistrict + district +  province + " รหัสไปรษณีย์ " + code;

        // return add_num + add_drom + add_rnum + add_f + add_g + add_r + add_al + add_subdis + add_dis + add_pro + " รหัสไปรษณีย์ " + code;
    }

    public String getlocation() {
        return latitude + " " + longtitude;
    }
}
