package cn.js.nanhaistaffhome.views.home.content.message;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cn.js.nanhaistaffhome.R;

/**
 * Created by JS on 8/12/15.
 */
public class MessageAdapter extends BaseAdapter {

    private Context context;

    protected MessageAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount(){
        return 10;
    }

    @Override
    public Object getItem(int index){
        return null;
    }

    @Override
    public long getItemId(int index){
        return index;
    }

    @Override
    public View getView(int position,View view,ViewGroup parent){
        return View.inflate(context, R.layout.item_message,null);
    }
}
