/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import com.avalon.core.exception.AvalonException;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

// TODO Calendar 性能比较差
public class DateTimeUtils {
    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    public static String getCurrentDateTimeString() {
        return getDateTimeFormat(new Date(System.currentTimeMillis()));
    }

    public static String getCurrentDateString() {
        return getDateFormat(new Date(System.currentTimeMillis()));
    }

    public static String getDateTimeFormat(String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getDateFormat(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String getDateTimeFormat(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Time getCurrentTime() {
        return new Time(getCurrentDate().getTime());
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Time parseTime(String format, String time) throws AvalonException {
        try {
            return new Time(new SimpleDateFormat(format).parse(time).getTime());
        } catch (ParseException e) {
            throw new AvalonException(e.getMessage());
        }
    }

    public static Date parseDate(String format, String date) throws AvalonException {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            throw new AvalonException(e.getMessage());
        }
    }

    /**
     * 获取UTC时间戳
     *
     * @param date
     * @return
     */
    public static Long getUTCDate(Date date) {
        return date.getTime() - TimeZone.getDefault().getRawOffset();
    }

    public static Integer compare(Date startDate, Date endDate) {
        return startDate.compareTo(endDate);
    }

    public static Boolean isSmallerDate(Date startDate, Date endDate) {
        return compare(startDate, endDate) < 0;
    }

    /**
     * description: 获取当月开始时间
     * version: 1.0
     * date: 2022/5/23 15:59
     * author: AN
     *
     * @param
     * @return java.util.Date
     */
    public static Date getMonthStartDateTime() {
        return getAssignMonthStartDateTime(0);
    }

    /**
     * description: 获取指定后几个月的开始日期
     * version: 1.0
     * date: 2022/5/28 15:18
     * author: AN
     * eg: 当前 5月，month 为 1,返回 6 月开始日期
     *
     * @param month 后几个月
     * @return java.util.Date
     */
    public static Date getAssignMonthStartDateTime(Integer month) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * description: 延迟月份的指定日期
     * version: 1.0
     * date: 2022/5/30 10:51
     * author: AN
     *
     * @param day   日期
     * @param month 延迟月数
     * @return java.util.Date
     */
    public static Date getDelayMonthAssignDay(Integer day, Integer month) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.set(Calendar.DAY_OF_MONTH, day);// 设置为1号,当前日期既为本月第一天
        calendar.add(Calendar.MONTH, month);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * description: 获取指定月指定时间
     * version: 1.0
     * date: 2022/5/30 10:53
     * author: AN
     *
     * @param day   天
     * @param month 月
     * @return java.util.Date
     */
    public static Date getMonthAssignDay(Integer day, Integer month) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.set(Calendar.DAY_OF_MONTH, day);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.MONTH, month);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * description: 获取当月最后时间
     * version: 1.0
     * date: 2022/5/23 15:59
     * author: AN
     *
     * @param
     * @return java.util.Date
     */
    public static Date getMonthEndDateTime() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Date(calendar.getTimeInMillis());
    }


    /**
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @return
     */
    public static int getSeason() {

        int season = 0;

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    /**
     * description: 获取季度该季度的最后一个月
     * version: 1.0
     * date: 2022/5/30 10:47
     * author: AN
     *
     * @param season
     * @return int
     */
    public static int getSeasonLastMonth(Integer season) {
        switch (season) {
            case 1:
                return Calendar.MARCH;
            case 2:
                return Calendar.JUNE;
            case 3:
                return Calendar.SEPTEMBER;
            case 4:
                return Calendar.DECEMBER;
            default:
                return 0;
        }
    }

    /**
     * description: 获取季度最后一月的日期
     * version: 1.0
     * date: 2022/5/30 10:47
     * author: AN
     *
     * @param season
     * @return java.util.Date
     */
    public static Date getSeasonLastMonthDate(Integer season) {
        int month = getSeasonLastMonth(season);
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.set(Calendar.MONTH, month);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * description: 获取当前季度的最后一个月日期
     * version: 1.0
     * date: 2022/5/30 10:48
     * author: AN
     *
     * @param
     * @return java.util.Date
     */
    public static Date getCurrentSeasonLastMonthDate() {
        Integer season = getSeason();
        return getSeasonLastMonthDate(season);
    }

    /**
     * description: 获取该季度最后一个月的指定日期
     * version: 1.0
     * date: 2022/5/30 10:59
     * author: AN
     *
     * @param day 指定第几天
     * @return java.util.Date
     */
    public static Date getCurrentSeasonLastMonthAssignDay(Integer day, Integer delaySeason) {
        Integer season = getSeason();
        Integer month = getSeasonLastMonth(0 + season);
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.MONTH, month);
        calendar.add(calendar.MONTH, 0 + delaySeason * 3);
        calendar.set(calendar.DAY_OF_MONTH, day);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date getCurrentSeasonLastMonthAssignDay(Integer day) {
        return getCurrentSeasonLastMonthAssignDay(day, 0);
    }

    /**
     * @param hour 小时
     * @return String
     */
    public static Date dateRoll(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.HOUR_OF_DAY, hour);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * description: 获取上一个月
     * version: 1.0
     * date: 2022/6/15 9:57
     * author: AN
     * calendar.get(Calendar.MONTH) 0~11 分别代表1月~12月
     *
     * @return int 月份
     */
    public static int getLastMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) == 0 ? 12 : calendar.get(Calendar.MONTH);

    }

    /**
     * 获取两个时间的间隔 秒
     *
     * @param start
     * @param end
     * @return
     */
    public static Long getTimeSpan(Date start, Date end) {
        return (end.getTime() - start.getTime()) / 1000;
    }

    /**
     * 获取两个时间的间隔 分
     *
     * @param start
     * @param end
     * @return
     */
    public static Long getTimeSpanMinute(Date start, Date end) {
        return getTimeSpan(start, end) / 60;
    }

    /**
     * 获取两个时间的间隔  小时
     *
     * @param start
     * @param end
     * @return
     */
    public static Long getTimeSpanHour(Date start, Date end) {
        Long timeSpan = getTimeSpan(start, end);
        return timeSpan / 3600;
    }

    /**
     * 获取两个时间的间隔  天
     *
     * @param start
     * @param end
     * @return
     */
    public static Long getTimeSpanDay(Date start, Date end) {
        Long timeSpan = getTimeSpan(start, end);
        return timeSpan / (3600 * 24);
    }

    /**
     * 获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static Integer getWeekOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 增加天数
     *
     * @param days
     * @return
     */
    public static Date plusDays(Date date, Integer days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    // 获得当前月--开始日期
    public static Date getMinMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    // 获得当前月--结束日期
    public static Date getMaxMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date time = calendar.getTime();
        return time;
    }

    /**
     * 获取本次结算日期
     *
     * @return
     */
    public static Date getSettleDate() {
        Date currentDate = getCurrentDate();

        Integer weekOfDate = DateTimeUtils.getWeekOfDate(currentDate);
        Date localSettleDate;
        if (weekOfDate == 2 || weekOfDate == 3 || weekOfDate == 4) {//本周123
            Integer diff = 4 - weekOfDate + 4;
            localSettleDate = DateTimeUtils.plusDays(currentDate, diff);
        } else if (weekOfDate == 5 || weekOfDate == 6 || weekOfDate == 7) {
            Integer diff = 7 - weekOfDate + 4;
            localSettleDate = DateTimeUtils.plusDays(currentDate, diff);
        } else {
            localSettleDate = DateTimeUtils.plusDays(currentDate, 3);
        }
        return localSettleDate;//本次结算日
    }
}
