package com.qin.fragment.main.owner;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.qin.adapter.recyclerview.MarginDecorationCarType;
import com.qin.adapter.recyclerview.allcar.AllCarModelRecyclerViewAdapter;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.allcartype.carmodel.CarModel;
import com.qin.pojo.allcartype.carmodel.Lists;
import com.qin.pojo.allcartype.carmodel.Result;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public class CarModelFragment extends BaseFragment {

    private String CARMODEL_URL = "http://api.jisuapi.com/car/type?appkey=eade6fcaffe9929d&parentid=";

    @BindView(R.id.rlv_main_carmodel)
    RecyclerView rlvMainCarmodel;
    Unbinder unbinder;
    private View mView;
    private Dialog mDialog;
    private String mParentid;
    private List<com.qin.pojo.allcartype.carmodel.Result> mResultList;
    private List<Lists> carModelList = new ArrayList();

    @Override
    public View initView() {
        return null;
    }

    @Override
    protected void initData() {
        accessCarModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_owner_carmodel, null, false);
        unbinder = ButterKnife.bind(this, mView);
        initDialog();
        mParentid = getArguments().getString("parentid");
        Log.i("Model", mParentid);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initRecycleView(final List<Lists> resultList) {
        rlvMainCarmodel.setLayoutManager(new GridLayoutManager(mActivity, 2));
        rlvMainCarmodel.setItemAnimator(new DefaultItemAnimator());
        rlvMainCarmodel.addItemDecoration(new MarginDecorationCarType(20));
        AllCarModelRecyclerViewAdapter adapter = new AllCarModelRecyclerViewAdapter(resultList, mActivity);
        rlvMainCarmodel.setAdapter(adapter);
        adapter.setOnItemClickListener(new AllCarModelRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("list_id", resultList.get(position).getId());
                CarFragment carFragment = new CarFragment();
                carFragment.setArguments(bundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_main_cartype, carFragment).addToBackStack("carFragment").commit();
            }
        });
        mDialog.dismiss();
    }

    private void accessCarModel() {
        mDialog.show();
        String url = CARMODEL_URL + mParentid;
        OkGo.<String>get(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("Model", response.body());
                try {
                    parseDataCarModel(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                Log.i("Model", "极速数据异常");
                ToastUtils.showBgResource(mActivity, "车型控极速数据异常");
            }
        });
    }

    private void parseDataCarModel(String body) {
        Gson gson = new Gson();
        CarModel carModel = gson.fromJson(body, CarModel.class);
        mResultList = carModel.getResult();
        if (mResultList != null && mResultList.size() > 0) {
            for (int i = 0; i < mResultList.size(); i++) {
                carModelList.addAll(mResultList.get(i).getList());
            }
        }
        initRecycleView(carModelList);
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
