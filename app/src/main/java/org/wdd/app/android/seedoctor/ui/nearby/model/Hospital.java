package org.wdd.app.android.seedoctor.ui.nearby.model;

import org.wdd.app.android.seedoctor.location.LatLong;

import java.util.Arrays;

/**
 * Created by richard on 11/28/16.
 */

public class Hospital {
    private String name;//医院名称
    private String address;//地址
    private LatLong latLong;//位置
    private String telephone;//电话
    private String website;//网址
    private String postcode;//邮编
    private String email;//电子邮件
    private String[] imgUrls;
    private long distance;//距离当前位置的距离
    private String typeDes;//类型描述

    public Hospital() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLong getLatLong() {
        return latLong;
    }

    public void setLatLong(LatLong latLong) {
        this.latLong = latLong;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String[] imgUrls) {
        this.imgUrls = imgUrls;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getTypeDes() {
        return typeDes;
    }

    public void setTypeDes(String typeDes) {
        this.typeDes = typeDes;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", latLong=" + latLong +
                ", telephone='" + telephone + '\'' +
                ", website='" + website + '\'' +
                ", postcode='" + postcode + '\'' +
                ", email='" + email + '\'' +
                ", imgUrls=" + Arrays.toString(imgUrls) +
                ", distance=" + distance +
                ", typeDes='" + typeDes + '\'' +
                '}';
    }
}
