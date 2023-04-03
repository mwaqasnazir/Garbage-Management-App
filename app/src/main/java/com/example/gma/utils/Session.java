package com.example.gma.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private String userName = "";
    private String userRole = "user_role";
    private String userId = "user_id";

    public Session(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);

    }
    public String getUserRole() {
        return sp.getString(userRole, "");
    }

    public void setUserRole(String role) {
        spEditor = sp.edit();
        spEditor.putString(userRole, role);
        spEditor.apply();
    }
    public String getUserid() {
        return sp.getString(userId, "");
    }

    public void setUserid(String userid) {
        spEditor = sp.edit();
        spEditor.putString(userId, userid);
        spEditor.apply();
    }
    public void destroy(){
        spEditor = sp.edit();
        spEditor.remove(userRole);
        spEditor.remove(userId);
        spEditor.clear();
        spEditor.apply();
    }
}
