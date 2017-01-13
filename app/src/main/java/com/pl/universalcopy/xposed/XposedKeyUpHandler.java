package com.pl.universalcopy.xposed;

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
            Intent notificationIntent = new Intent(Constant.UNIVERSAL_COPY_BROADCAST_XP);
            try {
                mContext.sendBroadcast(notificationIntent);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };
}
