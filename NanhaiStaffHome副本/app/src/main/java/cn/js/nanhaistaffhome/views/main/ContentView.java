package cn.js.nanhaistaffhome.views.main;

import android.animation.ObjectAnimator;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.Constant;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.activitys.MainActivity;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnNaviBarActionListener;
import cn.js.nanhaistaffhome.interfaces.OnSimpleHttpRequestListener;
import cn.js.nanhaistaffhome.interfaces.OnTabBarSelectedListener;
import cn.js.nanhaistaffhome.utils.DateUtil;
import cn.js.nanhaistaffhome.utils.SharedPreferenceUtil;
import cn.js.nanhaistaffhome.views.main.community.CommunityInfoView;
import cn.js.nanhaistaffhome.views.main.myservice.MyServiceView;
import cn.js.nanhaistaffhome.views.main.servicemarket.ServiceMarketView;
import cn.js.nanhaistaffhome.views.main.staffhome.StaffHomeView;
import cn.js.nanhaistaffhome.views.main.whqy.WHQYView;
import cn.js.nanhaistaffhome.views.main.zhzx.ZHZXView;
import cn.js.nanhaistaffhome.views.others.MyViewPager;

/**
 * Created by JS on 8/6/15.
 */
public class ContentView extends RelativeLayout {

    private String[] TITLES = {"知工会","护权益","职工 · 家","惠服务"};
    private MyViewPager viewPager;
    private ArrayList<IContentPage> pages = new ArrayList<>();
    private TabBar tabBar;
    public NavigationBar navigationBar;
    private ListView resultLv;
    private String[] resultTitles;
    private Long[] resultIds;
    private SharedPreferenceUtil spu;
    private boolean isLogined;
    private MainActivity mainActivity;

    public ContentView(MainActivity context,int parent,int child){
        super(context);
        mainActivity = context;
        spu = SharedPreferenceUtil.getInstance(context);
        isLogined = spu.getBoolean(Constant.SP_KEY_IS_LOGIN,false);
        init(context,parent,child);
    }

    private void init(final MainActivity context, final int parent,int child){
        LayoutInflater.from(context).inflate(R.layout.activity_main,this);
        viewPager = (MyViewPager)findViewById(R.id.viewPager);
        //搜索结果列表，已隐藏
        resultLv = (ListView)findViewById(R.id.lv_result);
        resultLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (resultIds != null && resultIds[i]>0){
                    ActivityManager.toNewsDetailActivity(context,resultIds[i],"");
                }
            }
        });

        navigationBar = (NavigationBar)findViewById(R.id.top_bar);
        navigationBar.setOnNaviBarActionListener(new OnNaviBarActionListener() {
            @Override
            public void onLeftBtnClick() {
                mainActivity.menuDrawer.toggleMenu();
            }

            @Override
            public void onRightBtnClick() {

                if (!isLogined) {
                    mainActivity.showUnLoginAlertDialog();
                    return;
                }

                if (tabBar.getCurrentSelected() == 1) {
                    ActivityManager.toAddGuestbookActivity(context, 3);//在线维权
                } else {
                    ActivityManager.toAddGuestbookActivity(context, 0);
                }
            }

            @Override
            public void onSearch(String text) {
                if (TextUtils.isEmpty(text)) {
                    resultIds = new Long[0];
                    resultTitles = new String[0];
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            getContext(), R.layout.item_search_result, resultTitles);
                    resultLv.setAdapter(adapter);
                } else {
                    String time = DateUtil.formatDateToStr(new Date());
                    search(text, time);
                }
            }

            @Override
            public void onFocusChange(boolean b) {
                if (b) {
                    ObjectAnimator animator = ObjectAnimator.ofInt(resultLv, "visibility", GONE, VISIBLE);
                    animator.setDuration(150);
                    animator.start();
                } else {
                    ObjectAnimator animator = ObjectAnimator.ofInt(resultLv, "visibility", VISIBLE, GONE);
                    animator.setDuration(150);
                    animator.start();
                    resultLv.setAdapter(null);
                }
            }
        });

        //初始化栏目视图
        pages.add(new CommunityInfoView(this));
        pages.add(new WHQYView(context,parent==1?child:0));
        pages.add(new StaffHomeView(this));
        pages.add(new MyServiceView(context));

        viewPager.setAdapter(new ViewPagerAdapter(pages));
        viewPager.setScrollAble(false);
        viewPager.setCurrentItem(parent);

        tabBar = (TabBar)findViewById(R.id.tab_bar);
        tabBar.setOnTabBarSelectedListener(new OnTabBarSelectedListener() {
            @Override
            public void onTabBarSelected(int index) {
                viewPager.setCurrentItem(index);
                navigationBar.setTitle(TITLES[index]);
            }
        });
        navigationBar.setTitle(TITLES[0]);
        tabBar.setCurrentSelected(parent);
    }

    class ViewPagerAdapter extends PagerAdapter {
        private List<IContentPage> list = null;

        public ViewPagerAdapter(List<IContentPage> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View page = list.get(position).getContentPage();
            container.addView(page);
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position).getContentPage());
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    private void search(String text,String time){
        int channelId = pages.get(viewPager.getCurrentItem()).getCurrentChannelId();
        HttpClient.search(channelId, 100, time, 1, text, new OnSimpleHttpRequestListener(){
            @Override
            public void onRequestEnd(String result){
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code",1)==0){
                        JSONArray array = obj.getJSONArray("data");
                        if (array!=null && array.length()>0){
                            int len = array.length();
                            resultTitles = new String[len];
                            resultIds = new Long[len];
                            for (int i=0;i<len;i++){
                                JSONObject ob = array.getJSONObject(i);
                                resultTitles[i] = ob.optString("TITLE");
                                resultIds[i] = ob.optLong("CONTENT_ID",-1);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    getContext(),R.layout.item_search_result,resultTitles);
                            resultLv.setAdapter(adapter);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
