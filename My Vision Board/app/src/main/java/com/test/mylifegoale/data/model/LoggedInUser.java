package com.test.mylifegoale.data.model;

import com.google.android.gms.common.data.DataHolder;
import com.test.mylifegoale.data.APIService;

public class LoggedInUser {
    private static String userName;
    private static String id;
    private static String fullName;
    private static String email;
    private static Boolean isVerified;

    public static void setUserName(String userName) { LoggedInUser.userName = userName;}
    public static String getUserName() { return userName; }

    public static void setId(String id) { LoggedInUser.id = id;}
    public static String getUserId() { return id; }

    public static void setUserFullName(String fullName) { LoggedInUser.fullName = fullName;}
    public static String getUserFullName() { return fullName; }


    public static void setUserEmail(String email) { LoggedInUser.email = email;}
    public static String getUserEmail() { return email; }

    public static void setUserVerifiedStatus(Boolean isVerified) { LoggedInUser.isVerified = isVerified;}
    public static Boolean getUserVerifiedStatus() { return isVerified; }
}