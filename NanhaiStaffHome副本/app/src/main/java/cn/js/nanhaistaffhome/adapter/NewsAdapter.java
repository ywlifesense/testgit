package cn.js.nanhaistaffhome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.js.nanhaistaffhome.models.News;
import cn.js.nanhaistaffhome.utils.MyTextUtils;
import cn.js.nanhaistaffhome.views.main.community.NewsItem;

/**
 * Created by JS on 2016/3/21.
 */
public class NewsAdapter extends BaseAdapter {

    private List<News> newsList = new ArrayList<>();
    private Context context;

    public NewsAdapter(Context context){
        this.context = context;
    }

    public void refresh(List<News> newsList){
        if (newsList == null){
            this.newsList.clear();
        }else {
            this.newsList = newsList;
        }
        notifyDataSetChanged();
    }

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
            item = new NewsItem(context);
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
