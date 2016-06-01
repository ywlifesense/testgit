package cn.js.nanhaistaffhome.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件管理器
 * @author JS
 * @since 2015-1-7
 * */
public class FileUtil{
	
	public static final String FOLDER_CACHE = "Cache";
	public static final String FOLDER_IMAGES = "Images";
	protected final String appName;
	
	public FileUtil(String appName){
		this.appName = appName;
		//有待改进：不存在sdcard，读写权限exception，自定义exception
	}

	/**
	 * 判断是否存在该图片
	 * */
	public static boolean exists(String file){
		return new File(file).exists();
	}
	
	/**
	 * 获取程序根目录
	 * @return 返回文件夹对象File
	 * @author JS
	 * @since 2015-1-7
	 */
	public File getApplicationRoot() {
		String path = SDCardUtil.getSDCardPath();
		if (TextUtils.isEmpty(path)){
			return null;
		}

		File folder = new File(path,appName);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}
	
	/**
	 * 获取程序保存图片的目录
	 * @return 返回图片目录对象File
	 * @author JS
	 * @since 2015-1-7
	 */
	public File getImageFolder(){
		if (getApplicationRoot()==null){
			return null;
		}
		File folder = new File(getApplicationRoot(),FOLDER_IMAGES);
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder;
	}
	
	/**
	 * 获取程序保存文件缓存的目录
	 * @return 返回缓存目录对象File
	 * @author JS
	 * @since 2015-1-7
	 */
	public File getCacheFolder(){
		if (getApplicationRoot()==null){
			return null;
		}
		File folder = new File(getApplicationRoot(),FOLDER_CACHE);
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder;
	}
	
	/**
	 * 获取程序根目录下自定义文件夹
	 * @param folderName 程序根目录下的文件夹名称，当文件夹名称为空时，返回程序主目录
	 * @author JS
	 * @since 2015-1-7
	 * */
	public File getCustomFolderInAppRoot(String folderName){
		if (getApplicationRoot()==null){
			return null;
		}
		File folder = getApplicationRoot();
		if(!TextUtils.isEmpty(folderName)){
			File tmp = new File(folder,folderName);
			if(!tmp.exists()){
				tmp.mkdirs();
			}
			folder = tmp;
		}
		return folder;
	}
	
	/**
	 * 根据文件夹名称获取路径,这里所获取文件夹是在程序根目录的文件夹
	 * @param folderName 文件夹名称
	 * @author JS
	 * @since 2015-1-7
	 * */
	public String getFolderPathInAppRoot(String folderName){
		String path = "";
		if(folderName.equals(FOLDER_CACHE)){
			path = getCacheFolder().toString();
		}else if(folderName.equals(FOLDER_IMAGES)){
			path = getImageFolder().toString();
		}else{
			path = getCustomFolderInAppRoot(folderName).toString();
		}
		return path;
	}
	
	/**
	 * 删除文件
	 * @param path 文件路径
	 * @author JS
	 * @since 2015-1-7
	 */
	public boolean deleteFile(String path) {
		File file = new File(path);
		return deleteFile(file);
	}
	
	/**
	 * 删除图片
	 * @param file 文件对象File
	 * @author JS
	 * @since 2015-1-7
	 */
	public boolean deleteFile(File file) {
		boolean isSec = false;
		if (file.exists()) {
			isSec = file.delete();
		}
		return isSec;
	}
	
	/**
	 * 创建文件
	 * @param path 文件所在文件夹路径
	 * @param name 文件名称
	 * @author JS
	 * @since 2015-1-7
	 */
	public File newFile(String path, String name) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, name);
		if(!file.exists()){
			try {
				if(!file.createNewFile()){
					file = null;
				}
			} catch (IOException e) {
				file = null;
			}
		}
		return file;
	}

	/**
	 * 将RES的GIF保存到SD卡
	 * @param resId
	 * @param file
	 * @return  
	 * @author JS
	 * @since 2015-1-7
	 */
	public String saveGif(Context c,int resId, File file) {
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		FileOutputStream fos = null;
		try {
			is = c.getResources().openRawResource(resId);
	        byte[] b = new byte[1024];
	        baos = new ByteArrayOutputStream();
	        int len = -1;
	        while ((len = is.read(b)) != -1) {
	        	baos.write(b, 0, len);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		byte[] result = baos.toByteArray();
		try {
			fos = new FileOutputStream(file);
	        fos.write(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return file.getAbsolutePath();
	}

	public static File toFile(Context context,Uri uri){
		String res = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
		if(cursor.moveToFirst()){
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		File file = new File(res);
		return file;
	}
	
}
