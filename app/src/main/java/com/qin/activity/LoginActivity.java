package com.qin.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.adapter.recyclerview.LoginRecyclerViewAdapter;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.dao.LoginUsers;
import com.qin.util.KeyboardTool;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.qin.view.button.CircularProgressButton;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.iv_login_logo)
    ImageView ivLoginLogo;
    @BindView(R.id.ll_login_username)
    LinearLayout llLoginUsername;
    @BindView(R.id.et_login_username)
    EditText etLoginUsername;
    @BindView(R.id.iv_login_clear_username)
    ImageView ivLoginClearUsername;
    @BindView(R.id.iv_login_more_user)
    ImageView ivLoginMoreUser;
    @BindView(R.id.tv_login_error_username)
    TextView tvLoginErrorUsername;
    @BindView(R.id.ll_login_password)
    LinearLayout llLoginPassword;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.iv_login_clear_password)
    ImageView ivLoginClearPassword;
    @BindView(R.id.iv_login_show_password)
    ImageView ivLoginShowPassword;
    @BindView(R.id.tv_login_error_password)
    TextView tvLoginErrorPassword;
    @BindView(R.id.cb_login_remember_pwd)
    CheckBox cbLoginRememberPwd;
    @BindView(R.id.tv_login_forgetpwd)
    TextView tvLoginForgetpwd;
    @BindView(R.id.tv_login_register)
    TextView tvLoginRegister;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.btn_login)
    CircularProgressButton btnLogin;
    @BindView(R.id.ll_login_bottom)
    LinearLayout llLoginBottom;
    @BindView(R.id.rl_login_content)
    RelativeLayout rlLoginContent;
    @BindView(R.id.fl_login_container)
    FrameLayout flLoginContainer;
    private boolean showPassword = false;
    private View mPopView;
    private PopupWindow mPopupWindow;
    private boolean isUp = false;
    private List<LoginUsers> users = new ArrayList<>();
    ObjectAnimator animator2, animator1;
    private LinearLayout.LayoutParams mParams;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initDialog();
        //logo开启动画
        ((Animatable) ivLoginLogo.getDrawable()).start();
        mParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mParams.weight = (int) 0.75;
        boolean isChecked = SPUtils.getInstance(this).getBoolean(ConstantValues.LOGIN_CHECKBOX, false);
        //判断复选框是否被选中
        if (isChecked) {
            //获取保存到sp中的数据
            String username = SPUtils.getInstance(this).getString(ConstantValues.LOGIN_USERNAME, "");
            String password = SPUtils.getInstance(this).getString(ConstantValues.LOGIN_PASSWORD, "");
            etLoginUsername.setText(username);
            etLoginPassword.setText(password);
            cbLoginRememberPwd.setChecked(isChecked);

            if (!TextUtils.isEmpty(username)) {
                translateUp(llLoginUsername);
                ivLoginClearUsername.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(password)) {
                translateUp(llLoginPassword);
                ivLoginClearPassword.setVisibility(View.VISIBLE);
            }
        }
        //获取保存到sp中的list数据
        String json = SPUtils.getInstance(this).getString(ConstantValues.LOGIN_USERSLIST, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<LoginUsers>>() {
            }.getType();
            List<LoginUsers> userInfos = new ArrayList<>();
            userInfos = gson.fromJson(json, type);
            for (int i = 0; i < userInfos.size(); i++) {
                Log.d(TAG, userInfos.get(i).getUser_phonenumber() + ":" + userInfos.get(i).getUser_password());
                users.add(userInfos.get(i));
            }
        }

        btnLogin.setIndeterminateProgressMode(true);
        /**
         *    监听用户名编辑框焦点改变
         */
        etLoginUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(etLoginUsername.getText())) {
                        translateUp(llLoginUsername);
                    }
                } else {
                    username();
                    if (TextUtils.isEmpty(etLoginUsername.getText())) {
                        translateDown(llLoginUsername);
                    } else {

                    }
                }
            }
        });
        /**
         *     监听密码编辑框焦点改变
         */
        etLoginPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(etLoginPassword.getText())) {
                        translateUp(llLoginPassword);
                    }
                } else {
                    password();
                    if (TextUtils.isEmpty(etLoginPassword.getText())) {
                        translateDown(llLoginPassword);
                    } else {

                    }
                }
            }
        });
        /**
         *  监听用户名编辑框内容改变
         */
        etLoginUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                username();
            }
        });
        /**
         *  监听密码编辑框内容改变
         */
        etLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password();
            }
        });

    }

    /**
     * 上移动画
     * @param view
     */
    private void translateUp(View view) {
        AnimationSet set = new AnimationSet(false);
        TranslateAnimation t = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, -1.3f);
        t.setDuration(500);

        ScaleAnimation s = new ScaleAnimation(1f, 0.7f, 1, 0.7f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        s.setDuration(500);
        s.setRepeatCount(1);
        s.setRepeatMode(Animation.REVERSE);

        AlphaAnimation a = new AlphaAnimation(1f, 0.2f);
        a.setDuration(500);
        a.setRepeatMode(Animation.REVERSE);
        a.setRepeatCount(1);

        set.addAnimation(t);
        set.addAnimation(s);
        set.addAnimation(a);
        set.setFillAfter(true);
        view.startAnimation(set);
    }

    /**
     * 下移动画
     * @param view
     */
    private void translateDown(View view) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation t = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, -1.3f
                , Animation.RELATIVE_TO_SELF, 0);
        t.setDuration(500);
        t.setFillAfter(true);

        ScaleAnimation s = new ScaleAnimation(1f, 0.7f, 1f, 0.7f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        s.setDuration(500);
        s.setFillAfter(true);
        s.setRepeatCount(1);
        s.setRepeatMode(Animation.REVERSE);

        AlphaAnimation a = new AlphaAnimation(1f, 0.2f);
        a.setDuration(500);
        a.setRepeatMode(Animation.REVERSE);
        a.setRepeatCount(1);

        set.addAnimation(t);
        set.addAnimation(s);
        view.startAnimation(set);
    }

    /**
     * 初始化popwindow
     * @param layoutRes
     * @param width
     * @param height
     */
    public void initPopWindow(int layoutRes, int width, int height) {
        mPopView = View.inflate(this, R.layout.pop_login_user, null);
        mPopupWindow = new PopupWindow(mPopView, width, height, true);
        RecyclerView rlvLoginUser = mPopView.findViewById(R.id.rlv_login_user);
        final LinearLayout llLoginDelete = mPopView.findViewById(R.id.ll_login_delete);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvLoginUser.setLayoutManager(layoutManager);
        final LoginRecyclerViewAdapter adapter = new LoginRecyclerViewAdapter(users);
        rlvLoginUser.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rlvLoginUser.setItemAnimator(new DefaultItemAnimator());
        rlvLoginUser.setAdapter(adapter);
        adapter.setOnItemClickListener(new LoginRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, LoginRecyclerViewAdapter.ViewName name, final int postion) {
                if (LoginRecyclerViewAdapter.ViewName.ITEM == name) {
                    RelativeLayout layout = (RelativeLayout) layoutManager.findViewByPosition(postion);
                    TextView text = (TextView) layout.getChildAt(1);
                    etLoginUsername.setText(users.get(postion).getUser_phonenumber());
                    etLoginPassword.setText(users.get(postion).getUser_password());
                    mPopupWindow.dismiss();
                }
                if (LoginRecyclerViewAdapter.ViewName.IMAGEVIEW == name) {
                    users.remove(postion);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        llLoginDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.clear();
                adapter.notifyDataSetChanged();
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivLoginMoreUser.setImageResource(R.mipmap.down_login);
            }
        });
        mPopupWindow.setAnimationStyle(R.style.popSearchAnimtion);
        mPopupWindow.showAsDropDown(etLoginUsername, 0, 5);
    }

    /**
     * 跳转activity的回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10) {
            Bundle bundle = data.getExtras();
            String username = bundle.getString("username", "");
            String password = bundle.getString("password", "");
            Log.i(TAG, username + password);
            etLoginUsername.setText(username);
            etLoginPassword.setText(password);
            translateUp(llLoginUsername);
            translateUp(llLoginPassword);
        }
    }

    @SuppressLint("RestrictedApi")
    @OnClick({R.id.iv_login_logo, R.id.iv_login_clear_username, R.id.iv_login_more_user, R.id.iv_login_clear_password, R.id.iv_login_show_password, R.id.tv_login_forgetpwd, R.id.tv_login_register, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_login_logo:
                break;
            case R.id.iv_login_clear_username:
                etLoginUsername.setText("");
                break;
            case R.id.iv_login_more_user:
                //    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (isSoftShowing()) {
                    KeyboardTool.hideKeyboard(this, ivLoginMoreUser);
                }
                ivLoginMoreUser.setImageResource(R.mipmap.up_login);
                initPopWindow(R.layout.pop_login_user, etLoginUsername.getWidth(), (int) (ScreenUtils.getWindowHeight(this) / 2.3));

                break;
            case R.id.iv_login_clear_password:
                etLoginPassword.setText("");
                break;
            case R.id.iv_login_show_password:
                if (showPassword) {
                    // 显示密码
                    ivLoginShowPassword.setImageDrawable(getResources().getDrawable(R.mipmap.eye_open));
                    etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword = !showPassword;
                } else {
                    // 隐藏密码
                    ivLoginShowPassword.setImageDrawable(getResources().getDrawable(R.mipmap.eye_close));
                    etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword = !showPassword;
                }
                break;
            case R.id.tv_login_forgetpwd:
                Intent intent1 = new Intent();
                intent1.setClass(this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.tv_login_register:
                Intent intent = new Intent();
                intent.setClass(this, RegisterActivity.class);
                startActivityForResult(intent, 10, ActivityOptionsCompat.makeSceneTransitionAnimation(this, tvLoginRegister, "tvLoginRegister").toBundle());
                break;
            case R.id.btn_login:
                btnLogin.setVisibility(View.GONE);
                mDialog.show();
                btnLogin.setProgress(50);
                if (username() && password()) {
                    // 进行网络请求
                    String userName = etLoginUsername.getText().toString().trim();
                    String passWord = etLoginPassword.getText().toString().trim();
                    LoginUsers user = new LoginUsers();
                    user.setUser_phonenumber(userName);
                    user.setUser_password(passWord);
                    users.add(user);
                    HashSet<LoginUsers> h = new HashSet(users);
                    users.clear();
                    users.addAll(h);
                    Gson gson = new Gson();
                    String json = gson.toJson(users);
                    // 上传用户名和密码
                    accessNet(userName, passWord);
                    // 登陆成功才保存用户名和密码
                    if (cbLoginRememberPwd.isChecked()) {
                        SPUtils.getInstance(this).putString(ConstantValues.LOGIN_USERNAME, userName, true);
                        SPUtils.getInstance(this).putString(ConstantValues.LOGIN_PASSWORD, passWord, true);
                        SPUtils.getInstance(this).putString(ConstantValues.LOGIN_USERSLIST, json, true);
                    }
                    SPUtils.getInstance(this).putBoolean(ConstantValues.LOGIN_CHECKBOX, cbLoginRememberPwd.isChecked(), true);
                }
                break;
        }
    }

    private void accessNet(String userName, String passWord) {
//        LoginUsers user = new LoginUsers();
//        user.setUser_phonenumber(userName);
//        user.setUser_password(passWord);
//        Gson gson = new Gson();
//        final String json = gson.toJson(user);
        OkGo.<String>post(ConstantValues.URL_LOGIN)
                .tag(this)
                .params("user_phonenumber", userName)
                .params("user_password", passWord)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("login", response.body());
                        // 处理服务器返回的信息\
                        mDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String code = jsonObject.getString("code");
                            if (code.equals("0")) {
                                String user_id = jsonObject.getString("user_id");
                                String user_name = jsonObject.getString("user_name");
                                String user_phonenumber = jsonObject.getString("user_phonenumber");
                                MyApplication.setUserid(user_id);
                                SPUtils.getInstance(LoginActivity.this).putString("user_id", user_id, true);
                                SPUtils.getInstance(LoginActivity.this).putString("user_name", user_name, true);
                                SPUtils.getInstance(LoginActivity.this).putString("user_phonenumber", user_phonenumber, true);
                                Log.i("login", code + "");
                                EnterMainActivity(user_id);
                            } else {
                                btnLogin.setVisibility(View.VISIBLE);
                                btnLogin.setProgress(-1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            btnLogin.setVisibility(View.VISIBLE);
                            btnLogin.setProgress(-1);
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        btnLogin.setVisibility(View.VISIBLE);
                        btnLogin.setProgress(0);
                        ToastUtils.showBgResource(LoginActivity.this, "网络错误！");
                    }
                });
    }

    /**
     * 判断用户名是否为空
     * @return
     */
    private boolean username() {
        if (TextUtils.isEmpty(etLoginUsername.getText().toString().trim())) {
            ivLoginClearUsername.setVisibility(View.INVISIBLE);
            tvLoginErrorUsername.setVisibility(View.VISIBLE);
            return false;
        } else {
            ivLoginClearUsername.setVisibility(View.VISIBLE);
            tvLoginErrorUsername.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    /**
     * 判断密码是否为空
     * @return
     */
    private boolean password() {
        if (TextUtils.isEmpty(etLoginPassword.getText().toString().trim())) {
            ivLoginClearPassword.setVisibility(View.INVISIBLE);
            tvLoginErrorPassword.setVisibility(View.VISIBLE);
            return false;
        } else {
            ivLoginClearPassword.setVisibility(View.VISIBLE);
            tvLoginErrorPassword.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    /**
     * 判断软键盘是否可见
     * @return
     */
    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }

    @Override
    public void onBackPressed() {
        if (btnLogin.getProgress() != 0) {
            btnLogin.setProgress(0);
        } else {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 界面跳转
     * @param id
     */
    private void EnterMainActivity(String id) {
        Intent intent = new Intent();
        intent.putExtra("user_id", id);
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 初始化对话框
     */
    private void initDialog() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(this), ScreenUtils.getWindowHeight(this));
        params.width = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        params.height = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        mDialog = new Dialog(this);
        View view = View.inflate(this, R.layout.dialog_login, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                btnLogin.setVisibility(View.VISIBLE);
//                btnLogin.setProgress(0);
            }
        });
    }
}
