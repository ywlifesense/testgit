package cn.js.nanhaistaffhome.models;

import org.json.JSONObject;

import cn.js.nanhaistaffhome.http.HttpConstant;

/**
 * Created by JS on 8/18/15.
 */
public class User {

    private long id;
    private String userName;
    private String email;
    private String registerTime;
    private String realName;
    private int sex;//0:女 1:男
    private String birth;
    private String comfrom;
    private String qq;
    private String msn;
    private String mobile;
    private String intro;
    private String phone;
    private String sign;
    private String password;
    private String avatarUrl;

    private User(){}

    public User(String userName,String pwd){
        this.userName = userName;
        this.password = pwd;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){this.id = id;}

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String pwd){
        this.password = pwd;
    }

    public String getEmail(){
        return email;
    }

    public String getRegisterTime(){
        return registerTime;
    }

    public String getRealName(){
        return realName;
    }

    public int getSex(){
        return sex;
    }

    public String getBirth(){
        return birth;
    }

    public String getComfrom(){
        return comfrom;
    }

    public String getQq(){
        return qq;
    }

    public String getMsn(){
        return msn;
    }

    public String getMobile(){
        return mobile;
    }

    public String getIntro(){
        return intro;
    }

    public String getPhone(){
        return phone;
    }

    public String getSign(){
        return sign;
    }

    public String getAvatarUrl(){
        return avatarUrl;
    }

    public static User build(JSONObject obj){
        User user = new User();
        user.id = obj.optLong("USER_ID");
        user.userName = obj.optString("USERNAME");
        user.email = obj.optString("EMAIL");
        user.registerTime = obj.optString("REGISTER_TIME");
        user.realName = obj.optString("REALNAME");
        user.sex = obj.optInt("GENDER");
        user.birth = obj.optString("BIRTHDAY");
        user.comfrom = obj.optString("COMEFROM");
        user.qq = obj.optString("QQ");
        user.msn = obj.optString("MSN");
        user.mobile = obj.optString("MOBILE");
        user.intro = obj.optString("INTRO");
        user.phone = obj.optString("PHONE");
        user.sign = obj.optString("USER_SIGNATURE");
        user.avatarUrl = obj.optString("USER_IMG");
        return user;
    }
}
