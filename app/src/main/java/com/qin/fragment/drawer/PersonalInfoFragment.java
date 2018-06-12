package com.qin.fragment.drawer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
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

public class PersonalInfoFragment extends BaseFragment {
    @BindView(R.id.tv_personal_alter)
    TextView mTvPersonalAlter;
    @BindView(R.id.tv_personal_phonenumber)
    TextView mTvPersonalPhonenumber;
    @BindView(R.id.tv_personal_username)
    TextView mTvPersonalUsername;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String mUserid;
    private Dialog mDialog;
    Unbinder unbinder;
    private String mSpUser_id;

    @Override
    protected void initData() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
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
        View view = inflater.inflate(R.layout.fragment_personalinfo, null, false);
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
                            String user_phonenumber = jsonObject.getString("user_phonenumber");
                            mTvPersonalUsername.setText(user_name);
                            mTvPersonalPhonenumber.setText(user_phonenumber);
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

    @OnClick(R.id.tv_personal_alter)
    public void onViewClicked() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_drawer, new PersonalInfoUpdateFragment());
        fragmentTransaction.addToBackStack("personal");
        fragmentTransaction.commit();
    }
}
