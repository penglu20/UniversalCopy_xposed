package com.pl.universalcopy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WakeUpBR extends BroadcastReceiver {
    public static final String UNIVERSAL_COPY_WAKE_UP_ACTION="universal_copy_wake_up_action";
    public WakeUpBR() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(UNIVERSAL_COPY_WAKE_UP_ACTION)){
            Intent intent1=new Intent(context, NotifyService.class);
            try {
                context.startService(intent1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
