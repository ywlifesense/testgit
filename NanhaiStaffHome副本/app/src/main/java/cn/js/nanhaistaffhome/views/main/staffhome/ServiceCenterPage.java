package cn.js.nanhaistaffhome.views.main.staffhome;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.adapter.NewsAdapter;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.http.HttpConstant;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.models.News;
import cn.js.nanhaistaffhome.utils.DateUtil;
import cn.js.silverbullet.uikit.refreshlistview.IOnLoadMoreListener;
import cn.js.silverbullet.uikit.refreshlistview.IOnRefreshListener;
import cn.js.silverbullet.uikit.refreshlistview.RefreshListView;

/**
 * Created by JS on 2016/3/21.
 */
public class ServiceCenterPage extends RefreshListView{

    private List<News> newsList = new ArrayList<>();
    private NewsAdapter adapter;

    public ServiceCenterPage(Context context){
        super(context);
        adapter = new NewsAdapter(context);
        setAdapter(adapter);
        setDivider(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
        setDividerHeight(0);
        getServiceList(DateUtil.formatDateToStr(new Date()), 1);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = newsList.get(i - 1);
                ActivityManager.toWebActivity(getContext(), "职工 · 家服务中心", HttpConstant.HOST + "/zgjfwzx/" + news.getContentId() + ".jhtml");
            }
        });
        setOnLoadMoreListener(new IOnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                if (newsList.size()>0){
                    int size = newsList.size();
                    getServiceList(newsList.get(size-1).getReleaseDate(),1);
                }else {
                    getServiceList(DateUtil.formatDateToStr(new Date()), 1);
                }
            }
        });

        setOnRefreshListener(new IOnRefreshListener() {
            @Override
            public void OnRefresh() {
                if (newsList.size()>0){
                    getServiceList(newsList.get(0).getReleaseDate(),0);
                }else {
                    getServiceList(DateUtil.formatDateToStr(new Date()), 0);
                }
            }
        });
    }

    private void getServiceList(String time, final int type){
        HttpClient.getNewsByTime(462, 10, time, type, new OnHttpRequestListener() {
            @Override
            public void onRequestStart() {}

            @Override
            public void onRequestEnd(String result) {
                if (type == 0){
                    onRefreshComplete();
                }else {
                    onLoadMoreComplete(false);
                }
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.optInt("code")==0){
                        JSONArray array = object.getJSONArray("data");
                        if (array!=null && array.length()>0){
                            int len = array.length();
                            for (int i=0;i<len;i++){
                                if (type == 1) {
                                    newsList.add(News.build(array.getJSONObject(i)));
                                }else {
                                    newsList.add(0,News.build(array.getJSONObject(i)));
                                }
                            }

                            adapter.refresh(newsList);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestCancal() {
                if (type == 0){
                    onRefreshComplete();
                }else {
                    onLoadMoreComplete(false);
                }
            }
        });
    }

}
