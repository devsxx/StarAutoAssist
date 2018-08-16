package com.app.starautoassist.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


import static android.content.ContentValues.TAG;

public class IncomingSms extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] obj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < obj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) obj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    String verificationCode = getVerificationCode(message);
                    Log.e(TAG, "OTP received: " + verificationCode);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: " + verificationCode + ", message: " + message, duration);
                    toast.show();
                    try {
                        if (senderNum.equals("63001")) {
                            //Config.msg = verificationCode;
                            Intent myIntent = new Intent("otp");
                            myIntent.putExtra("message",verificationCode);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private String getVerificationCode(String message) {
       // String code = null;
        /*int index = message.indexOf();
        if (index != -1) {
            int start = index + 3;
            int length = 6;
            code = message.substring(start, start + length);
            return code;
        } else return code;*/
        String[] code = message.split(":");
        return code[1];


    }
}
