package com.qin.fragment.drawer.history;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.adapter.recyclerview.MarginDecorationVertical;
import com.qin.adapter.recyclerview.breakhistory.BreakAlarmRecyclerViewAdapter;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.breakalarmhistory.BreakAlarmHistory;
import com.qin.pojo.breakalarmhistory.Result;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class BreakAlarmHistoryfragment extends BaseFragment {

    @BindView(R.id.rlv_history_breakalarm)
    RecyclerView mRlvHistoryBreakalarm;
    @BindView(R.id.tv_nodata)
    TextView mTvNodata;

    private String mUserid;
    private Dialog mDialog;
    private String mSpUser_id;

    private Unbinder unbinder;
    private List<Result> mResultList;
    private BreakAlarmRecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserid = MyApplication.getUserid();
        mSpUser_id = SPUtils.getInstance(mActivity).getString("user_id", "");
        if (mUserid == null) {
            mUserid = mSpUser_id;
        }
    }

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_breakalarm, null, false);
        unbinder = ButterKnife.bind(this, view);
        initDialog();
        mRlvHistoryBreakalarm.setLayoutManager(new LinearLayoutManager(mActivity));
        mRlvHistoryBreakalarm.setItemAnimator(new DefaultItemAnimator());
        mRlvHistoryBreakalarm.addItemDecoration(new MarginDecorationVertical(20));
        return view;
    }

    @Override
    protected void initData() {
        mTvNodata.setVisibility(View.VISIBLE);
        accessNet(mUserid);
    }

    private void accessNet(String userid) {
        mDialog.show();
        OkGo.<String>post(ConstantValues.URL_BREAKAlARMHISTORY)
                .tag(this)
                .params("user_id", userid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("url_breakalarmhistory", response.body());
                        mDialog.dismiss();
                        String body = response.body();
                        parseeData(body);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(mActivity, "网络错误！");
                    }
                });
    }

    private void parseeData(String json) {
        Gson gson = new Gson();
        BreakAlarmHistory history = gson.fromJson(json, BreakAlarmHistory.class);
        String code = history.getCode();
        mResultList = history.getResult();
        if (code.equals("0") && mResultList.size() > 0) {
            mTvNodata.setVisibility(View.GONE);
            mRlvHistoryBreakalarm.setVisibility(View.VISIBLE);
            mAdapter = new BreakAlarmRecyclerViewAdapter(mResultList, mActivity);
            mRlvHistoryBreakalarm.setAdapter(mAdapter);
        }else{
            mTvNodata.setVisibility(View.VISIBLE);
            mRlvHistoryBreakalarm.setVisibility(View.GONE);
        }
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
}
