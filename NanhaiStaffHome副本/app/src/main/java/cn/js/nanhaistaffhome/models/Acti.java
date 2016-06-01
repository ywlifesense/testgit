package cn.js.nanhaistaffhome.models;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by JS on 8/20/15.
 * 活动
 */
public class Acti {
    private int rn;
    private int id;
    private int userId;
    private String title;
    private String description;
    private String txt;
    private String releaseDate;
    private int orgId;
    private String charger;
    private String address;
    private String dateLine;
    private String[] imgs;

    public int getRn() {
        return rn;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return getDescription();
    }

    public String getTxt() {
        return txt;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getOrgId() {
        return orgId;
    }

    public String getCharger() {
        return charger;
    }

    public String getAddress() {
        return address;
    }

    public String getDateLine() {
        return dateLine;
    }

    public static Acti build(JSONObject obj) {
        Acti act = new Acti();
        if (obj != null) {
            act.rn = obj.optInt("RN");
            act.id = obj.optInt("ID");
            act.userId = obj.optInt("USER_ID");
            act.title = obj.optString("TITLE");
            act.description = obj.optString("DESCRIPTION");
            act.txt = obj.optString("TXT");
            act.releaseDate = obj.optString("RELEASE_DATE");
            act.orgId = obj.optInt("ORG_ID");
            act.charger = obj.optString("CHARGER");
            act.address = obj.optString("ADDRESS");
            act.dateLine = obj.optString("DATELINE");
            if (obj.has("imgs")) {
                JSONArray array = obj.optJSONArray("imgs");
                if (array != null && array.length() > 0) {
                    int len = array.length();
                    act.imgs = new String[len];
                    for (int i = 0; i < len; i++) {
                        act.imgs[i] = array.optString(i);
                    }
                }
            }
        }
        return act;
    }
}
