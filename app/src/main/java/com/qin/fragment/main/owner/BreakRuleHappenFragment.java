package com.qin.fragment.main.owner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.adapter.recyclerview.breakrule.BreakRuleHappenRecyclerViewAdapter;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.map.activity.baidu.BreakRuleMapActivity;
import com.qin.pojo.breakrule.BreakRule;
import com.qin.pojo.breakrule.Result;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class BreakRuleHappenFragment extends BaseFragment implements OnRefreshLoadMoreListener, View.OnClickListener {

    private static final String BREAKRULE_LAT = "http://api.jisuapi.com/illegaladdr/coord?lat=";
    private static final String BREAKRULE_LNG = "&lng=";
    private static final String BREAKRULE_NUM = "&num=";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_breakrule_happen_range)
    ImageView ivBreakruleHappenRange;
    @BindView(R.id.ll_breakrule_happen_range)
    LinearLayout llBreakruleHappenRange;
    @BindView(R.id.tv_breakrule_happen_synthesize)
    TextView tvBreakruleHappenSynthesize;
    @BindView(R.id.tv_breakrule_happen_distance)
    TextView tvBreakruleHappenDistance;
    @BindView(R.id.rlv_breakrule_happen)
    RecyclerView rlvBreakruleHappen;
    @BindView(R.id.refresh_breakrule_happen)
    SmartRefreshLayout refreshBreakruleHappen;
    Unbinder unbinder;
    @BindView(R.id.tv_toilet_no)
    TextView tvToiletNo;
    private String mNum = "20";
    private static final String BREAKRULE_RANGE = "&range=";
    private String mRange = "5000";
    private static final String BREAKRULE_END = "&appkey=eade6fcaffe9929d";
    private List<Result> mResult;
    private double mLatitude;
    private double mLongitude;
    private Dialog mDialog;
    private View popView;
    private int popIsShowing = 1;
    private DisplayMetrics mDisplayMetrics;
    private int mWidthPixels;
    private int mHeightPixels;
    public PopupWindow popupWindow;
    private RelativeLayout mRl_pop_bound1;
    private RelativeLayout mRl_pop_bound2;
    private RelativeLayout mRl_pop_bound3;
    private RelativeLayout mRl_pop_bound4;
    private RelativeLayout mRl_pop_bound5;
    private ImageView ivPop1;
    private ImageView ivPop2;
    private ImageView ivPop3;
    private ImageView ivPop4;
    private ImageView ivPop5;
    private BreakRuleHappenRecyclerViewAdapter mAdapter;

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_breakrule_happen, null, false);
        initDialog(mActivity);
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
        mLatitude = mActivity.getIntent().getExtras().getDouble(ConstantValues.MAIN_LOCATION_LAT, 119.203488);
        mLongitude = mActivity.getIntent().getExtras().getDouble(ConstantValues.MAIN_LOCATION_LON, 26.062197);
        mDisplayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mWidthPixels = mDisplayMetrics.widthPixels;
        mHeightPixels = mDisplayMetrics.heightPixels;
        Log.i("parking", mLatitude + "---" + mLongitude);
        tvBreakruleHappenSynthesize.setTextColor(getResources().getColor(R.color.red));
        refreshBreakruleHappen.setOnRefreshLoadMoreListener(this);
        refreshBreakruleHappen.setPrimaryColorsId(R.color.colorGreen, android.R.color.white);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        rlvBreakruleHappen.setItemAnimator(new DefaultItemAnimator());
        rlvBreakruleHappen.addItemDecoration(new DividerItemDecoration(mActivity, VERTICAL));
        rlvBreakruleHappen.setLayoutManager(layoutManager);
        initPopWindow(mActivity, R.layout.popwindow_search_bound, mWidthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);

        mDialog.show();
        accessNetBreakRuleHappen(mLatitude, mLongitude, 2000);
    }

    private void accessNetBreakRuleHappen(double latitude, double longitude, int range) {
        Log.i("BreakRule", "accessNetBreakRule");
        String url = BREAKRULE_LAT + latitude + BREAKRULE_LNG + longitude + BREAKRULE_NUM + mNum
                + BREAKRULE_RANGE + range + BREAKRULE_END;
        OkGo.<String>post(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("BreakRule", response.body());
                try {
                    tvToiletNo.setVisibility(View.GONE);
                    refreshBreakruleHappen.setVisibility(View.VISIBLE);
                    parseDataBreakRuleHappen(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                    tvToiletNo.setVisibility(View.VISIBLE);
                    refreshBreakruleHappen.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                Log.i("BreakRule", "违章高发地极速数据异常");
                ToastUtils.showBgResource(mActivity, "违章高发地异常");
            }
        });
    }

    private void parseDataBreakRuleHappen(String json) {
        Gson gson = new Gson();
        BreakRule breakRule = gson.fromJson(json, BreakRule.class);
        mResult = breakRule.getResult();
        Log.i("breakhappen",mResult.toString());
        mAdapter = new BreakRuleHappenRecyclerViewAdapter(mResult);
        rlvBreakruleHappen.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BreakRuleHappenRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BreakRuleHappenRecyclerViewAdapter.ViewName viewName, int postion) {
                if (BreakRuleHappenRecyclerViewAdapter.ViewName.ITEM == viewName) {
                    Intent intent = new Intent();
                    intent.putExtra("result", mResult.get(postion));
                    intent.setClass(mActivity, BreakRuleMapActivity.class);
                    startActivity(intent);
                }
                if (BreakRuleHappenRecyclerViewAdapter.ViewName.IMAGEVIEW == viewName) {

                }
            }
        });
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

    private void initPopListener() {
        mRl_pop_bound1 = popView.findViewById(R.id.rl_pop_bound1);
        mRl_pop_bound2 = popView.findViewById(R.id.rl_pop_bound2);
        mRl_pop_bound3 = popView.findViewById(R.id.rl_pop_bound3);
        mRl_pop_bound4 = popView.findViewById(R.id.rl_pop_bound4);
        mRl_pop_bound5 = popView.findViewById(R.id.rl_pop_bound5);
        ivPop1 = popView.findViewById(R.id.iv_pop1);
        ivPop2 = popView.findViewById(R.id.iv_pop2);
        ivPop3 = popView.findViewById(R.id.iv_pop3);
        ivPop4 = popView.findViewById(R.id.iv_pop4);
        ivPop5 = popView.findViewById(R.id.iv_pop5);
        mRl_pop_bound1.setOnClickListener(this);
        mRl_pop_bound2.setOnClickListener(this);
        mRl_pop_bound3.setOnClickListener(this);
        mRl_pop_bound4.setOnClickListener(this);
        mRl_pop_bound5.setOnClickListener(this);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowGray(1.0f);
            }
        });
    }

    public void initPopWindow(Context context, int layoutRes, int width, int height) {
        popView = View.inflate(mActivity, R.layout.popwindow_search_bound, null);
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
        initPopListener();
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

    @OnClick({R.id.ll_breakrule_happen_range, R.id.tv_breakrule_happen_synthesize, R.id.tv_breakrule_happen_distance})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_breakrule_happen_range:
                if (popIsShowing == 2) {
                    popupWindow.dismiss();
                    popIsShowing = 1;
                } else {
                    popupWindow.showAsDropDown(llBreakruleHappenRange, 0, 30);
                    setWindowGray(0.7f);
                }
                break;
            case R.id.tv_breakrule_happen_synthesize:
                tvBreakruleHappenDistance.setTextColor(Color.GRAY);
                tvBreakruleHappenSynthesize.setTextColor(getResources().getColor(R.color.red));
                break;

            case R.id.tv_breakrule_happen_distance:
                tvBreakruleHappenDistance.setTextColor(getResources().getColor(R.color.red));
                tvBreakruleHappenSynthesize.setTextColor(Color.GRAY);
                break;
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        ToastUtils.showBgResource(mActivity, "已加载所有数据");
        refreshLayout.finishLoadMoreWithNoMoreData();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        accessNetBreakRuleHappen(mLatitude, mLongitude, 20000);
        refreshLayout.finishRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pop_bound1:
                accessNetBreakRuleHappen(mLatitude, mLongitude, 500);
                mAdapter.notifyDataSetChanged();
                rlvBreakruleHappen.setAdapter(mAdapter);
                ivPop1.setVisibility(View.VISIBLE);
                ivPop2.setVisibility(View.GONE);
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.rl_pop_bound2:
                ivPop1.setVisibility(View.GONE);
                accessNetBreakRuleHappen(mLatitude, mLongitude, 1000);
                mAdapter.notifyDataSetChanged();
                rlvBreakruleHappen.setAdapter(mAdapter);
                ivPop2.setVisibility(View.VISIBLE);
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.rl_pop_bound3:
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                ivPop3.setVisibility(View.VISIBLE);
                accessNetBreakRuleHappen(mLatitude, mLongitude, 2000);
                mAdapter.notifyDataSetChanged();
                rlvBreakruleHappen.setAdapter(mAdapter);
                ivPop4.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop5.setVisibility(View.GONE);
                break;
            case R.id.rl_pop_bound4:
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop3.setVisibility(View.GONE);
                accessNetBreakRuleHappen(mLatitude, mLongitude, 5000);
                mAdapter.notifyDataSetChanged();
                rlvBreakruleHappen.setAdapter(mAdapter);
                ivPop4.setVisibility(View.VISIBLE);
                ivPop5.setVisibility(View.GONE);
                break;
            case R.id.rl_pop_bound5:
                ivPop1.setVisibility(View.GONE);
                accessNetBreakRuleHappen(mLatitude, mLongitude, 10000);
                mAdapter.notifyDataSetChanged();
                rlvBreakruleHappen.setAdapter(mAdapter);
                ivPop2.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.VISIBLE);
                break;
        }
    }
}
