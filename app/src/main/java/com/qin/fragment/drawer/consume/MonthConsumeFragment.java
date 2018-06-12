package com.qin.fragment.drawer.consume;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
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
 * Created by Administrator on 2018/5/5.
 */

public class MonthConsumeFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    Unbinder unbinder;
    @BindView(R.id.barchart1)
    BarChart mBarchart1;
    @BindView(R.id.barchart2)
    BarChart mBarchart2;
    @BindView(R.id.barchart3)
    BarChart mBarchart3;

    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private String mUserid;
    private Dialog mDialog;
    private String mSpUser_id;
    private Consume mConsume1;
    private Consume mConsume2;
    private Consume mConsume3;

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
        View view = inflater.inflate(R.layout.fragment_consume_scrollmonth, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        initDialog();
        mDialog.show();
        accessNet1(mUserid,2018);
        accessNet2(mUserid,2017);
        accessNet3(mUserid,2016);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void accessNet1(String userid, int year) {
        OkGo.<String>post(ConstantValues.URL_HISTORYCOST)
                .tag(this)
                .params("user_id", userid)
                .params("year", year)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("nodata", response.body());
                //        mDialog.dismiss();
                        String body = response.body();
                        parseData1(body);
                        initBarChart1();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(mActivity,"服务器繁忙");
                    }
                });
    }

    private void parseData1(String json) {
        Gson gson = new Gson();
        mConsume1 = gson.fromJson(json, Consume.class);
    }


    public void initBarChart1() {
        mTfRegular = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Light.ttf");
        mBarchart1.getDescription().setEnabled(false);
//        mBarChart.setDrawBorders(true);
        // scaling can now only be done on x- and y-axis separately
        mBarchart1.setPinchZoom(false);
        mBarchart1.setDrawBarShadow(false);
        mBarchart1.setDrawGridBackground(false);
        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(mActivity, R.layout.custom_marker_view);
        mv.setChartView(mBarchart1); // For bounds control
        mBarchart1.setMarker(mv); // Set the marker to the chart
        Legend legend = mBarchart1.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(true);
        legend.setTypeface(mTfLight);
        legend.setYOffset(0f);
        legend.setXOffset(10f);
        legend.setYEntrySpace(0f);
        legend.setTextSize(8f);

        XAxis xAxis = mBarchart1.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        YAxis leftAxis = mBarchart1.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mBarchart1.getAxisRight().setEnabled(false);
        setDataBar1();
    }

    public void setDataBar1() {

        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        int startYear = 1;
        int endYear = 13;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

        for (int i = startYear; i < endYear; i++) {
            yVals1.add(new BarEntry(i, Float.parseFloat(mConsume1.getMonth_cost().get(i - 1).getCate1())));
            yVals2.add(new BarEntry(i, Float.parseFloat(mConsume1.getMonth_cost().get(i - 1).getCate2())));
            yVals3.add(new BarEntry(i, Float.parseFloat(mConsume1.getMonth_cost().get(i - 1).getCate3())));
            yVals4.add(new BarEntry(i, Float.parseFloat(mConsume1.getMonth_cost().get(i - 1).getCate4())));
        }

        BarDataSet set1, set2, set3, set4;

        if (mBarchart1.getData() != null && mBarchart1.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mBarchart1.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mBarchart1.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mBarchart1.getData().getDataSetByIndex(2);
            set4 = (BarDataSet) mBarchart1.getData().getDataSetByIndex(3);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            set4.setValues(yVals4);
            mBarchart1.getData().notifyDataChanged();
            mBarchart1.notifyDataSetChanged();

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

            mBarchart1.setData(data);
        }

        // specify the width each bar should have
        mBarchart1.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        mBarchart1.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mBarchart1.getXAxis().setAxisMaximum(startYear + mBarchart1.getBarData().getGroupWidth(groupSpace, barSpace) * 12);
        mBarchart1.groupBars(startYear, groupSpace, barSpace);
        mBarchart1.animateXY(1400, 1400, Easing.EasingOption.EaseInCubic, Easing.EasingOption.EaseInOutQuad);
        //    mBarChart.getData().setHighlightEnabled(false);
        mBarchart1.invalidate();
    }


    private void accessNet2(String userid, int year) {
        OkGo.<String>post(ConstantValues.URL_HISTORYCOST)
                .tag(this)
                .params("user_id", userid)
                .params("year", year)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("nodata", response.body());
                   //     mDialog.dismiss();
                        String body = response.body();
                        parseData2(body);
                        initBarChart2();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(mActivity,"服务器繁忙");
                    }
                });
    }

    private void parseData2(String json) {
        Gson gson = new Gson();
        mConsume2 = gson.fromJson(json, Consume.class);
    }


    public void initBarChart2() {
        mTfRegular = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Light.ttf");
        mBarchart2.getDescription().setEnabled(false);
//        mBarChart.setDrawBorders(true);
        // scaling can now only be done on x- and y-axis separately
        mBarchart2.setPinchZoom(false);
        mBarchart2.setDrawBarShadow(false);
        mBarchart2.setDrawGridBackground(false);
        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(mActivity, R.layout.custom_marker_view);
        mv.setChartView(mBarchart2); // For bounds control
        mBarchart2.setMarker(mv); // Set the marker to the chart
        Legend legend = mBarchart2.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(true);
        legend.setTypeface(mTfLight);
        legend.setYOffset(0f);
        legend.setXOffset(10f);
        legend.setYEntrySpace(0f);
        legend.setTextSize(8f);

        XAxis xAxis = mBarchart2.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        YAxis leftAxis = mBarchart2.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mBarchart2.getAxisRight().setEnabled(false);
        setDataBar2();
    }

    public void setDataBar2() {

        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        int startYear = 1;
        int endYear = 13;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

        for (int i = startYear; i < endYear; i++) {
            yVals1.add(new BarEntry(i, Float.parseFloat(mConsume2.getMonth_cost().get(i - 1).getCate1())));
            yVals2.add(new BarEntry(i, Float.parseFloat(mConsume2.getMonth_cost().get(i - 1).getCate2())));
            yVals3.add(new BarEntry(i, Float.parseFloat(mConsume2.getMonth_cost().get(i - 1).getCate3())));
            yVals4.add(new BarEntry(i, Float.parseFloat(mConsume2.getMonth_cost().get(i - 1).getCate4())));
        }

        BarDataSet set1, set2, set3, set4;

        if (mBarchart2.getData() != null && mBarchart2.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mBarchart2.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mBarchart2.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mBarchart2.getData().getDataSetByIndex(2);
            set4 = (BarDataSet) mBarchart2.getData().getDataSetByIndex(3);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            set4.setValues(yVals4);
            mBarchart2.getData().notifyDataChanged();
            mBarchart2.notifyDataSetChanged();

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

            mBarchart2.setData(data);
        }

        // specify the width each bar should have
        mBarchart2.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        mBarchart2.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mBarchart2.getXAxis().setAxisMaximum(startYear + mBarchart2.getBarData().getGroupWidth(groupSpace, barSpace) * 12);
        mBarchart2.groupBars(startYear, groupSpace, barSpace);
        mBarchart2.animateXY(1400, 1400, Easing.EasingOption.EaseInCubic, Easing.EasingOption.EaseInOutQuad);
        //   mBarchart2
        mBarchart2.invalidate();
    }


    private void accessNet3(String userid, int year) {
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
                        parseData3(body);
                        initBarChart3();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(mActivity,"服务器繁忙");
                    }
                });
    }

    private void parseData3(String json) {
        Gson gson = new Gson();
        mConsume3 = gson.fromJson(json, Consume.class);
    }


    public void initBarChart3() {
        mTfRegular = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Light.ttf");
        mBarchart3.getDescription().setEnabled(false);
//        mBarChart.setDrawBorders(true);
        // scaling can now only be done on x- and y-axis separately
        mBarchart3.setPinchZoom(false);
        mBarchart3.setDrawBarShadow(false);
        mBarchart3.setDrawGridBackground(false);
        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(mActivity, R.layout.custom_marker_view);
        mv.setChartView(mBarchart3); // For bounds control
        mBarchart3.setMarker(mv); // Set the marker to the chart
        Legend legend = mBarchart3.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(true);
        legend.setTypeface(mTfLight);
        legend.setYOffset(0f);
        legend.setXOffset(10f);
        legend.setYEntrySpace(0f);
        legend.setTextSize(8f);

        XAxis xAxis = mBarchart3.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        YAxis leftAxis = mBarchart3.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mBarchart3.getAxisRight().setEnabled(false);
        setDataBar3();
    }

    public void setDataBar3() {

        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        int startYear = 1;
        int endYear = 13;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

        for (int i = startYear; i < endYear; i++) {
            yVals1.add(new BarEntry(i, Float.parseFloat(mConsume3.getMonth_cost().get(i - 1).getCate1())));
            yVals2.add(new BarEntry(i, Float.parseFloat(mConsume3.getMonth_cost().get(i - 1).getCate2())));
            yVals3.add(new BarEntry(i, Float.parseFloat(mConsume3.getMonth_cost().get(i - 1).getCate3())));
            yVals4.add(new BarEntry(i, Float.parseFloat(mConsume3.getMonth_cost().get(i - 1).getCate4())));
        }

        BarDataSet set1, set2, set3, set4;

        if (mBarchart3.getData() != null && mBarchart3.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mBarchart3.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mBarchart3.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mBarchart3.getData().getDataSetByIndex(2);
            set4 = (BarDataSet) mBarchart3.getData().getDataSetByIndex(3);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            set4.setValues(yVals4);
            mBarchart3.getData().notifyDataChanged();
            mBarchart3.notifyDataSetChanged();

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

            mBarchart3.setData(data);
        }

        // specify the width each bar should have
        mBarchart3.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        mBarchart3.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mBarchart3.getXAxis().setAxisMaximum(startYear + mBarchart3.getBarData().getGroupWidth(groupSpace, barSpace) * 12);
        mBarchart3.groupBars(startYear, groupSpace, barSpace);
        mBarchart3.animateXY(1400, 1400, Easing.EasingOption.EaseInCubic, Easing.EasingOption.EaseInOutQuad);
        //    mBarChart.getData().setHighlightEnabled(false);
        mBarchart3.invalidate();
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

}
