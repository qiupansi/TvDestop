package com.diyo.activity.desktop.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.diyo.activity.desktop.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 天气业务类
 */
public class WeatherBiz {
	private static final String dbName = "db_weather.db";


	private static void copyDb(Context context) {
		String DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		if (!(new File(DB_PATH + dbName)).exists()) {
			File f = new File(DB_PATH);
			if (!f.exists()) {
				f.mkdir();
			}
			try {
				InputStream is = context.getAssets().open(dbName);
				OutputStream os = new FileOutputStream(DB_PATH + dbName);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				os.flush();
				os.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 从assets拷贝天气数据到本地数据库
	 * @param context
	 */
	public static void copyWetherData(Context context) {
		byte[] buf = new byte[30720]; // 30k
		try {
			File file = context.getDatabasePath(dbName);
			FileOutputStream os = new FileOutputStream(file);// 得到数据库文件的写入流
			InputStream is = context.getAssets().open(dbName);// 得到数据库文件的数据流
			int count = -1;
			while ((count = is.read(buf)) != -1) {
				os.write(buf, 0, count);
			}
			is.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			buf = null;
		}
	}

	public static final String DEFAULT_CITYCODE = "101010100"; // 默认北京编码

	/**
	 * 获取默认的城市编号 先取偏好设置，有设置直接返回，如果没有则自动粗略定位，
	 * 定位失败初始为北京
	 * @param context
	 * @return
	 */
	public static String getCityCode(final Context context) {
		SharedPreferences sp = context.getSharedPreferences("settingSPF", Context.MODE_PRIVATE);
		String cityCode = sp.getString("weather_city_code", "0");
		Log.d("Weather-cityCode:" + cityCode);
		if (cityCode.equals("0")) {
			String cityCodeAuto = getRoughlyLocation(context);
			Log.d("Weather-cityCodeAuto:"+cityCodeAuto);
			if (cityCodeAuto != null) {
				return cityCodeAuto;
			} else {
				return DEFAULT_CITYCODE;
			}
		} else {
			return cityCode;
		}
	}

	/**
	 * 根据城市编码请求天气数据
	 * @param cityCode
	 * @return
	 */
	public static CityWeatherInfoBean getWeatherFromHttp(String cityCode) {
		String url = "http://www.weather.com.cn/data/cityinfo/" + cityCode
				+ ".html";
		Log.d("getWeatherFromHttp-url:"+url);
		String json = HttpUtils.getContent(url);
		Log.d("getWeatherFromHttp-json:"+json);
		if (json != null) {
			try {
				CityWeatherInfoBean bean = new CityWeatherInfoBean();
				JSONObject jsonObject = new JSONObject(json);
				Log.d("jsonObject:"+jsonObject);
				JSONObject jsonInfro = jsonObject.getJSONObject("weatherinfo");
				bean.setCityId(jsonInfro.getString("cityid"));
				bean.setCityName(jsonInfro.getString("city"));
				bean.setfTemp(jsonInfro.getString("temp1"));
				bean.settTemp(jsonInfro.getString("temp2"));
				bean.setDnstr(jsonInfro.getString("img1"));
				bean.setWeatherInfo(jsonInfro.getString("weather"));
				return bean;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取当前的粗略位置
	 *
	 * @return
	 */
	public static String getRoughlyLocation(Context context) {
		String location[] = new String[4];
		SQLiteDatabase weatherDb;
		String url = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";
		String json = HttpUtils.getContent(url);
		Log.d("Weather-json:"+json);
		if (json == null) {
			return null;
		}
		try {
			JSONObject object = new JSONObject(json);
			location[0] = object.getString("country");   // 国
			location[1] = object.getString("province"); // 省
			location[2] = object.getString("city");    // 市
			location[3] = object.getString("district");// 区
			// 将获取到的位置转化为城市编码
			weatherDb = SQLiteDatabase.openDatabase(    //获取已经存在的数据库对象
					context.getDatabasePath("db_weather.db").toString(),
					null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;		// 如果JSON解析出错直接返回空
		} catch (Exception e) {
			e.printStackTrace();
			//copyWetherData(context);
			copyDb(context);
			weatherDb = SQLiteDatabase.openDatabase(
					context.getDatabasePath("db_weather.db").toString(), null,
					SQLiteDatabase.OPEN_READONLY);
		}
		// 查询城市编号
		Cursor cursor = null;
		if (location[3] != null && !location[3].equals("")) {
			cursor = weatherDb.query(
					"citys",
					new String[] { "city_num" },
					"name=?",
					new String[] { location[2] + "." + location[3] },
					null,
					null,
					null);
		} else {
			cursor = weatherDb.query(
					"citys",
					new String[] { "city_num" },
					"name=?",
					new String[] { location[2] },
					null,
					null,
					null);
		}
		if (cursor.getCount() > 0 && cursor.moveToFirst()) {
			String citycode = cursor.getString(cursor.getColumnIndex("city_num"));
			cursor.close();
			weatherDb.close();
			return citycode;
		} else {
			return null;
		}
	}
}
