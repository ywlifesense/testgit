package cn.js.nanhaistaffhome.activitys;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.http.HttpConstant;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.models.Acti;
import cn.js.nanhaistaffhome.utils.HtmlImageGetter;
import cn.js.nanhaistaffhome.utils.ImageFileUtil;

/**
 * Created by JS on 8/20/15.
 */
public class ActiDetailActivity extends BaseActivity {

    private TextView titleView;
    private TextView secondTleView;
    private TextView thirdTleView;
    private TextView contentView;
    private ImageFileUtil imageFileUtil;

    private long contentId;
    private String content;
    private String startDate,endDate;
    private String charger;
    private String[] imgUrls;
    private String title;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_detail);

        titleView = (TextView)findViewById(R.id.tv_title);
        secondTleView = (TextView)findViewById(R.id.tv_second_title);
        thirdTleView = (TextView)findViewById(R.id.tv_third_title);
        contentView = (TextView)findViewById(R.id.tv_content);

        contentId = getIntent().getLongExtra("id",0);
        getActivityDetail(contentId);
    }

    public void onClick(View view){
        if (view.getId()==R.id.btn_back){
            finish();
        }
    }

    private void getActivityDetail(long id){
        HttpClient.getActivityContent(id, new OnHttpRequestListener() {
            @Override
            public void onRequestStart() {
                showProgressDialog("正在加载，请稍后...");
            }

            @Override
            public void onRequestEnd(String result) {
                boolean isHideProgress = true;
                try{
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code",1)==0){
                        JSONObject acti = obj.getJSONObject("data");
                        content = acti.optString("TXT");
                        startDate = acti.optString("DATELINE");
                        endDate = acti.optString("DATEEND");
                        charger = acti.optString("CHARGER");
                        title = acti.optString("TITLE");

                        if (acti.has("imgs")) {
                            JSONArray array = acti.getJSONArray("imgs");
                            if (array != null && array.length() > 0) {
                                int len = array.length();
                                imgUrls = new String[len];
                                for (int i = 0; i < len; i++) {
                                    imgUrls[i] = array.getString(i);
                                }
                            }
                        }

                        titleView.setText(title);
                        secondTleView.setText("主办单位："+charger);
                        thirdTleView.setText("活动时间："+startDate+" - "+endDate);
//                        contentView.setText(Html.fromHtml(content));
                        if (imgUrls != null && imgUrls.length > 0) {
                            isHideProgress = false;
                            downloadImage(imgUrls, 0);
                        }else {
                            contentView.setText(Html.fromHtml(content));
                        }
                    }else {
                        showToast(obj.optString("msg"));
                    }
                }catch (Exception e){
                    showToast("加载失败，请稍后再试");
                }finally {
                    if (isHideProgress) {
                        hideProgressDialog();
                    }
                }
            }

            @Override
            public void onRequestCancal() {
                hideProgressDialog();
            }
        });
    }

    private void downloadImage(final String[] imgUrls, final int index) {
        if (imgUrls != null && imgUrls.length > 0) {

            if (imageFileUtil == null) {
                imageFileUtil = new ImageFileUtil(StaffApplication.APPFolder);
            }

            String path = imgUrls[index];
            String url;
            if (path.startsWith("http://")){
                url = imgUrls[index];
            }else {
                url = HttpConstant.HOST + imgUrls[index];
            }
            final String fileName = String.valueOf(url.hashCode());
            if (imageFileUtil.isFileInCacheFolder(fileName)) {
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
                        imageFileUtil.saveJPGToCacheFolder(fileName, loadedImage);
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
}
