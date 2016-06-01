package cn.js.nanhaistaffhome.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnSimpleHttpRequestListener;
import cn.js.nanhaistaffhome.models.Service;
import cn.js.nanhaistaffhome.utils.DateUtil;
import cn.js.nanhaistaffhome.views.main.servicemarket.ServiceItem;
import cn.js.silverbullet.uikit.refreshlistview.IOnLoadMoreListener;
import cn.js.silverbullet.uikit.refreshlistview.IOnRefreshListener;
import cn.js.silverbullet.uikit.refreshlistview.RefreshListView;

/**
 * Created by JS on 2016/3/21.
 */
public class ServiceMarketActivity extends BaseActivity {

    private final static int ID = 446;

    private List<Service> list = new ArrayList<>();
    private RefreshListView listView;
    private MyAdapter adapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicemarket);

        listView = (RefreshListView)findViewById(R.id.list_view);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActivityManager.toNewsDetailActivity(ServiceMarketActivity.this, l,"");
            }
        });

        listView.setOnLoadMoreListener(new IOnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                if (list.size()==0){
                    getServiceList(DateUtil.formatDateToStr(new Date()),1);
                }else {
                    String time = list.get(list.size()-1).getReleaseData();
                    getServiceList(time,1);
                }
            }
        });

        listView.setOnRefreshListener(new IOnRefreshListener() {
            @Override
            public void OnRefresh() {
                if (list.size()==0){
                    getServiceList(DateUtil.formatDateToStr(new Date()),0);
                }else {
                    String time = list.get(0).getReleaseData();
                    getServiceList(time,0);
                }
            }
        });
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        getServiceList(DateUtil.formatDateToStr(new Date()),1);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return list.get(position).getId();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ServiceItem item;
            if (view == null) {
                item = new ServiceItem(ServiceMarketActivity.this);
            } else {
                item = (ServiceItem) view;
            }

            Service service = list.get(position);
            item.setImage(service.getTypeImg());
            item.setTitle(service.getTitle());
            item.setDescription(service.getDescription());
            item.setDate(service.getReleaseData());

            return item;
        }
    }

    private void getServiceList(String time, final int type) {
        HttpClient.getNewsByTime(ID, 10, time, type, new OnSimpleHttpRequestListener() {
            @Override
            public void onRequestEnd(String result) {
                if (type == 1) {
                    listView.onLoadMoreComplete(false);
                } else {
                    listView.onRefreshComplete();
                }

                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code", 1) == 0) {
                        JSONArray array = obj.optJSONArray("data");
                        if (array != null && array.length() > 0) {
                            int len = array.length();
                            for (int i = 0; i < len; i++) {
                                Service service = Service.build(array.getJSONObject(i));
                                if (type == 1) {
                                    list.add(service);
                                } else {
                                    list.add(0, service);
                                }
                            }
                            listView.setAdapter(new MyAdapter());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
