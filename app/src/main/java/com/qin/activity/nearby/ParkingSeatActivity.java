package com.qin.activity.nearby;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviTheme;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.qin.R;
import com.qin.constant.ConstantValues;
import com.qin.util.ToastUtils;
import com.qin.view.ParkingSeat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/5 0005.
 */

public class ParkingSeatActivity extends AppCompatActivity implements INaviInfoCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.parking_seat)
    ParkingSeat parkingSeat;
    @BindView(R.id.tv_parking_number)
    TextView tvParkingNumber;
    @BindView(R.id.btn_parking_order)
    Button btnParkingOrder;
    @BindView(R.id.btn_parking_life)
    TextView btnParkingLife;
    @BindView(R.id.btn_parking_xisan)
    TextView btnParkingXisan;
    @BindView(R.id.btn_parking_xujingge)
    TextView btnParkingXujingge;
    @BindView(R.id.btn_parking_kejiyuan)
    TextView btnParkingKejiyuan;
    @BindView(R.id.parking_seat_xisan)
    ParkingSeat parkingSeatXisan;
    @BindView(R.id.parking_seat_xujingge)
    ParkingSeat parkingSeatXujingge;
    @BindView(R.id.parking_seat_kejiyuan)
    ParkingSeat parkingSeatKejiyuan;
    @BindView(R.id.btn_parking_go)
    Button btnParkingGo;
    private String mLat;
    private String mLon;
    private String mName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_seat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("福州大学停车场");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLat = getIntent().getExtras().getString(ConstantValues.PARKING_LAT, null);
        mLon = getIntent().getExtras().getString(ConstantValues.PARKING_LON, null);
        mName = getIntent().getExtras().getString(ConstantValues.PARKING_POI, null);
        Log.i("latlng",mLat+"================"+mLon);

        parkingSeat.setScreenName("福州大学生活区停车场");//设置屏幕名称
        parkingSeat.setMaxSelected(1);//设置最多选中
        parkingSeat.setData(7, 10);

        parkingSeatXisan.setScreenName("西三停车场");//设置屏幕名称
        parkingSeatXisan.setMaxSelected(1);//设置最多选中
        parkingSeatXisan.setData(6, 15);

        parkingSeatXujingge.setScreenName("虚静阁停车场");//设置屏幕名称
        parkingSeatXujingge.setMaxSelected(1);//设置最多选中
        parkingSeatXujingge.setData(8, 12);

        parkingSeatKejiyuan.setScreenName("福州大学科技园车场");//设置屏幕名称
        parkingSeatKejiyuan.setMaxSelected(1);//设置最多选中
        parkingSeatKejiyuan.setData(10, 20);

        parkingSeat.setSeatChecker(new ParkingSeat.SeatChecker() {
            @Override
            public boolean isValidSeat(int row, int column) {
                return !(column == 2 || column == 12);
            }

            @Override
            public boolean isSold(int row, int column) {
                boolean a = row == 1 && column == 1 || row == 4 && column == 5
                        || row == 4 && column == 8
                        || row == 5 && column == 5
                        || row == 4 && column == 2
                        || row == 2 && column == 10
                        || row == 6 && column == 9;
                return a;
            }

            @Override
            public void checked(int row, int column) {
                Log.i("aaa", row + "--checked---" + column);

            }

            @Override
            public void unCheck(int row, int column) {
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }
        });
        parkingSeatXisan.setSeatChecker(new ParkingSeat.SeatChecker() {
            @Override
            public boolean isValidSeat(int row, int column) {
                return !(column == 2 || column == 12);
            }

            @Override
            public boolean isSold(int row, int column) {
                boolean a = row == 1 && column == 1 || row == 4 && column == 5
                        || row == 4 && column == 8
                        || row == 5 && column == 5
                        || row == 4 && column == 2
                        || row == 2 && column == 10
                        || row == 6 && column == 9;
                return a;
            }

            @Override
            public void checked(int row, int column) {
                Log.i("aaa", row + "--checked---" + column);

            }

            @Override
            public void unCheck(int row, int column) {
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }
        });
        parkingSeatXujingge.setSeatChecker(new ParkingSeat.SeatChecker() {
            @Override
            public boolean isValidSeat(int row, int column) {
                return !(column == 2 || column == 12);
            }

            @Override
            public boolean isSold(int row, int column) {
                boolean a = row == 1 && column == 1 || row == 4 && column == 5
                        || row == 4 && column == 8
                        || row == 5 && column == 5
                        || row == 4 && column == 2
                        || row == 2 && column == 10
                        || row == 6 && column == 9;
                return a;
            }

            @Override
            public void checked(int row, int column) {
                Log.i("aaa", row + "--checked---" + column);

            }

            @Override
            public void unCheck(int row, int column) {
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }
        });
        parkingSeatKejiyuan.setSeatChecker(new ParkingSeat.SeatChecker() {
            @Override
            public boolean isValidSeat(int row, int column) {
                return !(column == 2 || column == 12);
            }

            @Override
            public boolean isSold(int row, int column) {
                boolean a = row == 1 && column == 1 || row == 4 && column == 5
                        || row == 4 && column == 8
                        || row == 5 && column == 5
                        || row == 4 && column == 2
                        || row == 2 && column == 10
                        || row == 6 && column == 9;
                return a;
            }

            @Override
            public void checked(int row, int column) {
                Log.i("aaa", row + "--checked---" + column);

            }

            @Override
            public void unCheck(int row, int column) {
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }
        });
    }

    @OnClick({R.id.btn_parking_go,R.id.btn_parking_order, R.id.btn_parking_life, R.id.btn_parking_xisan, R.id.btn_parking_xujingge, R.id.btn_parking_kejiyuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_parking_order:
                ToastUtils.showBgResource(this, "预约");
                break;
            case R.id.btn_parking_go:
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext()
                        ,new AmapNaviParams(new Poi(null, null, "")
                                , null
                                , new Poi(mName, new LatLng(Double.parseDouble(mLat),Double.parseDouble(mLon)), "")
                                , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
                        , ParkingSeatActivity.this);
                break;
            case R.id.btn_parking_life:
                parkingSeat.setVisibility(View.VISIBLE);
                parkingSeatXisan.setVisibility(View.GONE);
                parkingSeatXujingge.setVisibility(View.GONE);
                parkingSeatKejiyuan.setVisibility(View.GONE);
                btnParkingLife.setBackgroundResource(R.drawable.shape_theme_background_shadow);
                btnParkingKejiyuan.setBackground(null);
                btnParkingXisan.setBackground(null);
                btnParkingXujingge.setBackground(null);
                break;
            case R.id.btn_parking_xisan:
                parkingSeat.setVisibility(View.GONE);
                parkingSeatXisan.setVisibility(View.VISIBLE);
                parkingSeatXujingge.setVisibility(View.GONE);
                parkingSeatKejiyuan.setVisibility(View.GONE);
                btnParkingLife.setBackground(null);
                btnParkingKejiyuan.setBackground(null);
                btnParkingXisan.setBackgroundResource(R.drawable.shape_theme_background_shadow);
                btnParkingXujingge.setBackground(null);
                break;
            case R.id.btn_parking_xujingge:
                parkingSeat.setVisibility(View.GONE);
                parkingSeatXisan.setVisibility(View.GONE);
                parkingSeatXujingge.setVisibility(View.VISIBLE);
                parkingSeatKejiyuan.setVisibility(View.GONE);
                btnParkingLife.setBackground(null);
                btnParkingKejiyuan.setBackground(null);
                btnParkingXisan.setBackground(null);
                btnParkingXujingge.setBackgroundResource(R.drawable.shape_theme_background_shadow);
                break;
            case R.id.btn_parking_kejiyuan:
                parkingSeat.setVisibility(View.GONE);
                parkingSeatXisan.setVisibility(View.GONE);
                parkingSeatXujingge.setVisibility(View.GONE);
                parkingSeatKejiyuan.setVisibility(View.VISIBLE);
                btnParkingLife.setBackground(null);
                btnParkingKejiyuan.setBackgroundResource(R.drawable.shape_theme_background_shadow);
                btnParkingXisan.setBackground(null);
                btnParkingXujingge.setBackground(null);
                break;
        }
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

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

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }
}
