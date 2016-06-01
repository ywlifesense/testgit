package cn.js.nanhaistaffhome.utils;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.js.nanhaistaffhome.R;

/**
 * Created by JS on 8/14/15.
 */
public class ViewUtils {

    public static void addBuildingView(ViewGroup vg){
        ImageView imageView = new ImageView(vg.getContext());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(180,180));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.ic_building);
        vg.addView(imageView);
    }
}
