package com.pl.universalcopy.xposed;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.KeyEvent;

import com.pl.universalcopy.Constant;

/**
 * Created by penglu on 2017/1/11.
 */

public class XposedKeyUpHandler {

    public static final int LONG_PRESS_DELAY = 500;
    private Context mContext;

    public XposedKeyUpHandler(Context application){
        mContext=application;
    }

    private KeyEvent lastKeyEvent;
    public void onKeyEvent(KeyEvent keyEvent){
        if (lastKeyEvent!=null){
            if ((lastKeyEvent.getKeyCode()==KeyEvent.KEYCODE_VOLUME_DOWN && keyEvent.getKeyCode()==KeyEvent.KEYCODE_VOLUME_UP )
                    ||(keyEvent.getKeyCode()==KeyEvent.KEYCODE_VOLUME_DOWN && lastKeyEvent.getKeyCode()==KeyEvent.KEYCODE_VOLUME_UP )){
                if (keyEvent.getEventTime()-lastKeyEvent.getEventTime()<LONG_PRESS_DELAY){
                    longPressRunnable.run();
                }
            }
        }
        lastKeyEvent=keyEvent;
    }

    public void onKeyLongPress(KeyEvent keyEvent){
        int keyCode =keyEvent.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            longPressRunnable.run();
        }
    }

    Runnable longPressRunnable=new Runnable() {
        @Override
        public void run() {
            //转发到notifyService中，判断是否开启功能，再去触发
            Intent notificationIntent = new Intent();
            notificationIntent.setComponent(new ComponentName(Constant.PACKAGE_NAME,Constant.OUT_SIDE_CALL_ACTIVITY_NAME));
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                mContext.startActivity(notificationIntent);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };
}
