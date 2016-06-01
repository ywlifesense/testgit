package cn.js.nanhaistaffhome.views.others;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by JS on 8/8/15.
 */
public class MyViewPager extends ViewPager {

    private boolean scrollAble = true;

    public MyViewPager(Context context){
        super(context);
    }

    public MyViewPager(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    public void setScrollAble(boolean scrollAble){
        this.scrollAble = scrollAble;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        if (scrollAble){
            return super.onInterceptTouchEvent(event);
        }else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (scrollAble){
            return  super.onTouchEvent(event);
        }else {
            return false;
        }
    }
}
