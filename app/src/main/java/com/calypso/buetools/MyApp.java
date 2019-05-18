package com.calypso.buetools;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApp extends Application {
    private static RequestQueue mQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyApp","Application--onCreate()");
         mQueue = Volley.newRequestQueue(this.getApplicationContext());
    }

    public static RequestQueue getRequestQueue(){
         return  mQueue;
    }
}
