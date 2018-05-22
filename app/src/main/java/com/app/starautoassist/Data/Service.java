package com.app.starautoassist.Data;

public class Service {

    private String servicename;
    private int serviceimage;

    public Service() {
    }

    public Service(String servicename, int serviceimage) {

        this.servicename = servicename;
        this.serviceimage = serviceimage;
    }

    public String getServicename() {

        return servicename;
    }

    public void setServicename(String servicename) {

        this.servicename = servicename;
    }

    public int getServiceimage() {

        return serviceimage;
    }

    public void setServiceimage(int serviceimage) {

        this.serviceimage = serviceimage;
    }
}
