package cn.js.nanhaistaffhome;

import java.util.Arrays;
import java.util.List;

/**
 * Created by JS on 8/16/15.
 */
public class Constant {

    public final static String SP_KEY_IS_LOGIN = "isLogin";
    public final static String SP_KEY_USER_NAME = "username";
    public final static String SP_KEY_PASSWORD = "password";
    public final static String SP_KEY_USER_ID = "userid";

    public final static List<Integer> CTG_IDS = Arrays.asList(1, 2, 3, 23);
    public final static List<String> CTG_NAMES = Arrays.asList("在线咨询", "投诉建议", "法律援助", "在线维权");

//    public final static List<String> WHQY_CHILD_NAMES = Arrays.asList("法律法规","法律常识","法律文书","常见问题","在线维权","");
//    public final static List<Integer> WHQY_CHILD_IDS = Arrays.asList(443,444,442,92,23);

    public final static List<String> WHQY_CHILD_NAMES = Arrays.asList("法律法规","法律常识","常见问题");
    public final static List<Integer> WHQY_CHILD_IDS = Arrays.asList(114,444,92);
}
