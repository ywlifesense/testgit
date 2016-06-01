package cn.js.nanhaistaffhome.models;

import org.json.JSONObject;

/**
 * Created by JS on 8/27/15.
 */
public class Words {

    private long id;
    private int rn;
    private String createTime;
    private String title;
    private String content;
    private String reply;
    private String email;
    private String phone;
    private String qq;

    public long getId() {
        return id;
    }

    public int getRn() {
        return rn;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getReply() {
        return reply;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getQq() {
        return qq;
    }

    public static Words build(JSONObject obj) {
        Words words = null;
        if (obj != null) {
            words = new Words();
            words.id = obj.optLong("GUESTBOOK_ID");
            words.createTime = obj.optString("CREATE_TIME");
            words.rn = obj.optInt("RN");
            words.title = obj.optString("TITLE");
            words.content = obj.optString("CONTENT");
            words.reply = obj.optString("REPLY");
            words.email = obj.optString("EMAIL");
            words.phone = obj.optString("PHONE");
            words.qq = obj.optString("QQ");
        }
        return words;
    }
}
