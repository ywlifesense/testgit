package cn.js.nanhaistaffhome;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

import cn.js.nanhaistaffhome.models.User;
import cn.js.nanhaistaffhome.utils.ImageFileUtil;
import cn.js.nanhaistaffhome.utils.SDCardUtil;

/**
 * Created by js on 8/1/15.
 */
public class StaffApplication extends Application{

    public final static String APPFolder = "StaffHome";

    private static StaffApplication instance;

    private User user;

    public static StaffApplication getInstance(){
        return instance;
    }

    public void onCreate(){
        super.onCreate();
        instance = this;
        initImageLoader(getApplicationContext());
    }

    public User getUser(){
        if (user == null){
            user = new User("","");
        }
        return user;
    }

    public void setUser(User user){this.user = user;}

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000));
//        config.writeDebugLogs(); // Remove for release app

        config.defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                .cacheInMemory(true)   //设置图片不缓存于内存中
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的质量
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片的缩放类型，该方法可以有效减少内存的占用
                .displayer(new FadeInBitmapDisplayer(100))
                .showImageForEmptyUri(R.drawable.image_default)
                .showImageOnFail(R.drawable.image_default)
                .showImageOnLoading(R.drawable.image_default)
                .build());

        //设置缓存路径
        try {
            File cacheDir = new ImageFileUtil(APPFolder).getCacheFolder();
            config.diskCache(new UnlimitedDiskCache(cacheDir));
        }catch (Exception e){
            Toast.makeText(instance,"内存卡不可用",Toast.LENGTH_SHORT).show();
            config.defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                    .cacheInMemory(true)   //设置图片不缓存于内存中
                    .cacheOnDisk(false)
                    .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的质量
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片的缩放类型，该方法可以有效减少内存的占用
                    .displayer(new FadeInBitmapDisplayer(100))
                    .showImageForEmptyUri(R.drawable.image_default)
                    .showImageOnFail(R.drawable.image_default)
                    .showImageOnLoading(R.drawable.image_default)
                    .build());
        }

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
