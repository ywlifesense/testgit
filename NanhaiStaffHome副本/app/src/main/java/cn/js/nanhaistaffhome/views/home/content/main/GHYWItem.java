package cn.js.nanhaistaffhome.views.home.content.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.http.HttpConstant;
import cn.js.nanhaistaffhome.utils.MyTextUtils;

/**
 * Created by JS on 8/31/15.
 */
public class GHYWItem extends LinearLayout{

    private ImageView imageView;
    private TextView titleTv;
    private TextView desTv;

    public GHYWItem(Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_ghyw, this);

        imageView = (ImageView)findViewById(R.id.iv_news_image);
        titleTv = (TextView)findViewById(R.id.tv_title);
        desTv = (TextView)findViewById(R.id.tv_descript);

    }

    public void setImage(String path){
        if (imageView == null){
            return;
        }
        if (MyTextUtils.isEmpty(path)){
            imageView.setVisibility(GONE);
            imageView.setImageBitmap(null);
        }else {
            imageView.setVisibility(VISIBLE);
            ImageLoader.getInstance().displayImage(path, imageView);
        }
    }

    public void setTitle(String title){
        titleTv.setText(title);
    }

    public void setDescription(String des){
        if (MyTextUtils.isEmpty(des)){
            desTv.setText("");
        }else {
            desTv.setText(des);
        }
    }
}
