package cn.js.nanhaistaffhome.views.main.community;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.http.HttpConstant;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.models.News;
import cn.js.nanhaistaffhome.utils.DateUtil;
import cn.js.nanhaistaffhome.utils.MyTextUtils;
import cn.js.nanhaistaffhome.views.others.MyViewPager;
import cn.js.silverbullet.uikit.refreshlistview.IOnLoadMoreListener;
import cn.js.silverbullet.uikit.refreshlistview.IOnRefreshListener;
import cn.js.silverbullet.uikit.refreshlistview.RefreshListView;

/**
 * Created by JS on 8/8/15.
 *
 */
public class NewsListPage extends LinearLayout {

    private RefreshListView listView;
    private int channelId;
    private String tabTle;
    private boolean isLoading;
    private List<News> list = new ArrayList<>();
    private List<News> newsList = new ArrayList<>();
    private MyAdapter adapter;// listview

    private TextView bannerTitleView;

    private List<News> bannerList = new ArrayList<>();//只有首页的三个（）
    private List<ImageView> pagers = new ArrayList<>();
    private MyViewPager viewPager;

    private Handler handler;

    public NewsListPage(Context context, int channelId, String tabTle) {
        super(context);
        this.channelId = channelId;
        this.tabTle = tabTle;
        loadNews(DateUtil.formatDateToStr(new Date()),1);
    }

    private void initBannerView(){
        listView.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.header_ghywei_listview, null));
        viewPager = (MyViewPager)findViewById(R.id.viewpager);
        bannerTitleView = (TextView)findViewById(R.id.tv_banner_title);
        bannerTitleView.setText(bannerList.get(0).getTitle());
        int len = bannerList.size();
        for (int i=0;i<len;i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(bannerList.get(i).getTypeImg(), imageView);
            pagers.add(imageView);
            final int position = i;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityManager.toNewsDetailActivity(getContext(),bannerList.get(position).getContentId(),tabTle);
                }
            });
        }
        final ImageView indicatorTv = (ImageView)findViewById(R.id.iv_indicator);
        final Integer[] indicators = {R.drawable.indicator_0,R.drawable.indicator_1,R.drawable.indicator_2};
        ViewPagerAdapter adapter = new ViewPagerAdapter(pagers);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bannerTitleView.setText(bannerList.get(position).getTitle());
                indicatorTv.setImageResource(indicators[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        handler = new Handler();
        handler.postDelayed(runAdRunnable,2000);
    }

    private Runnable runAdRunnable = new Runnable() {
        @Override
        public void run() {
            int current = viewPager.getCurrentItem();
            int next = current + 1;
            if (current == 2){
                next = 0;
            }
            viewPager.setCurrentItem(next,true);
            handler.postDelayed(runAdRunnable,2000);
        }
    };

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
                if (list.size() == 0) {
                    loadNews(DateUtil.formatDateToStr(new Date()), 1);
                } else {
                    String time = list.get(list.size() - 1).getReleaseDate();
                    loadNews(time, 1);
                }
            }
        });
        listView.setOnRefreshListener(new IOnRefreshListener() {
            @Override
            public void OnRefresh() {
                if (list.size() == 0){
                    loadNews(DateUtil.formatDateToStr(new Date()), 0);
                }else {
                    String time = list.get(0).getReleaseDate();
                    loadNews(time, 0);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityManager.toNewsDetailActivity(getContext(),id,tabTle);
            }
        });
        addView(listView);

        if (channelId == 111){
            initBannerView();
        }
    }

    public int getChannelId(){
        return channelId;
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public Object getItem(int position) {
            return newsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return newsList.get(position).getContentId();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            NewsItem item;
            if (view == null) {
                item = new NewsItem(getContext());
            } else {
                item = (NewsItem) view;
            }
            News news = newsList.get(position);
            item.setTitle(news.getTitle());
            if (MyTextUtils.isEmpty(news.getTypeImg())){
                item.setImageViewVisible(true);
            }else {
                item.setImageViewVisible(false);
                item.setImage(news.getTypeImg());
            }
            item.setDate(news.getReleaseDate());
            item.setDescription(news.getDescription());
            return item;
        }
    }

    private void loadNews(String time, final int type) {
        if (!isLoading) {
            HttpClient.getNewsByTime(channelId, 10, time, type, new OnHttpRequestListener() {
                @Override
                public void onRequestStart() {
                    isLoading = true;
                }

                @Override
                public void onRequestEnd(String result) {
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
                                        if (type == 0){
                                            list.add(0,news);
                                        }else {
                                            list.add(news);
                                        }
                                    }

                                    newsList.clear();
                                    bannerList.clear();

                                    if (channelId != 111){
                                        newsList.addAll(list);
                                    }else {
                                        for (int i=0;i<3;i++){
                                            bannerList.add(list.get(i));
                                        }

                                        newsList.addAll(list);

//                                        int total = list.size();
//                                        for (int i=0;i<total;i++){
//                                            newsList.add(list.get(i));
//                                        }
                                    }

                                    if (listView == null) {
                                        initListView();
                                    } else {
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (listView != null) {
                        if(type == 0){
                            listView.onRefreshComplete();
                        }else {
                            listView.onLoadMoreComplete(false);
                        }
                    }
                }

                @Override
                public void onRequestCancal() {
                    isLoading = false;
                    if (listView != null) {
                        if(type == 0){
                            listView.onRefreshComplete();
                        }else {
                            listView.onLoadMoreComplete(false);
                        }
                    }
                }
            });
        }
    }

    class ViewPagerAdapter extends PagerAdapter {
        private List<ImageView> list = null;

        public ViewPagerAdapter(List<ImageView> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View page = list.get(position);
            container.addView(page);
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

}
