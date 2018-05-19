package com.app.starautoassist.Helper;

public class GetSet {
    private static boolean isLogged = false;
    private static String Email = null;
    private static String Password = null;
    private static String imageUrl = null;
    private static String userId = null;
    private static String userName = null;
    private static String Firstname = null;
    private static String Lastname = null;

    public static String getServices() {
        return services;
    }

    public static void setServices(String services) {
        GetSet.services = services;
    }

    private static String services = null;

    public static String getCompanyname() {
        return Companyname;
    }

    public static void setCompanyname(String companyname) {
        Companyname = companyname;
    }

    public static String getLicenceimg() {
        return Licenceimg;
    }

    public static void setLicenceimg(String licenceimg) {
        Licenceimg = licenceimg;
    }

    public static String getStreet() {
        return street;
    }

    public static void setStreet(String street) {
        GetSet.street = street;
    }

    public static String getArea() {
        return area;
    }

    public static void setArea(String area) {
        GetSet.area = area;
    }

    public static String getLat() {
        return lat;
    }

    public static void setLat(String lat) {
        GetSet.lat = lat;
    }

    public static String getLon() {
        return lon;
    }

    public static void setLon(String lon) {
        GetSet.lon = lon;
    }

    private static String Companyname = null;
    private static String Licenceimg = null;
    private static String street = null;
    private static String area = null;
    private static String lat = null;
    private static String lon = null;

    public static String getUser_type() {
        return User_type;
    }

    public static void setUser_type(String user_type) {
        User_type = user_type;
    }

    private static String User_type = null;

    public static String getMobileno() {
        return Mobileno;
    }

    public static void setMobileno(String mobileno) {
        Mobileno = mobileno;
    }

    private static String Mobileno = null;


    public static String getFirstname() {
        return Firstname;
    }

    public static void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public static String getLastname() {
        return Lastname;
    }

    public static void setLastname(String lastname) {
        Lastname = lastname;
    }



    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        GetSet.userId = userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        GetSet.userName = userName;
    }


    public static boolean isIsLogged() {
        return isLogged;
    }

    public static void setIsLogged(boolean isLogged) {
        GetSet.isLogged = isLogged;
    }

    public static String getEmail() {
        return Email;
    }

    public static void setEmail(String email) {
        Email = email;
    }

    public static String getPassword() {
        return Password;
    }

    public static void setPassword(String password) {
        Password = password;
    }

    public static String getImageUrl() {
        return imageUrl;
    }

    public static void setImageUrl(String imageUrl) {
        GetSet.imageUrl = imageUrl;
    }



    public static void reset() {
        GetSet.setIsLogged(false);
        GetSet.setEmail(null);
        GetSet.setPassword(null);
        GetSet.setUserId(null);
        GetSet.setUserName(null);
        GetSet.setImageUrl(null);
    }
}
