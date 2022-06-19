package com.example.myapplication.session;

import android.app.Application;

public class SessionManager extends Application {

    public static  String getToken() {
        return token;
    }

    public static void setToken(String token) {
        SessionManager.token = token;
    }

    static String token;

    public static int getStatusCode() {
        return statusCode;
    }

    public static void setStatusCode(int statusCode) {
        SessionManager.statusCode = statusCode;
    }

    static int statusCode;
}
