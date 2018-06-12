package com.qin.fragment.drawer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviTheme;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.adapter.recyclerview.MarginDecorationVertical;
import com.qin.adapter.recyclerview.collect.CollectRecyclerViewAdapter;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.map.util.AmapTTSController;
import com.qin.pojo.usercollect.Result;
import com.qin.pojo.usercollect.UserCollect;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class MyCollectionFragment extends BaseFragment implements INaviInfoCallback {

    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView_collect)
    RecyclerView mRecyclerViewCollect;
    Unbinder unbinder;
    @BindView(R.id.tv_collect)
    TextView mTvCollect;
    private String mUserid;
    private Dialog mDialog;
    private String mSpUser_id;
    private List<Result> mResultList;
    private CollectRecyclerViewAdapter mAdapter;
    private AmapTTSController amapTTSController;

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
        View view = inflater.inflate(R.layout.fragment_mycollection, null, false);
        unbinder = ButterKnife.bind(this, view);
        amapTTSController = AmapTTSController.getInstance(mActivity);
        amapTTSController.init();
        return view;
    }

    @Override
    protected void initData() {

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerViewCollect.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewCollect.addItemDecoration(new MarginDecorationVertical(20));
        mRecyclerViewCollect.setLayoutManager(layoutManager);
        initDialog();
        accessNet();
    }

    public void accessNet() {
        mDialog.show();
        OkGo.<String>post(ConstantValues.URL_COLLECTQUERY).tag(this).params("user_id", mUserid).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("collectinfo", response.body());
                mDialog.dismiss();
                parseData(response.body());
            }

            @Override
            public void onError(Response<String> response) {
                mDialog.dismiss();
                ToastUtils.showBgResource(mActivity, "服务器出错！");
            }
        });
    }

    public void accessNetDelte(String collect_id, final int postion) {
        mDialog.show();
        OkGo.<String>post(ConstantValues.URL_COLLECTDELETE).tag(this)
                .params("user_id", mUserid)
                .params("collect_id", collect_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("collectinfo", response.body());
                        mDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String code = jsonObject.getString("code");
                            if (code.equals("0")) {
                                mResultList.remove(postion);
                                mAdapter.notifyDataSetChanged();
                                mRecyclerViewCollect.setAdapter(mAdapter);
                                ToastUtils.showBgResource(mActivity, "已移除！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        mDialog.dismiss();
                        ToastUtils.showBgResource(mActivity, "服务器出错！");
                    }
                });
    }

    public void accessNetDeleteAll() {
        mDialog.show();
        OkGo.<String>post(ConstantValues.URL_COLLECTDELETEALL).tag(this)
                .params("user_id", mUserid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("collectinfo", response.body());
                        mDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String code = jsonObject.getString("code");
                            if (code.equals("0")) {
                                mResultList.clear();
                                mRecyclerViewCollect.setVisibility(View.GONE);
                                mTvCollect.setVisibility(View.VISIBLE);
                                mAdapter.notifyDataSetChanged();
                                mRecyclerViewCollect.setAdapter(mAdapter);
                            } else if (code.equals("1")){
                                ToastUtils.showBgResource(mActivity, "没有收藏记录！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        mDialog.dismiss();
                        ToastUtils.showBgResource(mActivity, "服务器出错！");
                    }
                });
    }

    private void parseData(String json) {
        Gson gson = new Gson();
        UserCollect userCollect = gson.fromJson(json, UserCollect.class);
        String code = userCollect.getCode();
        if (code.equals("0")) {
            mResultList = userCollect.getResult();
            if (mResultList.size() > 0) {
                mRecyclerViewCollect.setVisibility(View.VISIBLE);
                mTvCollect.setVisibility(View.GONE);
                mAdapter = new CollectRecyclerViewAdapter(mResultList);
                mRecyclerViewCollect.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new CollectRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, CollectRecyclerViewAdapter.ViewName viewName, int postion) {
                        if (CollectRecyclerViewAdapter.ViewName.IMAGEVIEW == viewName) {
                            accessNetDelte(mResultList.get(postion).getCollect_id(), postion);
                        } else if (CollectRecyclerViewAdapter.ViewName.ITEM == viewName) {
                            double lat = mResultList.get(postion).getLat();
                            double lng = mResultList.get(postion).getLng();
                            String name = mResultList.get(postion).getName();
                            AmapNaviPage.getInstance().showRouteActivity(mActivity
                                    , new AmapNaviParams(new Poi(null, null, "")
                                            , null
                                            , new Poi(name, new LatLng(
                                            lat
                                            , lng), "")
                                            , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
                                    , MyCollectionFragment.this);
                        }
                    }
                });
            }else{
                mRecyclerViewCollect.setVisibility(View.GONE);
                mTvCollect.setVisibility(View.VISIBLE);
            }
        }
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

    @OnClick(R.id.tv_delete)
    public void onViewClicked() {
        accessNetDeleteAll();
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

        amapTTSController.onGetNavigationText(s);
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

        amapTTSController.stopSpeaking();
    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }
}
