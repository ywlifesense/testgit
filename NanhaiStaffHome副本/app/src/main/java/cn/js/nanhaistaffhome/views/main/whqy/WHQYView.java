package cn.js.nanhaistaffhome.views.main.whqy;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

import cn.js.nanhaistaffhome.Constant;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.activitys.MainActivity;
import cn.js.nanhaistaffhome.utils.DensityUtils;
import cn.js.nanhaistaffhome.views.main.IContentPage;
import cn.js.nanhaistaffhome.views.main.community.NewsListPage;
import cn.js.nanhaistaffhome.views.main.zhzx.ZHZXListPage;
import cn.js.nanhaistaffhome.views.others.MyViewPager;
import cn.js.nanhaistaffhome.views.others.ViewPagerIndicator;

/**
 * Created by JS on 8/13/15.
 * 维护权益
 */
public class WHQYView extends LinearLayout implements IContentPage{

    private ViewPagerIndicator indicator;
    private MyViewPager viewPager;
    private ArrayList<View> pages = new ArrayList<>();

    public WHQYView(MainActivity context,int selectDefault){
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        indicator = new ViewPagerIndicator(context);
        indicator.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context, 36)));
        indicator.setBackgroundResource(R.color.tab_bg);
        indicator.setTabItemTitles(Constant.WHQY_CHILD_NAMES);
        addView(indicator);

        View line = new View(context);
        line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
        line.setBackgroundResource(R.color.text_color_second);
        addView(line);

        viewPager = new MyViewPager(context);
        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        for (int i = 0; i < Constant.WHQY_CHILD_IDS.size(); i++) {
            int id = Constant.WHQY_CHILD_IDS.get(i);
            if (id > 0) {
//                if (i == Constant.WHQY_CHILD_IDS.size() - 1) {
//                    pages.add(new ZHZXListPage(context, Constant.WHQY_CHILD_IDS.get(i)));
//                } else {
                    pages.add(new NewsListPage(context, Constant.WHQY_CHILD_IDS.get(i),Constant.WHQY_CHILD_NAMES.get(i)));
//                }
            }
        }
        viewPager.setAdapter(new ViewPagerAdapter(pages));
        addView(viewPager);
        viewPager.setCurrentItem(selectDefault);

        indicator.setViewPager(viewPager, selectDefault);
    }

    class ViewPagerAdapter extends PagerAdapter {
        private List<View> list = null;

        public ViewPagerAdapter(List<View> list) {
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

    @Override
    public View getContentPage(){
        return this;
    }

    @Override
    public int getCurrentChannelId(){
        int item = viewPager.getCurrentItem();
        if (item >= 0 && item <= 3){
            return ((NewsListPage)pages.get(item)).getChannelId();
        }
        return -1;
    }
}
