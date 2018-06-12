package com.qin.fragment.main.owner;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.adapter.recyclerview.limited.EndCodeLimitedRecyclerViewAdapter;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.limitd.PlateNumLimitedAct;
import com.qin.pojo.limitd.city.PlateNumLimitedCity;
import com.qin.pojo.limitd.city.Result;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class EndCodeLimitedFragment extends BaseFragment {

    private static final String URL_CITY = "http://api.jisuapi.com/vehiclelimit/city?appkey=eade6fcaffe9929d";
    private static final String URL_ACT = "http://api.jisuapi.com/vehiclelimit/query?appkey=eade6fcaffe9929d&city=";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_limited)
    Button btnLimited;
    Unbinder unbinder;
    @BindView(R.id.tv_limitd_citycode)
    TextView tvLimitdCitycode;
    @BindView(R.id.tv_limitd_city)
    TextView tvLimitdCity;
    @BindView(R.id.tv_limitd_date)
    TextView tvLimitdDate;
    @BindView(R.id.tv_limitd_time)
    TextView tvLimitdTime;
    @BindView(R.id.tv_limitd_area)
    TextView tvLimitdArea;
    @BindView(R.id.tv_limitd_summary)
    TextView tvLimitdSummary;
    @BindView(R.id.tv_limitd_number)
    TextView tvLimitdNumber;
    @BindView(R.id.tv_limitd_numberrule)
    TextView tvLimitdNumberrule;
    private Dialog mDialog;
    private View popView;
    private int popIsShowing = 1;
    private DisplayMetrics mDisplayMetrics;
    private int mWidthPixels;
    private int mHeightPixels;
    public PopupWindow popupWindow;
    private RecyclerView mRecyclerViewPop;
    private EndCodeLimitedRecyclerViewAdapter mAdapter;
    private List<Result> mResultCity;
    private LinearLayoutManager mLayoutManager;
    private String mCityCode;

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_endcodelimited, null, false);
        initDialog(mActivity);
        initPopWindow(mActivity, R.layout.popwindow_search_bound, mWidthPixels - 60, LinearLayout.LayoutParams.WRAP_CONTENT);
        unbinder = ButterKnife.bind(this, view);
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

    private void accessNetLimitedCity() {
        Log.i("LimitedCit", "accessNetAirQuality");
        String url = URL_CITY;
        OkGo.<String>post(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("LimitedCit", response.body());
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                parseDataLimitedCity(response.body());
            }

            @Override
            public void onError(Response<String> response) {
                Log.i("LimitedCit", "尾号限行异常");
                ToastUtils.showBgResource(mActivity, "尾号限行异常极速数据");
            }
        });
    }

    private void parseDataLimitedCity(String json) {
        Gson gson = new Gson();
        PlateNumLimitedCity limited = gson.fromJson(json, PlateNumLimitedCity.class);
        mResultCity = limited.getResult();
        Log.i("LimitedCit", "mResultCity" + "---" + limited.getMsg());
        mAdapter = new EndCodeLimitedRecyclerViewAdapter(mResultCity);
        mRecyclerViewPop.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new EndCodeLimitedRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, EndCodeLimitedRecyclerViewAdapter.ViewName viewName, int postion) {
                if (EndCodeLimitedRecyclerViewAdapter.ViewName.ITEM == viewName) {
                    ToastUtils.showBgResource(mActivity, "填入城市码");
//                    RelativeLayout layout = (RelativeLayout) mLayoutManager.findViewByPosition(postion);
//                    TextView text = (TextView) layout.getChildAt(0);
//                    tvLimitdCitycode.setText(text.getText().toString());
                    mCityCode = mResultCity.get(postion).getCity();
                    btnLimited.setVisibility(View.VISIBLE);
                    tvLimitdCitycode.setText(mCityCode);
                    popupWindow.dismiss();
                }
            }
        });
    }

    private void accessNetLimitedAct() {
        Log.i("LimitedAct", "accessNetAirQuality");
        String url = URL_ACT + mCityCode;
        OkGo.<String>post(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("LimitedAct", response.body());
                parseDataLimitedAct(response.body());
            }

            @Override
            public void onError(Response<String> response) {
                Log.i("LimitedAct", "尾号限行异常");
                ToastUtils.showBgResource(mActivity, "尾号限行异常极速数据");
            }
        });
    }

    private void parseDataLimitedAct(String json) {
        Gson gson = new Gson();
        PlateNumLimitedAct limitedAct = gson.fromJson(json, PlateNumLimitedAct.class);
        com.qin.pojo.limitd.Result result = limitedAct.getResult();
        tvLimitdCity.setText("城市 : "+result.getCityname());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String t=format.format(new Date(String.valueOf(result.getDate())));
        tvLimitdDate.setText("日期 : "+t+ "      " + result.getWeek());
        tvLimitdArea.setText("限行区域 : "+result.getArea());
        tvLimitdNumber.setText("限行尾号 : "+result.getNumber());
        tvLimitdNumberrule.setText("尾号规则 : "+result.getNumberrule());
        tvLimitdSummary.setText("限行摘要 : "+result.getSummary());
        List<String> time = result.getTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < time.size(); i++) {
            sb.append(time.get(i) + "\n");
        }
        tvLimitdTime.setText("限行时间 : "+sb.toString());
        mDialog.dismiss();
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

    public void initPopWindow(Context context, int layoutRes, int width, int height) {
        popView = View.inflate(mActivity, R.layout.popwindow_endcode_limited, null);
        mRecyclerViewPop = popView.findViewById(R.id.recyclerView_endcode_limited);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerViewPop.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewPop.addItemDecoration(new DividerItemDecoration(mActivity, VERTICAL));
        mRecyclerViewPop.setLayoutManager(mLayoutManager);
        popupWindow = new PopupWindow(popView, width, height, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popIsShowing = 1;
        popView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        popupWindow.setAnimationStyle(R.style.popSearchAnimtion);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowGray(1.0f);
            }
        });
    }

    public void setWindowGray(float f) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = f;
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_limitd_citycode, R.id.btn_limited})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_limitd_citycode:
                mDialog.show();
                accessNetLimitedCity();
                popupWindow.showAsDropDown(tvLimitdCitycode, -24, 30);
                break;
            case R.id.btn_limited:
                mDialog.show();
                accessNetLimitedAct();
                break;
        }
    }
}
