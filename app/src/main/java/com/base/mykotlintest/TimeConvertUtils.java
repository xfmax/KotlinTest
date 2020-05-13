package com.base.mykotlintest;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间转换工具类
 *
 * 请尽量将 SimpleDateFormat 抽成常量，不要在函数中定义 SimpleDateFormat，
 *
 * @author jackrex
 */
public class TimeConvertUtils {

    /**
     * covert 73 to 01:13 or 01'13"
     *
     * @param sumSecond 73
     * @param useColon true to return 01:13, false to return 01'13"
     */
    public static String convertSecondTo0000String(long sumSecond, boolean useColon) {
        String minuteString = sumSecond / 60 > 9 ? "" + sumSecond / 60 : "0" + sumSecond / 60;

        String secondString = sumSecond % 60 > 9 ? "" + sumSecond % 60 : "0" + sumSecond % 60;

        return useColon ? (minuteString + ":" + secondString) : (minuteString + "'" + secondString + "\"");
    }
}
