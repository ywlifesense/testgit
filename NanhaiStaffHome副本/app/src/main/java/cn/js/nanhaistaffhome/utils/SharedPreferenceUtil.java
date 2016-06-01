package cn.js.nanhaistaffhome.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferenceUtil工�?
 */
public class SharedPreferenceUtil {
	private static SharedPreferenceUtil sharedPreferenceUtil;

	private static SharedPreferences sharedPreferences;
	private String KEY = "roco_sharedpreferences";

	private SharedPreferenceUtil(Context context) {
		sharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
	}

	public static SharedPreferenceUtil getInstance(Context context) {
		if (sharedPreferenceUtil == null) {
			sharedPreferenceUtil = new SharedPreferenceUtil(context);
		}
		return sharedPreferenceUtil;
	}
	
	/**
	 * 设置SharedPreferences key
	 * */
	public void setSharedPreferencesKey(String key){
		this.KEY = key;
	}

	/**
	 * 设置String类�???
	 * 
	 * @param key
	 * @param value
	 */
	public void putString(String key, String value) {
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 设置long类�???
	 * 
	 * @param key
	 * @param value
	 */
	public void putLong(String key, long value) {
		Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 设置int类�???
	 * 
	 * @param key
	 * @param value
	 */
	public void putInt(String key, int value) {
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 设置Boolean类�???
	 * 
	 * @param key
	 * @param value
	 */
	public void putBoolean(String key, boolean value) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 设置Float类�???
	 * 
	 * @param key
	 * @param value
	 */
	public void putFloat(String key, float value) {
		Editor editor = sharedPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	/**
	 * 读�?String类�??��?�??�?"
	 * 
	 * @param key
	 */
	public String getString(String key) {
		return sharedPreferences == null ? "" : sharedPreferences.getString(key, "");
	}

	/**
	 * 读�?boolean类�??��?�??�?alse;
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key, boolean deafultValue) {
		return sharedPreferences.getBoolean(key, deafultValue);
	}

	/**
	 * 读�?int类�??��?�??�?
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return sharedPreferences.getInt(key, 0);
	}
	
	/**
	 * 读�?int类�??��?�???��?�??
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key, int defValue) {
		return sharedPreferences.getInt(key, defValue);
	}

	/**
	 * 读�?long类�??��?�??�?
	 * 
	 * @param key
	 * @return
	 */
	public long getLong(String key) {
		return sharedPreferences.getLong(key, 0);
	}

	/**
	 * 读�?float类�??��?�??�?
	 * 
	 * @param key
	 * @return
	 */
	public float getFloat(String key) {
		return sharedPreferences.getFloat(key, 0);
	}

	/** ?��????�??此�?�?*/
	public boolean has(String key) {
		return sharedPreferences.contains(key);
	}

}
