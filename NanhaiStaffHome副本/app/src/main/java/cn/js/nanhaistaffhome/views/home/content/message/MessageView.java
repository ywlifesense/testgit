package cn.js.nanhaistaffhome.views.home.content.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.js.nanhaistaffhome.R;

/**
 * Created by JS on 8/10/15.
 */
public class MessageView extends LinearLayout {
    private ListView listView;

    public MessageView(Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.content_message, this);

        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(new MessageAdapter(context));
    }
}
