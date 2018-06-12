package com.qin.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.qin.R;
import com.qin.constant.ConstantValues;
import com.qin.pojo.registeruser.Register;
import com.qin.util.ToastUtils;
import com.qin.view.dialog.SuccessTickView;
import com.qin.view.textview.MyTextWatcher;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.tv_register_step_one)
    TextView tvRegisterStepOne;
    @BindView(R.id.line_register_one)
    View lineRegisterOne;
    @BindView(R.id.tv_register_step_two)
    TextView tvRegisterStepTwo;
    @BindView(R.id.line_register_two)
    View lineRegisterTwo;
    @BindView(R.id.tv_register_step_three)
    TextView tvRegisterStepThree;
    @BindView(R.id.iv_register_step_one)
    ImageView ivRegisterStepOne;
    @BindView(R.id.iv_register_step_two)
    ImageView ivRegisterStepTwo;
    @BindView(R.id.iv_register_step_three)
    ImageView ivRegisterStepThree;
    @BindView(R.id.et_register_phone)
    EditText etRegisterPhone;
    @BindView(R.id.iv_register_clean_phone)
    ImageView ivRegisterCleanPhone;
    @BindView(R.id.et_register_password)
    EditText etRegisterPassword;
    @BindView(R.id.iv_register_clean_password)
    ImageView ivRegisterCleanPassword;
    @BindView(R.id.et_register_password_con)
    EditText etRegisterPasswordCon;
    @BindView(R.id.iv_register_clean_passwordcon)
    ImageView ivRegisterCleanPasswordcon;
    @BindView(R.id.et_register_name)
    EditText etRegisterName;
    @BindView(R.id.iv_register_clean_name)
    ImageView ivRegisterCleanName;
    @BindView(R.id.et_register_chepai)
    EditText etRegisterChepai;
    @BindView(R.id.iv_register_clean_chepai)
    ImageView ivRegisterCleanChepai;
    @BindView(R.id.et_register_chejia)
    EditText etRegisterChejia;
    @BindView(R.id.iv_register_clean_chejia)
    ImageView ivRegisterCleanChejia;
    @BindView(R.id.et_register_enginenumber)
    EditText etRegisterEnginenumber;
    @BindView(R.id.iv_register_clean_enginenumber)
    ImageView ivRegisterCleanEnginenumber;
    @BindView(R.id.et_register_insurancename)
    EditText etRegisterInsurancename;
    @BindView(R.id.iv_register_clean_insurancename)
    ImageView ivRegisterCleanInsurancename;
    @BindView(R.id.et_register_insurancetel)
    EditText etRegisterInsurancetel;
    @BindView(R.id.iv_register_clean_insurancetel)
    ImageView ivRegisterCleanInsurancetel;
    @BindView(R.id.et_register_baodan)
    EditText etRegisterBaodan;
    @BindView(R.id.iv_register_clean_baodan)
    ImageView ivRegisterCleanBaodan;
    @BindView(R.id.et_register_producer)
    EditText etRegisterProducer;
    @BindView(R.id.iv_register_clean_producer)
    ImageView ivRegisterCleanProducer;
    @BindView(R.id.et_register_dealer)
    EditText etRegisterDealer;
    @BindView(R.id.iv_register_clean_dealer)
    ImageView ivRegisterCleanDealer;
    @BindView(R.id.btn_register_one_next)
    Button btnRegisterOneNext;
    @BindView(R.id.ll_register_one_content)
    LinearLayout llRegisterOneContent;
    @BindView(R.id.et_register_two_code)
    EditText etRegisterTwoCode;
    @BindView(R.id.btn_register_two_getcode)
    Button btnRegisterTwoGetcode;
    @BindView(R.id.btn_register_two_register)
    Button btnRegisterTwoRegister;
    @BindView(R.id.ll_register_two_content)
    LinearLayout llRegisterTwoContent;
    @BindView(R.id.register_mask_right_success)
    View registerMaskRightSuccess;
    @BindView(R.id.register_mask_left_success)
    View registerMaskLeftSuccess;
    @BindView(R.id.register_successtick)
    SuccessTickView registerSuccesstick;
    @BindView(R.id.btn_register_three_login)
    Button btnRegisterThreeLogin;
    @BindView(R.id.ll_registered_exit)
    LinearLayout llRegisteredExit;
    @BindView(R.id.ll_registered_error)
    LinearLayout llRegisteredError;
    @BindView(R.id.ll_registered_success)
    LinearLayout llRegisteredSuccess;
    @BindView(R.id.btn_register_three_exit)
    Button mBtnRegisterThreeExit;
    @BindView(R.id.btn_register_three_error)
    Button mBtnRegisterThreeError;
    @BindView(R.id.toobar)
    Toolbar mToobar;
    @BindView(R.id.tv_register_upload)
    TextView mTvRegisterUpload;
    @BindView(R.id.iv_register_upload)
    ImageView mIvRegisterUpload;
    private int i = -1;
    private Handler mHandler = new Handler();
    private View mView;
    private String code;
    private View mMask_left_success;
    private View mMask_right_success;
    private LinearLayout mLl_registering;
    private LinearLayout mLl_registered_success;
    private SuccessTickView mMSuccessTick;
    private AnimationSet mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initTextWatcherListener();
        setSupportActionBar(mToobar);
        mToobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initTextWatcherListener() {
        etRegisterPhone.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterPhone.getText())) {
                    ivRegisterCleanPhone.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanPhone.setVisibility(View.GONE);
                }
            }
        });

        etRegisterPassword.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterPassword.getText())) {
                    ivRegisterCleanPassword.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanPassword.setVisibility(View.GONE);
                }
            }
        });

        etRegisterPasswordCon.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterPasswordCon.getText())) {
                    ivRegisterCleanPasswordcon.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanPasswordcon.setVisibility(View.GONE);
                }
            }
        });
        etRegisterName.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterName.getText())) {
                    ivRegisterCleanName.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanName.setVisibility(View.GONE);
                }
            }
        });
        etRegisterChepai.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterChepai.getText())) {
                    ivRegisterCleanChepai.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanChepai.setVisibility(View.GONE);
                }
            }
        });
        etRegisterChejia.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterChejia.getText())) {
                    ivRegisterCleanChejia.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanChejia.setVisibility(View.GONE);
                }
            }
        });
        etRegisterEnginenumber.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterEnginenumber.getText())) {
                    ivRegisterCleanEnginenumber.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanEnginenumber.setVisibility(View.GONE);
                }
            }
        });
        etRegisterInsurancename.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterInsurancename.getText())) {
                    ivRegisterCleanInsurancename.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanInsurancename.setVisibility(View.GONE);
                }
            }
        });
        etRegisterInsurancetel.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterInsurancetel.getText())) {
                    ivRegisterCleanInsurancetel.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanInsurancetel.setVisibility(View.GONE);
                }
            }
        });
        etRegisterBaodan.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterBaodan.getText())) {
                    ivRegisterCleanBaodan.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanBaodan.setVisibility(View.GONE);
                }
            }
        });
        etRegisterProducer.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterProducer.getText())) {
                    ivRegisterCleanProducer.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanProducer.setVisibility(View.GONE);
                }
            }
        });
        etRegisterDealer.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterDealer.getText())) {
                    ivRegisterCleanDealer.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanDealer.setVisibility(View.GONE);
                }
            }
        });
    }


    @OnClick({R.id.tv_register_upload,R.id.iv_register_upload,R.id.btn_register_three_exit, R.id.btn_register_three_error, R.id.iv_register_clean_phone, R.id.iv_register_clean_password, R.id.iv_register_clean_passwordcon, R.id.iv_register_clean_name, R.id.iv_register_clean_chepai, R.id.iv_register_clean_chejia, R.id.iv_register_clean_enginenumber, R.id.iv_register_clean_insurancename, R.id.iv_register_clean_insurancetel, R.id.iv_register_clean_baodan, R.id.iv_register_clean_producer, R.id.iv_register_clean_dealer, R.id.btn_register_one_next, R.id.btn_register_two_getcode, R.id.btn_register_two_register, R.id.btn_register_three_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_register_clean_phone:
                ivRegisterCleanPhone.setVisibility(View.GONE);
                etRegisterPhone.setText("");
                break;
            case R.id.iv_register_clean_password:
                etRegisterPassword.setText("");
                ivRegisterCleanPassword.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_passwordcon:
                etRegisterPasswordCon.setText("");
                ivRegisterCleanPasswordcon.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_name:
                etRegisterName.setText("");
                ivRegisterCleanName.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_chepai:
                etRegisterChepai.setText("");
                ivRegisterCleanChepai.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_chejia:
                etRegisterChejia.setText("");
                ivRegisterCleanChejia.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_enginenumber:
                etRegisterEnginenumber.setText("");
                ivRegisterCleanEnginenumber.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_insurancename:
                etRegisterInsurancename.setText("");
                ivRegisterCleanInsurancename.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_insurancetel:
                etRegisterInsurancetel.setText("");
                ivRegisterCleanInsurancetel.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_baodan:
                etRegisterBaodan.setText("");
                ivRegisterCleanBaodan.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_producer:
                etRegisterProducer.setText("");
                ivRegisterCleanProducer.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_dealer:
                etRegisterDealer.setText("");
                ivRegisterCleanDealer.setVisibility(View.GONE);
                break;
            case R.id.iv_register_upload:
                break;
            case R.id.tv_register_upload:
                RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(RegisterActivity.this, TITLE);
                dialogChooseImage.show();
                break;
            case R.id.btn_register_one_next:
                if (TextUtils.isEmpty(etRegisterPhone.getText().toString().trim())
                        || TextUtils.isEmpty(etRegisterPhone.getText().toString().trim())
                        || TextUtils.isEmpty(etRegisterPassword.getText().toString().trim())
                        || TextUtils.isEmpty(etRegisterPasswordCon.getText().toString().trim())
                        || TextUtils.isEmpty(etRegisterName.getText().toString().trim())
                        ) {
                    ToastUtils.showBgResource(RegisterActivity.this, "有必填项未完成！");
                } else {
                    Animator animatorleft = AnimatorInflater.loadAnimator(this, R.animator.animator_register_stepleftexit);
                    animatorleft.setTarget(llRegisterOneContent);
                    animatorleft.start();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            llRegisterOneContent.setVisibility(View.GONE);
                        }
                    }, 500);
                    llRegisterTwoContent.setVisibility(View.VISIBLE);
                    Animator animatorright = AnimatorInflater.loadAnimator(this, R.animator.animator_register_steprightenter);
                    animatorright.setTarget(llRegisterTwoContent);
                    animatorright.start();
                    lineRegisterOne.setBackgroundColor(getResources().getColor(R.color.green));
                    tvRegisterStepTwo.setBackground(getResources().getDrawable(R.drawable.vector_register_step_selected));
                }
                break;
            case R.id.btn_register_two_getcode:

                break;
            case R.id.btn_register_two_register:
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
                params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.7f);
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final Dialog dialog = new Dialog(RegisterActivity.this);
                mView = getLayoutInflater().inflate(R.layout.dialog_ok, null, false);
                dialog.setContentView(mView, params);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                mMask_left_success = mView.findViewById(R.id.mask_left_success);
                mMask_right_success = mView.findViewById(R.id.mask_right_success);
                final View mask_left_error = mView.findViewById(R.id.mask_left_error);
                final View mask_right_error = mView.findViewById(R.id.mask_right_error);
                final ImageView iv_x = mView.findViewById(R.id.iv_x);
                final ImageView iv_network_error = mView.findViewById(R.id.iv_network_error);
                mMSuccessTick = (SuccessTickView) mView.findViewById(R.id.success_tick);
                mLl_registering = mView.findViewById(R.id.ll_registering);
                mLl_registered_success = mView.findViewById(R.id.ll_registered_success);
                final LinearLayout ll_registered_error = mView.findViewById(R.id.ll_registered_error);
                final LinearLayout ll_registered_networkerror = mView.findViewById(R.id.ll_registered_networkerror);
                final ProgressWheel progressWheel = mView.findViewById(R.id.progressWheel);
                mAnimation = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.success_mask_layout);
                dialog.show();

                Register register = new Register();
                register.setUser_baodan(etRegisterBaodan.getText().toString().trim());
                register.setUser_baoxian(etRegisterInsurancename.getText().toString().trim());
                register.setUser_baoxiantel(etRegisterInsurancetel.getText().toString().trim());
                register.setUser_chejia(etRegisterChejia.getText().toString().trim());
                register.setUser_chepai(etRegisterChepai.getText().toString().trim());
                register.setUser_dealer(etRegisterDealer.getText().toString().trim());
                register.setUser_engine(etRegisterEnginenumber.getText().toString().trim());
                register.setUser_name(etRegisterName.getText().toString().trim());
                register.setUser_password(etRegisterPassword.getText().toString().trim());
                register.setUser_phonenumber(etRegisterPhone.getText().toString().trim());
                register.setUser_producer(etRegisterProducer.getText().toString().trim());
                Gson gson = new Gson();
                String json = gson.toJson(register);
                Log.i("register", json);
                OkGo.<String>post(ConstantValues.URL_REGISTER).tag(this).upJson(json).execute(new StringCallback() {

                    private String msg;

                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("register", response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            code = jsonObject.getString("code");
                            msg = jsonObject.getString("msg");
                            new CountDownTimer(400 * 7, 400) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    i++;
                                    switch (i) {
                                        case 0:
                                            progressWheel.setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                                            break;
                                        case 1:
                                            progressWheel.setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                                            break;
                                        case 2:
                                            progressWheel.setBarColor(getResources().getColor(R.color.success_stroke_color));
                                            break;
                                        case 3:
                                            progressWheel.setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                                            break;
                                        case 4:
                                            progressWheel.setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                                            break;
                                        case 5:
                                            progressWheel.setBarColor(getResources().getColor(R.color.warning_stroke_color));
                                            break;
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    i = -1;
                                    if (code.equals("0")) {
                                        accessNetPic();

                                    } else if (code.equals("1")) {
                                        //网络错误显示对话框
                                        mLl_registering.setVisibility(View.GONE);
                                        ll_registered_networkerror.setVisibility(View.VISIBLE);
                                        AnimationSet set2 = new AnimationSet(RegisterActivity.this, null);
                                        ScaleAnimation scaleAnimation2 = new ScaleAnimation(
                                                0, 1,
                                                0, 1,
                                                Animation.RELATIVE_TO_SELF, 0.5f,
                                                Animation.RELATIVE_TO_SELF, 0.5f);
                                        scaleAnimation2.setDuration(500);
                                        AlphaAnimation alphaAnimation2 = new AlphaAnimation(0, 1);
                                        alphaAnimation2.setDuration(500);
                                        set2.addAnimation(scaleAnimation2);
                                        set2.addAnimation(alphaAnimation2);
                                        iv_network_error.startAnimation(set2);
                                        showThreeContent(llRegisteredError);

                                    } else if (code.equals("2")) {
                                        // 注册失败显示对话框
                                        mLl_registering.setVisibility(View.GONE);
                                        ll_registered_error.setVisibility(View.VISIBLE);
                                        mask_left_error.startAnimation(mAnimation.getAnimations().get(0));
                                        mask_right_error.startAnimation(mAnimation.getAnimations().get(1));

                                        AnimationSet set1 = new AnimationSet(RegisterActivity.this, null);
                                        ScaleAnimation scaleAnimation = new ScaleAnimation(
                                                0, 1,
                                                0, 1,
                                                Animation.RELATIVE_TO_SELF, 0.5f,
                                                Animation.RELATIVE_TO_SELF, 0.5f);
                                        scaleAnimation.setDuration(500);
                                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                                        alphaAnimation.setDuration(500);
                                        set1.addAnimation(scaleAnimation);
                                        set1.addAnimation(alphaAnimation);
                                        iv_x.startAnimation(set1);
                                        showThreeContent(llRegisteredExit);

                                    }

                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }
                            }.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showBgResource(RegisterActivity.this, msg);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.i("register", "");
                        //网络错误显示对话框
                        mLl_registering.setVisibility(View.GONE);
                        ll_registered_networkerror.setVisibility(View.VISIBLE);
                        AnimationSet set2 = new AnimationSet(RegisterActivity.this, null);
                        ScaleAnimation scaleAnimation2 = new ScaleAnimation(
                                0, 1,
                                0, 1,
                                Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                        scaleAnimation2.setDuration(500);
                        AlphaAnimation alphaAnimation2 = new AlphaAnimation(0, 1);
                        alphaAnimation2.setDuration(500);
                        set2.addAnimation(scaleAnimation2);
                        set2.addAnimation(alphaAnimation2);
                        iv_network_error.startAnimation(set2);
                        showThreeContent(llRegisteredError);
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.btn_register_three_login:
                String username = etRegisterPhone.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_register_three_exit:
                if (llRegisteredExit.getVisibility() == View.VISIBLE) {
                    Animator animatorleft1 = AnimatorInflater.loadAnimator(this, R.animator.animator_register_steprightexit);
                    animatorleft1.setTarget(llRegisteredSuccess);
                    animatorleft1.start();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            llRegisteredSuccess.setVisibility(View.GONE);
                        }
                    }, 500);
                    llRegisterOneContent.setVisibility(View.VISIBLE);
                    Animator animatorright1 = AnimatorInflater.loadAnimator(this, R.animator.animator_register_stepleftenter);
                    animatorright1.setTarget(llRegisterOneContent);
                    animatorright1.start();
                    lineRegisterOne.setBackgroundColor(getResources().getColor(R.color.gray));
                    lineRegisterTwo.setBackgroundColor(getResources().getColor(R.color.gray));
                    tvRegisterStepThree.setBackground(getResources().getDrawable(R.drawable.vector_register_step_unselected));
                    tvRegisterStepTwo.setBackground(getResources().getDrawable(R.drawable.vector_register_step_unselected));
                }
                llRegisteredExit.setVisibility(View.GONE);
                break;
            case R.id.btn_register_three_error:
                if (llRegisteredError.getVisibility() == View.VISIBLE) {
                    Animator animatorleft2 = AnimatorInflater.loadAnimator(this, R.animator.animator_register_steprightexit);
                    animatorleft2.setTarget(llRegisteredSuccess);
                    animatorleft2.start();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            llRegisteredSuccess.setVisibility(View.GONE);
                        }
                    }, 500);
                    llRegisterOneContent.setVisibility(View.VISIBLE);
                    Animator animatorright2 = AnimatorInflater.loadAnimator(this, R.animator.animator_register_stepleftenter);
                    animatorright2.setTarget(llRegisterOneContent);
                    animatorright2.start();
                    lineRegisterOne.setBackgroundColor(getResources().getColor(R.color.gray));
                    lineRegisterTwo.setBackgroundColor(getResources().getColor(R.color.gray));
                    tvRegisterStepThree.setBackground(getResources().getDrawable(R.drawable.vector_register_step_unselected));
                    tvRegisterStepTwo.setBackground(getResources().getDrawable(R.drawable.vector_register_step_unselected));
                }
                llRegisteredError.setVisibility(View.GONE);
                break;
        }
    }

    private void showThreeContent(final LinearLayout linearLayout) {
        Animator animatorleft1 = AnimatorInflater.loadAnimator(this, R.animator.animator_register_stepleftexit);
        animatorleft1.setTarget(llRegisterTwoContent);
        animatorleft1.start();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                llRegisterTwoContent.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                Animator animatorright1 = AnimatorInflater.loadAnimator(RegisterActivity.this, R.animator.animator_register_steprightenter);
                animatorright1.setTarget(linearLayout);
                animatorright1.start();
            }
        }, 200);
        lineRegisterTwo.setBackgroundColor(getResources().getColor(R.color.green));
        tvRegisterStepThree.setBackground(getResources().getDrawable(R.drawable.vector_register_step_selected));
    }

    private void accessNetPic() {
        BitmapDrawable background = (BitmapDrawable) mIvRegisterUpload.getDrawable();
        Bitmap bitmap = background.getBitmap();
        String base64 = bitmapToBase64(bitmap);
        OkGo.<String>post(ConstantValues.URL_REGISTERPIC
        )
                .tag(this)
                .params("register_pic", base64)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("mIvRegisterUpload", response.body());
                        //注册成功显示对话框
                        mLl_registering.setVisibility(View.GONE);
                        mLl_registered_success.setVisibility(View.VISIBLE);
                        mMask_left_success.startAnimation(mAnimation.getAnimations().get(0));
                        mMask_right_success.startAnimation(mAnimation.getAnimations().get(1));
                        mMSuccessTick.startTickAnim();
                        //TODO 横向注册成功才会变绿
                        Animator animatorleft1 = AnimatorInflater.loadAnimator(RegisterActivity.this, R.animator.animator_register_stepleftexit);
                        animatorleft1.setTarget(llRegisterTwoContent);
                        animatorleft1.start();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                llRegisterTwoContent.setVisibility(View.GONE);
                                llRegisteredSuccess.setVisibility(View.VISIBLE);
                                Animator animatorright1 = AnimatorInflater.loadAnimator(RegisterActivity.this, R.animator.animator_register_steprightenter);
                                animatorright1.setTarget(llRegisteredSuccess);
                                animatorright1.start();
                            }
                        }, 200);
                        lineRegisterTwo.setBackgroundColor(getResources().getColor(R.color.green));
                        tvRegisterStepThree.setBackground(getResources().getDrawable(R.drawable.vector_register_step_selected));
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showBgResource(RegisterActivity.this, "网络错误！");
                    }
                });
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE:
                //选择相册之后的处理
                if (resultCode == RESULT_OK) {
//                  RxPhotoTool.cropImage(ActivityUser.this, );// 裁剪图片
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA:
                //选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                   /* data.getExtras().get("data");*/
//                    RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);
// 裁剪图片
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                }

                break;
            case RxPhotoTool.CROP_IMAGE:
                /// /普通裁剪后的处理
                Glide.with(this).
                        load(RxPhotoTool.cropImageUri).
                        diskCacheStrategy(DiskCacheStrategy.RESULT).
                        bitmapTransform(new CropCircleTransformation(this)).
                        thumbnail(0.5f).
                        placeholder(R.mipmap.upload_camera).
                        priority(Priority.LOW).
                        error(R.mipmap.upload_camera).
                        fallback(R.mipmap.upload_camera).
                        into(mIvRegisterUpload);
//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;

            case UCrop.REQUEST_CROP:
                //UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                        roadImageView(resultUri, mIvRegisterUpload);
                    RxSPTool.putContent(this, "AVATAR", resultUri.toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR:
                //UCrop裁剪错误之后的处理
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, ImageView imageView) {
        Glide.with(RegisterActivity.this).
                load(uri).
                asBitmap().
                into(imageView);

        return (new File(RxPhotoTool.getImageAbsolutePath(RegisterActivity.this, uri)));
    }

    private void initUCrop(Uri uri) {
        //Uri destinationUri = RxPhotoTool.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(RegisterActivity.this.getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(RegisterActivity.this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(RegisterActivity.this, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        //options.setOvalDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }


    @Override
    public void onBackPressed() {
        if (llRegisteredSuccess.getVisibility() == View.VISIBLE) {
            Animator animatorleft = AnimatorInflater.loadAnimator(this, R.animator.animator_register_steprightexit);
            animatorleft.setTarget(llRegisteredSuccess);
            animatorleft.start();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    llRegisteredSuccess.setVisibility(View.GONE);
                }
            }, 500);
            llRegisterOneContent.setVisibility(View.VISIBLE);
            Animator animatorright = AnimatorInflater.loadAnimator(this, R.animator.animator_register_stepleftenter);
            animatorright.setTarget(llRegisterOneContent);
            animatorright.start();
            lineRegisterOne.setBackgroundColor(getResources().getColor(R.color.gray));
            lineRegisterTwo.setBackgroundColor(getResources().getColor(R.color.gray));
            tvRegisterStepThree.setBackground(getResources().getDrawable(R.drawable.vector_register_step_unselected));
            tvRegisterStepTwo.setBackground(getResources().getDrawable(R.drawable.vector_register_step_unselected));
        }
        if (llRegisterTwoContent.getVisibility() == View.VISIBLE) {
            Animator animatorleft = AnimatorInflater.loadAnimator(this, R.animator.animator_register_steprightexit);
            animatorleft.setTarget(llRegisterTwoContent);
            animatorleft.start();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    llRegisterTwoContent.setVisibility(View.GONE);
                }
            }, 500);
            llRegisterOneContent.setVisibility(View.VISIBLE);
            Animator animatorright = AnimatorInflater.loadAnimator(this, R.animator.animator_register_stepleftenter);
            animatorright.setTarget(llRegisterOneContent);
            animatorright.start();
            lineRegisterOne.setBackgroundColor(getResources().getColor(R.color.gray));
            tvRegisterStepTwo.setBackground(getResources().getDrawable(R.drawable.vector_register_step_unselected));
        } else {
            finish();
        }
    }
}
