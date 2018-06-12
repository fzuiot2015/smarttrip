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
import com.qin.pojo.forecast.PlateForecast;
import com.qin.pojo.forecast.Result;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class PlateForecastFragment extends BaseFragment {

    private static final String URL = "http://api.jisuapi.com/lsplateluck/query?lsplate=";
    private static final String KEY = "&appkey=eade6fcaffe9929d";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_forecast)
    EditText etForecast;
    @BindView(R.id.btn_forecast_start)
    Button btnForecastStart;
    @BindView(R.id.tv_forecast_lsplate)
    TextView tvForecastLsplate;
    @BindView(R.id.tv_forecast_province)
    TextView tvForecastProvince;
    @BindView(R.id.tv_forecast_city)
    TextView tvForecastCity;
    @BindView(R.id.tv_forecast_score)
    TextView tvForecastScore;
    @BindView(R.id.tv_forecast_luck)
    TextView tvForecastLuck;
    @BindView(R.id.tv_forecast_content)
    TextView tvForecastContent;
    @BindView(R.id.tv_forecast_character)
    TextView tvForecastCharacter;
    @BindView(R.id.tv_forecast_characterdetail)
    TextView tvForecastCharacterdetail;
    Unbinder unbinder;
    private Dialog mDialog;

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_plateforecast, null, false);
        unbinder = ButterKnife.bind(this, view);
        initDialog(mActivity);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_forecast_start)
    public void onViewClicked() {
        accessNetForecast();
    }

    private void accessNetForecast() {
        String trim = etForecast.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            ToastUtils.showBgResource(mActivity, "车牌号不能为空");
            return;
        } else {
            mDialog.show();
            String url = URL + trim + KEY;
            OkGo.<String>post(url).tag(this).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.i("Forecast", response.body());
                    parseDataForecast(response.body());
                }

                @Override
                public void onError(Response<String> response) {
                    Log.i("Forecast", "车牌号预测异常");
                    ToastUtils.showBgResource(mActivity, "车牌号预测异常");
                }
            });
        }
    }

    private void parseDataForecast(String json) {
        Gson gson = new Gson();
        PlateForecast plateForecast = null;
        try {
            plateForecast = gson.fromJson(json, PlateForecast.class);
            String msg = plateForecast.getMsg();
            String status = plateForecast.getStatus();
            mDialog.dismiss();
            if (!status.equals("0")) {
                ToastUtils.showBgResource(mActivity, msg);
            } else {
                Result result = plateForecast.getResult();
                tvForecastCity.setText("城市 ： " + result.getCity());
                tvForecastCharacterdetail.setText("具体表现 ： " + result.getCharacterdetail());
                tvForecastCharacter.setText("主人个性 ： " + result.getCharacter());
                tvForecastContent.setText("吉凶分析 ： " + result.getContent());
                tvForecastLsplate.setText("车牌号 ： " + result.getLsplate());
                tvForecastLuck.setText("吉凶 ： " + result.getLuck());
                tvForecastProvince.setText("省份 ： " + result.getProvince());
                tvForecastScore.setText("数理分数 ： " + result.getScore());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mDialog.dismiss();
            ToastUtils.showBgResource(mActivity, "访问失败");
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
}
