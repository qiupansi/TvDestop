package com.diyo.activity.desktop.util;

import android.util.Log;

import com.diyo.activity.desktop.R;

import java.text.DecimalFormat;

public class StringUtil {

	public static String stringForTime(long timeMs) {
		long totalSeconds = timeMs / 1000;

		long seconds = totalSeconds % 60;
		long minutes = (totalSeconds / 60) % 60;
		long hours = totalSeconds / 3600;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public static String longToSec(long timeMS) {
		float seconds = (float) timeMS / 1000.0f;
		return new DecimalFormat("#.#").format(seconds) + "秒";
	}

	/**
	 * long类型转换为String类型
	 * @param timeMs
	 * @return
	 */
	public static String longTimeToString(long timeMs) {
		long totalSeconds = timeMs / 1000;
		long seconds = totalSeconds % 60;
		long minutes = (totalSeconds / 60) % 60;
		long hours = totalSeconds / 3600;
		String stringTime = null;
		if (hours == 0 && minutes == 0) {
			stringTime = seconds + "秒";
		} else if (hours == 0 && minutes != 0) {
			stringTime = minutes + "分" + seconds + "秒";
		} else if (hours != 0) {
			stringTime = hours + "小时" + minutes + "分";
		}
		return stringTime;
	}

	public static int[] getWeaResByWeather(String weather) {
		String[] strs = weather.split("转|到");
		int[] resIds = new int[strs.length];
		for (int i = 0; i < strs.length; i++) {
			resIds[i] = getWeaResByWeather1(strs[i]);
			Log.i("info", "拆分后的天气：" + strs[i]);
		}
		if (resIds.length == 3) {
			if (resIds[0] == 0) {
				int[] newResids = new int[2];
				newResids[0] = resIds[1];
				newResids[1] = resIds[2];
				resIds = newResids;
			}
		} else if (resIds.length == 1) {
			int[] newResids = new int[2];
			newResids[0] = resIds[0];
			newResids[1] = 0;
			resIds = newResids;
		}
		return resIds;
	}

	/**
	 * 根据天气状况切换相应图片
	 * @param weather
	 * @return
	 */
	public static int getWeaResByWeather1(String weather) {
		if (weather.equals("阴")) {
			return R.drawable.ic_weather_cloudy_l;
		} else if (weather.equals("多云")) {
			return R.drawable.ic_weather_partly_cloudy_l;
		} else if (weather.equals("晴")) {
			return R.drawable.ic_weather_clear_day_l;
		} else if (weather.equals("小雨")) {
			return R.drawable.ic_weather_chance_of_rain_l;
		} else if (weather.equals("中雨")) {
			return R.drawable.ic_weather_rain_xl;
		} else if (weather.equals("大雨")) {
			return R.drawable.ic_weather_heavy_rain_l;
		} else if (weather.equals("暴雨")) {
			return R.drawable.ic_weather_heavy_rain_l;
		} else if (weather.equals("大暴雨")) {
			return R.drawable.ic_weather_heavy_rain_l;
		} else if (weather.equals("特大暴雨")) {
			return R.drawable.ic_weather_heavy_rain_l;
		} else if (weather.equals("阵雨")) {
			return R.drawable.ic_weather_chance_storm_l;
		} else if (weather.equals("雷阵雨")) {
			return R.drawable.ic_weather_thunderstorm_l;
		} else if (weather.equals("小雪")) {
			return R.drawable.ic_weather_chance_snow_l;
		} else if (weather.equals("中雪")) {
			return R.drawable.ic_weather_flurries_l;
		} else if (weather.equals("大雪")) {
			return R.drawable.ic_weather_snow_l;
		} else if (weather.equals("暴雪")) {
			return R.drawable.ic_weather_snow_l;
		} else if (weather.equals("冰雹")) {
			return R.drawable.ic_weather_icy_sleet_l;
		} else if (weather.equals("雨夹雪")) {
			return R.drawable.ic_weather_icy_sleet_l;
		} else if (weather.equals("风")) {
			return R.drawable.ic_weather_windy_l;
		} else if (weather.equals("龙卷风")) {
			return R.drawable.ic_weather_windy_l;
		} else if (weather.equals("雾")) {
			return R.drawable.ic_weather_fog_l;
		}
		return 0;
	}

}
