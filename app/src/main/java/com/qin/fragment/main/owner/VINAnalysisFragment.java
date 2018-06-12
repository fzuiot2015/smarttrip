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
import com.qin.pojo.vin.Result;
import com.qin.pojo.vin.VINAnalysis;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class VINAnalysisFragment extends BaseFragment {

    private static final String URL = "http://apis.haoservice.com/efficient/vinservice?vin=";
    @BindView(R.id.btn_vin_start)
    Button btnVinStart;
    @BindView(R.id.vin0)
    TextView vin0;
    @BindView(R.id.vin1)
    TextView vin1;
    @BindView(R.id.vin2)
    TextView vin2;
    @BindView(R.id.vin3)
    TextView vin3;
    @BindView(R.id.vin4)
    TextView vin4;
    @BindView(R.id.vin5)
    TextView vin5;
    @BindView(R.id.vin6)
    TextView vin6;
    @BindView(R.id.vin7)
    TextView vin7;
    @BindView(R.id.vin8)
    TextView vin8;
    @BindView(R.id.vin9)
    TextView vin9;
    @BindView(R.id.vin10)
    TextView vin10;
    @BindView(R.id.vin11)
    TextView vin11;
    @BindView(R.id.vin12)
    TextView vin12;
    @BindView(R.id.vin13)
    TextView vin13;
    @BindView(R.id.vin14)
    TextView vin14;
    @BindView(R.id.vin15)
    TextView vin15;
    @BindView(R.id.vin16)
    TextView vin16;
    @BindView(R.id.vin17)
    TextView vin17;
    @BindView(R.id.vin18)
    TextView vin18;
    @BindView(R.id.vin19)
    TextView vin19;
    @BindView(R.id.vin20)
    TextView vin20;
    @BindView(R.id.vin21)
    TextView vin21;
    @BindView(R.id.vin22)
    TextView vin22;
    @BindView(R.id.vin23)
    TextView vin23;
    @BindView(R.id.vin24)
    TextView vin24;
    @BindView(R.id.vin25)
    TextView vin25;
    @BindView(R.id.vin26)
    TextView vin26;
    @BindView(R.id.vin27)
    TextView vin27;
    @BindView(R.id.vin28)
    TextView vin28;
    @BindView(R.id.vin29)
    TextView vin29;
    @BindView(R.id.vin_container)
    SmartRefreshLayout vinContainer;
    @BindView(R.id.et_vin)
    EditText etVin;
    private String vin;
    private static final String KEY = "&key=e9f3882f9ae44fb88b91d192c6bacbef";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Unbinder unbinder;
    private Dialog mDialog;
    private int mId;

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_vinanalysis, null, false);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void accessNetVIN() {
        String s = etVin.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {
            ToastUtils.showBgResource(mActivity, "VIN码不能为空");
            return;
        } else {
            mDialog.show();
            vin = s;
            String url = URL + vin + KEY;
            OkGo.<String>post(url).tag(this).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.i("VIN", response.body());
                    parseDataVIN(response.body());
                    mDialog.dismiss();
                }

                @Override
                public void onError(Response<String> response) {
                    Log.i("VIN", "VIN解析异常haoservice");
                    ToastUtils.showBgResource(mActivity, "VIN解析异常haoservice");
                    vinContainer.setVisibility(View.GONE);
                }
            });
        }
    }

    private void parseDataVIN(String json) {
        Gson gson = new Gson();
        VINAnalysis vinAnalysis = gson.fromJson(json, VINAnalysis.class);
        int error_code = vinAnalysis.getError_code();
        if (error_code == 0) {
            Result result = vinAnalysis.getResult();
            vinContainer.setVisibility(View.VISIBLE);
            vin0.setText(result.getVINNF());
            vin1.setText(result.getCJMC());
            vin2.setText(result.getPP());
            vin3.setText(result.getCX());
            vin4.setText(result.getPL());
            vin5.setText(result.getFDJXH());
            vin6.setText(result.getBSQLX());
            vin7.setText(result.getDWS());
            vin8.setText(result.getPFBZ());
            vin9.setText(result.getCLDM());
            vin10.setText(result.getSSNF());
            vin11.setText(result.getTCNF());
            vin12.setText(result.getZDJG());
            vin13.setText(result.getSSYF());
            vin14.setText(result.getSCNF());
            vin15.setText(result.getNK());
            vin16.setText(result.getCXI());
            vin17.setText(result.getXSMC());
            vin18.setText(result.getCLLX());
            vin19.setText(result.getJB());
            vin20.setText(result.getCSXS());
            vin21.setText(result.getCMS());
            vin22.setText(result.getZWS());
            vin23.setText(result.getGL());
            vin24.setText(result.getRYLX());
            vin25.setText(result.getBSQMS());
            vin26.setText(result.getRYBH());
            vin27.setText(result.getQDFS());
            vin28.setText(result.getFDJGS());
            vin29.setText(result.getLevelId() + "");
        } else {
            ToastUtils.showBgResource(mActivity, "返回码不是0");
            vinContainer.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_vin_start)
    public void onViewClicked() {
        accessNetVIN();
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
}
