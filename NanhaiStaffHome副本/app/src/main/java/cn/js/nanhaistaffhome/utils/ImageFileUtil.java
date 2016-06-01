package cn.js.nanhaistaffhome.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.js.nanhaistaffhome.StaffApplication;

public class ImageFileUtil extends FileUtil{

	private static ImageFileUtil instance;

	public static ImageFileUtil getInstance(){
		if (instance == null){
			instance = new ImageFileUtil(StaffApplication.APPFolder);
		}
		return instance;
	}

	public ImageFileUtil(String appName) {
		super(appName);
	}
	
	public String saveBitmapToSDCard(String path,String fileName,byte[] bytes,CompressFormat format){
		String filePath = "";
		File file = new File(path,fileName);
		try {
			if(!file.exists()){
				file.createNewFile();
			}
		} catch (IOException e) {
			Log.e(appName,"在保存图片时出错：" + e.toString());
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
			fOut.write(bytes);
			fOut.flush();
			fOut.close();
			filePath = file.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}
	
	public String saveBitmapToSDCard(String path,String fileName,Bitmap bmp,CompressFormat format) {
		String filePath = "";
		File file = newFile(path,fileName);
		if(file != null){
			try {
				  FileOutputStream fOut = new FileOutputStream(file);
				  bmp.compress(format, 100, fOut);
				  fOut.flush();
				  fOut.close();
				  filePath = file.toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}
	
	public String saveBitmapToImageFolder(String fileName,Bitmap bmp,CompressFormat format){
		return saveBitmapToSDCard(getImageFolder().toString(), fileName, bmp, format);
	}
	
	public String savePNGToImageFolder(String fileName,Bitmap bmp){
		return saveBitmapToSDCard(getImageFolder().toString(), fileName, bmp, CompressFormat.PNG);
	}

	public String savePNGToImageFolder(String fileName,byte[] bytes){
		return saveBitmapToSDCard(getImageFolder().toString(), fileName, bytes, CompressFormat.PNG);
	}
	
	public String saveJPGToImageFolder(String fileName,Bitmap bmp){
		return saveBitmapToSDCard(getImageFolder().toString(), fileName, bmp, CompressFormat.JPEG);
	}

	public String saveJPGToCacheFolder(String fileName,Bitmap bmp){
		return saveBitmapToSDCard(getCacheFolder().toString(), fileName, bmp, CompressFormat.JPEG);
	}

	public String saveJPGToImageFolder(String fileName,byte[] bytes){
		return saveBitmapToSDCard(getImageFolder().toString(), fileName, bytes, CompressFormat.JPEG);
	}

	public boolean deleteFileFromImageFolder(String fileName){
		File file = new File(getImageFolder().toString(),fileName);
		return deleteFile(file);
	}
	
	public CompressFormat judgeImageCompressFormat(String path){
		CompressFormat res = null;
		int pos = path.lastIndexOf(".");
		String type = path.substring(pos+1);
		if(type.equals("png")){
			res = CompressFormat.PNG;
		}else if(type.equals("jpg") || type.equals("jpeg")){
			res = CompressFormat.JPEG;
		}
		return res;
	}

	public boolean isFileInCacheFolder(String fileName){
		if (getCacheFolder()==null){
			return false;
		}
		File file = new File(getCacheFolder(),fileName);
		if (file.exists()){
			return true;
		}
		return false;
	}

	public boolean isFileInImageFolder(String fileName){
		if (getImageFolder()==null){
			return false;
		}
		File file = new File(getImageFolder(),fileName);
		if (file.exists()){
			return true;
		}
		return false;
	}

	public Drawable getImageDrawable(String uri){
		if(!exists(uri)) return null;
		try{
			InputStream inp = new FileInputStream(new File(uri));
			return BitmapDrawable.createFromStream(inp, "img");
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	public Drawable getDrawableFromCacheFolder(String fileName){
		File file = new File(getCacheFolder(),fileName);
		if (!file.exists()){
			return null;
		}
		try {
			InputStream in = new FileInputStream(file);
			return BitmapDrawable.createFromStream(in,fileName);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public Bitmap getBitmapFromCacheFolder(String fileName){
		File file = new File(getCacheFolder(),fileName);
		return BitmapFactory.decodeFile(file.getAbsolutePath());
	}

	public Drawable getDrawableFromImageFolder(String fileName){
		File file = new File(getImageFolder(),fileName);
		if (!file.exists()){
			return null;
		}
		try {
			InputStream in = new FileInputStream(file);
			return BitmapDrawable.createFromStream(in,fileName);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}
