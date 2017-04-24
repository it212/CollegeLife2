package com.it212.collegelife.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;



import com.it212.collegelife.R;
import com.it212.collegelife.utils.Model;
import com.it212.collegelife.utils.SpUtils;

public class SplashActivity extends Activity {

    private boolean isTurnTo;
    private boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isOpen = SpUtils.getInstan().getBoolean(SpUtils.IS_OPEN,false);
        Model.getInstance().getClobeThreadPool().execute(new Runnable() {
            @Override
            public void run() {


            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOpen) {
                    turnTomain();
                } else {
                    startActivity(new Intent(SplashActivity.this, GlideActivity.class));
                    finish();
                }
            }
        }, 2000);
    }

    private void turnTomain() {
        if (!isTurnTo) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            isTurnTo = true;
            finish();
        }
    }


}
