package cn.js.nanhaistaffhome.views.main.staffhome;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.adapter.ViewPagerAdapter;
import cn.js.nanhaistaffhome.utils.DensityUtils;
import cn.js.nanhaistaffhome.views.main.ContentView;
import cn.js.nanhaistaffhome.views.main.IContentPage;
import cn.js.nanhaistaffhome.views.others.MyViewPager;
import cn.js.nanhaistaffhome.views.others.ViewPagerIndicator;

/**
 * Created by JS on 8/8/15.
 * 社区信息
 */
public class StaffHomeView extends LinearLayout implements IContentPage{

    private List<String> mDatas = Arrays.asList("服务中心", "活动信息",  "活动展示");
    private int[] channelIds = {111, 110, 113};
    private ViewPagerIndicator indicator;
    private MyViewPager viewPager;
    private ArrayList<View> pages = new ArrayList<>();
    private Context context;

    public StaffHomeView(final ContentView contentView) {
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
        pages.add(new ServiceCenterPage(context));
        pages.add(new ActivityInfoPage(context));
        pages.add(new ActivityShowPage(context));
        viewPager.setAdapter(new ViewPagerAdapter(pages));
        addView(viewPager);

        indicator.setViewPager(viewPager, 0);
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
