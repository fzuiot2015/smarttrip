package com.qin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qin.R;
import com.qin.adapter.recyclerview.MainCarControlRecyclerViewAdapter;
import com.qin.adapter.recyclerview.MarginDecorationVertical;
import com.qin.pojo.carcontrol.Lists;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class CarControlMoreActivity extends AppCompatActivity {
    @BindView(R.id.rlv_main_carcontrolmore)
    RecyclerView rlvMainCarcontrolmore;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<Lists> mArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carcontrolmore);
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra("title");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mArrayList = getIntent().getParcelableArrayListExtra("CARCONTROL");
        rlvMainCarcontrolmore.setLayoutManager(new LinearLayoutManager(this));
        rlvMainCarcontrolmore.setItemAnimator(new DefaultItemAnimator());
        rlvMainCarcontrolmore.addItemDecoration(new MarginDecorationVertical(20));
        MainCarControlRecyclerViewAdapter adapter = new MainCarControlRecyclerViewAdapter(mArrayList, this);
        rlvMainCarcontrolmore.setAdapter(adapter);
        adapter.setOnItemClickListener(new MainCarControlRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("URL", mArrayList.get(position).getUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(CarControlMoreActivity.this, CarControlWebActivity.class);
                startActivity(intent);
            }
        });
    }
}
