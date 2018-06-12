package com.qin.fragment.main.owner;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.adapter.recyclerview.MarginDecorationVertical;
import com.qin.adapter.recyclerview.allcar.AllCarBrandRecyclerViewAdapter;
import com.qin.adapter.recyclerview.allcar.AllCarRecyclerViewAdapter;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.allcartype.AllCarType;
import com.qin.pojo.allcartype.Result;
import com.qin.pojo.allcartype.car.Car;
import com.qin.pojo.allcartype.car.Lists;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public class CarFragment extends BaseFragment {

    @BindView(R.id.rlv_main_car)
    RecyclerView rlvMainCar;
    private String URL = "http://api.jisuapi.com/car/car?appkey=eade6fcaffe9929d&parentid=";
    Unbinder unbinder;
    private View mView;
    private Dialog mDialog;
    private String mList_id;
    private List<Lists> mResultList;

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
        mView = inflater.inflate(R.layout.fragment_owner_car, null, false);
        unbinder = ButterKnife.bind(this, mView);
        initDialog();
        mList_id = getArguments().getString("list_id");
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initRecycleView(final List<Lists> resultList) {
        rlvMainCar.setLayoutManager(new LinearLayoutManager(mActivity));
        rlvMainCar.setItemAnimator(new DefaultItemAnimator());
        rlvMainCar.addItemDecoration(new MarginDecorationVertical(20));
        AllCarRecyclerViewAdapter adapter = new AllCarRecyclerViewAdapter(resultList, mActivity);
        rlvMainCar.setAdapter(adapter);
        adapter.setOnItemClickListener(new AllCarRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("dtail_id", resultList.get(position).getId());
                CarDetailFragment carDetailFragment = new CarDetailFragment();
                carDetailFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_main_cartype, carDetailFragment).addToBackStack("ok").commit();
            }
        });
        mDialog.dismiss();
    }

    private void accessCar() {
        mDialog.show();
        String url = URL+mList_id;
        OkGo.<String>get(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("car", response.body());
                try {
                    parseDataCar(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                Log.i("car", "极速数据异常");
                ToastUtils.showBgResource(mActivity, "车型控极速数据异常");
            }
        });
    }

    private void parseDataCar(String body) {
        Gson gson = new Gson();
        Car car = gson.fromJson(body, Car.class);
        com.qin.pojo.allcartype.car.Result result = car.getResult();
        mResultList = result.getList();
        //TODO 开了两次车型大全
        initRecycleView(mResultList);
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
