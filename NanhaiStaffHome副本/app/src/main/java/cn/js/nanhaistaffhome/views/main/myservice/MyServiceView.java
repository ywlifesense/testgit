package cn.js.nanhaistaffhome.views.main.myservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.http.HttpConstant;
import cn.js.nanhaistaffhome.views.main.IContentPage;

/**
 * Created by JS on 2016/3/20.
 */
public class MyServiceView extends LinearLayout implements IContentPage,View.OnClickListener{

    private TextView wyrhTv;//我要入会
    private TextView wyzxTv;//我要咨询
    private TextView wytjTv;//我要调解
    private TextView bslcTv;//办事流程
    private TextView knbfTv;//困难帮扶
    private TextView flfwTv;//法律服务
    private TextView ylhzTv;//医疗互助
    private TextView ldjsTv;//劳动竞赛
    private TextView ldbhTv;//劳动保护
    private TextView wttjTv;//文体推介
    private TextView bmfwTv;//便民服务
    private TextView zgcsTv;//职工超市
    private ImageView bsImg;

    public MyServiceView(Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.content_myservice, this);
        wyrhTv = (TextView)findViewById(R.id.tv_wyrh);
        wyzxTv = (TextView)findViewById(R.id.tv_wyzx);
        wytjTv = (TextView)findViewById(R.id.tv_wytj);
        bslcTv = (TextView)findViewById(R.id.tv_bslc);
        knbfTv = (TextView)findViewById(R.id.tv_knbf);
        flfwTv = (TextView)findViewById(R.id.tv_flfw);
        ylhzTv = (TextView)findViewById(R.id.tv_ylhz);
        ldjsTv = (TextView)findViewById(R.id.tv_ldjs);
        ldbhTv = (TextView)findViewById(R.id.tv_ldbh);
        wttjTv = (TextView)findViewById(R.id.tv_wttj);
        bmfwTv = (TextView)findViewById(R.id.tv_bmfw);
        zgcsTv = (TextView)findViewById(R.id.tv_zgcs);
        bsImg = (ImageView)findViewById(R.id.iv_bookshop);

        wyrhTv.setOnClickListener(this);
        wyzxTv.setOnClickListener(this);
        wytjTv.setOnClickListener(this);
        bslcTv.setOnClickListener(this);
        knbfTv.setOnClickListener(this);
        flfwTv.setOnClickListener(this);
        ylhzTv.setOnClickListener(this);
        ldjsTv.setOnClickListener(this);
        ldbhTv.setOnClickListener(this);
        wttjTv.setOnClickListener(this);
        bmfwTv.setOnClickListener(this);
        zgcsTv.setOnClickListener(this);
        bsImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_wyrh:
                if (StaffApplication.getInstance().getUser().getId()>0) {
                    ActivityManager.toWYRHActivity(getContext());
                }else {
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_wyzx:
                if (StaffApplication.getInstance().getUser().getId()>0) {
                    ActivityManager.toWYZXActivity(getContext());
                }else {
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_wytj:
                ActivityManager.toWebActivity(getContext(), "我要调解", HttpConstant.HOST + "/aytj.jhtml");
                break;
            case R.id.tv_bslc:
                ActivityManager.toWebActivity(getContext(), "办事流程", HttpConstant.HOST + "/bslc.jhtml");
                break;
            case R.id.tv_knbf:
                ActivityManager.toWebActivity(getContext(),"困难帮扶", HttpConstant.HOST+"/knbf/index.jhtml");
                break;
            case R.id.tv_flfw:
                ActivityManager.toWebActivity(getContext(),"法律服务",HttpConstant.HOST+"/applsfw/index.jhtml");
                break;
            case R.id.tv_ylhz:
                ActivityManager.toWebActivity(getContext(),"医疗互助",HttpConstant.HOST+"/ylhz/index.jhtml");
                break;
            case R.id.tv_ldjs:
                ActivityManager.toWebActivity(getContext(),"劳动竞赛",HttpConstant.HOST+"/lmjs/index.jhtml");
                break;
            case R.id.tv_ldbh:
                ActivityManager.toLDBHActivity(getContext());
                break;
            case R.id.tv_wttj:
                ActivityManager.toWTTJActivity(getContext());
                break;
            case R.id.tv_bmfw:
                ActivityManager.toWebActivity(getContext(),"便民服务",HttpConstant.HOST+"/appbmfw/index.jhtml");
                break;
            case R.id.tv_zgcs:
                ActivityManager.toWebActivity(getContext(), "职工超市", HttpConstant.HOST + "/appshop.jhtml");
                break;
            case R.id.iv_bookshop:
                ActivityManager.toWebActivity(getContext(), "南海工会电子职工书屋", "http://weikan.magook.com/?org=fsnhzgh-wk");
                break;
        }
    }

    public View getContentPage(){
        return this;
    }

    public int getCurrentChannelId(){
        return -1;
    }

}
