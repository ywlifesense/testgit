package cn.js.nanhaistaffhome.models;

import android.text.TextUtils;

import org.json.JSONObject;

import cn.js.nanhaistaffhome.http.HttpConstant;

/**
 * Created by JS on 8/26/15.
 */
public class Service {
    private long id;
    private String releaseData;
    private String description;
    private String typeImg;
    private int rn;
    private String title;

    public long getId(){
        return id;
    }

    public String getReleaseData(){
        return releaseData;
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

    public static Service build(JSONObject object){
        Service service = null;
        if (object != null){
            service = new Service();
            service.id = object.optInt("CONTENT_ID");
            service.releaseData = object.optString("RELEASE_DATE");
            service.description = object.optString("DESCRIPTION");
            String url = object.optString("TYPE_IMG");
            if (!url.startsWith("http://") && !TextUtils.isEmpty(url)){
                url = HttpConstant.HOST + url;
            }
            service.typeImg = url;
            service.rn = object.optInt("RN");
            service.title = object.optString("TITLE");
        }
        return service;
    }
}
