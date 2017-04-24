package com.it212.collegelife.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.it212.collegelife.R;
import com.it212.collegelife.adapter.InvateAdapter;
import com.it212.collegelife.domain.InvationInfo;
import com.it212.collegelife.utils.Constant;
import com.it212.collegelife.utils.Model;

import java.util.List;

public class InvationActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout srl_invite;
    private ListView lv_invati;
    private LocalBroadcastManager manager;
    private InvateAdapter adapter;
    private BroadcastReceiver contcactChagedReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invation);
        srl_invite = (SwipeRefreshLayout) findViewById(R.id.srl_invite);
        lv_invati = (ListView) findViewById(R.id.lv_invati);
        srl_invite.setOnRefreshListener(this);
        adapter=new InvateAdapter();
        lv_invati.setAdapter(adapter);
        refresh();
        manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(contcactChagedReceiver,new IntentFilter(Constant.CONTACT_INVITE_CHANGED));

    }

    private void refresh() {

    }

    @Override
    public void onRefresh() {
        srl_invite.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.unregisterReceiver(contcactChagedReceiver);
    }
}
