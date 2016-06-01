package cn.js.nanhaistaffhome.views.main.activitycenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.js.nanhaistaffhome.R;

/**
 * Created by JS on 8/14/15.
 */
public class ActivityItem extends LinearLayout{

    protected Button detailBtn;
    private TextView titleTv;
    private TextView chargerTv;
    private TextView addressTv;
    private TextView dateTv;

    public ActivityItem(Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_activity, this);
        titleTv = (TextView)findViewById(R.id.tv_title);
        chargerTv = (TextView)findViewById(R.id.tv_charger);
        addressTv = (TextView)findViewById(R.id.tv_address);
        dateTv = (TextView)findViewById(R.id.tv_date);
        detailBtn = (Button)findViewById(R.id.btn_detail);
    }

    protected void setTitle(String title){
        titleTv.setText(title);
    }

    protected void setCharger(String title){
        chargerTv.setText("主办单位："+title);
    }

    protected void setAddress(String address){
        addressTv.setText("活动地点："+address);
    }

    protected void setDate(String date){
        dateTv.setText("活动时间："+date);
    }

}
