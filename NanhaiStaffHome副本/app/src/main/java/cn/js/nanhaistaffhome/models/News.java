package cn.js.nanhaistaffhome.models;

import android.text.TextUtils;

import org.json.JSONObject;

import cn.js.nanhaistaffhome.http.HttpConstant;
import cn.js.nanhaistaffhome.utils.MyTextUtils;

/**
 * Created by JS on 8/12/15.
 */
public class News {
    private long contentId;
    private String releaseDate;
    private String description;
    private String typeImg;
    private int rn;
    private String title;

    public long getContentId(){
        return contentId;
    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public String getDescription(){
        return description;
    }

    public String getTypeImg(){
        return typeImg;
    }

    public int getRn(){
        return rn;
    }

    public String getTitle(){
        return title;
    }

    public static News build(JSONObject obj){
        News news = new News();
        news.contentId = obj.optLong("CONTENT_ID");
        news.releaseDate = obj.optString("RELEASE_DATE");
        news.description = obj.optString("DESCRIPTION");
        String url = obj.optString("TYPE_IMG");
        if (!url.startsWith("http://") && !MyTextUtils.isEmpty(url)){
            url = HttpConstant.HOST + url;
        }
        news.typeImg = url;
        news.rn = obj.optInt("RN");
        news.title = obj.optString("TITLE");
        return news;
    }
}
