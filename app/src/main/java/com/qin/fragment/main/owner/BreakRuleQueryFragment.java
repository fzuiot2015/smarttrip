package com.qin.fragment.main.owner;

import android.app.Dialog;
import android.app.FragmentTransaction;
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
import com.qin.pojo.breakrulequery.BreakRuleQuery;
import com.qin.pojo.breakrulequery.Lists;
import com.qin.pojo.breakrulequery.Result;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class BreakRuleQueryFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Unbinder unbinder;
    @BindView(R.id.et_platenumber)
    EditText etPlatenumber;
    @BindView(R.id.et_enginenumber)
    EditText etEnginenumber;
    @BindView(R.id.et_vehicleidnumber)
    EditText etVehicleidnumber;
    @BindView(R.id.et_cityname)
    EditText etCityname;
    @BindView(R.id.et_hpzl)
    EditText etHpzl;
    @BindView(R.id.btn_checkout)
    Button btnCheckout;
    @BindView(R.id.tv_citycode)
    TextView tvCitycode;
    @BindView(R.id.ll_ok)
    LinearLayout llOk;
    @BindView(R.id.ll_owner)
    LinearLayout llOwner;
    private View mView;
    private static final String URL_PLATENUMBER = "http://apis.haoservice.com/weizhang/EasyQuery?plateNumber=";
    private String plateNumber;
    private static final String URL_ENGINENUMBER = "&engineNumber=";
    private String engineNumber;
    private static final String URL_VEHICLEIDNUMBER = "&vehicleIdNumber=";
    private String vehicleIdNumber;
    private static final String URL_CITYNAME = "&cityName=";
    private String cityName;
    private static final String URL_HPZL = "&hpzl=";
    private String mHpzl;
    private static final String URL_APPKEY = "&key=af1f35f8c53742ac996eb6da3d5c8223";
    private ArrayList<Lists> mList;
    private Dialog mDialog;

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_owner_breakrule_query, null, false);
        unbinder = ButterKnife.bind(this, mView);
        initDialog(mActivity);
        return mView;
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
    private void accesNetBreakRuleQuery() {
        String url = URL_PLATENUMBER + plateNumber + URL_ENGINENUMBER + engineNumber + URL_VEHICLEIDNUMBER + vehicleIdNumber
                + URL_CITYNAME + cityName + URL_HPZL + mHpzl + URL_APPKEY;
        //       String url = "http://apis.haoservice.com/weizhang/EasyQuery?plateNumber=%E9%84%82A7LT67&engineNumber=101800589&vehicleIdNumber=LSGPC52U6AF102554&cityName=%E6%AD%A6%E6%B1%89&hpzl=02&key=af1f35f8c53742ac996eb6da3d5c8223";
        OkGo.<String>get(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("BreakRuleQuery", response.body());
                parseDataBreakRuleQuery(response.body());
            }

            @Override
            public void onError(Response<String> response) {
                ToastUtils.showBgResource(mActivity, "违章查询haoservice异常");
            }
        });
    }

    private void parseDataBreakRuleQuery(String json) {
        Gson gson = new Gson();
        BreakRuleQuery breakRuleQuery = gson.fromJson(json, BreakRuleQuery.class);
        Result result = breakRuleQuery.getResult();
        mList = result.getLists();
        StringBuilder sb = new StringBuilder();
        sb.append(result.getCity()+result.getHpzl()+result.getProvince());
        sb.append("-----------");
        sb.append(mList.get(0).getAct()+mList.get(0).getCode()+mList.get(0).getHandled());
        mDialog.dismiss();
        Log.i("BreakRuleQuery", sb.toString());
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list",mList);
        bundle.putParcelable("result",result);
        BreakRuleQueryOKFragment okFragment = new BreakRuleQueryOKFragment();
        okFragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_owner,okFragment).addToBackStack("ok").commit();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_checkout)
    public void onViewClicked() {
        String platenumber = etPlatenumber.getText().toString().trim();
        String enginenumber = etEnginenumber.getText().toString().trim();
        String vehicleidnumber = etVehicleidnumber.getText().toString().trim();
        String cityname = etCityname.getText().toString().trim();
        String hpzl = etHpzl.getText().toString().trim();
        if (!TextUtils.isEmpty(platenumber) && !TextUtils.isEmpty(enginenumber) && !TextUtils.isEmpty(vehicleidnumber) && !TextUtils.isEmpty(cityname) && !TextUtils.isEmpty(hpzl)) {
            plateNumber = platenumber;
            engineNumber = enginenumber;
            vehicleIdNumber = vehicleidnumber;
            cityName = cityname;
            mHpzl = hpzl;
            mDialog.show();
            accesNetBreakRuleQuery();
        } else {
            ToastUtils.showBgResource(mActivity, "有输入项为空！");
        }
    }
}
