package com.example.imagesaver.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class ImageSaverPreferences {

    public static void setPreferencesNameLocation(Context context, String name, String location){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name);
        editor.putString("location", location);
        editor.apply();
    }

    public static String getPreferencesName(Context context){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedPref.getString("name", "");
    }

    public static String getPreferenceLocation(Context context){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedPref.getString("location", "");
    }

    public static void setPrefSupplierName(Context context, String name){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("supplierName", name);
        editor.apply();
    }

    public static String getPrefSupplierName(Context context){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedPref.getString("supplierName", "");
    }

    public static void setPrefSupplierList(Context context, String list){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("supplierList", list);
        editor.apply();
    }

    public static String getPrefSupplierList(Context context){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedPref.getString("supplierList", "");
    }

    public static void setShippingMethod(Context context, String method){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("shippingMethod", method);
        editor.apply();
    }

    public static String getShippingMethod(Context context){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedPref.getString("shippingMethod", "");
    }

    public static void setShippingType(Context context, String method){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("shippingType", method);
        editor.apply();
    }

    public static String getShippingType(Context context){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedPref.getString("shippingType", "");
    }

    public static void setCategory(Context context, String category){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("category", category);
        editor.apply();
    }

    public static String getCategory(Context context){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedPref.getString("category", "");
    }

    public static void setFamily(Context context, String family){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("family", family);
        editor.apply();
    }

    public static String getFamily(Context context){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedPref.getString("family", "");
    }

    public static void setSubFamily(Context context, String subfamily){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("subFamily", subfamily);
        editor.apply();
    }

    public static String getSubFamily(Context context){
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedPref.getString("subFamily", "");
    }

}
