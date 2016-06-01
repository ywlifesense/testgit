package cn.js.nanhaistaffhome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.js.nanhaistaffhome.activitys.ActiDetailActivity;
import cn.js.nanhaistaffhome.activitys.AddGuestbookActivity;
import cn.js.nanhaistaffhome.activitys.HomeActivity;
import cn.js.nanhaistaffhome.activitys.LDBHActivity;
import cn.js.nanhaistaffhome.activitys.LoginActivity;
import cn.js.nanhaistaffhome.activitys.MainActivity;
import cn.js.nanhaistaffhome.activitys.NewsDetailActivity;
import cn.js.nanhaistaffhome.activitys.RegisterActivity;
import cn.js.nanhaistaffhome.activitys.SearchCompanyActivity;
import cn.js.nanhaistaffhome.activitys.ServiceMarketActivity;
import cn.js.nanhaistaffhome.activitys.WTTJActivity;
import cn.js.nanhaistaffhome.activitys.WYRHActivity;
import cn.js.nanhaistaffhome.activitys.WYTJActivity;
import cn.js.nanhaistaffhome.activitys.WYZXActivity;
import cn.js.nanhaistaffhome.activitys.WebActivity;

/**
 * Created by JS on 8/3/15.
 */
public class ActivityManager {

    private static Intent intent;

    public static void toLoginActivity(Context context) {
        intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void toHomeActivity(Context context){
        intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void toMainActivity(Context context,int pIndex,int cIndex) {
        intent = new Intent(context, MainActivity.class);
        intent.putExtra("parent",pIndex);
        intent.putExtra("child",cIndex);
        context.startActivity(intent);
    }

    public static void toMainActivity(Context context) {
        intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void toRegisterActivity(Context context) {
        intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void toNewsDetailActivity(Context context, long contentId, String title) {
        intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("contentId", contentId);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }

    public static void toActiDetailActivity(Context context,long id){
        intent = new Intent(context, ActiDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    public static void toAddGuestbookActivity(Context context,int type){
        intent = new Intent(context, AddGuestbookActivity.class);
        intent.putExtra("index",type);
        context.startActivity(intent);
    }

    public static void toSearchCompanyActivity(Activity context,int requestCode){
        intent = new Intent(context, SearchCompanyActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    public static void toWebActivity(Context context,String title,String url){
        intent = new Intent(context, WebActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }


    public static void toWYRHActivity(Context context){
        intent = new Intent(context,WYRHActivity.class);
        context.startActivity(intent);
    }

    public static void toWYZXActivity(Context context){
        intent = new Intent(context,WYZXActivity.class);
        context.startActivity(intent);
    }

    public static void toWYTJActivity(Context context){
        intent = new Intent(context, WYTJActivity.class);
        context.startActivity(intent);
    }

    public static void toServiceMarketActivity(Context context){
        intent = new Intent(context, ServiceMarketActivity.class);
        context.startActivity(intent);
    }

    public static void toLDBHActivity(Context context){
        intent = new Intent(context, LDBHActivity.class);
        context.startActivity(intent);
    }

    public static void toWTTJActivity(Context context){
        intent = new Intent(context,WTTJActivity.class);
        context.startActivity(intent);
    }


}
