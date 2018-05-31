package com.app.starautoassist.Others;

import android.content.SharedPreferences;
import android.provider.Settings;

public class Constants {

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    /** FaceBook App id **/
    public static final String App_ID = "526541597734018";

    //Keywords
    public static String mobileno="mobileno";
    public static String password="password";
    public static String otp="otp";
    public static String firstname="firstname";
    public static String lastname="lastname";
    public static String email="email";

    public static String serviceid="serviceid";
    public static String servicename="servicename";
    public static String serviceimg="serviceimgurl";
    public static String servicecharge="servicecharge";
    public static String priceperkm="priceperkm";

    /** Twitter App id **/
    public static final String CONSUMER_KEY = "xsAxHRyZ3AoycrRpeK9F1rFqs";
    public static final String CONSUMER_SECRET = "BjqT5aGwG4lvY4sOCyT8fO61kNvw0yH07HLUTVax3nVxbbT9fH";

    /** Push notification key **/
    public static final String SENDER_ID = "24023470057";
    public static String REGISTER_ID="";
    public static String ANDROID_ID="";
    // PUSH API KEY AIzaSyCrpkOI5Trgk_GS7fqnE3o3I9ua7NXdXoU //
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "ah_firebase";

    //Requests
    //public static String BaseURL="http://starautoassist.com/services/";
    public static String BaseURL="http://towbago.shadowws.in/jsons/";
    public static String uploadimage="uploadimage";
    public static String login="login.php?";
    public static String getOTP="getotp.php?";
    public static String verifyOTP="verifyotp.php?";
    public static String profile="profile.php?";
    public static String registration="registration.php?";
    public static String getprofile="getprofile.php?";
    public static String getservices="getservices.php?";
    public static String uploadprofileimage="uploadprofileimage.php?";
    public static String changepassword="changepassword.php?";
    public static String forgotpassword="forgotpassword.php?";





}
