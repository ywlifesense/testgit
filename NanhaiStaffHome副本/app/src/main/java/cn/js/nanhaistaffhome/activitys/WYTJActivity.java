package cn.js.nanhaistaffhome.activitys;

import android.os.Bundle;
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
public class WYTJActivity extends BaseActivity {

    private List<News> newsList = new ArrayList<>();
    private NewsAdapter adapter;
    private RefreshListView refreshListView;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wytj);

        refreshListView = (RefreshListView)findViewById(R.id.list_view);

        adapter = new NewsAdapter(this);
        refreshListView.setAdapter(adapter);
        getServiceList(DateUtil.formatDateToStr(new Date()), 1);
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = newsList.get(i - 1);
                ActivityManager.toWebActivity(WYTJActivity.this, "我要调解",
                        HttpConstant.HOST + "/wytj/" + news.getContentId() + ".jhtml");
            }
        });

        refreshListView.setOnLoadMoreListener(new IOnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                if (newsList.size() > 0) {
                    int size = newsList.size();
                    getServiceList(newsList.get(size - 1).getReleaseDate(), 1);
                } else {
                    getServiceList(DateUtil.formatDateToStr(new Date()), 1);
                }
            }
        });

        refreshListView.setOnRefreshListener(new IOnRefreshListener() {
            @Override
            public void OnRefresh() {
                if (newsList.size() > 0) {
                    getServiceList(newsList.get(0).getReleaseDate(), 0);
                } else {
                    getServiceList(DateUtil.formatDateToStr(new Date()), 0);
                }
            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void getServiceList(String time,final int type){
        HttpClient.getNewsByTime(463, 10, time, type, new OnHttpRequestListener() {
            @Override
            public void onRequestStart() {}

            @Override
            public void onRequestEnd(String result) {
                if (type == 0){
                    refreshListView.onRefreshComplete();
                }else {
                    refreshListView.onLoadMoreComplete(false);
                }
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.optInt("code") == 0) {
                        JSONArray array = object.getJSONArray("data");
                        if (array != null && array.length() > 0) {
                            int len = array.length();
                            for (int i = 0; i < len; i++) {
                                if (type == 1) {
                                    newsList.add(News.build(array.getJSONObject(i)));
                                }else {
                                    newsList.add(0,News.build(array.getJSONObject(i)));
                                }
                            }

                            adapter.refresh(newsList);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestCancal() {
                if (type == 0){
                    refreshListView.onRefreshComplete();
                }else {
                    refreshListView.onLoadMoreComplete(false);
                }
            }
        });
    }
}
