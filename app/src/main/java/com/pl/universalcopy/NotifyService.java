package com.pl.universalcopy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.NotificationCompat;

import com.pl.universalcopy.utils.SPHelper;

import static com.pl.universalcopy.Constant.UNIVERSAL_COPY_BROADCAST_XP_DELAY;


/**
 * Created by l4656_000 on 2015/11/9.
 */
public class NotifyService extends Service {
    private static final int ONGOING_NOTIFICATION=10086;
    private Bitmap notifyIcon;
    private boolean isForegroundShow=false;
    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null ;
    }

    @Override
    public void onCreate() {
        //只有在同意使用了以后才开始service
        super.onCreate();
        IntentFilter intentFilter=new IntentFilter(UNIVERSAL_COPY_BROADCAST_XP_DELAY);
        try {
            registerReceiver(mUniversalCopyBR,intentFilter);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        adjustService();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(mUniversalCopyBR);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void adjustService() {
        boolean isForground = SPHelper.getBoolean(Constant.IS_UNIVERSAL_COPY_FOREGROUND, true);
        boolean totalSwitch = SPHelper.getBoolean(Constant.TOTAL_SWITCH, true);
        if (isForground && totalSwitch) {
            startForeground("",true);
        } else {
            stopForeground(true);
            isForegroundShow=false;
        }
    }

    private void startForeground(String msg,boolean isForce){
        if (isForegroundShow){
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if (notifyIcon==null){
            notifyIcon= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        }
        builder.setLargeIcon(notifyIcon);
        // TODO: 2016/11/15
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setWhen(System.currentTimeMillis());
        Intent notificationIntent = new Intent(Constant.UNIVERSAL_COPY_BROADCAST_XP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(getString(R.string.copy_title));
        builder.setContentText(getString(R.string.copy_msg));
        builder.setPriority(NotificationCompat.PRIORITY_MIN);

        Notification notification;
        if (Build.VERSION.SDK_INT<16){
            notification=builder.getNotification();
        }else{
            notification=builder.build();
        }
        startForeground(ONGOING_NOTIFICATION, notification);
        isForegroundShow=true;
    }

    private BroadcastReceiver mUniversalCopyBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (handler==null){
                handler=new Handler(Looper.getMainLooper());
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean totalSwitch = SPHelper.getBoolean(Constant.TOTAL_SWITCH, true);
                    if (totalSwitch) {
                        Intent notificationIntent = new Intent(Constant.UNIVERSAL_COPY_BROADCAST_XP);
                        sendBroadcast(notificationIntent);
                        if (! SPHelper.getBoolean(Constant.IS_UNIVERSAL_COPY_FOREGROUND, true)){
                            stopSelf();
                        }
                    }
                }
            },500);
        }
    };



}
