
/**   
 * @文件名: DateUtil.java
 * @包 tydic.portalCommon
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-5-30 下午05:37:17
 *  
 */
  
package tydic.portalCommon;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：DateUtil   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-5-30 下午05:37:17   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class DateUtil {
	
	/**
	 * <p>Des: 只包含年月日的时间格式
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	//用来全局控制 上一周，本周，下一周的周数变化
	private static int weeks = 0;
	/**
	 * @Title: getMothIntervals 
	 * @Description: 根据最大时间与月份差获取月份列表
	 * @param date 最大时间
	 * @param layDiff 相差多少个月
	 * @param dateformat 时间格式（带上天：yyyyMMdd）
	 * @return List<String> 倒序
	 * @throws
	 */
	public List<String> getMothIntervals(String date,int layDiff,String dateformat){
		List<String> list = new ArrayList<String>();
		date = date.split(" ")[0];
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
		String start = date;
		if(start.replaceAll("-", "").length() >= 6)
			start = start.replaceAll("-", "").substring(0,4) + "-" + start.replaceAll("-", "").substring(4,6) + "-" + "01";
		int abs_layday = java.lang.Math.abs(layDiff);
		Calendar c = Calendar.getInstance();
		try {
			if(dateformat.indexOf("-") == -1)
				start = start.replaceAll("-", "");
			list.add(start);
			c.setTime(dateFormat.parse(start));
			for(int i = 1; i <= abs_layday; i++){
				c.add(Calendar.MONTH, -1);
				String temp = dateFormat.format(c.getTime());
				if(dateformat.indexOf("-") == -1)
					temp = temp.replaceAll("-", "");
				list.add(temp);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * @Title: getWeekIntervals 
	 * @Description: 根据最大时间与周差获取周列表（注：周时间是星期一）
	 * @param date 最大时间
	 * @param layDiff 周差
	 * @param dateformat 时间格式（到天）
	 * @return List<String> 倒序
	 * @throws
	 */
	public List<String> getWeekIntervals(String date,int layDiff,String dateformat){
		List<String> list = new ArrayList<String>();
		date = date.split(" ")[0];
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
		int abs_layday = java.lang.Math.abs(layDiff);
		Calendar c = Calendar.getInstance();
		try {
			//现在日期对应周的星期一
			String nowMonday = getMondayOFWeek(dateFormat.parse(date),dateformat);
			if(dateformat.indexOf("-") == -1)
				nowMonday = nowMonday.replaceAll("-", "");
			list.add(nowMonday);
			c.setTime(dateFormat.parse(nowMonday));
			for(int i = 1; i <= abs_layday; i++){//一个循环是7天
				c.add(Calendar.DATE, -1*7);
				String temp = dateFormat.format(c.getTime());
				if(dateformat.indexOf("-") == -1)
					temp = temp.replaceAll("-", "");
				list.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 获取当前日期的周一
	 */
	public String getMondayOFWeek(Date d, String dateformat) {
		weeks = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
		int mondayPlus = this.getMondayPlus(dateFormat.format(d),dateformat);
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTimeInMillis((d).getTime());
		currentDate.add(currentDate.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		String preMonday = dateFormat.format(monday);
		return preMonday;
	}
	private static int getMondayPlus(String date, String dateFormat){
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);// 可以方便地修改日期格式
		Calendar cd = Calendar.getInstance();
		try {
			cd.setTime(sdf.parse(date));
			int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
			if (dayOfWeek == 1) {
				return 0;
			} else {
				return 1 - dayOfWeek;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * @Title: listAsc 
	 * @Description: list升序
	 * @param list
	 * @return List<String>   
	 * @throws
	 */
	public List<String> listAsc(List<String> list){
		if(list == null && list.size() == 0)
			return null;
		Collections.sort(list,new Comparator<String>(){
			public int compare(String s1, String s2) {
				return Integer.valueOf(s1) - Integer.valueOf(s2);
			}
		});
		return list;
	}
	
	/**
	 * 
	 * <p>Description 将date换化为yyyy-MM-dd格式的字符串</p>
	 * <p>Copyright Copyright(c)2007</p>
	 * @create time: 2007-3-2 下午02:39:30
	 * @version 1.0
	 * 
	 * @modified records:
	 */
	public static String formatToDate(Date date) {
		SimpleDateFormat formater = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return formater.format(date);
	}
	
	/**
	 * 
	 * <p>Description 将指定格式的字符串转化为日期类型</p>
	 * <p>Copyright Copyright(c)2007</p>
	 * @create time: 2007-3-2 下午03:07:49
	 * @version 1.0
	 * 
	 * @modified records:
	 */
	public static Date parseToDate(String date, String pattern) {
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		try {
			return formater.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}	
	/**
	 * 
	 * <p>Description 将传入的字符串格式的日期增加指定的月数</p>
	 * <p>Copyright Copyright(c)2007</p>
	 * @create time: 2007-3-5 上午09:08:57
	 * @version 1.0
	 * 
	 * @modified records:
	 */
	public static String addMonth(String date, int size) {
		Date d = parseToDate(date,DEFAULT_DATE_FORMAT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.MONTH, size);
		d = calendar.getTime();
		return formatToDate(d);
	}
	
	/**
	 * 
	 * <p>Description 将传入的字符串格式的日期增加指定的日期</p>
	 * <p>Copyright Copyright(c)2007</p>
	 * @create time: 2007-3-5 上午09:08:57
	 * @version 1.0
	 * 
	 * @modified records:
	 */
	public static String lastDay(String date, int size) {
		Date d = parseToDate(date,DEFAULT_DATE_FORMAT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DATE, size);
		d = calendar.getTime();
		return formatToDate(d);
	}
	
	
	/**
	 * 
	 * @author 占翔
	 * @description 将date换化为yyyy-MM-dd HH:mm
	 * @param date
	 * @return
	 * @modified
	 */
	public static String formatToTimeNosce(String date){
		Date thedate = parseToDate(date, "yyyy-MM-dd");
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMM");
		return formater.format(thedate);
	}
	/*
	 * 获取当前日期月的第一天
	 */
	public static String fistDay(Date date){
		int m = date.getMonth();  
		int y = date.getYear();  
		Date fistDay = new Date(y,m,1);  
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String first=formatter.format(fistDay);
		return first;
	}
	/*
	 * 获取当前日期月的第一天(精确到时分秒)
	 */
	public static String fistDayDetail(Date date){
		int m = date.getMonth();  
		int y = date.getYear();  
		Date fistDay = new Date(y,m,1);  
		DateFormat df2 = DateFormat.getDateTimeInstance();
	    String first=df2.format(fistDay);
		return first;
	}  
	
	public static Calendar getDateOfLastMonth(Calendar date) {
		Calendar lastDate = (Calendar) date.clone();
		lastDate.add(Calendar.MONTH, -1);
		return lastDate;
	}

	public static Calendar getDateOfLastMonth(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = sdf.parse(dateStr);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return getDateOfLastMonth(c);
		} catch (ParseException e) {
			throw new IllegalArgumentException(
					"Invalid date format(yyyyMMdd): " + dateStr);
		}
	}

 
	
     
	
	  public static String[] getAddMonths(int num,String newMonth) {
			String[] res = new String[num];
			newMonth=newMonth.replaceAll("-", "");
	        try {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	     	Date date = sdf.parse(newMonth);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			for (int i = 0; i<num ; i++) {
				 res[i]=(String)new SimpleDateFormat("yyyyMM").format(c.getTime());
				 c.add(Calendar.MONTH, -1);
			}
	        }catch(Exception  e){
	        	e.printStackTrace();
	        }
			return res;
		}
	  
		/** 获得给定日期当月的天数 */
		public static int getDays(String date_str, String type) {
			return (convertStrToCal(date_str, type)
					.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		/**
		  * 得到某年某月的最后一天
		  * 
		  * @param year
		  * @param month
		  * @return
		  */ 
		 public static String getLastDayOfMonth(int year, int month) {
		  Calendar cal = Calendar.getInstance();
		  cal.set(Calendar.YEAR, year);
		  cal.set(Calendar.MONTH, month-1);
		  cal.set(Calendar.DAY_OF_MONTH, 1);
		  int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		  cal.set(Calendar.DAY_OF_MONTH, value);
		  return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		 }

		/** 获得给定日期当月的天数 */
		public static int getDays(Calendar cal) {
			return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		}

		/** 获得给定日期当月的天数 */
		public static int getDays(Date date) {
			return (convertDateToCal(date).getActualMaximum(Calendar.DAY_OF_MONTH));
		}


	    public static String getCurrMonthLastDay(Calendar cal) {
			String s=(getYear(cal))+"-"+(getMonth(cal))+"-"+getDays(cal);
			//return convertStrToDate(s,"yyyy-MM-dd");
			return s;
		}
		/** 获得给定日历的年 */
		public static int getYear(Calendar cal) {
			return cal.get(Calendar.YEAR);
		}

		/** 获得给定日期的年 */
		public static int getYear(Date date) {
			return convertDateToCal(date).get(Calendar.YEAR);
		}

		/** 获得给定日期字符串对应的年 */
		public static int getYear(String date_str, String type) {
			return (convertStrToCal(date_str, type).get(Calendar.YEAR));
		}

		/** 获得给定日历的月 */
		public static int getMonth(Calendar cal) {
			return (cal.get(Calendar.MONTH) + 1);
		}

		/** 获得给定日期的月 */
		public static int getMonth(Date date) {
			return (convertDateToCal(date).get(Calendar.MONTH) + 1);
		}
		
		/** 字符转换日期(动态格式转换) */
		public static Date convertStrToDate(String date_str, String type) {
			SimpleDateFormat dateformat = new SimpleDateFormat(type);
			try {
				return dateformat.parse(date_str);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/** 日期转日历* */
		public static Calendar convertDateToCal(Date date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
		}
		/** 字符转换日历(动态格式转换) */
		public static Calendar convertStrToCal(String date_str, String type) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(convertStrToDate(date_str, type));
			return cal;
		}	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    System.out.println(sdf.format(getDateOfLastMonth("20130528").getTime()));// 上月日期
	}
	 public static long getInterval(String startDate, String endDate){
		 long interval = 0;
		  SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		  try {
		   Date start = ft.parse( startDate );
		   Date end = ft.parse( endDate );
		   interval = end.getTime() - start.getTime();
		   interval = interval / 1000 / 60 / 60 / 24;
		  } catch (ParseException e) {
		   e.printStackTrace();
		  }
		  return interval;
		 }
	
		/*
		 * 获取上月日期
		 */
		public static String getLastMon(String currentMon){
			String tempStr=currentMon.substring(4, currentMon.length());
			Integer retValue=0;
			if("01".equals(tempStr)){
				retValue=Integer.parseInt(currentMon)-89;
			}else{
				retValue=Integer.parseInt(currentMon)-1;
			}
			return String.valueOf(retValue);
		}
		

		/**
		 *  根据给定的格式，返回上一天的时间字符串。
		  * 方法描述：
		  * @param: 
		  * @return: 
		  * @version: 1.0
		  * @author: yanhaidong
		  * @version: 2013-5-17 下午02:25:40
		 */
		public static String getFormatLastDay(String format) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			return getFormatDateTime(calendar.getTime(), format);
		}
		/**
		 * 根据给定的格式与时间(Date类型的)，返回时间字符串。最为通用。<br>
		 * 
		 * @param date
		 *            指定的日期
		 * @param format
		 *            日期格式字符串
		 * @return String 指定格式的日期字符串.
		 */
		public static String getFormatDateTime(Date date, String format) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}		
}
