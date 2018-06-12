package com.qin.fragment.drawer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.qin.R;
import com.qin.fragment.BaseFragment;
import com.qin.util.ScreenUtils;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class ProducerFragment extends BaseFragment {
    @BindView(R.id.webView)
    WebView mWebView;
//    @BindView(R.id.refreshLayout)
//    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    Unbinder unbinder;
    private Dialog mDialog;

    @Override
    protected void initData() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshLayout) {
//
//            }
//        });
//        mRefreshLayout.autoRefresh();
        mWebView.loadUrl("https://github.com/qindaliang");

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
           //     mRefreshLayout.finishRefresh();
                view.loadUrl(String.format(Locale.CHINA, "javascript:document.body.style.paddingTop='%fpx'; void 0", DensityUtil.px2dp(mWebView.getPaddingTop())));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDialog.dismiss();
                }
            },500);
            }
        });
    }
    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producer, null, false);
        unbinder = ButterKnife.bind(this, view);
        initDialog();
        mDialog.show();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
