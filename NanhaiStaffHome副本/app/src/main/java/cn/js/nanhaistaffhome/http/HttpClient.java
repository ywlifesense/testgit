package cn.js.nanhaistaffhome.http;

import java.util.HashMap;
import java.util.Map;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;

/**
 * Created by JS on 8/12/15.
 * 后台数据获取工具类
 */
public class HttpClient {

    public static void register(final String username,final String password, final String email,final String mobile,
                                final OnHttpRequestListener listener){
        class RegisterAsync extends HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                Map<String,String> mp = new HashMap<>();
                mp.put("userName",username);
                mp.put("password",password);
                mp.put("email",email);
                mp.put("mobile",mobile);
                return HttpHelper.getInstance().send(HttpConstant.URL_REGISTER,mp);
            }
        }
        new RegisterAsync().setOnHttpRequestListener(listener).execute();
    }

    public static void login(final String username, final String password,final OnHttpRequestListener listener) {
        class LoginAsync extends HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                Map<String,String> mp = new HashMap<>();
                mp.put("username",username);
                mp.put("password",password);
                return HttpHelper.getInstance().send(HttpConstant.URL_LOGIN,mp);
            }
        }
        new LoginAsync().setOnHttpRequestListener(listener).execute();
    }

    public static void getUserInfo(final String username,final OnHttpRequestListener listener){
        class GetUserInfoAsync extends HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                Map<String,String> mp = new HashMap<>();
                mp.put("userName",username);
                return HttpHelper.getInstance().send(HttpConstant.URL_GET_USER_INFO,mp);
            }
        }
        new GetUserInfoAsync().setOnHttpRequestListener(listener).execute();
    }

    public static void modifyUserInfo(final String userName,final String realName,final String email,final String gender,
                                      final String birth,final String qq,final String msn,final String comfrom,
                                      final String intro,final String mobile,final String phone,
                                      final String sign,OnHttpRequestListener listener){
        class ModifyUserInfoAsync extends HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                Map<String,String> mp = new HashMap<>();
                mp.put("userName",userName);
                mp.put("realName",realName);
                mp.put("email",email);
                mp.put("gender",gender);
                mp.put("birthday",birth);
                mp.put("qq",qq);
                mp.put("msn",msn);
                mp.put("comfrom",comfrom);
                mp.put("intro",intro);
                mp.put("mobile",mobile);
                mp.put("phone",phone);
                mp.put("sign",sign);
                return HttpHelper.getInstance().send(HttpConstant.URL_MODIFY_USER_INFO,mp);
            }
        }
        new ModifyUserInfoAsync().setOnHttpRequestListener(listener).execute();
    }

//    public static void getNews(final int channelId, final int start, final int end, final OnHttpRequestListener listener) {
//        class GetNewsAsync extends HttpAsyncTask {
//            @Override
//            protected String doInBackground(String... params) {
//                Map<String, String> mp = new HashMap<>();
//                mp.put("channel_id", String.valueOf(channelId));
//                mp.put("start", String.valueOf(start));
//                mp.put("end", String.valueOf(end));
//                return HttpHelper.getInstance().send(HttpConstant.URL_GET_NEWS, mp);
//            }
//        }
//        new GetNewsAsync().setOnHttpRequestListener(listener).execute();
//    }

    public static void getNewsByTime(final int channelId, final int size, final String time, final int type,
                                     final OnHttpRequestListener listener) {
        class GetNewsAsync extends HttpAsyncTask {
            @Override
            protected String doInBackground(String... params) {
                Map<String, String> mp = new HashMap<>();
                mp.put("channel_id", String.valueOf(channelId));
                mp.put("size", String.valueOf(size));
                mp.put("type", String.valueOf(type));
                mp.put("time", time);
                return HttpHelper.getInstance().send(HttpConstant.URL_GET_NEWS_BY_TIME, mp);
            }
        }
        new GetNewsAsync().setOnHttpRequestListener(listener).execute();
    }

    public static void getNewsDetail(final long contentId,final OnHttpRequestListener listener){
        class GetNewsDetail extends HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                Map<String, String> mp = new HashMap<>();
                mp.put("content_id",String.valueOf(contentId));
                return HttpHelper.getInstance().send(HttpConstant.URL_GET_NEWS_DETAIL,mp);
            }
        }
        new GetNewsDetail().setOnHttpRequestListener(listener).execute();
    }

    /**
     * 通过时间获取活动信息列表
     * @param userName 用户名称
     * @param time 时间点，格式：yyyy-MM-dd HH:mm:ss
     * @param type 获取的数据类型方向。0表示获取下拉刷新的数据，1表示获取上拉加载更多数据
     * @param size 每次获取的条数
     * @param listener HTTP访问监听器:OnHttpRequestListener
     * */
    public static void getActivityNews(final String userName,final String time,final int size,final int type,OnHttpRequestListener listener){
        class GetActivityNewsDetail extends HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                Map<String, String> mp = new HashMap<>();
                mp.put("userName",userName);
                mp.put("time",time);
                mp.put("size",String.valueOf(size));
                mp.put("type",String.valueOf(type));
                return HttpHelper.getInstance().send(HttpConstant.URL_ACTIVITIES_BY_TIME,mp);
            }
        }
        new GetActivityNewsDetail().setOnHttpRequestListener(listener).execute();
    }

    public static void getActivityContent(final long id,OnHttpRequestListener listener){
        class GetActivityContent extends HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                Map<String, String> mp = new HashMap<>();
                mp.put("id",String.valueOf(id));
                return HttpHelper.getInstance().send(HttpConstant.URL_ACTIVITY_CONTENT,mp);
            }
        }
        new GetActivityContent().setOnHttpRequestListener(listener).execute();
    }

    public static void modifyPwd(final String userName, final String oldPwd, final String newPwd,OnHttpRequestListener listener){
        class ModifyPwdAsync extends  HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                Map<String, String> mp = new HashMap<>();
                mp.put("userName",userName);
                mp.put("oldp",oldPwd);
                mp.put("newp",newPwd);
                return HttpHelper.getInstance().send(HttpConstant.URL_MODIFY_PASSWORD,mp);
            }
        }
        new ModifyPwdAsync().setOnHttpRequestListener(listener).execute();
    }

    public static void auth(final String userName, final String idNo,final String memberid,
                            final String unionName,final String politicsStatus,final String education,
                            final String telephone,OnHttpRequestListener listener){
        class  AutoAsync extends  HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                Map<String, String> mp = new HashMap<>();
                mp.put("userName",userName);
                mp.put("idNo",idNo);
                mp.put("memberid",memberid);
                mp.put("unionName",unionName);
                mp.put("politicsStatus",politicsStatus);
                mp.put("education",education);
                mp.put("telephone",telephone);
                return HttpHelper.getInstance().send(HttpConstant.URL_AUTH,mp);
            }
        }
        new AutoAsync().setOnHttpRequestListener(listener).execute();
    }

    /**
     * 获取咨询列表
     * @param ctgId 咨询的类别，数字。1在线咨询，2投诉与建议，3法律援助，23在线维权
     * @param size 每页的数据量
     * @param type 获取的数据类型，数字。0表示获取下拉刷新的数据，1表示获取上拉加载更多数据
     * @param time 客户端缓存的时间字段
     * */
    public static void getGuestbook(final int ctgId, final int size, final int type
            ,final String time,OnHttpRequestListener listener){
        class GetGuestbook extends HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                Map<String,String> mp = new HashMap<>();
                mp.put("ctgId",String.valueOf(ctgId));
                mp.put("size",String.valueOf(size));
                mp.put("type",String.valueOf(type));
                mp.put("time",time);
                return HttpHelper.getInstance().send(HttpConstant.URL_GET_GUESTBOOK,mp);
            }
        }
        new GetGuestbook().setOnHttpRequestListener(listener).execute();
    }

    /**
     * 发布咨询
     * @param mp 参数
     * @param listener OnHttpRequestListener
     * */
    public static void addGuestbook(final Map<String,String> mp,OnHttpRequestListener listener){
        class AddGuestbook extends HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                return HttpHelper.getInstance().send(HttpConstant.URL_ADD_GUESTBOOK,mp);
            }
        }
        new AddGuestbook().setOnHttpRequestListener(listener).execute();
    }

    /**
     * 上传图片
     * @param username 用户昵称
     * @param password 用户密码（明文）
     * @param fileBase64 图片Base64数据
     */
    public static void uploadImage(final String username, final String password, final String fileBase64,OnHttpRequestListener listener){
        class  UploadImageAsync extends HttpAsyncTask{
            @Override
            protected String doInBackground(String... params){
                Map<String,String> mp = new HashMap<>();
                mp.put("username",username);
                mp.put("password",password);
                mp.put("fileBase64",fileBase64);
                mp.put("suffix","jpg");
                return HttpHelper.getInstance().send(HttpConstant.URL_UPLOAD_IMAGE,mp);
            }
        }
        new UploadImageAsync().setOnHttpRequestListener(listener).execute();
    }

    /**
     * 检查认证情况
     * @param userId 用户ID
     * */
    public static void checkAuth(final long userId,OnHttpRequestListener listener){
        class CheckAuthAsync extends HttpAsyncTask{
            @Override
            public String doInBackground(String... params){
                Map<String,String> mp = new HashMap<>();
                mp.put("userid",String.valueOf(userId));
                return HttpHelper.getInstance().send(HttpConstant.URL_CHECK_AUTH, mp);
            }
        }
        new CheckAuthAsync().setOnHttpRequestListener(listener).execute();
    }

    /**
     * 获取企业列表
     * */
    public static void getCompanyList(final String searchKey,OnHttpRequestListener listener){
        class GetCompanyList extends HttpAsyncTask{
            @Override
            public String doInBackground(String... params){
                String result = "";
                try{
                    Map<String,String> mp = new HashMap<>();
                    mp.put("searcheKey",searchKey);
                    result = HttpHelper.getInstance().send(HttpConstant.URL_COMPANY_LIST,mp);
                }catch (Exception e){}
                return result;
            }
        }
        new GetCompanyList().setOnHttpRequestListener(listener).execute();
    }

    /**
     * 文章搜索
     * */
    public static void search(final int channelId, final int size, final String time, final int type, final String title,OnHttpRequestListener listener){
        class SearchAsync extends HttpAsyncTask{
            @Override
            public String doInBackground(String... params){
                Map<String,String> mp = new HashMap<>();
                mp.put("channel_id",String.valueOf(channelId));
                mp.put("size",String.valueOf(size));
                mp.put("time",time);
                mp.put("type",String.valueOf(type));
                mp.put("title",title);
                return HttpHelper.getInstance().send(HttpConstant.URL_SEARCH,mp);
            }
        }
        new SearchAsync().setOnHttpRequestListener(listener).execute();
    }

    /**
     * 获取最新版本
     * */
    public static void getNewestVersion(OnHttpRequestListener listener){
        class VersionAsync extends HttpAsyncTask{
            @Override
            public String doInBackground(String... params){
                String result = "";
                try{
                    result = HttpHelper.getInstance().sendGet(HttpConstant.URL_NEWEST_VERSION);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return result;
            }
        }
        new VersionAsync().setOnHttpRequestListener(listener).execute();
    }

}
