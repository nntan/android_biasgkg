package com.luan.dms_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luan.dms_management.R;
import com.luan.dms_management.models.ProductCheck;
import com.luan.dms_management.models.PromotionProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PromotionProductAdapter extends BaseAdapter {
    private Context context;
    private List<PromotionProduct> items;

    public PromotionProductAdapter(Context context, List<PromotionProduct> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_addproduct, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.check.setChecked(false);

        PromotionProduct item = items.get(position);

        holder.txtname.setText(item.getItemName());
        holder.txtprice.setText(item.getItemCode());
        holder.txtinventory.setText(item.getItemCode());

        Glide.with(context).load(R.drawable.noimage)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.img_product)
        ImageView imageView;
        @BindView(R.id.txtname_product)
        TextView txtname;
        @BindView(R.id.txtprice_product)
        TextView txtprice;
        @BindView(R.id.txtinventory_product)
        TextView txtinventory;
        @BindView(R.id.checkbox_product)
        CheckBox check;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
