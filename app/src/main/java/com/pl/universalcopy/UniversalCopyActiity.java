package com.pl.universalcopy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UniversalCopyActiity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendBroadcast(new Intent(Constant.UNIVERSAL_COPY_BROADCAST_XP_DELAY));
        finish();
        return;
    }
}
