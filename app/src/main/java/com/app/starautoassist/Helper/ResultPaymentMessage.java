package com.app.starautoassist.Helper;

import com.ipay.obj.MCTokenizationRet;

/**
 * Created by tonyb on 3/16/2016.
 */
public class ResultPaymentMessage {


    MCTokenizationRet mcTokenizationRet;

    String strResultTitle;
    String strResultInfo;
    String strResultExtra;

    public MCTokenizationRet getMcTokenizationRet() {
        return mcTokenizationRet;
    }

    public void setMcTokenizationRet(MCTokenizationRet mcTokenizationRet) {
        this.mcTokenizationRet = mcTokenizationRet;
    }


    public String getStrResultTitle() {
        return strResultTitle;
    }

    public void setStrResultTitle(String strResultTitle) {
        this.strResultTitle = strResultTitle;
    }

    public String getStrResultInfo() {
        return strResultInfo;
    }

    public void setStrResultInfo(String strResultInfo) {
        this.strResultInfo = strResultInfo;
    }

    public String getStrResultExtra() {
        return strResultExtra;
    }

    public void setStrResultExtra(String strResultExtra) {
        this.strResultExtra = strResultExtra;
    }


}
