package cn.js.nanhaistaffhome.views.home.content.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import cn.js.nanhaistaffhome.R;

/**
 * Created by JS on 8/12/15.
 */
public class MessageItemView extends RelativeLayout{

    protected MessageItemView(Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_message,this);
    }
}
