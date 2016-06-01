package cn.js.nanhaistaffhome.views.main.activitycenter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.activitys.MainActivity;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.interfaces.OnSimpleHttpRequestListener;
import cn.js.nanhaistaffhome.models.Acti;
import cn.js.nanhaistaffhome.utils.DateUtil;
import cn.js.nanhaistaffhome.views.main.IContentPage;
import cn.js.silverbullet.uikit.refreshlistview.IOnLoadMoreListener;
import cn.js.silverbullet.uikit.refreshlistview.IOnRefreshListener;
import cn.js.silverbullet.uikit.refreshlistview.RefreshListView;

/**
 * Created by JS on 8/13/15.
 * 活动中心
 */
public class ActivityCenterView extends LinearLayout implements IContentPage{

    private MainActivity parent;
    private RefreshListView listView;
    private List<Acti> activities = new ArrayList<>();

    public ActivityCenterView(MainActivity context) {
        super(context);
        setBackgroundResource(R.color.content_bg);
        parent = context;

        listView = new RefreshListView(context);
        listView.setBackgroundResource(R.color.transparent);
        listView.setSelector(R.color.transparent);
        listView.setVerticalScrollBarEnabled(false);

        listView.setOnRefreshListener(new IOnRefreshListener() {
            @Override
            public void OnRefresh() {
                String time;
                if (activities.size() > 0) {
                    time = activities.get(0).getReleaseDate();
                } else {
                    time = DateUtil.formatDateToStr(new Date());

                }
                getActivities(time, 10, 0);
            }
        });

        listView.setOnLoadMoreListener(new IOnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                String time;
                int size = activities.size();
                if (size > 0) {
                    time = activities.get(size - 1).getReleaseDate();
                } else {
                    time = DateUtil.formatDateToStr(new Date());

                }
                getActivities(time, 10, 1);
            }
        });

        addView(listView);
        String currentTime = DateUtil.formatDateToStr(new Date());
        getActivities(currentTime, 10, 1);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return activities.size();
        }

        @Override
        public Object getItem(int position) {
            return activities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, final ViewGroup parent) {
            ActivityItem item;
            if (view == null) {
                item = new ActivityItem(getContext());
            } else {
                item = (ActivityItem) view;
            }
            Acti acti = activities.get(position);
            item.setTitle(acti.getTitle());
            item.setCharger(acti.getCharger());
            item.setAddress(acti.getAddress());
            item.setDate(acti.getDateLine());
            item.detailBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityManager.toActiDetailActivity(getContext(), activities.get(position).getId());
                }
            });
            return item;
        }
    }

    private void getActivities(String time, int size, final int type) {
        HttpClient.getActivityNews(StaffApplication.getInstance().getUser().getUserName(),
                time, size, type,
                new OnSimpleHttpRequestListener() {
                    @Override
                    public void onRequestEnd(String result) {
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.optInt("code", 1) == 0) {
                                JSONArray array = obj.getJSONArray("data");
                                if (array != null && array.length() > 0) {
                                    int len = array.length();
                                    for (int i = 0; i < len; i++) {
                                        Acti acti = Acti.build(array.getJSONObject(i));
                                        if (type==0){
                                            activities.add(0,acti);
                                        }else {
                                            activities.add(acti);
                                        }
                                    }
                                    listView.setAdapter(new MyAdapter());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (type == 0) {
                            listView.onRefreshComplete();
                        } else if (type == 1) {
                            listView.onLoadMoreComplete(false);
                        }
                    }
                });
    }

    @Override
    public View getContentPage(){
        return this;
    }

    @Override
    public int getCurrentChannelId(){
        return -1;
    }

}
