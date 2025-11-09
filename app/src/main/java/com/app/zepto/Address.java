package com.app.zepto;

import java.io.Serializable;

public class Address implements Serializable {
    private String id;
    private String fullName;
    private String mobile;
    private String pincode;
    private String address;
    private String landmark;
    private String city;
    private String state;
    private String addressType; // Home, Work, Other
    private boolean isDefault;

    public Address() {
        this.id = String.valueOf(System.currentTimeMillis());
        this.isDefault = false;
    }

    public Address(String fullName, String mobile, String pincode, String address,
                   String landmark, String city, String state, String addressType) {
        this();
        this.fullName = fullName;
        this.mobile = mobile;
        this.pincode = pincode;
        this.address = address;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.addressType = addressType;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getAddressType() { return addressType; }
    public void setAddressType(String addressType) { this.addressType = addressType; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }

    public String getCompleteAddress() {
        return address + ", " + landmark + ", " + city + ", " + state + " - " + pincode;
    }
}
