package com.qin.fragment.drawer.history;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
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
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.consume.Consume;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class HistoryNoDataFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_reload)
    TextView mTvReload;
    Unbinder unbinder;
    @BindView(R.id.ll_error)
    LinearLayout mLlError;
    private String mUserid;
    private Dialog mDialog;
    private String mSpUser_id;

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
        View view = inflater.inflate(R.layout.fragment_history_nodata, null, false);
        initDialog();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        mDialog.show();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        accessNet(mUserid, 2018);
    }

    private void accessNet(String userid, int year) {
        OkGo.<String>post(ConstantValues.URL_HISTORYCOST)
                .tag(this)
                .params("user_id", userid)
                .params("year", year)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("nodata", response.body());
                        mDialog.dismiss();
                        mLlError.setVisibility(View.GONE);
                        mToolbar.setVisibility(View.GONE);
                        String body = response.body();
                        Consume consume = parseData(body);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("consume", consume);
                        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        HistoryCenterFragment historyCenterFragment = new HistoryCenterFragment();
                        historyCenterFragment.setArguments(bundle);
                        transaction.replace(R.id.fl_his_nodata, historyCenterFragment);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                transaction.commit();
                            }
                        }, 100);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        try {
                            mToolbar.setVisibility(View.VISIBLE);
                            mLlError.setVisibility(View.VISIBLE);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    private Consume parseData(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Consume.class);
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

    @OnClick(R.id.tv_reload)
    public void onViewClicked() {
        mDialog.show();
        accessNet(mUserid, 2017);
    }
}
