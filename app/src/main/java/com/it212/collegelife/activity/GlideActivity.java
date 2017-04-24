package com.it212.collegelife.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.it212.collegelife.R;
import com.it212.collegelife.utils.DipUtils;
import com.it212.collegelife.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

public class GlideActivity extends Activity {
    private ViewPager vp_glide;
    private Button btn_toMain;
    private LinearLayout ll_poins;
    private List<ImageView> imageViews;
    private MyVPAdapter adapter;
    private ImageView iv_select_point;
    private int[] ids = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private int leftmax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        btn_toMain = (Button) findViewById(R.id.btn_toMain);
        vp_glide = (ViewPager) findViewById(R.id.vp_glide);
        ll_poins = (LinearLayout) findViewById(R.id.ll_poins);
        iv_select_point = (ImageView) findViewById(R.id.iv_select_point);
        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(GlideActivity.this);
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);
            ImageView point = new ImageView(GlideActivity.this);
            point.setBackgroundResource(R.drawable.point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DipUtils.dp2px(this, 10), DipUtils.dp2px(this, 10));
            point.setLayoutParams(params);
            if (i != 0) {
                params.leftMargin = 10;
            }
            ll_poins.addView(point);
        }
        btn_toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.getInstan().save(SpUtils.IS_OPEN,true);
                startActivity(new Intent(GlideActivity.this, MainActivity.class));
                finish();
            }
        });
        adapter = new MyVPAdapter();
        vp_glide.setAdapter(adapter);
        vp_glide.addOnPageChangeListener(new MyVPChangeListener());
        iv_select_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onGlobalLayout() {
            iv_select_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            leftmax = ll_poins.getChildAt(1).getLeft() - ll_poins.getChildAt(0).getLeft();
        }
    }

    class MyVPChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int leftmargin = position * leftmax + (int) (positionOffset * leftmax);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_select_point.getLayoutParams();
            params.leftMargin = leftmargin;
            iv_select_point.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position) {

            if (position == imageViews.size() - 1) {
                btn_toMain.setVisibility(View.VISIBLE);
            } else {
                btn_toMain.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyVPAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
