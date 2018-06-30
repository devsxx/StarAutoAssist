package com.app.starautoassist.Others;

import android.content.SharedPreferences;
import android.provider.Settings;

public class Constants {

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    /** FaceBook App id **/
    public static final String App_ID = "203876700441500";

    //Keywords
    public static String mobileno="mobileno";
    public static String password="password";
    public static String userimage="userimage";
    public static String otp="otp";
    public static String type="type";
    public static String firstname="firstname";
    public static String lastname="lastname";
    public static String status="status";
    public static String timestamp="timestamp";
    public static String email="email";
    public static String id="id";
    public static String cno="cno";
    public static String model="model";
    public static String brand="brand";
    public static String plateno="plateno";
    public static String logo="logo";

    public static String serviceid="serviceid";
    public static String servicename="servicename";
    public static String service_name="service_name";
    public static String serviceprovider_id="serviceprovider_id";
    public static String companyname="companyname";
    public static String address="address";
    public static String providerimage="providerimage";
    public static String service_description="service_description";
    public static String overall_rating="overall_rating";
    public static String client_id="client_id";
    public static String rating="rating";
    public static String review="review";
    public static String serviceimg="serviceimgurl";
    public static String servicecharge="serviceamount";
    public static String priceperkm="priceperkm";
    public static String Requested_Time="reqtime";

    /** Twitter App id **/
    public static final String CONSUMER_KEY = "xsAxHRyZ3AoycrRpeK9F1rFqs";
    public static final String CONSUMER_SECRET = "BjqT5aGwG4lvY4sOCyT8fO61kNvw0yH07HLUTVax3nVxbbT9fH";

    /** Push notification key **/
    public static final String SENDER_ID = "876306079294";
    public static String REGISTER_ID="";
    public static String ANDROID_ID="";
    // PUSH API KEY AIzaSyBMsq7PBaRZpNP_0B27cuKtC0hfZK5kuBs //
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
    public static String uploadimage="uploadprofileimage.php?";
    public static String addcar="addcar.php?";
    public static String delcar="delcar.php?";
    public static String getcars="get_car_list.php?";
    public static String getmycars="getmycars_list.php?";
    public static String login="login.php?";
    public static String send_response_to_sp ="send_response_to_sp.php?";
    public static String cancel_req ="cancel_req.php?";
    public static String getOTP="getotp.php?";
    public static String getSocialOTP="socialotp.php?";
    public static String verifyOTP="verifyotp.php?";
    public static String profile="profile.php?";
    public static String registration="registration.php?";
    public static String socialreg="socialreg.php?";
    public static String registration_pushid="FCMRegistration.php?";
    public static String getprofile="getprofile.php?";
    public static String getservices="getservices.php?";
    public static String get_req_status="get_req_status.php?";
    public static String get_accepted_list="get_accepted_request.php?";
    public static String get_fuel_request="getfueldetails.php?";
    public static String send_req_towing="send_request_towing.php?";
    public static String send_req_flattyre="send_request_tyre.php?";
    public static String send_req_jumpstart="send_request_jumpstart.php?";
    public static String send_req_fuel="send_request_fuel.php?";
    public static String uploadprofileimage="uploadprofileimage.php?";
    public static String changepassword="changepassword.php?";
    public static String forgotpassword="forgotpassword.php?";
    public static Boolean isLocationpermission_enabled=false;





}
