package cn.js.nanhaistaffhome.utils;

import android.os.Environment;

import java.io.File;

/**
 * SDCard信息获取工具
 * @author JS
 * @since 2015-1-8
 * */
public class SDCardUtil {
	
	/**
	 * SD卡是否存在
	 * @author JS
	 * @since 2015-1-7
	 */
	public static boolean isSDCardExist() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取SD卡路径
	 * @return 返回SD卡根目录
	 * @author JS
	 * @since 2015-1-7
	 */
	public static String getSDCardPath() {
		File storageDirectory = null;
		if( isSDCardExist() ){
			storageDirectory = Environment.getExternalStorageDirectory();
		}
		else {
			storageDirectory = Environment.getDataDirectory();
		}
		return storageDirectory==null?"":storageDirectory.toString();
	}

}
