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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luan.nt on 7/31/2017.
 */

public class AddProductAdapter extends BaseAdapter {
    private Context context;
    private List<ProductCheck> item;
    private ArrayList<ProductCheck> search;

    public AddProductAdapter(Context context, List<ProductCheck> item) {
        this.context = context;
        this.item = item;
        this.search = new ArrayList<>();
        this.search.addAll(this.item);
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
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_addproduct, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.check.setChecked(false);
        if (this.item.get(position).isCheck()) {
            holder.check.setChecked(true);
        }

        holder.txtname.setText(this.item.get(position).getItem().getItemName());
//        holder.txtprice.setText("Giá chẵn: " + this.item.get(position).getItem().getInvUom() + " - Giá lẻ: " + this.item.get(position).getItem().getRetail());
//        holder.txtinventory.setText("Tồn chẵn: " + this.item.get(position).getItem().getBuyUoM() + " - Tồn lẻ: " + this.item.get(position).getItem().getRetail());

        holder.txtprice.setText("Giá chẵn: ---- Giá lẻ: ----");
        holder.txtinventory.setText("Tồn chẵn: ---- Tồn lẻ: ----");

        Glide.with(context).load(R.drawable.noimage)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        return convertView;
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        this.item.clear();
        if (charText.length() == 0) {
            item.addAll(search);

        } else {
            for (ProductCheck productCheck : search) {
                if (charText.length() != 0 && productCheck.getItem().getItemName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    item.add(productCheck);
                } else if (charText.length() != 0 && productCheck.getItem().getItemName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    item.add(productCheck);
                }
            }
        }
        notifyDataSetChanged();
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
