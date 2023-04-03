package com.example.gma.models;

// Its a model class for Getting & Setting Data
public class User {
    public String id;
    public String username;
    public String email;
    public String phoneNo;
    public String gender;
    public String role;
    public String status;
    public String imgUrl;

    // Getter Setters
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getDatebirth() {
        return datebirth;
    }

    public void setDatebirth(long datebirth) {
        this.datebirth = datebirth;
    }

    public String vehicleId;
    public long datebirth;

    public String getImgUrl() {
        return imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Here i am defining parameterized Constructor for the User Class
    public User(String id,String email,String phoneNumber, String name, String imageUrl,String status) {
        this.id = id;
        this.imgUrl = imageUrl;
        this.email = email;
        this.phoneNo =phoneNumber;
        this.status = status;
        this.username=name;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User() {
    }
}
