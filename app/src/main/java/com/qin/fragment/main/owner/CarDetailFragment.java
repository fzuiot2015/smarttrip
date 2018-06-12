package com.qin.fragment.main.owner;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.qin.fragment.BaseFragment;
import com.qin.pojo.allcartype.cardetail.CarDetail;
import com.qin.pojo.allcartype.cardetail.Result;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public class CarDetailFragment extends BaseFragment {

    @BindView(R.id.tv_main_cardetail)
    TextView tvMainCardetail;
    private String URL = "http://api.jisuapi.com/car/detail?appkey=eade6fcaffe9929d&carid=";
    Unbinder unbinder;
    private View mView;
    private Dialog mDialog;
    private String mDtail_id;

    @Override
    public View initView() {
        return null;
    }

    @Override
    protected void initData() {
        accessCar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_owner_cardetail, null, false);
        unbinder = ButterKnife.bind(this, mView);
        initDialog();
        mDtail_id = getArguments().getString("dtail_id");
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void accessCar() {
        mDialog.show();
        String url = URL + mDtail_id;
        OkGo.<String>get(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("cardtail", response.body());
                try {
                    parseDataCar(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                Log.i("cardtail", "极速数据异常");
                ToastUtils.showBgResource(mActivity, "极速服务器异常");
            }
        });
    }

    private void parseDataCar(String body) {
        mDialog.dismiss();
        Gson gson = new Gson();
        CarDetail carDetail = gson.fromJson(body, CarDetail.class);
        Result carDetailResult = carDetail.getResult();
        tvMainCardetail.setText(body);
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
    }
}
