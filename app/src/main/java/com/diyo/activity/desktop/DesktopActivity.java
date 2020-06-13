package com.diyo.activity.desktop;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diyo.activity.desktop.adapter.MoreAdapter;
import com.diyo.activity.desktop.util.Log;
import com.diyo.activity.desktop.util.StringUtil;
import com.diyo.activity.desktop.util.SystemUtil;
import com.diyo.activity.desktop.view.SmoothHorizontalScrollView;
import com.diyo.activity.desktop.weather.CityWeatherInfoBean;
import com.diyo.activity.desktop.weather.WeatherReceiver;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by tingbaby on 2015/10/28.
 */
public class DesktopActivity extends AppCompatActivity implements WeatherReceiver.WeatherUpdateListener,
        View.OnFocusChangeListener{
    private LinearLayout mGallery;
    private List<ResolveInfo> datas;
    private MoreAdapter adapter;
    private SmoothHorizontalScrollView hs;
    private ImageView iv_cover;
    private DisplayMetrics displayMetrics;
    private ImageView refImageView;	 // 放置阴影的控件
    private FrameLayout[] fls = new FrameLayout[5];			//块
    private ImageView[] poster = new ImageView[5];		 //海报控件
    private int randomTemp = -1;
    private LinearLayout tvLayout;
    private TextView tv_city,tv_country,tv_temperature,tv_time;
    private ImageView iv_temperature;
    TimeReceiver timeReceiver;
    private WeatherReceiver weatherReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal);
        displayMetrics = getResources().getDisplayMetrics();
        initView();
        initRegister();
        initData();
    }

    private void initView() {
        hs = (SmoothHorizontalScrollView) findViewById(R.id.id_horizontal);
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);
        iv_cover = (ImageView) findViewById(R.id.apps_iv_cover);

        refImageView = (ImageView) findViewById(R.id.tv_show_reflected_img);
        tvLayout = (LinearLayout) findViewById(R.id.tv_show_layout);

        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_time = (TextView) findViewById(R.id.tv_time);
        iv_temperature = (ImageView) findViewById(R.id.iv_temperature);

        fls[0] = (FrameLayout) findViewById(R.id.tv_show_fl_0);
        fls[1] = (FrameLayout) findViewById(R.id.tv_show_fl_1);
        fls[2] = (FrameLayout) findViewById(R.id.tv_show_fl_2);
        fls[3] = (FrameLayout) findViewById(R.id.tv_show_fl_3);
        fls[4] = (FrameLayout) findViewById(R.id.tv_show_fl_4);

        poster[0] = (ImageView) findViewById(R.id.tv_show_post_0);
        poster[1] = (ImageView) findViewById(R.id.tv_show_post_1);
        poster[2] = (ImageView) findViewById(R.id.tv_show_post_2);
        poster[3] = (ImageView) findViewById(R.id.tv_show_post_3);
        poster[4] = (ImageView) findViewById(R.id.tv_show_post_4);

        int[] bgSelector = { R.mipmap.blue_no_shadow,
                R.mipmap.dark_no_shadow, R.mipmap.green_no_shadow,
                R.mipmap.orange_no_shadow, R.mipmap.pink_no_shadow};
        int bgSize = bgSelector.length;
        for (int i = 0; i < 5; i++) {
            //poster[i].setOnClickListener(this);
            poster[i].setImageResource(bgSelector[createRandom(bgSize)]);
            fls[i].setOnFocusChangeListener(this);
            //backgrounds[i].setVisibility(View.GONE);
        }
        Bitmap reflectImg = ImageReflect.createCutReflectedImage(
                ImageReflect.convertViewToBitmap(tvLayout), 14);
        refImageView.setImageBitmap(reflectImg);
    }

    @Override
    protected void onStart() {
        (((ViewGroup) mGallery.getChildAt(0))).requestFocus();
        super.onStart();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        focusChange.onGetFocus(getCurrentFocus(),0);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {
        if (isFocus) {
            getFocus(view);
            Log.d("getFocus");
            //iv_cover.bringToFront();
        } else {
            Log.d("cancelFocus");
            cancelFocus(view);
        }
    }

    /**
     * 获取焦点
     * @param v
     */
    public void getFocus(final View v) {
        float x = 0, y = 0;
        iv_cover.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params = iv_cover.getLayoutParams();
        int padding = 0;
        x = v.getX() +100;
        padding = 1;
        params.width = v.getWidth() + padding+2;
        y = v.getY() + 458;
        params.height = v.getHeight() + padding +2;
        iv_cover.setScaleType(ImageView.ScaleType.FIT_XY);
        iv_cover.requestLayout();
        ScaleAnimation localScaleAnimation = new ScaleAnimation(1.0F, 1.1F, 1.0F, 1.1F, 1, 0.5F, 1, 0.5F);
        localScaleAnimation.setFillAfter(true);
        localScaleAnimation.setInterpolator(new AccelerateInterpolator());
        localScaleAnimation.setDuration(100L);
        v.startAnimation(localScaleAnimation);
        moveFocus(1.1f, 1.1f, x, y, v);
        //iv_cover.setX(x);
        //iv_cover.setY(y);
    }

    private void moveFocus(float scalex, float scaley, float x, float y, final View v) {
        Log.d("scalex:"+scalex+",scaley:"+scaley+",x:"+x+",y:"+y);
        ObjectAnimator animSX = ObjectAnimator.ofFloat(iv_cover, View.SCALE_X, scalex);
        ObjectAnimator animSY = ObjectAnimator.ofFloat(iv_cover, View.SCALE_Y, scaley);
        ObjectAnimator animX = ObjectAnimator.ofFloat(iv_cover, "x", iv_cover.getX(), x);
        ObjectAnimator animY = ObjectAnimator.ofFloat(iv_cover, "y", iv_cover.getY(), y);
        ObjectAnimator animA = ObjectAnimator.ofFloat(iv_cover, "alpha", 1f, 1f);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.setDuration(150L);
        animSetXY.setInterpolator(new AccelerateInterpolator());
        animSetXY.playTogether(animA, animSX, animSY, animX, animY);
        animSetXY.start();
        iv_cover.bringToFront();
    }

    public void cancelFocus(final View v) {
        iv_cover.setVisibility(View.GONE);
        ScaleAnimation localScaleAnimation = new ScaleAnimation(1.1F, 1.0F, 1.1F, 1.0F, 1, 0.5F, 1, 0.5F);
        localScaleAnimation.setFillAfter(true);
        localScaleAnimation.setInterpolator(new AccelerateInterpolator());
        localScaleAnimation.setDuration(100L);
        v.startAnimation(localScaleAnimation);
    }

    /**
     * 创建随机数
     * @param size
     * @return
     */
    private int createRandom(int size) {
        Random random = new Random();
        int randomIndex = random.nextInt(size);
        // 如果本次随机与上次一样，重新随机
        while (randomIndex == randomTemp) {
            randomIndex = random.nextInt(size);
        }
        randomTemp = randomIndex;
        return randomIndex;
    }

    private void initData() {
        tv_time.setText(SystemUtil.getTime());
        datas = queryFilterAppInfo(this);
        adapter = new MoreAdapter(this, datas, focusChange);
        for (int position = 0; position < datas.size(); position++) {
            View view = adapter.getView(position, null, mGallery);
            mGallery.addView(view);
        }
        //发送天气广播
        sendBroadcast(new Intent(WeatherReceiver.RESPONSE_WEATHER));
    }

    @Override
    public void updateWeather(CityWeatherInfoBean bean) {
        if (bean == null) {
            return;
        }
        if (tv_city != null)
            tv_city.setText(bean.getCityName());
        if (tv_temperature != null)
            tv_temperature.setText(bean.getfTemp() + "~" + bean.gettTemp());
        int[] ids = StringUtil.getWeaResByWeather(bean.getWeatherInfo());
        if (ids[0] != 0) {
            Log.d("ids[0]" + ids[0]);
            if (iv_temperature != null)
                iv_temperature.setVisibility(View.VISIBLE);
                iv_temperature.setImageResource(ids[0]);
        } else {
            if (iv_temperature != null)
                iv_temperature.setVisibility(View.GONE);
        }
        /*if (ids[1] != 0) {
            Log.d("ids[1]"+ids[1]);
            if (iv_temperature != null)
                iv_temperature.setVisibility(View.VISIBLE);
                iv_temperature.setImageResource(ids[1]);
        } else {
            if (iv_temperature != null)
                iv_temperature.setVisibility(View.GONE);
        }*/
        //AnimationSetUtils.SetFlickerAnimation(tv_temperature, bean.getWeatherInfo(), temp);
    }

    private void initRegister(){
        timeReceiver = new TimeReceiver();
        IntentFilter filterTime = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(timeReceiver, filterTime);

        weatherReceiver = new WeatherReceiver();
        Log.d("WeatherReceiver");
        registerReceiver(weatherReceiver, new IntentFilter(WeatherReceiver.RESPONSE_WEATHER));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeReceiver);
        unregisterReceiver(weatherReceiver);
    }

    private FocusChange focusChange = new FocusChange() {

        @Override
        public boolean onGetFocus(View view, int position) {
            int x, y, w, h;
            w = 100;
            h = 74;
            x = 54;
            y = 49;
            iv_cover.requestLayout();
            iv_cover.setVisibility(View.VISIBLE);
            if (displayMetrics.widthPixels == 1920) { //屏幕宽度是1920
                //Log.d("displayMetrics.widthPixels==1920","");
                w = 101;
                h = 74;
                x = 55;
                y = 49;
            } else if (displayMetrics.widthPixels == 1280) { //屏幕宽度是1280
                //Log.d("displayMetrics.widthPixels==1280","");
                w = 2;
                h = 2;
                x = -45;
                y = 165;
            }
            int[] location = new int[2];            //location [0]--->x坐标,location [1]--->y坐标
            int[] l = new int[2];
            iv_cover.getLocationOnScreen(l);         //获取iv_cover在整个屏幕内的绝对坐标
            view.getLocationOnScreen(location);     //获取view在整个屏幕内的绝对坐标
            final int width = (int) (view.getWidth() + w * displayMetrics.density-20);
            final int height = (int) (view.getHeight() + h * displayMetrics.density-20);
            //Log.d("width:"+width+",height:"+height,"");
            final ViewGroup.LayoutParams params = iv_cover.getLayoutParams();
            int offsetY = 10;
            final float toX = view.getX() - x * displayMetrics.density - hs.getScrollX();
            final float toY = view.getY() + y * displayMetrics.density + 60 + offsetY;
            ScaleAnimation localScaleAnimation = new ScaleAnimation(1.0F, 1.1F, 1.0F, 1.1F, 1, 0.5F, 1, 0.5F);
            localScaleAnimation.setFillAfter(true);
            localScaleAnimation.setInterpolator(new AccelerateInterpolator());
            localScaleAnimation.setDuration(210);
            view.startAnimation(localScaleAnimation);
            iv_cover.setX(toX);
            iv_cover.setY(toY);
            params.width = width;
            params.height = height;
            return false;
        }

        @Override
        public boolean onLostFocus(View view) {
            iv_cover.setVisibility(View.GONE);
            ScaleAnimation localScaleAnimation = new ScaleAnimation(1.1F, 1.0F, 1.1F, 1.0F, 1, 0.5F, 1, 0.5F);
            localScaleAnimation.setFillAfter(true);
            localScaleAnimation.setInterpolator(new AccelerateInterpolator());
            localScaleAnimation.setDuration(110);
            view.startAnimation(localScaleAnimation);
            return false;
        }
    };

    /**
     * 查询所有App信息
     *
     * @param context
     * @return
     */
    public static List<ResolveInfo> queryFilterAppInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //手机里面有开始界面的所有程序
        List<ResolveInfo> listAppcations = pm.queryIntentActivities(mainIntent, 0);
        Collections.sort(listAppcations, new ResolveInfo.DisplayNameComparator(pm));
        return listAppcations;
    }

    /**
     * 系统时间监听器
     */
    public class TimeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                tv_time.setText(SystemUtil.getTime());
            }
        }
    }
}
