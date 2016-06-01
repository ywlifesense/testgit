package cn.js.nanhaistaffhome.activitys;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;

import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.js.nanhaistaffhome.R;

/**
 * Created by JS on 2016/3/21.
 */
public class WebActivity extends BaseActivity {

    //test for update   show


    //teststst
    private TextView titleTv;
    private WebView webView;
    private FrameLayout mFullscreenContainer;
    private FrameLayout mContentView;
    private View mCustomView = null;
    private MyWebChromeClient chromeClient;
    private RelativeLayout tabBar;
    private volatile boolean isX5WebViewEnabled = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        initViews();
        initWebView();

        if (getPhoneAndroidSDK() >= 14) {// 4.0 需打开硬件加速
            getWindow().setFlags(0x1000000, 0x1000000);
        }

        preinitX5WebCore();
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        titleTv.setText(title);
         webView.loadUrl(url);


//
//        webView.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//                webView.loadUrl(url);
//                return true;
//            }
//        });
    }


    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {// preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view
            QbSdk.preInit(this, myCallback);// 设置X5初始化完成的回调接口
            // 第三个参数为true：如果首次加载失败则继续尝试加载；
        }
    }

    private QbSdk.PreInitCallback myCallback = new QbSdk.PreInitCallback() {

        @Override
        public void onViewInitFinished() {// 当X5webview 初始化结束后的回调
            new WebView(WebActivity.this);
            WebActivity.this.isX5WebViewEnabled = true;
        }

        @Override
        public void onCoreInitFinished() {
        }
    };

    private void initViews() {
        titleTv = (TextView) findViewById(R.id.tv_title);
        webView = (WebView) findViewById(R.id.webview);
        tabBar = (RelativeLayout) findViewById(R.id.tab_bar);
        mFullscreenContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
        mContentView = (FrameLayout) findViewById(R.id.main_content);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.

                SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.

                SOFT_INPUT_STATE_HIDDEN);

    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        settings.setPluginsEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }


        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//land 横屏显示  隐藏TabBar
            tabBar.setVisibility(View.GONE);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//port 竖屏显示
            tabBar.setVisibility(View.VISIBLE);
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            webView.loadData("", "text/html; charset=UTF-8", null);
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    class MyWebChromeClient extends WebChromeClient {

        private CustomViewCallback mCustomViewCallback;
        public int mOriginalOrientation = 1;


        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (myCallback != null) {
                Log.e("WebAty", "调用onShowCustomView方法");
               myCallback.onCustomViewHidden();
                myCallback = null;
                return;
            }

            long id = Thread.currentThread().getId();
            ViewGroup parent = (ViewGroup) webView.getParent();
            String s = parent.getClass().getName();
            parent.removeView(webView);
            parent.addView(view);
            myView = view;
            myCallback = callback;
            chromeClient = this;
        }

        private View myView = null;
        private CustomViewCallback myCallback = null;


        public void onHideCustomView() {

            Log.e("WebAty", "调用onHideCustomView方法");


            long id = Thread.currentThread().getId();


            if (myView != null) {

                if (myCallback != null) {
                    myCallback.onCustomViewHidden();
                    myCallback = null;
                }

                ViewGroup parent = (ViewGroup) myView.getParent();
                parent.removeView(myView);
                parent.addView(webView);
                myView = null;
            }
        }
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showProgressDialog("正在请求新闻，请稍后...");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideProgressDialog();
            super.onPageFinished(view, url);
        }

    }

    public static int getPhoneAndroidSDK() {
        // TODO Auto-generated method stub
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;

    }

    @Override
    public void onPause() {// 继承自Activity
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {// 继承自Activity
        super.onResume();
        webView.onResume();
    }

}
