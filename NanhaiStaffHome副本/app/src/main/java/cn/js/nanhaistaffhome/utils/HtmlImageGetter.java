package cn.js.nanhaistaffhome.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.http.HttpConstant;

/**
 * Created by JS on 8/21/15.
 */
public class HtmlImageGetter implements Html.ImageGetter {

    private TextView target;
    private Drawable defaultDrawable;
    private CacheManager cacheManager;

    public HtmlImageGetter(TextView target) {
        this.target = target;
        defaultDrawable = target.getContext().getResources().getDrawable(R.drawable.image_default);
        cacheManager = CacheManager.getInstance();
    }

    @Override
    public Drawable getDrawable(String path) {
        String url;
        if (path.startsWith("http://")){
            url = path;
        }else {
            url = HttpConstant.HOST + path;
        }
        final String imgKey = String.valueOf(url.hashCode());

        if (cacheManager.isFileInCache(imgKey)){

            Drawable drawable = new BitmapDrawable(target.getResources(),cacheManager.getImageBitmap(imgKey));

            if (drawable != null) {
                int targetW = target.getWidth() - target.getPaddingLeft() - target.getPaddingRight();
                float scale = (float) targetW / drawable.getIntrinsicWidth();
                int targetH = (int) (drawable.getIntrinsicHeight() * scale);
                drawable = zoomDrawable(drawable, targetW, targetH);
                drawable.setBounds(0, 0, targetW, targetH);
                return drawable;
            } else {
                Log.i("HtmlImageGetter", "The key : " + imgKey + " of image 's url : " + url + " is null");
            }
        }

        Drawable urlDrawable = new URLDrawable(defaultDrawable);
        return urlDrawable;
    }

    private Bitmap drawableToBitmap(Drawable drawable)// drawable 转换成bitmap
    {
        int width = drawable.getIntrinsicWidth();   // 取drawable的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // 建立对应bitmap
        Canvas canvas = new Canvas(bitmap);         // 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);      // 把drawable内容画到画布中
        return bitmap;
    }

    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);// drawable转换成bitmap
        Matrix matrix = new Matrix();   // 创建操作图片用的Matrix对象
        float scaleWidth = ((float) w / width);   // 计算缩放比例
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);       // 建立新的bitmap，其内容是对原bitmap的缩放后的图
        return new BitmapDrawable(newbmp);       // 把bitmap转换成drawable并返回
    }

    public class URLDrawable extends BitmapDrawable {

        private Drawable drawable;

        public URLDrawable(Drawable defaultDraw) {
            setDrawable(defaultDraw);
        }

        private void setDrawable(Drawable ndrawable) {
            drawable = ndrawable;
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight());
            setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight());
        }

        @Override
        public void draw(Canvas canvas) {
            drawable.draw(canvas);
        }
    }

}
