package com.qin.fragment.drawer.history;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.qin.R;
import com.qin.activity.consume.MonthConsumeActivity;
import com.qin.activity.consume.YearConsumeActivity;
import com.qin.adapter.viewpager.GasViewPagerAdapter;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.consume.Consume;
import com.qin.view.MyMarkerView;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class HistoryCenterFragment extends BaseFragment implements OnChartValueSelectedListener {
    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.indicator)
    MagicIndicator mIndicator;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.piechart)
    PieChart mPiechart;
    @BindView(R.id.barchart)
    BarChart mBarChart;
    @BindView(R.id.tv_consume_year)
    TextView mTvConsumeYear;
    @BindView(R.id.tv_consume_month)
    TextView mTvConsumeMonth;
    @BindView(R.id.tv_all_consume)
    TextView mTvAllConsume;
    @BindView(R.id.tv_parking_consume)
    TextView mTvParkingConsume;
    @BindView(R.id.tv_gas_consume)
    TextView mTvGasConsume;
    @BindView(R.id.tv_repair_consume)
    TextView mTvRepairConsume;
    @BindView(R.id.tv_out_consume)
    TextView mTvOutConsume;
    @BindView(R.id.tv_consume)
    TextView mTvConsume;
    @BindView(R.id.tv_record)
    TextView mTvRecord;
    @BindView(R.id.ll_consume)
    NestedScrollView mLlConsume;
    @BindView(R.id.ll_viewpager)
    LinearLayout mLlViewpager;
    private Typeface tf;
    protected String[] mParties = new String[]{"停车消费", "加油消费", "维修消费", "出险消费"};
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private static final String[] CHANNELS = new String[]{"停车记录", "加油记录", "出险记录", "维修记录", "故障报修", "事故报警"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private Consume mConsume;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConsume = getArguments().getParcelable("consume");

        Log.i("consume", mConsume.toString());
    }

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historycenter, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        mLlConsume.setVisibility(View.VISIBLE);
        mLlViewpager.setVisibility(View.GONE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        initDatas();
    }

    private void initDatas() {
        List<BaseFragment> lists = new ArrayList<>();
        lists.add(new ParkingHistoryfragment());
        lists.add(new GasHistoryfragment());
        lists.add(new OutInsuaranceHistoryfragment());
        lists.add(new RepairHistoryfragment());
        lists.add(new BreakRepairHistoryfragment());
        lists.add(new BreakAlarmHistoryfragment());
        mViewpager.setAdapter(new GasViewPagerAdapter(getFragmentManager(), lists));
        initMagicIndicator(mIndicator, mDataList);

        mTvParkingConsume.setText("停车消费 : " + mConsume.getCate1_cost() + "元");
        mTvGasConsume.setText("加油消费 : " + mConsume.getCate2_cost() + "元");
        mTvRepairConsume.setText("修车消费 : " + mConsume.getCate3_cost() + "元");
        mTvOutConsume.setText("出险消费 : " + mConsume.getCate4_cost() + "元");
        mTvAllConsume.setText("总消费 : " + mConsume.getYear_cost() + "元");

        initChart();
    }

    private void initChart() {
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
    }

    private void initMagicIndicator(MagicIndicator indicator, final List<String> list) {
        //indicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(mActivity);
        commonNavigator.setScrollPivotX(0.2f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(list.get(index));
                simplePagerTitleView.setTextColor(Color.parseColor("#000000"));
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewpager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
                // setup badge
//                if (index == 1 || index == 2) {
//                    ImageView badgeImageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.layout_red_dot_badge, null);
//                    badgePagerTitleView.setBadgeView(badgeImageView);
//                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_LEFT, -UIUtil.dip2px(context, 6)));
//                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
//                }
                // cancel badge when click tab, default true
                badgePagerTitleView.setAutoCancelBadge(true);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(Color.parseColor("#ebe4e3"));
                return indicator;
            }
        });
        indicator.setNavigator(commonNavigator);
        final FragmentContainerHelper fragmentContainerHelper = new FragmentContainerHelper(indicator);
        fragmentContainerHelper.setInterpolator(new OvershootInterpolator(2.0f));
        fragmentContainerHelper.setDuration(300);
        mViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                fragmentContainerHelper.handlePageSelected(position);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("2018\n年度消费总记录");
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

    @OnClick({R.id.tv_consume_year, R.id.tv_consume_month, R.id.tv_consume, R.id.tv_record})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_consume_year:
                intent = new Intent();
                intent.setClass(mActivity, YearConsumeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
            case R.id.tv_consume_month:
                intent = new Intent();
                intent.setClass(mActivity, MonthConsumeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
            case R.id.tv_consume:
                mLlConsume.setVisibility(View.VISIBLE);
                mLlViewpager.setVisibility(View.GONE);
                mTvConsume.setTextColor(getResources().getColor(R.color.red));
                mTvRecord.setTextColor(getResources().getColor(R.color.black));
                mTvRecord.setBackgroundColor(getResources().getColor(R.color.colorWrite));
                mTvConsume.setBackgroundResource(R.drawable.shape_background_consume);
                break;
            case R.id.tv_record:
                mTvConsume.setBackgroundColor(getResources().getColor(R.color.colorWrite));
                mTvRecord.setBackgroundResource(R.drawable.shape_background_consume);
                mLlViewpager.setVisibility(View.VISIBLE);
                mLlConsume.setVisibility(View.GONE);
                mTvRecord.setTextColor(getResources().getColor(R.color.red));
                mTvConsume.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }
}
