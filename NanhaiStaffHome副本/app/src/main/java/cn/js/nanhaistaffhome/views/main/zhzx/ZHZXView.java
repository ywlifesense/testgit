package cn.js.nanhaistaffhome.views.main.zhzx;

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
import cn.js.nanhaistaffhome.models.Words;
import cn.js.nanhaistaffhome.utils.DensityUtils;
import cn.js.nanhaistaffhome.views.main.IContentPage;
import cn.js.nanhaistaffhome.views.others.MyViewPager;
import cn.js.nanhaistaffhome.views.others.ViewPagerIndicator;

/**
 * Created by JS on 8/13/15.
 * 综合咨询
 */
public class ZHZXView extends LinearLayout implements IContentPage{

    private List<Words> list = new ArrayList<>();
    private ViewPagerIndicator indicator;
    private MyViewPager viewPager;
    private ArrayList<ZHZXListPage> pages = new ArrayList<>();

    public ZHZXView(final MainActivity context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        indicator = new ViewPagerIndicator(context);
        indicator.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context, 36)));
        indicator.setBackgroundResource(R.color.tab_bg);
        indicator.setTabItemTitles(Constant.CTG_NAMES);
        addView(indicator);

        View line = new View(context);
        line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
        line.setBackgroundResource(R.color.text_color_second);
        addView(line);

        viewPager = new MyViewPager(context);
        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        for (int i = 0; i < Constant.CTG_IDS.size(); i++) {
            pages.add(new ZHZXListPage(context, Constant.CTG_IDS.get(i)));
        }
        viewPager.setAdapter(new ViewPagerAdapter());
        addView(viewPager);

        indicator.setViewPager(viewPager, 0);
    }

    class ViewPagerAdapter extends PagerAdapter {
        public ViewPagerAdapter() {
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ZHZXListPage page = pages.get(position);
            container.addView(page);
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pages.get(position));
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
    public int getCurrentChannelId(){return -1;}
}
