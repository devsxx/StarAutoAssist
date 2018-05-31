package com.app.starautoassist.Others;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.starautoassist.R;
import com.facebook.FacebookSdk;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.crypto.Cipher;

@ReportsCrashes(mailTo = "iamdevshadowws@gmail.com")
public class Starautoassist_Application extends Application {
    public static IntentFilter filter;
    public static BroadcastReceiver networkStateReceiver;
    public static Dialog dialog;


    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        // init facebook //
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        FacebookSdk.setApplicationId(Constants.App_ID);

        Constants.ANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
        MultiDex.install(this);
    }

    /** function for check the network **/
    public static void registerReceiver(final Context ctx){
        if (networkStateReceiver == null) {
            Log.v("network dialog", "network dialog");
            dialog = new Dialog(ctx , R.style.PostDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.network_dialog);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            networkStateReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info = cm.getActiveNetworkInfo();
                    if (info != null
                            && (info.getState() == NetworkInfo.State.CONNECTED || info.getState() == NetworkInfo.State.CONNECTING)) {
                        Log.v("we are connected", "we are connected");
                    } else {
                        Log.v("Disconnected", "Disconnected");
                        try {
                            networkError(dialog, context);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            };

            ctx.registerReceiver(networkStateReceiver, filter);
        }

    }
    public static boolean checkLocationPermission(Context context)
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /** function unregister the network intent checking **/
    public static void unregisterReceiver(Context ctx){
        if (networkStateReceiver != null) {
            if (dialog != null){
                dialog.cancel();
            }
            dialog = null;
            ctx.unregisterReceiver(networkStateReceiver);
            networkStateReceiver = null;
        }
    }private static void networkError(final Dialog dia, final Context ctx){
        try {

            TextView ok = dia.findViewById(R.id.alert_button);
            TextView cancel = dia.findViewById(R.id.alert_cancel);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null){
                        dialog.cancel();
                    }
                    dialog = null;
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    ctx.startActivity(intent);
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null){
                        dialog.cancel();
                    }
                    dialog = null;
                }
            });
            Log.v("show", "show=" + dia.isShowing());
            dia.show();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (WindowManager.BadTokenException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    // Unregister this account/device pair within the server.
  public   void unregister(final Context context) {
        Log.v("Register_Id","Register_Id="+Constants.REGISTER_ID);
        Log.v("unRegister", "unRegister");


        Thread th = new Thread(new Runnable() {
            public void run() {
                /*String SOAP_ACTION = Constants.NAMESPACE + Constants.API_PUSH_UNREGISTER;

                SoapObject req = new SoapObject(Constants.NAMESPACE, Constants.API_PUSH_UNREGISTER);
                req.addProperty(Constants.SOAP_USERNAME, Constants.SOAP_USERNAME_VALUE);
                req.addProperty(Constants.SOAP_PASSWORD, Constants.SOAP_PASSWORD_VALUE);
                req.addProperty("deviceId", Constants.ANDROID_ID);

                SOAPParsing soap = new SOAPParsing();
                String json = soap.getJSONFromUrl(SOAP_ACTION, req);*/

              //  Log.v("json", "json" + json);
                Constants.REGISTER_ID = "";
            }
        });
        th.start();

        //GCMRegistrar.unregister(context);
    //    GCMRegistrar.setRegisteredOnServer(context, false);
        // At this point the device is unregistered from GCM, but still
        // registered in the our server.
        // We could try to unregister again, but it is not necessary:
        // if the server tries to send a message to the device, it will get
        // a "NotRegistered" error message and should unregister the device.

    }

    public static String loadJSONFromAsset(Context context,String name) {
        String json = null;
        try {

            InputStream is = context.getResources().getAssets().open(name);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.i("Class", info[i].getState().toString());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED || info[i].getState() == NetworkInfo.State.CONNECTING) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /** for closing the keyboard while touch outside **/
    public static void setupUI(Context context,View view) {
        final Activity act = (Activity) context;
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(act);
                    return false;
                }

            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(act,innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context
                    .getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException npe) {

        } catch (Exception e) {

        }
    }

    public static void dialog(Context ctx, String title,String content){
        final Dialog dialog = new Dialog(ctx ,R.style.AlertDialog);
        Display display = ((Activity) ctx).getWindowManager().getDefaultDisplay();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.default_dialog);
        dialog.getWindow().setLayout(display.getWidth()*90/100, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        TextView alertTitle = dialog.findViewById(R.id.alert_title);
        TextView alertMsg = dialog.findViewById(R.id.alert_msg);
        ImageView alertIcon = dialog.findViewById(R.id.alert_icon);
        TextView alertOk = dialog.findViewById(R.id.alert_button);

        alertTitle.setText(title);
        alertMsg.setText(content);
		/*if(title.equals(ctx.getResources().getString(R.string.alert)) || title.equals(ctx.getResources().getString(R.string.error))){
			alertIcon.setImageResource(R.drawable.alert_icon);
		}else if(title.equals(ctx.getResources().getString(R.string.success))){
			alertIcon.setImageResource(R.drawable.success_icon);
		}else if(title.equals(ctx.getResources().getString(R.string.network_error))){
			alertIcon.setImageResource(R.drawable.network_icon);
		}else{
			alertIcon.setVisibility(View.GONE);
		}*/


        alertOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        if(!dialog.isShowing()){
            dialog.show();
        }

    }

    public static boolean isRTL(Context context){
        return context.getResources().getConfiguration().locale.toString().equals("ar");
    }
  /*  public static void disabledialog(final Context ctx, final String content){
        ((Activity) ctx).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Dialog dialog = new Dialog(ctx ,R.style.AlertDialog);
                Display display = ((Activity) ctx).getWindowManager().getDefaultDisplay();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.default_dialog);
                dialog.getWindow().setLayout(display.getWidth()*90/100, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                TextView alertTitle = (TextView) dialog.findViewById(R.id.alert_title);
                TextView alertMsg = (TextView) dialog.findViewById(R.id.alert_msg);
                ImageView alertIcon = (ImageView) dialog.findViewById(R.id.alert_icon);
                TextView alertOk = (TextView) dialog.findViewById(R.id.alert_button);

                //alertTitle.setText(ctx.getResources().getString(R.string.alert));
                alertMsg.setText(content);
                //alertIcon.setImageResource(R.drawable.alert_icon);

                alertOk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try{
                            Constants.editor.clear();
                            Constants.editor.commit();
                            GetSet.reset();
                            FragmentMainActivity.HomeItems.clear();
                            if (FragmentMainActivity.homeAdapter != null){
                                FragmentMainActivity.homeAdapter.notifyDataSetChanged();
                            }
                            FragmentMainActivity.filterAry.clear();
                            FragmentMainActivity.currentPage = 0;
                            WelcomeActivity.fromSignout = true;

                        } catch(NullPointerException e){
                            e.printStackTrace();
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        ((Activity) ctx).finish();
                        Intent intent = new Intent (ctx, WelcomeActivity.class);
                        ((Activity) ctx).startActivity(intent);
                    }
                });

                if(!dialog.isShowing()){
                    dialog.show();
                }

            }
        });

    }*/
    /** function for avoiding emoji typing in keyboard **/
    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };
}
