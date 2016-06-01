package cn.js.nanhaistaffhome.views.home.content.main;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.activitys.HomeActivity;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.models.Acti;
import cn.js.nanhaistaffhome.models.News;
import cn.js.nanhaistaffhome.utils.DateUtil;
import cn.js.silverbullet.uikit.refreshlistview.IOnLoadMoreListener;
import cn.js.silverbullet.uikit.refreshlistview.RefreshListView;

/**
 * Created by JS on 8/30/15.
 */
public class MainView extends LinearLayout {

    private RefreshListView listView;
    private View headView;
    private List<News> list;
    private boolean isLoading;
    private MyAdapter adapter;
    public Acti newestActi;
    private ImageButton leftBtn;
    private HomeActivity parent;

    public MainView(HomeActivity context) {
        super(context);
        parent = context;
        LayoutInflater.from(context).inflate(R.layout.content_home, this);

        leftBtn = (ImageButton)findViewById(R.id.btn_left);
        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.menuDrawer.toggleMenu();
            }
        });

        getActivities();

        list = new ArrayList<>();
        listView = (RefreshListView) findViewById(R.id.list_view);
        headView = LayoutInflater.from(context).inflate(R.layout.home_listview_head, null);
        headView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        listView.addHeaderView(headView, null, false);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityManager.toNewsDetailActivity(getContext(), id,"");
            }
        });

        loadNews(DateUtil.formatDateToStr(new Date()),1);
        listView.setOnLoadMoreListener(new IOnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                if (list.size()==0){
                    loadNews(DateUtil.formatDateToStr(new Date()),1);
                }else {
                    String time = list.get(list.size()-1).getReleaseDate();
                    loadNews(time,1);
                }
            }
        });
    }

    public void showActivityFrame(boolean isShow){
        View activityItem = headView.findViewById(R.id.activity_frame);
        ImageView icNew = (ImageView)headView.findViewById(R.id.ic_activity_new);
        if (isShow){
            activityItem.setVisibility(VISIBLE);
            icNew.setVisibility(VISIBLE);
        }else {
            activityItem.setVisibility(GONE);
            icNew.setVisibility(GONE);
        }
    }

    private void getActivities() {
        String time = DateUtil.formatDateToStr(new Date());
        HttpClient.getActivityNews(StaffApplication.getInstance().getUser().getUserName(),
                time, 1, 1,
                new OnHttpRequestListener() {
                    @Override
                    public void onRequestStart() {
                    }

                    @Override
                    public void onRequestEnd(String result) {
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.optInt("code", 1) == 0) {
                                JSONArray array = obj.getJSONArray("data");
                                if (array != null && array.length() > 0) {
                                    int len = array.length();
                                    for (int i = 0; i < len; i++) {
                                        newestActi = Acti.build(array.getJSONObject(i));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (newestActi != null) {
                                ((TextView)findViewById(R.id.tv_act_title)).setText(newestActi.getTitle());
                                ((TextView)findViewById(R.id.tv_act_address)).setText("活动地点："+newestActi.getAddress());
                                ((TextView)findViewById(R.id.tv_act_date)).setText("活动时间："+newestActi.getDateLine());
                            }
                        }
                    }

                    @Override
                    public void onRequestCancal() {

                    }
                });
    }

    private void loadNews(String time,int type) {
        if (!isLoading) {
            HttpClient.getNewsByTime(111, 10, time, type, new OnHttpRequestListener() {
                @Override
                public void onRequestStart() {
                    isLoading = true;
                }

                @Override
                public void onRequestEnd(String result) {
                    if (listView != null) {
                        listView.onLoadMoreComplete(false);
                    }
                    isLoading = false;
                    if (!TextUtils.isEmpty(result)) {
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.optInt("code", 1) == 0) {
                                JSONArray array = obj.getJSONArray("data");
                                if (array != null && array.length() > 0) {
                                    int len = array.length();
                                    for (int i = 0; i < len; i++) {
                                        News news = News.build(array.getJSONObject(i));
                                        list.add(news);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onRequestCancal() {
                    isLoading = false;
                }
            });
        }
    }

    private class MyAdapter extends BaseAdapter {

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
            return list.get(position).getContentId();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            GHYWItem item;
            if (view == null) {
                item = new GHYWItem(getContext());
            } else {
                item = (GHYWItem) view;
            }
            News news = list.get(position);
            item.setImage(news.getTypeImg());
            item.setTitle(news.getTitle());
            item.setDescription(news.getDescription());
            return item;
        }
    }
}
