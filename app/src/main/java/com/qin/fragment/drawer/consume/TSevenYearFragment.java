package com.qin.fragment.drawer.consume;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class TSevenYearFragment extends BaseFragment implements OnChartValueSelectedListener {
    @BindView(R.id.piechart)
    PieChart mPiechart;
    Unbinder unbinder;
    @BindView(R.id.tv_parking_consume)
    TextView mTvParkingConsume;
    @BindView(R.id.tv_gas_consume)
    TextView mTvGasConsume;
    @BindView(R.id.tv_repair_consume)
    TextView mTvRepairConsume;
    @BindView(R.id.tv_out_consume)
    TextView mTvOutConsume;
    @BindView(R.id.tv_all_consume)
    TextView mTvAllConsume;
    private Typeface tf;
    protected String[] mParties = new String[]{"停车消费", "加油消费", "维修消费", "出险消费"};
    private String mUserid;
    private Dialog mDialog;
    private String mSpUser_id;
    private Consume mConsume;

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
        View view = inflater.inflate(R.layout.fragment_consume_seven, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void initData() {

        initDialog();
        mDialog.show();
        accessNet(mUserid, 2017);


    }

    private void initPieChart() {
        mPiechart.setUsePercentValues(true);
        mPiechart.getDescription().setEnabled(false);
        mPiechart.setExtraOffsets(5, 10, 5, 5);

        mPiechart.setDragDecelerationFrictionCoef(0.95f);

        tf = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Regular.ttf");

        mPiechart.setCenterTextTypeface(Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Light.ttf"));
        mPiechart.setCenterText(generateCenterSpannableText());

        mPiechart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mPiechart.setDrawHoleEnabled(true);
        mPiechart.setHoleColor(Color.WHITE);

        mPiechart.setTransparentCircleColor(Color.WHITE);
        mPiechart.setTransparentCircleAlpha(110);

        mPiechart.setHoleRadius(58f);
        mPiechart.setTransparentCircleRadius(61f);

        mPiechart.setDrawCenterText(true);

        mPiechart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPiechart.setRotationEnabled(true);
        mPiechart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mPiechart.setOnChartValueSelectedListener(this);

        setData(4, 100);

        //    mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);
        mPiechart.animateXY(1400, 1400, Easing.EasingOption.EaseInCubic, Easing.EasingOption.EaseInOutQuad);
        Legend l = mPiechart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("2017\n年度消费总记录");
        s.setSpan(new RelativeSizeSpan(2.0f), 0, 9, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 4, s.length() - 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 4, s.length() - 8, 0);
        s.setSpan(new RelativeSizeSpan(.65f), 4, s.length() - 3, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 3, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 3, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("PieChart",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) (Math.random() * mult) + mult / 5, mParties[i % mParties.length]));
//        }
        entries.add(new PieEntry((Float.parseFloat(mConsume.getCate1_cost())), mParties[0]));
        entries.add(new PieEntry((Float.parseFloat(mConsume.getCate2_cost())), mParties[1]));
        entries.add(new PieEntry((Float.parseFloat(mConsume.getCate3_cost())), mParties[2]));
        entries.add(new PieEntry((Float.parseFloat(mConsume.getCate4_cost())), mParties[3]));
        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


//        dataSet.setValueLinePart1OffsetPercentage(80.f);
//        dataSet.setValueLinePart1Length(0.2f);
//        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tf);
        mPiechart.setData(data);

        // undo all highlights
        mPiechart.highlightValues(null);
        mPiechart.invalidate();
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
                        initPieChart();
                        mTvParkingConsume.setText("停车消费 : "+mConsume.getCate1_cost()+"元");
                        mTvGasConsume.setText("加油消费 : "+mConsume.getCate2_cost()+"元");
                        mTvRepairConsume.setText("修车消费 : "+mConsume.getCate3_cost()+"元");
                        mTvOutConsume.setText("出险消费 : "+mConsume.getCate4_cost()+"元");
                        mTvAllConsume.setText("总消费 : "+mConsume.getYear_cost()+"元");
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
