package com.pl.universalcopy.utils;

import android.widget.Toast;

import com.pl.universalcopy.UCXPApp;


/**
 * Created by l4656_000 on 2015/12/27.
 */
public class ToastUtil {
    public static void show(String msg){
        Toast.makeText(UCXPApp.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
    public static void show(int rid){
        Toast.makeText(UCXPApp.getInstance(), rid, Toast.LENGTH_SHORT).show();
    }
    public static void showLong(int rid){
        Toast.makeText(UCXPApp.getInstance(), rid, Toast.LENGTH_LONG).show();
    }
}
