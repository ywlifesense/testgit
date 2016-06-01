package cn.js.nanhaistaffhome.views.main.zhzx;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.interfaces.OnSimpleHttpRequestListener;
import cn.js.nanhaistaffhome.models.Words;
import cn.js.nanhaistaffhome.utils.DateUtil;
import cn.js.silverbullet.uikit.refreshlistview.IOnLoadMoreListener;
import cn.js.silverbullet.uikit.refreshlistview.IOnRefreshListener;
import cn.js.silverbullet.uikit.refreshlistview.RefreshListView;

/**
 * Created by JS on 8/8/15.
 */
public class ZHZXListPage extends LinearLayout {

    private final static int NEWS_LIMIT = 10;

    private RefreshListView listView;
    private List<Words> list;
    private MyAdapter adapter;
    private int ctgId;

    public ZHZXListPage(Context context, int channelId) {
        super(context);
        this.ctgId = channelId;
        list = new ArrayList<>();
        initListView();

        getGuestbook(1, DateUtil.formatDateToStr(new Date()));
    }

    private void initListView() {
        listView = new RefreshListView(getContext());
        listView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        listView.setDivider(null);
        listView.setVerticalScrollBarEnabled(false);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnLoadMoreListener(new IOnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                if (list.size() > 0) {
                    int size = list.size();
                    getGuestbook(1, list.get(size - 1).getCreateTime());
                } else {
                    getGuestbook(1, DateUtil.formatDateToStr(new Date()));
                }
            }
        });
        listView.setOnRefreshListener(new IOnRefreshListener() {
            @Override
            public void OnRefresh() {
                if (list.size() > 0) {
                    getGuestbook(0, list.get(0).getCreateTime());
                } else {
                    getGuestbook(0, DateUtil.formatDateToStr(new Date()));
                }
            }
        });
        addView(listView);
    }

    private void getGuestbook(final int type, String time) {
        HttpClient.getGuestbook(ctgId, NEWS_LIMIT, type, time, new OnSimpleHttpRequestListener() {
            @Override
            public void onRequestEnd(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code", 1) == 0) {
                        JSONArray array = obj.getJSONArray("data");
                        if (array != null && array.length() > 0) {
                            int len = array.length();
                            for (int i = 0; i < len; i++) {
                                Words words = Words.build(array.getJSONObject(i));
                                if (type == 0) {
                                    list.add(0,words);
                                } else if (type == 1) {
                                    list.add(words);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (type == 0) {
                        listView.onRefreshComplete();
                    } else if (type == 1) {
                        listView.onLoadMoreComplete(false);
                    }
                }
            }
        });
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
            GuestbookItem item;
            if (view == null) {
                item = new GuestbookItem(getContext());
            } else {
                item = (GuestbookItem) view;
            }
            Words words = list.get(position);
            item.setTitle(words.getTitle());
            item.setDescription(words.getCreateTime());
            item.setQuestion(words.getContent());
            item.setResponse(words.getReply());
            return item;
        }
    }

}
