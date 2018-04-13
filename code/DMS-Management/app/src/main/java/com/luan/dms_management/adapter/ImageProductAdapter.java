package com.luan.dms_management.adapter;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luan.dms_management.R;
import com.luan.dms_management.utils.CommonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luan.nt on 7/31/2017.
 */

public class ImageProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> item;

    public ImageProductAdapter(Context context, ArrayList<String> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        RelativeLayout layout = (RelativeLayout) convertView;
        if (layout != null) {
            holder = (ViewHolder) layout.getTag();
        } else {
            layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_galery, null);
            holder = new ViewHolder(layout);
            layout.setTag(holder);
        }
        byte[] base64 = Base64.decode(item.get(position), Base64.DEFAULT);

        Glide.with(context).load(base64)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        return layout;
    }

    public class ViewHolder {
        @BindView(R.id.btndelete)
        TextView btndelete;
        @BindView(R.id.imagePro)
        ImageView imageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            CommonUtils.makeTextViewFont(context, btndelete);
        }
    }
}
