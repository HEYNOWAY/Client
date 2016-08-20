package com.example.luos.cst_project.Util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by luos on 2016/8/9.
 */

public class TimeUtil {

    public static String getAbsoluteTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getRelativeTime(String date) {
        Log.i("TimeUtil", "date=" + date);
        String time = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            Date dt1 = sdf.parse(date);

            Calendar cl = Calendar.getInstance();
            int year2 = cl.get(Calendar.YEAR);
            int month2 = cl.get(Calendar.MONTH);
            int day2 = cl.get(Calendar.DAY_OF_MONTH);
            int hour2 = cl.get(Calendar.HOUR_OF_DAY);
            int minute2 = cl.get(Calendar.MINUTE);
            int second2 = cl.get(Calendar.SECOND);

            cl.setTime(dt1);
            int year1 = cl.get(Calendar.YEAR);
            int month1 = cl.get(Calendar.MONTH);
            int day1 = cl.get(Calendar.DAY_OF_MONTH);
            int hour1 = cl.get(Calendar.HOUR_OF_DAY);
            int minute1 = cl.get(Calendar.MINUTE);
            int second1 = cl.get(Calendar.SECOND);

            //年份不同
            if (year1 != year2) {
                return time = year1 + "年" + month1 + "月" + day1;
            }

            //月份不同
            if (month1 != month2) {
                return time = (month1 + 1) + "月" + day1 + "日";
            }

            //天数不同，但相隔大于一天
            if (day1 != day2 && day2 - day1 >1) {
                return time = (month1 + 1) + "月" + day1 + "日";
            }

            //昨天，上午和下午
            if (day2 - day1 == 1) {
                if (hour1 > 12) {
                    return time = (month1 + 1) + "月" + day1 + "日  下午";
                } else {
                    return time = (month1 + 1) + "月" + day1 + "日  上午";
                }
            }

            //当天，但相隔大于一小时
            if (hour1 != hour2 && hour2 - hour1 >1) {
                return time = (hour2 - hour1) + "小时前";
            }

            //在相隔一小时，但分钟数不同
            if(hour2 - hour1 == 1){
                if (minute2 - minute1 > 0) {
                    return time = "1小时前";
                } else {
                    return time = (60 + minute2 - minute1) + "分钟前";
                }
            }

            //同一小时内
            if (minute1 == minute2) {
                return time = "刚才";
            } else {
                return time = (minute2 - minute1) + "分钟前";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getFormatTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yy年MM月dd日   HH:mm:ss");
        return sdf2.format(date);
    }

    private static String formatTime(int hour, int minute) {
        String time = "";
        if (hour < 10) {
            time += "0" + hour + ":";
        } else {
            time += hour + ":";
        }

        if (minute < 10) {
            time += "0" + minute;
        } else {
            time += minute;
        }
        System.out.println("format(hour, minute)=" + time);
        return time;
    }
}
