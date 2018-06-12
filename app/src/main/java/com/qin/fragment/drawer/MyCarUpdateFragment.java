package com.qin.fragment.drawer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.mycar.MyCarInfo;
import com.qin.pojo.mycar.MyCarInfoUpdate;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class MyCarUpdateFragment extends BaseFragment {

    @BindView(R.id.user_engine)
    EditText mUserEngine;
    @BindView(R.id.user_chepai)
    EditText mUserChepai;
    @BindView(R.id.user_producer)
    EditText mUserProducer;
    @BindView(R.id.user_chejia)
    EditText mUserChejia;
    @BindView(R.id.user_model_name)
    EditText mUserModelName;
    @BindView(R.id.user_dealer)
    EditText mUserDealer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_alter)
    Button mBtnAlter;
    @BindView(R.id.iv_mycar_plate_update)
    ImageView mIvMycarPlateUpdate;
    private String mUserid;
    private Dialog mDialog;
    private Unbinder unbinder;
    private String mSpUser_id;

    @Override
    protected void initData() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mActivity.finish();
                getFragmentManager().popBackStack();
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
        View view = inflater.inflate(R.layout.fragment_mycarupdate, null, false);
        initDialog();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void accessNet(String userid) {
        mDialog.show();
        OkGo.<String>post(ConstantValues.URL_MYCARUPDATEPIC)
                .tag(this)
                .params("user_id", userid)
                .params("register_pic", userid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("Personalinfo", response.body());
                        mDialog.dismiss();
                        String body = response.body();
                        //TODO 处理服务器返回的信息\
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            parseeData(body);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        MyCarInfo myCarInfo = gson.fromJson(json, MyCarInfo.class);
        mUserChejia.setText(myCarInfo.getUser_chejia());
        mUserChepai.setText(myCarInfo.getUser_chepai());
        mUserDealer.setText(myCarInfo.getUser_dealer());
        mUserEngine.setText(myCarInfo.getUser_engine());
        mUserModelName.setText(myCarInfo.getUser_model_name());
        mUserProducer.setText(myCarInfo.getUser_producer());
    }

    private void accessNetAlter(String userid) {
        mDialog.show();

        Gson gson = new Gson();
        MyCarInfoUpdate myCarInfoUpdate = new MyCarInfoUpdate();
        myCarInfoUpdate.setUser_id(mUserid);
        myCarInfoUpdate.setUser_chejia(mUserChejia.getText().toString());
        myCarInfoUpdate.setUser_chepai(mUserChepai.getText().toString());
        myCarInfoUpdate.setUser_dealer(mUserDealer.getText().toString());
        myCarInfoUpdate.setUser_producer(mUserProducer.getText().toString());
        myCarInfoUpdate.setUser_engine(mUserEngine.getText().toString());
        myCarInfoUpdate.setUser_model_name(mUserModelName.getText().toString());
        String toJson = gson.toJson(myCarInfoUpdate);

        OkGo.<String>post(ConstantValues.URL_MYCARUUPDATE)
                .tag(this)
                .upJson(toJson)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("Personalinfo", response.body());
                        mDialog.dismiss();
                        String body = response.body();
                        //TODO 处理服务器返回的信息\
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            String code = jsonObject.getString("code");
                            Log.i("MyCarUpdateFragment", "code======" + code);
                            if (code.equals("0")) {
                                 accessNet(mUserid);
                                ToastUtils.showBgResource(mActivity, "修改成功！");
                                // getFragmentManager().popBackStack();
                            } else {
                                ToastUtils.showBgResource(mActivity, "修改失败！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(mActivity, "网络错误！");
                    }
                });
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

    @OnClick({R.id.btn_alter, R.id.iv_mycar_plate_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_alter:
                mUserChejia.getText().toString();
                mUserChepai.getText().toString();
                mUserDealer.getText().toString();
                mUserEngine.getText().toString();
                mUserModelName.getText().toString();
                mUserProducer.getText().toString();
                accessNetAlter(mUserid);
                break;
            case R.id.iv_mycar_plate_update:

                break;
        }
    }
}
