package com.eyenorse.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.eyenorse.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间操作
 *
 * @author : zw
 * @time : 2013-8-25 21:14:30
 */
public class DateTimeHelper {
    public final static String mMdFormat = "M月d日";
    public static final String[] s_weekOfDays = {"星期日", "星期一", "星期二", "星期三",
            "星期四", "星期五", "星期六"};
    public final static String YYYYnianMMyueDDHHMM = "yyyy年MM月dd日 HH:mm";
    public final static String MD_HHMM = "M/d HH:mm";
    public final static String MMDD = "MM/dd";
    public final static String MMyueDD = "MM月dd日";
    public final static String mFormat = "yyyy-MM-dd";
    public final static String YYYMMDDHHMM = "yyy-MM-dd HH:mm";
    public final static String mFormat_n = "yyyyMMdd";
    public final static String mFormat_chattime = "yy-MM-dd";
    public final static String YYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public final static String mHourTimeFormat = "HH:mm:ss";
    public final static String mMinTimeFormat = "HH:mm:ss";
    public final static String FORMAT_HHmm = "HH:mm";
    public final static String myesterday = "昨天";
    public final static String YYYYnianMMyueDD = "yyyy年MM月dd日";
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private static String mTag = "DateTimeHelper";

    /**
     * 获取时间
     *
     * @return
     */
    public static String getDateTime() {
        return (String) DateFormat.format("yyyy-MM-dd kk:mm:ss",
                Calendar.getInstance());
    }

    /**
     * 获取日期
     *
     * @return
     */
    public static String getDate() {
        return getDate(Calendar.getInstance());
    }

    /**
     * 获取时间
     *
     * @param unix_timestamp
     * @return
     */
    public static String getDate(int unix_timestamp) {
        return getDate(unix_timestamp * 1000l);
    }

    public static String getChatDate(int unix_timestamp) {
        return getChatDate(unix_timestamp * 10001);
    }

    public static String getHourDate(int unix_timestamp) {
        return getHour(unix_timestamp * 10001);
    }


    /**
     * 获取天开始时间
     *
     * @param unix_timestamp
     * @return
     */
    public static long getStartTime(int unix_timestamp) {

        Calendar c = getCalendar(unix_timestamp);

        String format = "%d-%d-%d 00:00:01";
        return getDayTime(c, format);
    }
    /**
     * 转换成时分秒
     *
     * @param time
     * @return
     */
    public static String getHourMinSS(int time){
        int milliSecondTime = time*1000;
        int hour = milliSecondTime /(60*60*1000);
        int minute = (milliSecondTime - hour*60*60*1000)/(60*1000);
        int seconds = (milliSecondTime - hour*60*60*1000 - minute*60*1000)/1000;

        if(seconds >= 60 )
        {
            seconds = seconds % 60;
            minute+=seconds/60;
        }
        if(minute >= 60)
        {
            minute = minute % 60;
            hour  += minute/60;
        }

        String sh = "";
        String sm ="";
        String ss = "";
        if(hour <10) {
            sh = "0" + String.valueOf(hour);
        }else {
            sh = String.valueOf(hour);
        }
        if(minute <10) {
            sm = "0" + String.valueOf(minute);
        }else {
            sm = String.valueOf(minute);
        }
        if(seconds <10) {
            ss = "0" + String.valueOf(seconds);
        }else {
            ss = String.valueOf(seconds);
        }

        return sh +":"+sm+":"+ ss;
    }



    /**
     * 获取天结束时间
     *
     * @param unix_timestamp
     * @return
     */
    public static long getEndTime(int unix_timestamp) {
        Calendar c = getCalendar(unix_timestamp);

        String format = "%d-%d-%d 23:59:59";
        return getDayTime(c, format);
    }

    public static long getDayTime(Calendar cal, String format) {
        try {
            format = String
                    .format(format, cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH) + 1,
                            cal.get(Calendar.DAY_OF_MONTH));
            SimpleDateFormat sm = new SimpleDateFormat(YYMMDDHHMMSS,
                    Locale.CHINA);
            Date tmp;
            tmp = sm.parse(format);
            return tmp.getTime();
        } catch (ParseException e) {
            Log.e(mTag, "ParseException", e);
        } catch (Exception e) {
            Log.e(mTag, "Exception", e);
        } catch (Throwable e) {
            Log.e(mTag, "Throwable", e);
        }
        return 0l;
    }

    /**
     * 获取日期
     *
     * @param unix_timestamp
     * @return
     */
    public static String getDate(long unix_timestamp) {
        Date dt = new Date(unix_timestamp);
        return getDate(dt);
    }

    public static String getFormatDate(long unix_timestamp,
                                       CharSequence inFormat) {
        Long time = new Long(unix_timestamp);
        return simpleDateFormat.format(time);
    }

    public static String getChatDate(long unix_timestamp) {
        Date dt = new Date(unix_timestamp);
        return getChatDate(dt);
    }

    public static String getHour(long unix_timestamp) {
        Date dt = new Date(unix_timestamp);
        return getHour(dt);
    }

    /**
     * 获取日期
     *
     * @param dt
     * @return
     */
    public static String getDate(Date dt) {
        return (String) DateFormat.format(mFormat, dt);
    }

    public static String getFormatDate(Date dt, CharSequence inFormat) {
        return (String) DateFormat.format(inFormat, dt);
    }

    public static String getChatDate(Date dt) {
        return (String) DateFormat.format(mFormat_chattime, dt);
    }

    /**
     * 获取日期
     *
     * @param c
     * @return
     */
    public static String getDate(Calendar c) {
        return (String) DateFormat.format(mFormat, c);
    }

    public static String getHour(Date dt) {
        return (String) DateFormat.format(mHourTimeFormat, dt);
    }

    /**
     * 获取秒
     *
     * @return
     */
    public static int getSeconds() {
        Date dt = new Date();
        return dt.getSeconds();
    }

    /**
     * 获取秒
     *
     * @param date
     * @return
     */
    public static int getSeconds(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(mFormat);
            Date dt;
            dt = format.parse(date);
            return dt.getSeconds();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取分
     *
     * @param date
     * @return
     */
    public static int getMinutes(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date dt;
            dt = format.parse(date);
            return dt.getMinutes();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取时
     *
     * @param date
     * @return
     */
    public static int getHours(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date dt;
            dt = format.parse(date);
            return dt.getHours();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取unix_timestamp
     *
     * @return
     */
    public static long getUnixTimeStamp() {
        Date dt = new Date();
        return getUnixTimeStamp(dt);
    }

    /**
     * 获取unix_timestamp
     *
     * @param dt
     * @return
     */
    public static long getUnixTimeStamp(Date dt) {
        return (long) (dt.getTime() / 1000l);
    }

    /**
     * 获取unix_timestamp
     *
     * @param date
     * @return
     */
    public static long getUnixTimeStamp(String date) {
        Date dt;
        try {
            SimpleDateFormat format = new SimpleDateFormat(mFormat);
            dt = format.parse(date);
            return getUnixTimeStamp(dt);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
        } catch (Exception e) {
        } catch (Throwable e) {
        }
        return 0;
    }

    /**
     * 获取unix_timestamp
     *
     * @param date
     * @return
     */
    public static long getUnixTimeStamp(String date, String mformats) {
        Date dt;
        try {
            SimpleDateFormat format = new SimpleDateFormat(mformats);
            dt = format.parse(date);
            return getUnixTimeStamp(dt);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
        } catch (Exception e) {
        } catch (Throwable e) {
        }
        return 0;
    }

    public static String getDay(String date) {
        try {
            if (null == date || date.isEmpty()) {
                return "";
            }
            return date.replace("-", "");
        } catch (Exception e) {
            Log.e(mTag, "getDay date:" + date, e);
        } catch (Throwable e) {
            Log.e(mTag, "getDay date:" + date, e);
        }
        return date;
    }

    public static String getDay(String date, String inFormat) {
        return getDay(date, inFormat, mFormat);
    }

    public static String getDay(String date, String inFormat, String outFormat) {
        try {
            if (null == date || date.isEmpty()) {
                return "";
            }

            return (String) DateFormat.format(outFormat,
                    getDate(date, inFormat));
        } catch (Exception e) {
            Log.e(mTag, "getDay date:" + date, e);
        } catch (Throwable e) {
            Log.e(mTag, "getDay date:" + date, e);
        }
        return date;
    }

    /**
     * 当前年
     *
     * @return
     */
    public static int getYear() {
        Calendar c = Calendar.getInstance();
        return getYear(c);
    }

    public static int getYear(Calendar c) {
        return c.get(Calendar.YEAR);
    }

    public static int getYear(String date) {
        try {
            Date dt = getDate(date);
            if (null == dt) {
                return getYear();
            }
            Calendar cal = getCalendar(dt);
            return getYear(cal);
        } catch (Exception e) {
            Log.e(mTag, "Exception date:" + date, e);
        }
        return getYear();
    }

    public static Date getDate(String date) {
        return getDate(date, mFormat);
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = getCalendar();
        cal.setTime(date);
        return cal;
    }

    public static Calendar getCalendar(long unix_timestamp) {
        Date dt = new Date(unix_timestamp);
        return getCalendar(dt);
    }

    public static Calendar getCalendar(int unix_timestamp) {
        return getCalendar(unix_timestamp * 1000l);
    }

    public static Date getDate(String date, String format) {
        try {
            if (null == date || date.isEmpty()) {
                return null;
            }
            if (null == format || format.isEmpty()) {
                format = mFormat;
            }
            SimpleDateFormat ft = new SimpleDateFormat(format);
            return ft.parse(date);
        } catch (ParseException e) {
            Log.e(mTag, "Exception getDate date:" + date, e);
        } catch (Exception e) {
            Log.e(mTag, "Exception getDate date:" + date, e);
        }
        return null;
    }

    /**
     * 当前月
     *
     * @return
     */
    public static int getMonth() {
        return getMonth(getCalendar());
    }

    public static int getMonth(Calendar c) {
        return c.get(Calendar.MONTH);
    }

    public static int getMonth(String date) {
        try {
            Date dt = getDate(date);
            if (null == dt) {
                return getMonth();
            }
            Calendar cal = getCalendar(dt);
            return getMonth(cal);
        } catch (Exception e) {
            Log.e(mTag, "Exception", e);
        }
        return getMonth();
    }

    /**
     * 号
     *
     * @return
     */
    public static int getDayOfMonth() {
        Calendar c = Calendar.getInstance();
        return getDayOfMonth(c);
    }

    /**
     * @param c
     * @return
     */
    public static int getDayOfMonth(Calendar c) {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfMonth(String date) {
        try {
            Date dt = getDate(date);

            if (null == dt) {
                return getDayOfMonth();
            }
            Calendar cal = getCalendar(dt);
            return getDayOfMonth(cal);
        } catch (Exception e) {
            Log.e(mTag, "Exception", e);
        }
        return getDayOfMonth();
    }

    /**
     * @param diff
     * @return
     */
    public static Calendar afterMonth(int diff) {
        Calendar c = Calendar.getInstance();
        return afterMonth(c, diff);
    }

    /**
     * @param c
     * @param diff
     * @return
     */
    public static Calendar afterMonth(Calendar c, int diff) {
        c.add(Calendar.MONTH, diff);
        return c;
    }

    public static Calendar afterDay(int diff) {
        Calendar c = Calendar.getInstance();
        return afterDay(c, diff);
    }

    public static Calendar afterDay(Calendar c, int diff) {
        c.add(Calendar.DAY_OF_MONTH, diff);
        return c;
    }

    /**
     * 获取utc时间
     *
     * @return
     */
    public static CharSequence getUTCTime() {
        return getUTCTime(Locale.CHINA);
    }

    public static CharSequence getUTCTime(Locale local) {
        Calendar cal = Calendar.getInstance(local);
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return DateFormat.format("yyyy'-'MM'-'dd'T'kk':'mm':'ss'Z'", cal);
    }

    public static String getDate(int year, int monthOfYear, int dayOfMonth) {
        String split = "-";
        String time = year + split;
        if (10 > monthOfYear) {
            time = time + "0" + monthOfYear;
        } else {
            time += monthOfYear;
        }
        time += split;
        if (10 > dayOfMonth) {
            time = time + "0" + dayOfMonth;
        } else {
            time += dayOfMonth;
        }
        return time;
    }

    @SuppressLint("SimpleDateFormat")
    public static String format(String date, String old_pattern,
                                String new_pattern) {
        SimpleDateFormat df = new SimpleDateFormat(old_pattern);
        Date dt;
        try {
            dt = df.parse(date);
            return format(dt, new_pattern);
        } catch (ParseException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String format(String pattern) {

        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = new Date();
        return df.format(date);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static String formatLvbbDate(String str) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss a",
                Locale.UK);
        try {
            Date date = sdf1.parse(str);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = sdf.format(date);
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String formatLvbbExceptionDate(String str) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd,yyyy hh:mm:ss a",
                Locale.UK);
        try {
            Date date = sdf1.parse(str);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = sdf.format(date);
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取年龄（虚岁or实岁）
     * @param date 生日
     * @return
     * @throws Exception
     */
    public static String getAgeNew(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = sdf.format(date);
        Calendar cal = Calendar.getInstance();

        if (cal.before(date)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int year = Integer.parseInt(birthday.substring(0, 4));
        return yearNow - year + "";
    }

    /**
     * 获取年龄（虚岁or实岁）
     * @param birthDay  生日
     * @return
     * @throws Exception
     */
    public static String getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }

        return age + "";
    }

    /**
     * 获取当前月最后一天
     */
    public static Date getLastDayOfThisMonth() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH,
                ca.getActualMaximum(Calendar.DAY_OF_MONTH));

        return ca.getTime();
    }

    /**
     * 获取前月最后一天
     */
    public static Date getLastDayOfLastMonth() {
        // 获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 0);

        return cale.getTime();

    }

    /**
     * 获取当前月第一天
     */
    public static Date getFirstDayOfThisMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取前月的第一天
     */
    public static Date getFirstDayOfLastMonth() {
        Calendar cal_1 = Calendar.getInstance();
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);
        return cal_1.getTime();
    }

    /**
     * 获取倒数第几个月的第一天
     */
    public static Date getFirstDayOfLastMonths(int months) {
        Calendar cal_1 = Calendar.getInstance();
        cal_1.add(Calendar.MONTH, -months);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);
        return cal_1.getTime();
    }

    /**
     * 获取倒数第几个月的最后一天
     */
    public static Date getLastDayOfLastMonths(int months) {
        Calendar cal_1 = Calendar.getInstance();
        cal_1.add(Calendar.MONTH, -months);
        cal_1.set(Calendar.DAY_OF_MONTH, 0);
        return cal_1.getTime();
    }

    /**
     * 转换为几分钟前这样的格式
     *
     * @param sdate
     * @return
     */
    public static String friendlyTime(String sdate, String format) {
        SimpleDateFormat dateFormater2 = new SimpleDateFormat(format);

        Date time;

        try {
            time = dateFormater2.parse(sdate);
        } catch (ParseException e) {
            //LogUtils.e(e.getMessage());
            return null;
        }
        if (time == null) {
            return "";
        }

        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.format(cal.getTime());
        String paramDate = dateFormater2.format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.format(time);
        }
        return ftime;
    }

    public static String fomat(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static Date getDateFromString(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }
        final String trimStr = dateStr.trim();
        SimpleDateFormat df = null;
        switch (trimStr.length()) {
            case 19:
                df = new SimpleDateFormat(YYMMDDHHMMSS);
                try {
                    return df.parse(trimStr);

                } catch (ParseException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;

            case 16:
                df = new SimpleDateFormat(YYYMMDDHHMM);
                try {
                    return df.parse(trimStr);

                } catch (ParseException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            case 10:
                df = new SimpleDateFormat(mFormat);
                try {
                    return df.parse(trimStr);

                } catch (ParseException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            default:
                return null;
        }
    }

    public static boolean isSameOrLaterrDay(Calendar c1, Calendar c2) {
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);

        int day1 = c1.get(Calendar.DAY_OF_YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);
        if (year1 > year2 || (year1 == year2 && day1 >= day2)) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isBeforeOneDay(Calendar c1, Calendar c2) {
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        if (year1 < year2) {
            return true;

        } else if (year1 > year2) {
            return false;
        }
        int day1 = c1.get(Calendar.DAY_OF_YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);
        if (day1 < day2) {
            return true;
        } else {
            return false;
        }

    }

    public static String getWeekOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return s_weekOfDays[w];
    }

    /**
     * 得到中文形式的月份
     *
     * @param month
     * @return
     */
    public static String translateMonth(Context context, int month) {
        Resources resource = context.getResources();
        switch (month) {
            case 1:
                return resource.getString(R.string.month_1);
            case 2:
                return resource.getString(R.string.month_2);
            case 3:
                return resource.getString(R.string.month_3);
            case 4:
                return resource.getString(R.string.month_4);
            case 5:
                return resource.getString(R.string.month_5);
            case 6:
                return resource.getString(R.string.month_6);
            case 7:
                return resource.getString(R.string.month_7);
            case 8:
                return resource.getString(R.string.month_8);
            case 9:
                return resource.getString(R.string.month_9);
            case 10:
                return resource.getString(R.string.month_10);
            case 11:
                return resource.getString(R.string.month_11);
            case 12:
                return resource.getString(R.string.month_12);
        }
        return null;
    }

    /**
     * 转换为应用时间格式
     * e.g.（几分钟前...2月2日 10:33）
     *
     * @param sdate
     * @param format
     * @return
     */
    public static String toSystemFriendlyTime1(String sdate, String format) {
        format = "yyyy-MM-dd HH:mm";
        if (sdate.length() >= 19)
            sdate = sdate.substring(0, sdate.length() - 3);
        SimpleDateFormat dateFormater = new SimpleDateFormat(format);

        Date time;

        try {
            time = dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
        if (time == null) {
            return "";
        }

        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater.format(cal.getTime());
        String paramDate = dateFormater.format(time);
        String sdatetime = paramDate.substring(paramDate.length() - 5,
                paramDate.length());
        String nowday = curDate.substring(0, 10);
        String sdateday = paramDate.substring(0, 10);
        if (nowday.equals(sdateday)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        double lt = time.getTime() * 1.0 / 86400000;
        double ct = cal.getTimeInMillis() * 1.0 / 86400000;
        int days = (int) (ct - lt);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new Date());
        int day1 = Integer.parseInt(date.substring(date.length() - 2,
                date.length()));
        int day2 = Integer.parseInt(sdate.substring(8, 10));
        if (days >= 2 || (day1 - day2) != 1) {
            // if (!nowyear.equals(sdateyear))
            // ftime = paramDate;
            // else
            // ftime = paramDate.substring(5, paramDate.length());
            String stime = paramDate.substring(5, paramDate.length());
            String timeleft = stime.substring(0, 5);
            String str1 = timeleft.substring(0, 2);
            String str2 = timeleft.substring(3, 5);

            if (str1.substring(0, 1).equals("0"))
                str1 = str1.substring(1, 2);
            else
                str1 = str1.substring(0, 2);

            if (str2.substring(0, 1).equals("0"))
                str2 = str2.substring(1, 2);
            else
                str2 = str2.substring(0, 2);
            timeleft = str1 + "月" + str2 + "日";
            String timeright = stime.substring(stime.length() - 6,
                    stime.length());
            ftime = timeleft + timeright;
            return ftime;
        } else {
            // if (days == 1) {
            // ftime = "昨天" + sdatetime;
            // }
            if (day1 - day2 == 1)
                ftime = "昨天" + sdatetime;

        }
        return ftime;
    }

    public static String getEaseDateString(Date date){
        Calendar today =Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);
        int y1,y2=0;
        y1 =today.get(Calendar.YEAR);
        y2 =c2.get(Calendar.YEAR);

        if(y2!=y1){
            return fomat(date,YYYYnianMMyueDDHHMM);
        }else{
            int dayofYear =today.get(Calendar.DAY_OF_YEAR);
            int dayofYear2 =c2.get(Calendar.DAY_OF_YEAR);
            if(dayofYear!=dayofYear2){
                return fomat(date,MMyueDD);
            }else{
                return fomat(date,FORMAT_HHmm);
            }
        }

    }
    /**
     * 转换为应用时间格式
     * e.g.（2月2日 10:33）
     *
     * @param sdate
     * @return
     */
    public static String toSystemFriendlyTime2(String sdate) {
        if (TextUtils.isEmpty(sdate))
            return "";
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (sdate.length() >= 19) {
                sdate = sdate.substring(0, sdate.length() - 3);
            }
            sDateFormat.parse(sdate);
            sdate = sdate.substring(5, sdate.length());
            String timeleft = sdate.substring(0, 5);

            String str1 = timeleft.substring(0, 2);
            String str2 = timeleft.substring(3, 5);

            if (str1.substring(0, 1).equals("0"))
                str1 = str1.substring(1, 2);
            else
                str1 = str1.substring(0, 2);

            if (str2.substring(0, 1).equals("0"))
                str2 = str2.substring(1, 2);
            else
                str2 = str2.substring(0, 2);
            timeleft = str1 + "月" + str2 + "日";
//        timeleft = timeleft + "日";
//        timeleft = timeleft.replaceAll("0", "");
//        timeleft = timeleft.replaceAll("-", "月");
            String timeright = sdate.substring(sdate.length() - 6, sdate.length());
            sdate = timeleft + timeright;
        } catch (Exception e) {
            e.printStackTrace();
            return sdate;
        }
        return sdate;

    }
    /**
     * 转换为随访时间格式
     * e.g.（2013年2月2日）
     *
     * @param date
     * @return
     */
    public static String toSystemFriendlyTime3(Date date) {
        String sdate = null;
        try {
            String dateString = fomat(date, DateTimeHelper.YYYYnianMMyueDD);
            String timeleft = dateString.substring(5, 10);

            String str1 = timeleft.substring(0, 2);
            String str2 = timeleft.substring(3, 5);

            if (str1.substring(0, 1).equals("0"))
                str1 = str1.substring(1, 2);
            else
                str1 = str1.substring(0, 2);

            if (str2.substring(0, 1).equals("0"))
                str2 = str2.substring(1, 2);
            else
                str2 = str2.substring(0, 2);
            sdate = dateString.substring(0, 5) + str1 + "月" + str2 + "日";
        } catch (Exception e) {
            e.printStackTrace();
            return sdate;
        }
        return sdate;
    }

    /**
     * 输入"yyyy-MM-dd HH:mm:ss"格式字符串，获取utc时间
     * @param time
     * @return
     */
    public static long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 输入年月日，获取utc时间
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static long getStringToDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = new Date();
        try {
            date = calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    /**
     * 计算两个日期相差的月份数
     *
     * @param date1   日期1
     * @param date2   日期2
     * @param pattern 日期1和日期2的日期格式
     * @return 相差的月份数
     * @throws ParseException
     */
    public static int countMonths(String date1, String date2, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));
        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

        //开始日期若小月结束日期
        if (year < 0) {
            year = -year;
            return year * 12 + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        }
        return year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
    }

    /**
     * @param afterDay 0代表今天 1代表明天 2代表后台以此内推
     * @return 比如6月12日 6月1日
     */
    public static String formatYd(int afterDay) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(mMdFormat);//设置日期格式
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, afterDay);
            return df.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取从今天起往后N天的日期
     * @param afterDay 0代表今天 1代表明天 2代表后台以此内推
     * @return 比如6月12日 6月1日
     */
    public static String formatYd(int afterDay, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, afterDay);
            return df.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取从今天起往后N天的日期
     * @param afterDay 0代表今天 1代表明天 2代表后台以此内推
     * @return 比如6月12日 6月1日
     */
    public static Date formatYd1(int afterDay) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, afterDay);
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**获取从特定日期起往后N天的日期
     * @param mDate 起始日期
     * @param afterDay 0代表今天 1代表明天 2代表后台以此内推
     * @return 比如6月12日 6月1日
     */
    public static Date formatYd1(Date mDate, int afterDay) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mDate);
            calendar.add(Calendar.DAY_OF_MONTH, afterDay);
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取从今天起往后N天的日期
     * @param afterDay 0代表今天 1代表明天 2代表后台以此内推
     * @return 比如6月12日 6月1日
     */
    public static Date formatYd2(int afterDay, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, afterDay);
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate,Date bdate) throws ParseException
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @return 相差天数
     * @throws ParseException
     */
     public static int daysBetween(String smdate,String bdate, String format) throws ParseException{
         SimpleDateFormat sdf=new SimpleDateFormat(format);
         Calendar cal = Calendar.getInstance();
         cal.setTime(sdf.parse(smdate));
         long time1 = cal.getTimeInMillis();
         cal.setTime(sdf.parse(bdate));
         long time2 = cal.getTimeInMillis();
         long between_days=(time2-time1)/(1000*3600*24);

         return Integer.parseInt(String.valueOf(between_days));
     }
}