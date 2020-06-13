package com.diyo.activity.desktop.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.diyo.activity.desktop.util.Log;

/**
 * 天气广播接收
 */
public class WeatherReceiver extends BroadcastReceiver {

    public static final String RESPONSE_WEATHER =
            "com.mygica.itv52.v1.weatherservice.responseweather";

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("action:"+action);
        if (action.equals(RESPONSE_WEATHER)) {
            if (context instanceof WeatherUpdateListener) {
                final WeatherUpdateListener listener = (WeatherUpdateListener) context;
                new HttpWorkTask<CityWeatherInfoBean>(
                        new HttpWorkTask.ParseCallBack<CityWeatherInfoBean>() {

                            @Override
                            public CityWeatherInfoBean onParse() {
                                Log.d("onParse:");
                                String code = WeatherBiz.getCityCode(context);
                                return WeatherBiz.getWeatherFromHttp(code);
                            }
                        },
                        new HttpWorkTask.PostCallBack<CityWeatherInfoBean>() {

                            @Override
                            public void onPost(CityWeatherInfoBean result) {
                                listener.updateWeather(result);
                            }
                        }).execute();
            }
        }
    }

    /**
     * 天气更新监听接口
     */
    public interface WeatherUpdateListener {
        void updateWeather(CityWeatherInfoBean bean);
    }
}
