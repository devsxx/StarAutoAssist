package com.app.starautoassist.Helper;

/**
 * Chu su dung khi luu tru database
 * Created by tonyb on 3/17/2016.
 */
public class ResultPaymentObject {
    ResultPaymentMessage msg;

    String strTransID = "";
    String strRefNo = "";

    double dAmount = 0.00;
    String strRemark = "";
    String errDesc = "";
    String strInfo = "";
    String strTit = "";
    int iStatus;
    long lTime;


    public double getdAmount() {
        return dAmount;
    }

    public void setdAmount(double dAmount) {
        this.dAmount = dAmount;
    }

    public String getStrTit() {
        return strTit;
    }

    public void setStrTit(String strTit) {
        this.strTit = strTit;
    }

    public String getStrInfo() {
        return strInfo;
    }

    public void setStrInfo(String strInfo) {
        this.strInfo = strInfo;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    public String getStrRemark() {
        return strRemark;
    }

    public void setStrRemark(String strRemark) {
        this.strRemark = strRemark;
    }

    public String getStrRefNo() {
        return strRefNo;
    }

    public void setStrRefNo(String strRefNo) {
        this.strRefNo = strRefNo;
    }

    public int getiStatus() {
        return iStatus;
    }

    public void setiStatus(int iStatus) {
        this.iStatus = iStatus;
    }

    public long getlTime() {
        return lTime;
    }

    public void setlTime(long lTime) {
        this.lTime = lTime;
    }


    public String getStrTransID() {
        return strTransID;
    }

    public void setStrTransID(String strTransID) {
        this.strTransID = strTransID;
    }


    public ResultPaymentObject(ResultPaymentMessage message) {
        this.msg = message;
        extraDataInfoDevided(message.getStrResultExtra());
        setStrInfo(message.getStrResultInfo());
        setStrTit(message.getStrResultTitle());
    }

    public ResultPaymentObject() {

    }

    /**
     * @param extra
     */
    private void extraDataInfoDevided(String extra) {
        if (extra != null && extra.length() > 0) {
            String[] extraDat = extra.split("\n");
            int count = 0;
            for (String datLine : extraDat) {
                int indexEqual = datLine.indexOf("=");
                String dataPec = datLine.substring(indexEqual + 1);

                switch (count) {
                    case 0:
                        setStrTransID(dataPec.trim());
                        break;
                    case 1:
                        setStrRefNo(dataPec.trim());
                        break;
                    case 2:
                        dataPec = dataPec.trim();
                        setdAmount(Double.parseDouble(dataPec));
                        break;
                    case 3:
                        setStrRemark(dataPec);
                        break;
                    case 4:
                        setErrDesc(dataPec);
                        break;
                    default:
                        break;
                }
                count++;
            }
        }
    }
}
