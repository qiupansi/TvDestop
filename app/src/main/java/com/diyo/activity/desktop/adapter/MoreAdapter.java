package com.diyo.activity.desktop.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diyo.activity.desktop.FocusChange;
import com.diyo.activity.desktop.R;

import java.util.List;

/**
 * Created by tingbaby on 2015/10/28.
 */
public class MoreAdapter extends BaseAdapter {
    private List<ResolveInfo> datas;
    private Context context;
    private PackageManager pm;
    private FocusChange focusChange;
    private LayoutInflater inflater;

    public MoreAdapter(Context context,List<ResolveInfo> datas,FocusChange focusChange) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.focusChange = focusChange;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if(position==datas.size()){
            return null;
        }
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView = inflater.inflate(R.layout.activity_index_gallery_item, parent, false);
        holder = new ViewHolder();
        holder.appIcon = (ImageView) convertView.findViewById(R.id.id_index_gallery_item_image);
        holder.appName = (TextView) convertView.findViewById(R.id.id_index_gallery_item_text);
        convertView.setTag(holder);
        if(position != datas.size()){
            ResolveInfo info = datas.get(position);
            pm = context.getPackageManager();
            Drawable drawable = info.loadIcon(pm);
            holder.appIcon.setImageDrawable(drawable);
            holder.appName.setText(info.loadLabel(pm));
        } else {
            holder.appIcon.setImageResource(R.mipmap.add1);
            holder.appName.setText("Add app");
        }
        convertView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (focusChange != null) {
                    if (hasFocus) {
                        Log.d("onFocusChange", "onGetFocus");
                        //iv.animate().scaleX(1.1f).scaleY(1.1f);
                        focusChange.onGetFocus(v, position);
                    } else {
                        Log.d("onFocusChange", "onLostFocus");
                        //iv.animate().scaleX(1.0f).scaleY(1.0f);
                        focusChange.onLostFocus(v);
                    }
                }
            }
        });
        return convertView;
    }
    static class ViewHolder{
        private ImageView appIcon;
        private TextView appName;
    }
}
