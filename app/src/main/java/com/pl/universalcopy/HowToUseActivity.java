package com.pl.universalcopy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.universalcopy.utils.StatusBarCompat;


public class HowToUseActivity extends AppCompatActivity {
    public static final String GO_TO_OPEN_FROM_OUTER="go_to_open_from_outer";

    private View introMenu;
    private LinearLayout introContent;

    private TextView introTitle;
    private TextView introMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);

        StatusBarCompat.setupStatusBarView(this, (ViewGroup) getWindow().getDecorView(), true, R.color.colorPrimary);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.introduction);

        introMenu= (View) findViewById(R.id.intro_menu);
        introContent= (LinearLayout) findViewById(R.id.intro_content);

        introTitle = (TextView) findViewById(R.id.intro_title);
        introMsg = (TextView) findViewById(R.id.intro_msg);


        findViewById(R.id.overall_intro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introMenu.setVisibility(View.GONE);
                introContent.setVisibility(View.VISIBLE);
                introTitle.setText(R.string.overall_intro);
                introMsg.setText(R.string.overall_intro_msg);
            }
        });


        findViewById(R.id.about_universal_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introMenu.setVisibility(View.GONE);
                introContent.setVisibility(View.VISIBLE);
                introTitle.setText(R.string.about_universal_copy);
                introMsg.setText(R.string.about_universal_copy_msg);
            }
        });

        findViewById(R.id.about_xposed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introMenu.setVisibility(View.GONE);
                introContent.setVisibility(View.VISIBLE);
                introTitle.setText(R.string.about_xposed);
                introMsg.setText(R.string.about_xposed_msg);
            }
        });

        findViewById(R.id.open_from_outside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introMenu.setVisibility(View.GONE);
                introContent.setVisibility(View.VISIBLE);
                introTitle.setText(R.string.open_from_outside);
                introMsg.setText(R.string.open_from_outside_msg);
            }
        });



        Intent intent = getIntent();
        boolean goToOpenFromOuter=intent.getBooleanExtra(GO_TO_OPEN_FROM_OUTER,false);
        if (goToOpenFromOuter){
            findViewById(R.id.open_from_outside).performClick();
        }
    }


    @Override
    public void onBackPressed() {
        if (introMenu.getVisibility()==View.VISIBLE){
            super.onBackPressed();
        }else {
            introMenu.setVisibility(View.VISIBLE);
            introContent.setVisibility(View.GONE);
        }
    }
}
