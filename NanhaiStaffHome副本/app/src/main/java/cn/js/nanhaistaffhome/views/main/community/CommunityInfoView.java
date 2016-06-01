package cn.js.nanhaistaffhome.views.main.community;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.activitys.MainActivity;
import cn.js.nanhaistaffhome.utils.DensityUtils;
import cn.js.nanhaistaffhome.views.main.ContentView;
import cn.js.nanhaistaffhome.views.main.IContentPage;
import cn.js.nanhaistaffhome.views.others.MyViewPager;
import cn.js.nanhaistaffhome.views.others.ViewPagerIndicator;

/**
 * Created by JS on 8/8/15.
 * 社区信息
 */
public class CommunityInfoView extends LinearLayout implements IContentPage{

    private List<String> mDatas = Arrays.asList("工会要闻", "通知公告",  "服务信息");
    private int[] channelIds = {111, 110, 113};
    private ViewPagerIndicator indicator;// 指示器？
    private MyViewPager viewPager;
    private ArrayList<NewsListPage> pages = new ArrayList<>();
    private Context context;

    public CommunityInfoView(final ContentView contentView) {
        super(contentView.getContext());
        this.context = contentView.getContext();
        setOrientation(LinearLayout.VERTICAL);

        indicator = new ViewPagerIndicator(context);
        indicator.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context, 36)));
        indicator.setBackgroundResource(R.color.tab_bg);
        indicator.setTabItemTitles(mDatas);
        addView(indicator);

        View line = new View(context);
        line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
        line.setBackgroundResource(R.color.text_color_second);
        addView(line);

        viewPager = new MyViewPager(context);
        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        for (int i = 0; i < mDatas.size(); i++) {
            pages.add(new NewsListPage(context, channelIds[i],mDatas.get(i)));
        }
        viewPager.setAdapter(new ViewPagerAdapter(pages));
        addView(viewPager);

        indicator.setViewPager(viewPager, 0);
    }

    class ViewPagerAdapter extends PagerAdapter {
        private List<NewsListPage> list = null;

        public ViewPagerAdapter(List<NewsListPage> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            NewsListPage page = list.get(position);
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

    @Override
    public View getContentPage(){
        return this;
    }

    @Override
    public int getCurrentChannelId(){
        return channelIds[viewPager.getCurrentItem()];
    }

}
