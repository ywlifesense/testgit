package cn.js.nanhaistaffhome.views.home.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.utils.DensityUtils;

/**
 * Created by JS on 8/6/15.
 */
public class MessageToastView extends View {

    private Paint tPaint;
    private Paint bgPaint;

    private String text;

    public MessageToastView(Context context){
        super(context);

        tPaint = new Paint();
        tPaint.setAntiAlias(true);
        tPaint.setColor(Color.WHITE);
        tPaint.setTextSize(DensityUtils.sp2px(context,16));

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(context.getResources().getColor(R.color.orange));
    }

    public void setTextColor(int color){
        tPaint.setColor(color);
        postInvalidate();
    }

    public void setText(String str){
        this.text = str;
        postInvalidate();
    }

    public void setNumber(int number){
        this.text = String.valueOf(number);
        postInvalidate();
    }

    public void setTextSize(int size){
        tPaint.setTextSize(DensityUtils.sp2px(getContext(),size));
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, bgPaint);

        float th = getTextHeight(text);
        float tw = tPaint.measureText(text);

        canvas.drawText(text,getWidth()/2-tw/2,getHeight()/2+th/2,tPaint);
    }

    public float getTextHeight(String string){
        Rect rect = new Rect();
        tPaint.getTextBounds(string, 0, string.length(), rect);
        return rect.height();
    }

}
