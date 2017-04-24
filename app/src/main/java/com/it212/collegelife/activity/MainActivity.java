package com.it212.collegelife.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.it212.collegelife.R;
import com.it212.collegelife.fragment.HomeFragment;
import com.it212.collegelife.fragment.LifeFragment;
import com.it212.collegelife.fragment.MyselfFragment;
import com.it212.collegelife.fragment.ServiceFragment;


public class MainActivity extends FragmentActivity {

    private boolean exit;
    private RadioGroup rg_main_menu;
    private HomeFragment homeF;
    private LifeFragment lifeF;
    private ServiceFragment serviceF;
    private MyselfFragment myselfF;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        homeF = new HomeFragment();
        lifeF = new LifeFragment();
        serviceF = new ServiceFragment();
        myselfF = new MyselfFragment();
        rg_main_menu = (RadioGroup) findViewById(R.id.rg_main_menu);
        rg_main_menu.setOnCheckedChangeListener(new MyListener());

        rg_main_menu.check(R.id.rb_home);
    }

    class MyListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (checkedId) {
                case R.id.rb_home:
                    ft.replace(R.id.fl_content, homeF, "homeF");
                    break;
                case R.id.rb_life:
                    ft.replace(R.id.fl_content, lifeF, "homeF");
                    break;
                case R.id.rb_service:
                    ft.replace(R.id.fl_content, serviceF, "serviceF");
                    break;

                case R.id.rb_myself:
                    ft.replace(R.id.fl_content, myselfF, "homeF");
                    break;
            }
                ft.commit();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x001) {
                exit = false;
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (!exit) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                exit = true;
                handler.sendEmptyMessageDelayed(0x001, 2000);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

}
