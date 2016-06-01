package cn.js.nanhaistaffhome.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	public final static String TIME_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 根据源时间、格式，格式化另一种格式
	 * @param source 源时间
	 * @param inFormat 源时间格式
	 * @param outFormat 时间输出格式
	 * @return 返回目标格式的时间
	 * */
	public static String formatDateStr(String source,String inFormat,String outFormat){
		String res = "";
		SimpleDateFormat sdf = new SimpleDateFormat(inFormat,Locale.CHINA);
		try {
			Date date = sdf.parse(source);
			res = formatDateToStr(date,outFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String  formatDateToStr(Date date){
		return formatDateToStr(date,TIME_FORMAT_DEFAULT);
	}

	public static String formatDateToStr(Date date,String outFormat){
		SimpleDateFormat sdf = new SimpleDateFormat(outFormat,Locale.CHINA);
		return sdf.format(date);
	}
	
	/**
	 * 字串符转日期
	 * 
	 * @param dateStr
	 * @param formatStr
	 * @return 2014-6-21 下午5:11:18 Date
	 */
	public static Calendar StringToDate(String dateStr, String formatStr) {
		Calendar cld = Calendar.getInstance();
		DateFormat dd = new SimpleDateFormat(formatStr, Locale.CHINA);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cld.setTime(date);
		return cld;
	}
	
	/**
	 * 时间戳转为字符串
	 */
	public static String getStrTime(long cc_time,String format) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CHINA);
		re_StrTime = sdf.format(new Date(cc_time * 1000L));
		return re_StrTime;

	}
	
	/**
	 * 获取当前时间戳
	 * */
	public static long getCurrentTimeStamp(){
		return new Date().getTime();
	}

}
