package com.diyo.activity.desktop.util;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tingbaby on 2015/10/29.
 */
public class SystemUtil {
    private static Context context;

    public SystemUtil(Context context) {
        this.context = context;
    }

    //获取当前年月日，格式为英文形式
    public static String getDate(){
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter;
        if(isZh()) {
            formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        } else {
            formatter = new SimpleDateFormat(" MMMM dd, yyyy", Locale.ENGLISH);
        }
        return formatter.format(curDate);
    }

    //获取当前时间
    public static String getTime(){
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatime = new SimpleDateFormat ("HH:mm");
        return formatime.format(curDate);
    }

    /**
     *获取今天是星期几,格式为英文
     * @return
     */
    public static String getDay(){
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat format;
        if (isZh()) {
            format = new SimpleDateFormat("EEEE", Locale.CHINA);
        } else {
            format = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        }
        return format.format(curDate);
    }

    public static boolean isZh() {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }
}
