package cn.js.nanhaistaffhome.http;

/**
 * Created by JS on 8/12/15.
 */
public class HttpConstant {
    protected static final int TIMEOUT_IN_MILLIONS = 60000;
    public final static String TEST_HOST = "http://cgt.vicp.net:9988";
    public final static String REAL_HOST = "http://staffhome.nanhai.gov.cn";
    protected final static String ENCODING = "UTF-8";

    public final static String HOST = REAL_HOST;

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * */
    protected final static String URL_REGISTER = HOST + "/ApproveService/appRegister.do";

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * */
    protected final static String URL_LOGIN = HOST + "/ApproveService/login.do";

    /**
     * 获取新闻列表
     * @param channel_id 栏目ID
     * @param start 开始条数
     * @param end 结束条数
     * */
    protected final static String URL_GET_NEWS = HOST + "/ApproveService/getNews.do";

    protected final static String URL_GET_NEWS_BY_TIME = HOST + "/ApproveService/getNewsByTime.do";

    /**
     * 获取新闻详细
     * @param content_id 新闻ID
     * */
    protected final static String URL_GET_NEWS_DETAIL = HOST + "/ApproveService/docContent.do";

    /**获取用户信息*/
    protected final static String URL_GET_USER_INFO = HOST + "/ApproveService/getUserInfo.do";

    /**修改用户信息*/
    protected final static String URL_MODIFY_USER_INFO = HOST + "/ApproveService/updateUserInfo.do";

//    /**获取活动中心活动列表*/
//    protected final static String URL_ACTIVITIES = HOST + "/ApproveService/getActivityNews.do";

    /**通过时间获取活动信息列表*/
    protected final static String URL_ACTIVITIES_BY_TIME = HOST + "/ApproveService/getActivityNewsByTime.do";

    /**获取活动内容*/
    protected final static String URL_ACTIVITY_CONTENT = HOST + "/ApproveService/activityContent.do";

    /**修改用户密码*/
    protected final static String URL_MODIFY_PASSWORD = HOST + "/ApproveService/updatePwd.do";

    /**用户认证*/
    protected final static String URL_AUTH = HOST + "/ApproveService/auth.do";

    /**获取咨询列表*/
    protected final static String URL_GET_GUESTBOOK = HOST + "/ApproveService/getGuestBook.do";

    /**发布咨询*/
    protected final static String URL_ADD_GUESTBOOK = HOST + "/ApproveService/addGuestBook.do";

    /**图片上传*/
    protected final static String URL_UPLOAD_IMAGE = HOST + "/ApproveService/uploadImg.do";

    /**检查认证情况*/
    protected final static String URL_CHECK_AUTH = HOST + "/ApproveService/checkauth.do";

    /**文章搜索*/
    protected final static String URL_SEARCH = HOST + "/ApproveService/searchNewsByTime.do";

    /**获取企业列表*/
    protected final static String URL_COMPANY_LIST = HOST + "/ApproveService/enterprise.do";

    /**获取最新版本*/
    protected final static String URL_NEWEST_VERSION = HOST + "/version.jhtml";
}
