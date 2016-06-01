package cn.js.nanhaistaffhome.activitys;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.http.HttpConstant;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.utils.CacheManager;
import cn.js.nanhaistaffhome.utils.HtmlImageGetter;
import cn.js.nanhaistaffhome.utils.ImageFileUtil;

/**
 * Created by JS on 8/14/15.
 */
public class NewsDetailActivity extends BaseActivity {

    private long contentId;
    private String content;
    private String date;
    private String[] imgUrls;

    private WebView webView;
    private MyWebChromeClient chromeClient;

    private TextView titleView;
    private TextView secondTleView;
    private TextView contentView;
    private TextView tabTleView;

    private CacheManager cacheManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        titleView = (TextView) findViewById(R.id.tv_title);
        secondTleView = (TextView) findViewById(R.id.tv_second_title);
        contentView = (TextView) findViewById(R.id.tv_content);
        tabTleView = (TextView)findViewById(R.id.tv_navi_title);
        webView = (WebView) findViewById(R.id.webview);


        contentId = getIntent().getLongExtra("contentId", 0);
        String tabTle = getIntent().getStringExtra("title");
        tabTleView.setText(tabTle);

        if (contentId > 0) {
            HttpClient.getNewsDetail(contentId, new OnHttpRequestListener() {
                @Override
                public void onRequestStart() {
                    showProgressDialog("正在请求新闻，请稍后...");
                    pdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            if (!TextUtils.isEmpty(content)) {
                                contentView.setText(Html.fromHtml(content));
                            }
                        }
                    });
                }

                @Override
                public void onRequestEnd(String result) {
                    boolean isHideProgress = true;
                    try {
                        JSONObject obj = new JSONObject(result);
                        if (obj.optInt("code", 1) == 0) {
                            JSONObject news = obj.getJSONObject("data");
                            if (news != null) {
                                content = news.optString("TXT");
//                                content ="<embed width=\"400\" height=\"300\" menu=\"true\" loop=\"true\" play=\"true\" src=\"http://staffhome.nanhai.gov.cn/u/cms/www/201605/201055393v1j.mp4\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\"></embed>";
                                if(content.contains("mp4")){
                                    //说明这里是要添加一个视频播放
                                   initWebView();
                                    //获取url 交给播放器加载
                                   String url = content.substring(content.indexOf("http://staffhome.nanhai.gov.cn"),content.indexOf("mp4")+3);
                                   webView.loadUrl(url);
                                }
                                date = news.optString("RELEASE_DATE");
                                String title = news.optString("TITLE");

                                if (news.has("imgs")) {
                                    JSONArray array = news.getJSONArray("imgs");
                                    if (array != null && array.length() > 0) {
                                        int len = array.length();
                                        imgUrls = new String[len];
                                        for (int i = 0; i < len; i++) {
                                            imgUrls[i] = array.getString(i);
                                        }
                                    }
                                }

                                titleView.setText(title);
                                secondTleView.setText("时间:" + date + "     发布人：职工家");

                                if (imgUrls != null && imgUrls.length > 0) {
                                    isHideProgress = false;
                                    downloadImage(imgUrls, 0);
                                }else {
                                    contentView.setText(Html.fromHtml(content));
                                }
                            }
                        }else {
                            showToast(obj.optString("msg"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (isHideProgress) {
                            hideProgressDialog();
                        }
                    }
                }

                @Override
                public void onRequestCancal() {
                    hideProgressDialog();
                    showToast("新闻请求取消！");
                }
            });
        }
    }

    public void initWebView(){
        contentView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
    }






    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (cacheManager != null) {
            cacheManager.clearMemoryCache();
        }
    }

    private void downloadImage(final String[] imgUrls, final int index) {
        if (imgUrls != null && imgUrls.length > 0) {

            if (cacheManager == null){
                cacheManager = CacheManager.getInstance();
            }

            String path = imgUrls[index];
            String url;
            if (path.startsWith("http://")){
                url = imgUrls[index];
            }else {
                url = HttpConstant.HOST + imgUrls[index];
            }
            final String fileName = String.valueOf(url.hashCode());
            if (cacheManager.isFileInCache(fileName)) {
                Log.i("Image", "已缓存，无需下载：" + fileName);
                if (index < imgUrls.length - 1) {
                    downloadImage(imgUrls, index + 1);
                } else {
                    hideProgressDialog();
                    contentView.setText(Html.fromHtml(content, new HtmlImageGetter(contentView), null));
                }
            } else {
                Log.i("Image", "未缓存，需下载：" + fileName);


                ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        cacheManager.save(fileName,loadedImage);
                        if (index < imgUrls.length - 1) {
                            downloadImage(imgUrls, index + 1);
                        } else {
                            hideProgressDialog();
                            contentView.setText(Html.fromHtml(content, new HtmlImageGetter(contentView), null));
                        }
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        // Empty implementation
                        hideProgressDialog();
                        contentView.setText(Html.fromHtml(content, new HtmlImageGetter(contentView), null));
                    }
                });
            }
        }
    }

    class MyWebChromeClient extends WebChromeClient {

        private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;
        public int mOriginalOrientation = 1;


        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
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
        private IX5WebChromeClient.CustomViewCallback myCallback = null;


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
