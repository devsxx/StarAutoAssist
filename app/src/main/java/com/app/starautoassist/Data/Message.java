package com.app.starautoassist.Data;

public class Message {

    private String sid;
    private String smessage;
    private String stime;

    public Message(String sid, String smessage, String stime) {
        this.sid = sid;
        this.smessage = smessage;
        this.stime = stime;
    }

    public String getSid() {
        return sid;
    }

    public String getSmessage() {
        return smessage;
    }

    public String getStime() {
        return stime;
    }
}
