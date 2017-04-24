package com.it212.collegelife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.it212.collegelife.base.BaseFragment;

import java.util.List;

/**
 * Created by imac on 2017/4/8.
 */

public class MySelfAdapter extends FragmentPagerAdapter {

    private  List<BaseFragment> baseFragments;
    private  String[] titles;

    public MySelfAdapter(FragmentManager fm, List<BaseFragment> baseFragments, String[] titles) {
        super(fm);
       this.baseFragments =baseFragments;
       this.titles =titles;
    }

    @Override
    public Fragment getItem(int position) {
        return baseFragments.get(position);
    }

    @Override
    public int getCount() {
        return baseFragments.size();
    }




    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
