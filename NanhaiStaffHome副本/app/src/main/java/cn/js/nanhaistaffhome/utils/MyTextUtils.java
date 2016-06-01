package cn.js.nanhaistaffhome.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JS on 9/8/15.
 */
public class MyTextUtils {

    public static boolean isEmpty(String string){
        if (TextUtils.isEmpty(string) || string.equalsIgnoreCase("null")){
            return true;
        }
        return false;
    }

    public static String checkEmpty(String string){
        if (TextUtils.isEmpty(string) || string.equalsIgnoreCase("null")){
            return "";
        }
        return string;
    }

    public static String checkEmpty(String string,String def){
        if (TextUtils.isEmpty(string) || string.equalsIgnoreCase("null")){
            return def;
        }
        return string;
    }

    //判断手机格式是否正确
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }
    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }
    //判断是否全是数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
