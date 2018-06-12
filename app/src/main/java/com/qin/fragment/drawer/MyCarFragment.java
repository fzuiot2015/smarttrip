package com.qin.fragment.drawer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.activity.nearby.UpdateMyCarInfoActivity;
import com.qin.adapter.recyclerview.MarginDecorationVertical;
import com.qin.adapter.recyclerview.drawer.MyCarBreakRuleRecyclerViewAdapter;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.mycar.Illegal;
import com.qin.pojo.mycar.MyCarInfo;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class MyCarFragment extends BaseFragment {
    @BindView(R.id.tv_mycar_alter)
    TextView mTvMycarAlter;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.user_engine)
    TextView mUserEngine;
    @BindView(R.id.user_chepai)
    TextView mUserChepai;
    @BindView(R.id.user_producer)
    TextView mUserProducer;
    @BindView(R.id.user_chejia)
    TextView mUserChejia;
    @BindView(R.id.user_model_name)
    TextView mUserModelName;
    @BindView(R.id.user_dealer)
    TextView mUserDealer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView_mycar_breakrule)
    RecyclerView mRecyclerViewMycarBreakrule;
    @BindView(R.id.iv_mycar_plate)
    ImageView mIvMycarPlate;
    @BindView(R.id.ll_nodata)
    LinearLayout mLlNodata;
    private String mUserid;
    private Dialog mDialog;
    private String mSpUser_id;
    private List<Illegal> mIllegalList;
    private MyCarBreakRuleRecyclerViewAdapter mAdapter;

    @Override
    protected void initData() {
        mRefreshLayout.setVisibility(View.GONE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        accessNet(mUserid);
    }

    @Override
    public View initView() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserid = MyApplication.getUserid();
        mSpUser_id = SPUtils.getInstance(mActivity).getString("user_id", "");
        if (mUserid == null) {
            mUserid = mSpUser_id;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycar, null, false);
        unbinder = ButterKnife.bind(this, view);
        initDialog();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerViewMycarBreakrule.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewMycarBreakrule.addItemDecoration(new MarginDecorationVertical(20));
        mRecyclerViewMycarBreakrule.setLayoutManager(layoutManager);
        return view;
    }

    private void accessNet(String userid) {
        mDialog.show();
        OkGo.<String>post(ConstantValues.URL_MYCAR)
                .tag(this)
                .params("user_id", userid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("Carsonalinfo", response.body());
                        mDialog.dismiss();
                        mRefreshLayout.setVisibility(View.VISIBLE);
                        mLlNodata.setVisibility(View.GONE);
                        String body = response.body();
                        parseeData(body);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        mRefreshLayout.setVisibility(View.GONE);
                        mLlNodata.setVisibility(View.VISIBLE);
                        ToastUtils.showBgResource(mActivity, "网络错误！");
                    }
                });
    }

    private void parseeData(String json) {
        Gson gson = new Gson();
        MyCarInfo myCarInfo = gson.fromJson(json, MyCarInfo.class);
        String default_string = "无";
        String user_chejia = myCarInfo.getUser_chejia();
        if (user_chejia.equals(null) || user_chejia.equals("")) {
            mUserChejia.setText("车 架 号 ： " + default_string);
        } else {
            mUserChejia.setText("车 架 号 ： " + user_chejia);
        }
        String user_chepai = myCarInfo.getUser_chepai().trim();
        if (user_chepai.equals("")) {
            mUserChepai.setText("车 牌 号 ： " + default_string);
        } else {
            mUserChepai.setText("车 牌 号 ： " + user_chepai);
        }

        String user_dealer = myCarInfo.getUser_dealer().trim();

        if (user_dealer.equals("")) {
            mUserDealer.setText("经 销 商 ： " + default_string);
        } else {
            mUserDealer.setText("经 销 商 ： " + user_dealer);
        }
        String user_engine = myCarInfo.getUser_engine().trim();

        if (user_engine.equals("")) {
            mUserEngine.setText("发动机引擎 ： " + default_string);
        } else {
            mUserEngine.setText("发动机引擎 ： " + user_engine);
        }
        String user_model_name = myCarInfo.getUser_model_name().trim();

        if (user_model_name.equals("")) {
            mUserModelName.setText("车     型 ： " + default_string);
        } else {
            mUserModelName.setText("车     型 ： " + user_model_name);
        }
        String user_producer = myCarInfo.getUser_producer();

        if (user_producer.equals("")) {
            mUserProducer.setText(" 生 厂 商 ： " + default_string);
        } else {
            mUserProducer.setText(" 生 厂 商 ： " + user_producer);
        }
        Glide.with(mActivity).load(myCarInfo.getRegister_pic()).placeholder(R.mipmap.gas_blank).into(mIvMycarPlate);
        mIllegalList = myCarInfo.getIllegal();
        mAdapter = new MyCarBreakRuleRecyclerViewAdapter(mIllegalList);
        mRecyclerViewMycarBreakrule.setAdapter(mAdapter);
    }

    private void initDialog() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(mActivity), ScreenUtils.getWindowHeight(mActivity));
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        mDialog = new Dialog(mActivity);
        View view = View.inflate(mActivity, R.layout.dialog_loading, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_mycar_alter)
    public void onViewClicked() {
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.fl_drawer, new MyCarUpdateFragment());
//        fragmentTransaction.addToBackStack("mycarupdate");
//        fragmentTransaction.commit();

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(mActivity, UpdateMyCarInfoActivity.class);
        startActivityForResult(intent, 100);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10) {
            accessNet(mUserid);
        }
    }
}
