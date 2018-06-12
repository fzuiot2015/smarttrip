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
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.personalinfo.PersonalInfoUpdate;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class PersonalInfoUpdateFragment extends BaseFragment {
    @BindView(R.id.et_personalInfo)
    EditText mEtPersonalInfo;
    @BindView(R.id.btn)
    Button mBtn;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String mUserid;
    private Dialog mDialog;
    private String mSpUser_id;
    Unbinder unbinder;

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
        View view = inflater.inflate(R.layout.fragment_personalinfoupdate, null, false);
        unbinder = ButterKnife.bind(this, view);
        initDialog();

        return view;
    }

    private void accessNet(String userid) {
        mDialog.show();
        OkGo.<String>post(ConstantValues.URL_PERSONINFO)
                .tag(this)
                .params("user_id", userid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("Personalinfo", response.body());
                        mDialog.dismiss();
                        //TODO 处理服务器返回的信息\
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String user_name = jsonObject.getString("user_name");
                            mEtPersonalInfo.setText(user_name);
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

    private void accessNetAlter(String userid,String name) {
        mDialog.show();
        Gson gson = new Gson();
        PersonalInfoUpdate peronalInfoUpdate = new PersonalInfoUpdate();
        peronalInfoUpdate.setUser_id(userid);
        peronalInfoUpdate.setUser_name(mEtPersonalInfo.getText().toString().trim());
        String toJson = gson.toJson(peronalInfoUpdate);

        OkGo.<String>post(ConstantValues.URL_PERSONINFOUPDATE)
                .tag(this)
//                .params("user_id", userid)
//                .params("user_name", name)
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
                            Log.i("psersonalinfo", "code======" + code);
                            if (code.equals("0")) {
                                // accessNet(mUserid);
                                ToastUtils.showBgResource(mActivity, "修改成功！");
                                // getFragmentManager().popBackStack();
                            }else{
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

    @OnClick(R.id.btn)
    public void onViewClicked() {
        accessNetAlter(mUserid,mEtPersonalInfo.getText().toString().trim());
    }
}
