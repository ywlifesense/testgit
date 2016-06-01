package cn.js.nanhaistaffhome.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by JS on 2015/10/11.
 */
public class CacheManager {

    private static CacheManager instance;
    private static ImageFileUtil imageFileUtil;
    private HashMap<String,Bitmap> memoryCache;

    private CacheManager(){
        imageFileUtil = ImageFileUtil.getInstance();
        memoryCache = new HashMap<>();
    }

    public static CacheManager getInstance(){
        if (instance == null){
            instance = new CacheManager();
        }
        return instance;
    }

    public void save(String key,Bitmap bitmap){
        if (SDCardUtil.isSDCardExist()){
            imageFileUtil.saveJPGToCacheFolder(key, bitmap);
        }else {
            memoryCache.put(key,bitmap);
        }
    }

    public void clearMemoryCache(){
        Log.i("Cache","Clear Memory Cache");
        Iterator iterator = memoryCache.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next().toString();
            Bitmap val = memoryCache.get(key);
            if (val != null && !val.isRecycled()){
                val.recycle();
            }
            val = null;
        }
        memoryCache.clear();
    }

    public Bitmap getImageBitmap(String key){
        if (SDCardUtil.isSDCardExist() && imageFileUtil.isFileInCacheFolder(key)){
            Log.i("Cache","Get bitmap from SDCard");
            return imageFileUtil.getBitmapFromCacheFolder(key);
        }

        if (memoryCache.containsKey(key)){
            Log.i("Cache","Get bitmap from Memory");
            return memoryCache.get(key);
        }

        return null;
    }

    public boolean isFileInCache(String key){
        if (SDCardUtil.isSDCardExist() && imageFileUtil.isFileInCacheFolder(key)){
            return true;
        }

        if (memoryCache.containsKey(key)){
            return true;
        }
        return false;
    }
}
