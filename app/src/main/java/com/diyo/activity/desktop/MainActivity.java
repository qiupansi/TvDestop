package com.diyo.activity.desktop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.diyo.activity.desktop.adapter.GalleryAdapter;

import java.util.Collections;
import java.util.List;
//更多TV开发资源(如桌面，直播，教育，应用市场，文件管理器，设置，酒店应用等)，添加微信交流：qiupansi
//If you want more TV resource development,such as TvLauncher,TvLive,TvAppStore,TvSettings,TvFileManager,TvEducation,TvHotel,TvMusic,TvRemote and so on，Add me wechat：qiupansi
public class MainActivity extends Activity {
    private MyRecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<ResolveInfo> mDatas;
    private ImageView mImg;
    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pm = this.getPackageManager();
        mImg = (ImageView) findViewById(R.id.id_content);

        mDatas = queryFilterAppInfo(this);

        mRecyclerView = (MyRecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new GalleryAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnItemScrollChangeListener(new MyRecyclerView.OnItemScrollChangeListener() {

            @Override
            public void onChange(View view, int position) {
                ResolveInfo info = mDatas.get(position);
                Drawable drawable = info.loadIcon(pm);
                mImg.setImageDrawable(drawable);
            }
        });

        mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
//				Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT)
//						.show();
                ResolveInfo info = mDatas.get(position);
                Drawable drawable = info.loadIcon(pm);
                mImg.setImageDrawable(drawable);
            }
        });
    }

    /**
     * 查询所有App信息
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

}
