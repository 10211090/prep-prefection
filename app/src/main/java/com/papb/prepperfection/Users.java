package com.papb.prepperfection;

public class Users {
    String userId, name, email, profile;

    public Users(String userId, String name, String profile){
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public Users(){
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
