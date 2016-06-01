package cn.js.nanhaistaffhome.views.main.zhzx;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.js.nanhaistaffhome.R;

/**
 * Created by JS on 8/24/15.
 */
public class GuestbookItem extends LinearLayout {

    private TextView titleTv;
    private TextView desTv;
    private TextView questTv;
    private TextView responseTv;
    private View line;

    public GuestbookItem(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setPadding(16, 16, 16, 0);

        titleTv = new TextView(context);
        titleTv.setTextColor(getResources().getColor(R.color.app_theme_color));
        titleTv.setTextSize(15);
        titleTv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(titleTv);

        desTv = new TextView(context);
        desTv.setTextColor(getResources().getColor(R.color.text_color_second));
        desTv.setTextSize(13);
        desTv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(desTv);

        questTv = new TextView(context);
        questTv.setTextColor(getResources().getColor(R.color.text_color));
        questTv.setTextSize(13);
        questTv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(questTv);

        responseTv = new TextView(context);
        responseTv.setTextColor(getResources().getColor(R.color.red));
        responseTv.setTextSize(13);
        responseTv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(responseTv);

        line = new View(context);
        LayoutParams lineParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        lineParams.topMargin = 16;
        line.setLayoutParams(lineParams);
        line.setBackgroundResource(R.color.text_color_second);
        addView(line);


//        titleTv.setText("申请入会事项");
//        desTv.setText("admin 于 2013－8-20 12:30:00 评论道：");
//        questTv.setText("请问如何申请加入工会？");
//        responseTv.setText("管理员回复：你好，请现在网站上在线填写入会申请表，我们将会有与你联系。谢谢你的关注！");
    }

    public void setTitle(String title){
        titleTv.setText(title);
    }

    public void setDescription(String des){
        desTv.setText("匿名网友于" + des + "评论道：");
    }

    public void setQuestion(String question){
        questTv.setText(question);
    }

    public void setResponse(String response){
        if (TextUtils.isEmpty(response) || response.equalsIgnoreCase("null")){
            responseTv.setVisibility(View.GONE);
        }else{
            responseTv.setVisibility(View.VISIBLE);
        }
        responseTv.setText("管理员回复：" + response);
    }
}