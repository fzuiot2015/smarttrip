package com.qin.fragment.main.owner;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.breakdown.Body;
import com.qin.pojo.breakdown.BreakDownDTC;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class BreakDTCFragment extends BaseFragment {

    private static final String URL = "http://getDTC.api.juhe.cn/CarManagerServer/getDTC";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_dtc)
    EditText etDtc;
    @BindView(R.id.btn_dtc_query)
    Button btnDtcQuery;
    @BindView(R.id.tv_dtc_description)
    TextView tvDtcDescription;
    @BindView(R.id.tv_dtc_aftermath)
    TextView tvDtcAftermath;
    @BindView(R.id.tv_dtc_type)
    TextView tvDtcType;
    @BindView(R.id.tv_dtc_remind)
    TextView tvDtcRemind;
    Unbinder unbinder;
    private Dialog mDialog;

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_dtc, null, false);
        unbinder = ButterKnife.bind(this, view);
        initDialog(mActivity);
        return view;
    }

    @Override
    protected void initData() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }

    private void accessNetDTC() {
        String trim = etDtc.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            ToastUtils.showBgResource(mActivity, "请输入车辆故障码");
        } else {
            mDialog.show();
            OkGo.<String>post(URL).tag(this)
                    .params("code", trim)
                    .params("key", "b087874d2bbc636b4b23cbc90ce03c6f")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.i("DTC", response.body());
                            mDialog.dismiss();
                            parseDataDTC(response.body());
                        }

                        @Override
                        public void onError(Response<String> response) {
                            Log.i("DTC", "空异常");
                            mDialog.dismiss();
                            ToastUtils.showBgResource(mActivity, "异常");
                        }
                    });
        }
    }

    private void parseDataDTC(String json){
        Gson gson = new Gson();
        try {
            BreakDownDTC downDTC = gson.fromJson(json, BreakDownDTC.class);
            if (downDTC.getError_code().equals("0")) {
                Body body = downDTC.getResult().getBody();
                tvDtcAftermath.setText("造成影响 : " + body.getAftermath());
                tvDtcDescription.setText("故障描述 : " + body.getDescription());
                tvDtcRemind.setText("解决建议 : " + body.getRemind());
                tvDtcType.setText("故障类型 : " + body.getType());
            } else {
                ToastUtils.showBgResource(mActivity, downDTC.getReason());
            }
        } catch (Exception e) {
            e.printStackTrace();
            String ret_msg = null;
            try {
                ret_msg = new JSONObject(json).getString("ret_msg");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            ToastUtils.showBgResource(mActivity, ret_msg);
        }
    }

    private void initDialog(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(mActivity), ScreenUtils.getWindowHeight(mActivity));
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        mDialog = new Dialog(context);
        View view = View.inflate(context, R.layout.dialog_loading, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_dtc_query)
    public void onViewClicked() {
        accessNetDTC();
    }
}
