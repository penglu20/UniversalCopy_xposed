package com.pl.universalcopy.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.pl.universalcopy.Constant;
import com.pl.universalcopy.UCXPApp;

import java.util.Set;


public class SPHelper {

    private static Context mContext = UCXPApp.getInstance();
    private static final String MAINSPNAME= Constant.SP_NAME;

    private static SharedPreferences getSP(String tagName) {
        return mContext.getSharedPreferences(MAINSPNAME, Context.MODE_PRIVATE );
    }

    public synchronized static void save(String name, Boolean t) {
        SharedPreferences sp = getSP(name);
        if (sp == null) {
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(name, t);
            editor.commit();
        }
    }

    public synchronized static void save(String name, String t) {
        SharedPreferences sp = getSP(name);
        if (sp == null) {
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(name, t);
            editor.commit();
        }
    }

    public synchronized static void save(String name, Integer t) {
        SharedPreferences sp = getSP(name);
        if (sp == null) {
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(name, t);
            editor.commit();
        }
    }

    public synchronized static void save(String name, Long t) {
        SharedPreferences sp = getSP(name);
        if (sp == null) {
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(name, t);
            editor.commit();
        }
    }

    public synchronized static void save(String name, Float t) {
        SharedPreferences sp = getSP(name);
        if (sp == null) {
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat(name, t);
            editor.commit();
        }
    }

    public static String getString(String name, String defaultValue) {
        SharedPreferences sp = getSP(name);
        return sp.getString(name, defaultValue);
    }

    public static int getInt(String name, int defaultValue) {
        SharedPreferences sp = getSP(name);
            return sp.getInt(name, defaultValue);
    }

    public static float getFloat(String name, float defaultValue) {
        SharedPreferences sp = getSP(name);
            return sp.getFloat(name, defaultValue);
    }

    public static boolean getBoolean(String name, boolean defaultValue) {
        SharedPreferences sp = getSP(name);
            return sp.getBoolean(name, defaultValue);
    }

    public static long getLong(String name, long defaultValue) {
        SharedPreferences sp = getSP(name);
            return sp.getLong(name, defaultValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getStringSet(String name, Set<String> defaultValue) {
        SharedPreferences sp = getSP(name);
        return sp.getStringSet(name, defaultValue);
    }

    public static boolean contains(String name) {
        SharedPreferences sp = getSP(name);
            return sp.contains(name);
    }

    public static void remove(String name) {
        SharedPreferences sp = getSP(name);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(name);
            editor.commit();
    }

    public static void clear(){
        SharedPreferences sp = getSP(null);
        if (sp == null) {
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
        }
    }
}