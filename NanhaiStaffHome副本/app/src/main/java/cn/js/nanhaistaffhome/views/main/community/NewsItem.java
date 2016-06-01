package cn.js.nanhaistaffhome.views.main.community;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.utils.DateUtil;
import cn.js.nanhaistaffhome.utils.MyTextUtils;

/**
 * Created by JS on 8/8/15.
 */
public class NewsItem extends RelativeLayout{

    private TextView titleView;
    private ImageView imageView;
    private TextView dateView;
    private TextView fromView;
    private TextView descriptionView;
    private ImageButton openBtn;

    public NewsItem(Context context){
        super(context);
        init(context);
    }

    public NewsItem(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.item_news,this);

        openBtn = (ImageButton)findViewById(R.id.btn_open);
        titleView = (TextView)findViewById(R.id.tv_title);
        imageView = (ImageView)findViewById(R.id.iv_news_image);
        fromView = (TextView)findViewById(R.id.tv_come_from);
        descriptionView = (TextView)findViewById(R.id.tv_descript);
        dateView = (TextView)findViewById(R.id.tv_date);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void setImage(String url){
        ImageLoader.getInstance().displayImage(url, imageView);
    }

    public void setImageViewVisible(boolean isHide){
        if (isHide) {
            imageView.setVisibility(View.GONE);
        }else {
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void setDate(String date){
        dateView.setText(DateUtil.formatDateStr(date, "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日"));
    }

    public void setComeFrom(String from){
        fromView.setText("来源：" + from);
    }

    public void setDescription(String des){
        if (MyTextUtils.isEmpty(des)){
            descriptionView.setText("");
        }else {
            descriptionView.setText(des);
        }
    }

}
