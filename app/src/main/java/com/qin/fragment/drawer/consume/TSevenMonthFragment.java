package com.qin.fragment.drawer.consume;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
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
import com.qin.util.ToastUtils;
import com.qin.view.MyMarkerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class TSevenMonthFragment extends BaseFragment {

    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    @BindView(R.id.barchart)
    BarChart mBarChart;
    Unbinder unbinder;
    private String mUserid;
    private Dialog mDialog;
    private String mSpUser_id;
    private Consume mConsume;

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
        View view = inflater.inflate(R.layout.fragment_consume_month_seven, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        initDialog();
        mDialog.show();
        accessNet(mUserid,2017);
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
                        String body = response.body();
                        parseData(body);
                        initBarChart();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(mActivity,"服务器繁忙");
                    }
                });
    }

    private void parseData(String json) {
        Gson gson = new Gson();
        mConsume = gson.fromJson(json, Consume.class);
    }


    public void initBarChart() {
        mTfRegular = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Light.ttf");

        mBarChart.getDescription().setEnabled(false);

//        mBarChart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);

        mBarChart.setDrawBarShadow(false);

        mBarChart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(mActivity, R.layout.custom_marker_view);
        mv.setChartView(mBarChart); // For bounds control
        mBarChart.setMarker(mv); // Set the marker to the chart
        Legend legend = mBarChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(true);
        legend.setTypeface(mTfLight);
        legend.setYOffset(0f);
        legend.setXOffset(10f);
        legend.setYEntrySpace(0f);
        legend.setTextSize(8f);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mBarChart.getAxisRight().setEnabled(false);
        setDataBar();

    }

    public void setDataBar() {

        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        int startYear = 1;
        int endYear = 13;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

        for (int i = startYear; i < endYear; i++) {
            yVals1.add(new BarEntry(i, Float.parseFloat(mConsume.getMonth_cost().get(i - 1).getCate1())));
            yVals2.add(new BarEntry(i, Float.parseFloat(mConsume.getMonth_cost().get(i - 1).getCate2())));
            yVals3.add(new BarEntry(i, Float.parseFloat(mConsume.getMonth_cost().get(i - 1).getCate3())));
            yVals4.add(new BarEntry(i, Float.parseFloat(mConsume.getMonth_cost().get(i - 1).getCate4())));
        }

        BarDataSet set1, set2, set3, set4;

        if (mBarChart.getData() != null && mBarChart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mBarChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mBarChart.getData().getDataSetByIndex(2);
            set4 = (BarDataSet) mBarChart.getData().getDataSetByIndex(3);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            set4.setValues(yVals4);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(yVals1, "停车记录");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(yVals2, "加油记录");
            set2.setColor(Color.rgb(164, 228, 251));
            set3 = new BarDataSet(yVals3, "维修记录");
            set3.setColor(Color.rgb(242, 247, 158));
            set4 = new BarDataSet(yVals4, "出险记录");
            set4.setColor(Color.rgb(255, 102, 0));

            BarData data = new BarData(set1, set2, set3, set4);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(mTfLight);

            mBarChart.setData(data);
        }

        // specify the width each bar should have
        mBarChart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        mBarChart.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mBarChart.getXAxis().setAxisMaximum(startYear + mBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * 12);
        mBarChart.groupBars(startYear, groupSpace, barSpace);
        mBarChart.animateXY(1400, 1400, Easing.EasingOption.EaseInCubic, Easing.EasingOption.EaseInOutQuad);
        //    mBarChart.getData().setHighlightEnabled(false);
        mBarChart.invalidate();
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
