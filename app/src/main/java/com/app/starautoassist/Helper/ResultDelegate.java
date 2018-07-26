package com.app.starautoassist.Helper;

import android.util.Log;

import com.ipay.IPayIHResultDelegate;

public class ResultDelegate implements IPayIHResultDelegate {

    @Override
    public void onPaymentSucceeded(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {

    }

    @Override
    public void onPaymentFailed(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {

    }

    @Override
    public void onPaymentCanceled(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {

    }

    @Override
    public void onRequeryResult(String MerchantCode, String RefNo, String Amount, String Result) {
        Log.e("tag", "onRequeryResult");
    }

    @Override
    public void onConnectionError(String s, String s1, String s2, String s3, String s4, String s5, String s6) {

    }
}
