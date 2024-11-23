/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank.utils;

import com.avalon.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 类功能说明
 */
public class DateUtil {
    /**
     * @Date 2013年12月26日
     */
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    /**日期长度*/
    public static final int  DATE_CHANGE_LENGTH=18;
    /** 日期格式(8) yyyyMMdd */
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMdd";
    /** char(17) yyyyMMdd hh:mm:ss */
    public static final String DATE_FORMAT_YYYYMMDD4CHAR17 = "yyyyMMdd HH:mm:ss";

    /** char(17) yyyy/MM/dd hh:mm:ss */
    public static final String DATE_FORMAT_YYYY_MM_DD4CHAR17 = "yyyy/MM/dd HH:mm:ss";
    /**
     * 默认日期格式 Timestamp format must be yyyymmddhhmmss
     */
    public static final String DEFAULT_FORMAT = "yyyyMMddHHmmss";

    /**
     * 日期时间 Timestamp format must be yyyy-MM-dd hh:mm:ss[.SSS]
     */
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";

    /** 日期格式(10) yyyy-MM-dd */
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    /** 日期格式(8) 'HH:mm:ss 172421'时分秒 */
    public static final String DATE_FORMAT_HHMMSS = "HHmmss";

    public static final String UNIT_Y = "Y";// 年
    public static final String UNIT_M = "M";// 月
    public static final String UNIT_D = "D";// 日

    private static final long ND = 1000 * 24 * 60 * 60;// 一天的毫秒数
    private static final long NH = 1000 * 60 * 60;// 一小时的毫秒数
    private static final long NM = 1000 * 60;// 一分钟的毫秒数
    private static final long NS = 1000;// 一秒钟的毫秒数
    private static final int LEN_8 = 8;//
    private static final int LEN_14 = 14;//
    private static final int LEN_17 = 17;//
    
    private  static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    
    /**
     * @return yyyyMMdd格式的当前日期
     */
    public static String getyyyyMMddHHmmssSSS() {
        return get(YYYYMMDDHHMMSSSSS);
    }

    /**
     * @return yyyyMMdd格式的当前日期
     */
    public static String getyyyyMMddHHmmss() {
        return get(YYYYMMDDHHMMSS);
    }

    /**
     * @return yyyyMMdd格式的当前日期
     */
    public static String getyyyyMMdd() {
        return get(YYYYMMDD);
    }
    
    /**
     * @return yyyy-MM-dd格式的当前日期
     */
    public static String getFormatyyyyMMdd() {
        return get(DATE_FORMAT_YYYY_MM_DD);
    }

    
    /**
     * @return hhmmss格式的当前时间
     */
    public static String getHHmmss() {
        return get("HHmmss");
    }

    /**
     * @param format
     * @return format格式的当前日期时间
     */
    public static String get(String format) {
        if (format == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * @param format
     * @return format格式的当前日期时间
     */
    public static String get(String format, Date date) {
        if (format == null || date == null) {
            return null;
        }
        String dateString = null;
        try {
            dateString = new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            return dateString;
        }
        return dateString;
    }
    /**
     * 格式化输出日期
     * 
     * @param date
     *            2013年11月21日
     * @param format
     *            格式
     * @return 返回字符型日期 wangrf
     */
    public static String format(Date date, String format) {
        String result = "";
        if (date != null) {
            DateFormat df = new SimpleDateFormat(format);
            result = df.format(date);
        }
        return result;
    }
    
    
    /**
     * 2017-11-10 15:58:45
     * @param datestString yyyy-MM-dd HH:MM:SS
     * @return
     * @date 2017年11月20日
     */
    public static Date parseDate(String datestString) {
    	return parseDate("yyyy-MM-dd HH:mm:ss", datestString);
    }
    
    /**
     * @param format
     * @param datestString
     * @return
     * @date 2017年11月20日
     */
    public static Date parseDate(String format, String datestString) {
    	try {
			return new SimpleDateFormat(format).parse(datestString);
		} catch (ParseException e) {
			logger.error("", e);
			return null;
		}
    }
    
    
   

       /**
     * 得到当前系统日期，格式yyyyMMddHHmmssSSS
     * 
     * @return：字符串格式
     */
    public static String getCurDate20Str() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMMSSSSS);
        try {
            return sdf.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 得到当前系统日期，格式yyyyMMdd
     * 
     * @return：字符串格式
     */
    public static String getCurDate4Str() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
        try {
            return sdf.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 得到当前系统日期，格式yyyyMMdd HH:mm:ss
     * 
     * @return：字符串格式
     */
    public static String getCurDate4Char17() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD4CHAR17);
        try {
            return sdf.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
	 * @return yyyyMMdd格式的当前日期
	 */
	public static String getCurDate17Str() {
		return get(YYYYMMDDHHMMSSSSS);
	}
    
    /**
     * 将传入日期格式化成字符串，格式为：yyyyMMdd HH:mm:ss
     * 
     * @param date
     * @return 字符串格式
     */
    public static String getCurDate4Char17(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD4CHAR17);
        try {
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 将传入日期格式化成字符串，格式为：yyyy/MM/dd HH:mm:ss
     * 
     * @param date
     * @return 字符串格式
     */
    public static String getDateToStringChar17(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD4CHAR17);
        try {
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符串转换成日期 如果转换格式为空，则利用默认格式进行转换操作
     * 
     * @param str
     *            字符串
     * @param format
     *            日期格式
     * @return 日期
     * @throws ParseException
     */
    public static Date str2Date(String str, String format) {
        if (null == str || "".equals(str)) {
            return null;
        }
        // 如果没有指定字符串转换的格式，则用默认格式进行转换
        if (null == format || "".equals(format)) {
            format = DEFAULT_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转换为字符串
     * 
     * @param date
     *            日期
     * @param format
     *            日期格式
     * @return 字符串
     */
    public static String date2Str(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 时间戳转换为字符串
     * 
     * @param time
     * @return
     */
    public static String timestamp2Str(Timestamp time) {
        Date date = null;
        if (null != time) {
            date = new Date(time.getTime());
        }
        return date2Str(date, DEFAULT_FORMAT);
    }

    /**
     * 使用自定义格式 ，字符串转换时间戳
     * 
     * @param str
     * @param format
     * @return
     */
    public static Timestamp str2Timestamp(String str, String format) {
        Date date = str2Date(str, format);
        return new Timestamp(date.getTime());
    }

    /**
     * 使用默认格式 ，字符串转换时间戳
     * 
     * @param str
     * @return
     */
    public static Timestamp str2Timestamp(String str) {
        return str2Timestamp(str, null);
    }

    /**
     * 获取系统当前的时间戳
     * 
     * @return
     */
    public static Timestamp getCurTimestamp() {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * 日期加天、月、年
     * 
     * @param optype
     *            加类型
     * @param date
     *            基础日期
     * @param num
     *            所加数量
     * @return
     */

    private static String add_Date(int optype, String date, int num)
            throws Exception {
        String st_return = "";
        try {
            // 可支持的格式化数据是10位的
            String temp = date;
            date = date.replaceAll("/", "");
            date = date.replaceAll("-", "");
            date = date.substring(0, 8);
            //将时间拆分为YYYY-MM-DD
            if (date.length() == 8) {
                temp = date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
                        + date.substring(6, 8);
            }
            date = temp;
            DateFormat daf_date = DateFormat.getDateInstance(DateFormat.MEDIUM,
                    Locale.CHINA);
            daf_date.parse(date);
            Calendar calendar = daf_date.getCalendar();
            calendar.add(optype, num);
            String st_m = "";
            String st_d = "";
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            if (m <= 9) {
                st_m = "0" + m;
            } else {
                st_m = "" + m;
            }
            if (d <= 9) {
                st_d = "0" + d;
            } else {
                st_d = "" + d;
            }
            st_return = y + st_m + st_d;
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
        return st_return;
    }

    /**
     * 方法说明 ：增加秒数
     * 
     * @author 宋杰 2013年12月17日上午9:42:27
     * 
     * @param date
     *            基础日期
     * @param delta
     *            秒数的增量。可正可负
     * @return
     */
    public static Date add_Second(Date date, long delta) {

        long originalMillionSenconds = date.getTime();

        long rtnMillionSeconds = originalMillionSenconds + delta * 1000;

        return new Date(rtnMillionSeconds);
    }

    /**
     * 增加天数
     * 
     * @param date
     * @param n
     * @return
     */
    public static String add_Day(String date, int n) throws Exception {
        return DateUtil.add_Date(Calendar.DATE, date, n);
    }
    
    /**
     * 传入17位字符串日期，返回17为字符串日期
     * @author zhaozeyu
     * 2014年7月24日 下午5:06:32
     * @param date
     * @param n
     * @return
     * @throws Exception
     */
    public static String add_Day4Char17(String date, String n) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD4CHAR17);
        Date startdt = sdf.parse(date);
        long startms = startdt.getTime() + Long.parseLong(n) * ND;
        startdt.setTime(startms);
        return getCurDate4Char17(startdt);
    }

    /**
     * 增加月数
     * 
     * @param date
     * @param n
     * @return
     */
    public static String add_Month(String date, int n) throws Exception {
        return DateUtil.add_Date(Calendar.MONTH, date, n);
    }

    /**
     * 增加年数
     * 
     * @param date
     * @param n
     * @return
     */
    public static String add_Year(String date, int n) throws Exception {
        return DateUtil.add_Date(Calendar.YEAR, date, n);
    }

    /**
     * 日期加天、月、年
     * 
     * @param date
     *            日期
     * @param unit
     *            年（Y），月(M)，日(D)
     * @param n
     *            所加数量
     * @return
     * @throws Exception
     */
    public static String add_Date_YMD(String date, String unit, int n)
            throws Exception {
        String dateStr = "";
        if (DateUtil.UNIT_Y.equals(unit)) {
            dateStr = DateUtil.add_Year(date, n);
        } else if (DateUtil.UNIT_M.equals(unit)) {
            dateStr = DateUtil.add_Month(date, n);
        } else if (DateUtil.UNIT_D.equals(unit)) {
            dateStr = DateUtil.add_Day(date, n);
        } else {
            throw new Exception("传入参数增加类型【年（Y），月(M)，日(D)】有误");
        }
        return dateStr;
    }

    /**
     * 比较两个日期的大小
     * 
     * @param date1
     * @param date2
     * @return date1>date2 1 date1=date2 0 date1<date2 -1
     */
    public static int compareDate(String date1, String date2) throws Exception {
        date1 = DateUtil.changeDateStr4YMD(date1);
        date2 = DateUtil.changeDateStr4YMD(date2);
        int res = 0;
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM,
                Locale.CHINA);
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = df.parse(date1.substring(0, 4) + "-" + date1.substring(4, 6)
                    + "-" + date1.substring(6, 8));
            d2 = df.parse(date2.substring(0, 4) + "-" + date2.substring(4, 6)
                    + "-" + date2.substring(6, 8));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new Exception("日期错误，请检查要比较的时期是否合理");
        }
        int r = d1.compareTo(d2);
        if (r == 0) {
            res = 0;
        } else if (r < 0) {
            res = -1;
        } else {
            res = 1;
        }
        return res;
    }

    /**
     * 比较两个日期的大小
     * 
     * @param date1
     *            yyyyMMdd hh:mm:ss
     * @param date2
     *            yyyyMMdd hh:mm:ss
     * @return date1>date2 1 date1=date2 0 date1<date2 -1
     */
    public static int compareDateTime(String date1, String date2)
            throws Exception {
        date1 = DateUtil.changeDateStr4YMDHSM(date1);
        date2 = DateUtil.changeDateStr4YMDHSM(date2);
        Date d1 = DateUtil.str2Date(date1, DATE_FORMAT_YYYYMMDD4CHAR17);
        Date d2 = DateUtil.str2Date(date2, DATE_FORMAT_YYYYMMDD4CHAR17);
        int res = 0;
        int r = d1.compareTo(d2);
        if (r == 0) {
            res = 0;
        } else if (r < 0) {
            res = -1;
        } else {
            res = 1;
        }
        return res;
    }

    /**
     * 将日期类型的字符串转换"YYYY-MM-DD"类型的字符串
     * 
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static String changeDateStr(String dateStr) throws Exception {
        String dtStr = "";
        if (dateStr == null || "".equals(dateStr)) {
            throw new Exception("传入参数日期不能为空");
        } else if (dateStr.trim().length() < 8) {
            throw new Exception("传入参数日期有误");
        } else if (dateStr.trim().length() == 8) {// YYYYMMDD
            Date dt = DateUtil.str2Date(dateStr, DATE_FORMAT_YYYYMMDD);
            dtStr = DateUtil.date2Str(dt, DATE_FORMAT_YYYY_MM_DD);
        } else if (dateStr.trim().length() == 10) {// YYYY-MM-DD
            dtStr = dateStr;
        } else if (dateStr.trim().length() == 18) {// 20121218000000.000
            String dtStrTemp = dateStr.substring(0, 8);
            Date dt = DateUtil.str2Date(dtStrTemp, DATE_FORMAT_YYYYMMDD);
            dtStr = DateUtil.date2Str(dt, DATE_FORMAT_YYYY_MM_DD);
        }else if (dateStr.trim().length() == 17) {// 20140109 09:09:09
            String dtStrTemp = dateStr.substring(0, 8);
            Date dt = DateUtil.str2Date(dtStrTemp, DATE_FORMAT_YYYYMMDD);
            dtStr = DateUtil.date2Str(dt, DATE_FORMAT_YYYY_MM_DD);
        }else {
            throw new Exception("不能转换此日期格式：" + dateStr);
        }
        return dtStr;
    }
    
    /**
     * 将DD日期类型的字符串转换成跟当天的年、月拼装成的"YYYY-MM-DD"类型的字符串,需要判断闰年、每个月的具体情况
     * 
     * @author MacDancer
     * @param dateStr  DD格式的选择日期
     * @return
     * @throws Exception
     */
    public static String changeCurrentDateStr(String dateStr) throws Exception {
        String dtStr = "";
        if (dateStr == null || "".equals(dateStr)) {
            throw new Exception("传入参数日期不能为空");
        } else if (dateStr.trim().length() != 2) {
            throw new Exception("传入参数日期长度有误，长度必须为2位且介于01-31之间");
        } else if (dateStr.trim().length() == 2) {   // DD
        	String currentDateString = getFormatyyyyMMdd();   //yyyy-MM-dd
        	String yearAndMonth  = currentDateString.substring(0, currentDateString.length()-2);
        	String year = currentDateString.substring(0, 4);
        	String month = currentDateString.substring(5, 7);
        	String[] day30={"04","06","09","11"};//定义30天的所有月份
        	if((new Integer(year))%4 == 0) {   //如果是闰年
        		if(month.equals("02")) {
        			if(dateStr.equals("30") || dateStr.equals("31")) {
        				dateStr = "29";
        			}
        		} else if(isHave(day30, month)) {
        			if(dateStr.equals("31")) {
        				dateStr = "30";
        			}
        		} else{
        			//不做处理
        		}
        	} else {  //如果是平年
        		if(month.equals("02")) {
        			if(dateStr.equals("29") || dateStr.equals("30") || dateStr.equals("31")) {
        				dateStr = "28";
        			}
        		} else if(isHave(day30, month)) {
        			if(dateStr.equals("31")) {
        				dateStr = "30";
        			}
        		} else{
        			//不做处理
        		}
        	}
        	
            dtStr = yearAndMonth.concat(dateStr);
        }else {
            throw new Exception("不能转换此日期格式：" + dateStr);
        }
        return dtStr;
    }
    
	/**
	 * 此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符串
	 * @author MacDancer
	 */
	public static boolean isHave(String[] strs, String s) {
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].equals(s)) {// 循环查找字符串数组中的每个字符串中是否包含所有查找的内容
				return true;// 查找到了就返回真，不在继续查询
			}
		}
		return false;
	}

    /**
     * 将日期类型的字符串转换"YYYYMMDD"类型的字符串
     * 
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static String changeDateStr4YMD(String dateStr) throws Exception {
        String dtStr = "";
        if (dateStr == null || "".equals(dateStr)) {
            throw new Exception("传入参数日期不能为空");
        } else if (dateStr.trim().length() < 8) {
            throw new Exception("传入参数日期有误");
        } else if (dateStr.trim().length() == 8) {// YYYYMMDD
            dtStr = dateStr;
        } else if (dateStr.trim().length() == 10) {// YYYY-MM-DD
            Date dt = DateUtil.str2Date(dateStr, DATE_FORMAT_YYYY_MM_DD);
            dtStr = DateUtil.date2Str(dt, DATE_FORMAT_YYYYMMDD);
        } else if (dateStr.trim().length() == 18) {// 20121218000000.000
            String dtStrTemp = dateStr.substring(0, 8);
            Date dt = DateUtil.str2Date(dtStrTemp, DATE_FORMAT_YYYYMMDD);
            dtStr = DateUtil.date2Str(dt, DATE_FORMAT_YYYYMMDD);
        }
        // 20131227 19:21:21这种格式，可转化
        else if (dateStr.length() == DATE_CHANGE_LENGTH) {
            dtStr = dateStr.substring(0, 8);

        } else {
            throw new Exception("不能转换此日期格式：" + dateStr);
        }
        return dtStr;
    }

    /**
     * 将日期类型的字符串转换"yyyyMMdd hh:mm:ss"类型的字符串
     * 
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static String changeDateStr4YMDHSM(String dateStr) throws Exception {
        String dtStr = "";
        if (dateStr == null || "".equals(dateStr)) {
            throw new Exception("传入参数日期不能为空");
        } else if (dateStr.trim().length() < LEN_8 || dateStr.trim().length() < LEN_14) {
            throw new Exception("传入参数日期有误");
        } else if (dateStr.trim().length() == LEN_17) {// yyyyMMdd hh:mm:ss
            dtStr = dateStr;
        } else {
            throw new Exception("不能转换此日期格式：" + dateStr);
        }
        return dtStr;
    }

    /**
     * 计算两日前之前相差多少天，下取整
     * 
     * @param startdate
     *            enddate
     * @param format 
     * @return
     * @throws Exception
     */
    public static int dateDiff(String startdate, String enddate, String format)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date startdt = sdf.parse(startdate);
        Date enddt = sdf.parse(enddate);
        BigDecimal diff = new BigDecimal(enddt.getTime()).subtract(new BigDecimal(startdt.getTime()));
//      int day = diff / ND; // 计算差多少天
        BigDecimal day = diff.divideToIntegralValue(new BigDecimal(ND));// 计算差多少天
        return day.intValue();
    }
    
    /**
     * 计算日期到当前时间的距离，上取整
     * @author zhaozeyu
     * 2014年7月7日 上午10:56:46
     * @param startdate
     * @param format
     * @return
     * @throws Exception
     */
    public static int date2NowDiff(String startdate, String format)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date startdt = sdf.parse(startdate);
        Date enddt = Calendar.getInstance().getTime();
        BigDecimal diff = new BigDecimal(enddt.getTime()).subtract(new BigDecimal(startdt.getTime()));
//      int day = diff / ND; // 计算差多少天
        BigDecimal day = diff.divide(new BigDecimal(ND), BigDecimal.ROUND_CEILING);// 计算差多少天
        return day.intValue();
    }
    
    /**
     * 计算两日期之间相差 天 时 分
     * 
     * @param startdate
     *            enddate
     * @param format
     * @return
     * @throws Exception
     */
    public static String diffDate4Str(String startdate, String enddate,
            String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date startdt = sdf.parse(startdate);
        Date enddt = sdf.parse(enddate);
        long diff = enddt.getTime() - startdt.getTime();
        long day = diff / ND; // 计算差多少天
        long dayLeng = diff - day * ND;
        long hour = dayLeng / NH; // 计算差多少小时
        long minLeng = dayLeng - hour * NH;
        long min = minLeng / NM; // 计算差多少分钟

        StringBuffer dateStr = new StringBuffer();
        if (day > 0) {
            dateStr.append(day).append("天");
        }
        if (hour > 0) {
            dateStr.append(hour).append("小时");
        }
        if (min > 0) {
            dateStr.append(min).append("分");
        }

        return dateStr.toString();
    }

    /**
     * 
     * @param startdate
     * @param enddate
     * @param format
     * @return
     * @throws Exception
     */
    public static Map<String, Long> diffDate4Map(String startdate,
            String enddate, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date startdt = sdf.parse(startdate);
        Date enddt = sdf.parse(enddate);
        long diff = enddt.getTime() - startdt.getTime();
        long day = diff / ND; // 计算差多少天
        long dayLeng = diff - day * ND;
        long hour = dayLeng / NH; // 计算差多少小时
        long minLeng = dayLeng - hour * NH;
        long min = minLeng / NM; // 计算差多少分钟
        long secLeng = minLeng - min * NS;
        long sec = secLeng / NS;

        Map<String, Long> map = new HashMap<String, Long>();
        map.put("time",diff);
        map.put("day", day);
        map.put("hour", hour);
        map.put("min", min);
        map.put("sec", sec);
        return map;
    }

    /**
     * 计算两日前之前相差多少天
     * 
     * @param startdate
     * @param enddate
     * @return
     * @throws Exception
     */
    public static int dateDiff(String startdate, String enddate)
            throws Exception {
        String sdt = DateUtil.changeDateStr(startdate);
        String edt = DateUtil.changeDateStr(enddate);
        return DateUtil.dateDiff(sdt, edt, DATE_FORMAT_YYYY_MM_DD);
    }

    /**
     * 几天前的时间
     * 
     * @param dayAgo
     * @return
     */
    public static Date getDayAgo(int dayAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -dayAgo);
        return cal.getTime();
    }

    /**
     * 几年前的时间
     * 
     * @param yearAgo
     * @return
     */
    public static Date getYearAgo(int yearAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -yearAgo);
        return cal.getTime();
    }

    /**
     * 计算岁数
     * 
     * @param birthday
     *            支持格式：1988.8.8 或是1999-9-9
     * @return 返回岁数
     */
    public static int getAge(String birthday) {
        String[] birthdays = birthday.split("\\.");
        if (birthdays.length == 1) {
            birthdays = birthday.split("-");
        }
        int birthYear = Integer.valueOf(birthdays[0]);
        int birthMonth = Integer.valueOf(birthdays[1]);
        int theDay = Integer.valueOf(birthdays[2]);
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH) + 1;
        int nowDay = cal.get(Calendar.DAY_OF_MONTH);
        int age = nowYear - birthYear;
        if (nowMonth == birthMonth) {
            if (nowDay < theDay) {
                age--;
            }
        } else if (nowMonth < birthMonth) {
            age--;
        }
        return age;

    }
    
    /**
     * 将传入日期格式为yyyy-MM-dd格式的日期字符串转换成格式为yyyyMMdd HH:mm:ss格式的17位时间字符串
     * 
     * @param dateString
     * @return 字符串格式
     */
    public static String transOldDate2Char17(String dateString) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			Date date = null;
			try {
				date = sdf.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}  
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	    	return sdf1.format(date);
    }
    
    /**
     * 获取当前格式为yyyyMMdd HH:mm:ss的17位时间字符串
     *
     * @return 字符串格式
     */
    public static String getCurrentDate4Char17() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");  
			Date date = new Date();
	    	return sdf.format(date);
    }

    /**
     * 判断当前时间是否在范围内
     * @param time 比较的时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 在范围内 true， 不在范围内 false， startTime、endTime为空则返回true
     */
    public static boolean checkTimeInRange(String time, String startTime, String endTime) {
        if ( StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime) ){
            return true; //如果少了某个值，则返回在范围内
        }
    	if ( time.compareTo(startTime)<=0 ) {
			return false; //早于开始时间
		}
    	if ( time.compareTo(endTime)>=0 ) {
			return false; //晚于结束时间
		}
    	return true;
    }
    
    /**
     * 计算两个17位时间戳之间相差多少毫秒数，取整
     * 
     * @param startDateStamp  17位起始时间戳
     * @param endDateStamp    17位截止时间戳
     * @param format          格式：例如YYYYMMDDHHMMSSSSS
     * @return
     * @throws Exception
     */
    public static int timeDiffStamp(String startDateStamp, String endDateStamp, String format)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date startdt = sdf.parse(startDateStamp);
        Date enddt = sdf.parse(endDateStamp);
        BigDecimal diff = new BigDecimal(enddt.getTime()).subtract(new BigDecimal(startdt.getTime()));
        return diff.intValue();
    }
	
}
