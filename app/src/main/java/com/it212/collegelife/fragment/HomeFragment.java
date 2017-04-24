package com.it212.collegelife.fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.it212.collegelife.R;
import com.it212.collegelife.activity.MainActivity;
import com.it212.collegelife.base.BaseFragment;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * Created by imac on 2017/4/1.
 */
public class HomeFragment extends BaseFragment {

    private RadioGroup rg_home;

    private MainActivity mainActivity;
    private HSchool hSchool;
    private HNews hNews;
    private FragmentTransaction ft;

    @Override
    public View initView() {
        mainActivity= (MainActivity) context;
      View view=View.inflate(mainActivity, R.layout.homef,null);
        rg_home= (RadioGroup) view.findViewById(R.id.rg_home);
        hSchool = new HSchool();
        hNews = new HNews();
        rg_home.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ft = mainActivity.getSupportFragmentManager().beginTransaction();
                switch (checkedId){
                    case R.id.rb_school:
                        ft.replace(R.id.fl_home,hSchool,"");
                        break;
                    case R.id.rb_wx:
                        ft.replace(R.id.fl_home,hNews,"");
                        break;
                }
                ft.commit();
            }
        });
        rg_home.check(R.id.rb_school);
        return view;
    }

    private void initFragmert() {


    }

    @Override
    public void initData() {
        super.initData();


    }

    private void load() {
        RequestParams parmanx=new RequestParams("http://apicloud.mob.com/wx/article/search?page=1&cid=1&key=1caaf26d08b11&size=20");
        x.http().get(parmanx, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("666","result="+result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("ffff","-----------");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
