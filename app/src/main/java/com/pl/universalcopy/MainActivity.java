package com.pl.universalcopy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import com.pl.universalcopy.utils.SPHelper;
import com.pl.universalcopy.utils.StatusBarCompat;

public class MainActivity extends BaseActivity {

    private SwitchCompat monitorLongClickSwitch;
    private SwitchCompat monitorClickSwitch;
    private SwitchCompat notifySwitch;
    private SwitchCompat totalSwitchSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarCompat.setupStatusBarView(this, (ViewGroup) getWindow().getDecorView(), true, R.color.colorPrimary);
        setContentView(R.layout.activity_main);
        startNotifyService();

        totalSwitchSwitch= (SwitchCompat) findViewById(R.id.total_switch_switch);
        notifySwitch= (SwitchCompat) findViewById(R.id.notify_switch);
        monitorClickSwitch= (SwitchCompat) findViewById(R.id.key_trigger_switch);
        monitorLongClickSwitch= (SwitchCompat) findViewById(R.id.long_key_trigger_switch);

        findViewById(R.id.total_switch_rl).setOnClickListener(mOnClickListener);
        findViewById(R.id.notify_rl).setOnClickListener(mOnClickListener);
        findViewById(R.id.key_trigger_rl).setOnClickListener(mOnClickListener);
        findViewById(R.id.long_key_trigger_rl).setOnClickListener(mOnClickListener);
        findViewById(R.id.donate_rl).setOnClickListener(mOnClickListener);
        findViewById(R.id.guide_rl).setOnClickListener(mOnClickListener);

        totalSwitchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPHelper.save(Constant.TOTAL_SWITCH,isChecked);
                startNotifyService();
            }
        });
        notifySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPHelper.save(Constant.OPEN_NOTIFY,isChecked);
                startNotifyService();
            }
        });
        monitorClickSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPHelper.save(Constant.MONITOR_CLICK,isChecked);
            }
        });
        monitorLongClickSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPHelper.save(Constant.MONITOR_LONG_CLICK,isChecked);
            }
        });

        totalSwitchSwitch.setChecked(SPHelper.getBoolean(Constant.TOTAL_SWITCH,true));
        notifySwitch.setChecked(SPHelper.getBoolean(Constant.OPEN_NOTIFY,true));
        monitorClickSwitch.setChecked(SPHelper.getBoolean(Constant.MONITOR_CLICK,true));
        monitorLongClickSwitch.setChecked(SPHelper.getBoolean(Constant.MONITOR_LONG_CLICK,true));

    }

    private void startNotifyService() {
        Intent notificationIntent = new Intent(this,NotifyService.class);
        try {
            startService(notificationIntent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            switch (id){
                case R.id.total_switch_rl:
                    totalSwitchSwitch.setChecked(!totalSwitchSwitch.isChecked());
                    break;
                case R.id.notify_rl:
                    notifySwitch.setChecked(!notifySwitch.isChecked());
                    break;
                case R.id.key_trigger_rl:
                    monitorClickSwitch.setChecked(!monitorClickSwitch.isChecked());
                    break;
                case R.id.long_key_trigger_rl:
                    monitorLongClickSwitch.setChecked(!monitorLongClickSwitch.isChecked());
                    break;
                case R.id.donate_rl:
                    startActivity(new Intent(MainActivity.this,DonateActivity.class));
                    break;
                case R.id.guide_rl:
                    startActivity(new Intent(MainActivity.this,HowToUseActivity.class));
                    break;
            }
        }
    };

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }
}
