package com.example.wifiscanner;

public class Element {
    private String NetworkName;
    private String Sec;
    private String Lvl;
    private String Bssid;
    private String Freq;
    private String TimeStamp;

    public Element(String title, String security, String level,String  bssid,String frequency,String timestamp) {
        this.NetworkName = title;
        this.Sec = security;
        this.Lvl = level;
        this.Bssid = bssid;
        this.Freq = frequency;
        this.TimeStamp = timestamp;
    }

    public String getNetworkName() {
        return NetworkName;
    }

    public String getSec() {
        return Sec;
    }

    public String getLvl() {
        return Lvl;
    }

    public String getBssid() {
        return Bssid;
    }

    public String getFreq() {
        return Freq;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }
}
