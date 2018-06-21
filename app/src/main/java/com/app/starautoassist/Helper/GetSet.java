package com.app.starautoassist.Helper;

public class GetSet {
    private static boolean isLogged = false;
    private static String Email = null;
    private static String Password = null;
    private static String imageUrl = null;
    private static String clientid = null;

    public static String getBrand() {
        return brand;
    }

    public static void setBrand(String brand) {
        GetSet.brand = brand;
    }

    public static String getModel() {
        return model;
    }

    public static void setModel(String model) {
        GetSet.model = model;
    }

    public static String getPlatenot() {
        return platenot;
    }

    public static void setPlatenot(String platenot) {
        GetSet.platenot = platenot;
    }

    private static String brand = null;
    private static String model = null;
    private static String platenot = null;

    private static String imagename = null;
    private static String userId = null;
    private static String userName = null;
    private static String Firstname = null;
    private static String Lastname = null;

    private static String Companyname = null;
    private static String Licenceimg = null;
    private static String address = null;



    public static String getImagename() {
        return imagename;
    }

    public static void setImagename(String imagename) {
        GetSet.imagename = imagename;
    }

    public static String getSocialimg() {
        return socialimg;
    }

    public static void setSocialimg(String socialimg) {
        GetSet.socialimg = socialimg;
    }

    private static String socialimg = null;

    public static String getClientid() {
        return clientid;
    }

    public static void setClientid(String clientid) {
        GetSet.clientid = clientid;
    }



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

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        GetSet.address = address;
    }




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
