package com.app.starautoassist.Helper;

import android.util.Log;

import com.app.starautoassist.Others.Constants;
import com.ipay.IPayIHResultDelegate;

import java.io.Serializable;

public class ResultDelegate implements IPayIHResultDelegate, Serializable {
    private static final long serialVersionUID = 10001L;
    private static final String TAG = ResultDelegate.class.getSimpleName();

    @Override
    public void onPaymentSucceeded(String TransId, String RefNo, String Amount,
                                   String Remark, String AuthCode, String CCName,
                                   String CCNo, String S_bankname, String S_country) {
        Constants.resultTitle = "SUCCESS";
        GetSet.setTransid(TransId);
        Log.d(TAG, "Remark: " + Remark);
    }

    @Override
    public void onPaymentFailed(String TransId, String RefNo, String Amount,
                                String Remark, String ErrDesc, String CCName,
                                String CCNo, String S_bankname, String S_country) {
        Constants.resultTitle = "FAILURE";
        GetSet.setErrDes(ErrDesc);
        Log.d(TAG, "ErrDesc: " + ErrDesc);
        Log.d(TAG, "Remark: " + Remark);
    }

    @Override
    public void onPaymentCanceled(String TransId, String RefNo, String Amount,
                                  String Remark, String ErrDesc, String CCName,
                                  String CCNo, String S_bankname, String S_country) {

        Constants.resultTitle = "CANCELED";
        GetSet.setErrDes(ErrDesc);
        Log.d(TAG, "ErrDesc: " + ErrDesc);
        Log.d(TAG, "Remark: " + Remark);

    }

    @Override
    public void onRequeryResult(String MerchantCode, String RefNo,
                                String Amount, String Result) {
        Constants.resultTitle = "Requery Result";
        Constants.resultInfo = "";
    }

    @Override
    public void onConnectionError(String merchantCode, String merchantKey,
                                  String RefNo, String Amount, String Remark, String lang, String country) {
        Constants.resultTitle = "CONNECTION ERROR";
        GetSet.setErrDes("Something Went Wrong");
    }
}
