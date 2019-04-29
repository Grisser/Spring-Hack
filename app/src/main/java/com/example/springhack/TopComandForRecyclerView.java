package com.example.springhack;

import android.support.v4.app.FragmentActivity;

public class TopComandForRecyclerView {


    private String comandName;
    private String XP;
    private String id;
    public FragmentActivity activity;

    public TopComandForRecyclerView(String comandName, String XP , String id, FragmentActivity activity) {
        this.id = id;
        this.comandName = comandName;
        this.XP = XP;
        this.activity = activity;

    }


    public String getID() {
        return this.id;
    }
    public String getXP() {
        return this.XP;
    }



    public FragmentActivity getActivity() {
        return activity;
    }

    public String getcomandName() {
        return comandName;
    }

    public String getName() {
        return this.comandName;
    }

    public void setcomandName(String comandName) {
        this.comandName = comandName;
    }

    public void setName(String name) {
        this.comandName = name;
    }



    public void setXP(String XP) {
        this.XP = XP;
    }


}

