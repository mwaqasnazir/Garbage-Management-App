package com.example.gma.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Vehicle {
    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String vehicleNo;
    public String model;
    public String type;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String uId;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("vehicleNo", vehicleNo);
        result.put("model", model);
        result.put("type", type);
        result.put("uId", type);

        return result;
    }
}
