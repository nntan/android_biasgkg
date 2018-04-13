package com.luan.dms_management.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.models.OrderbyDoc;
import com.luan.dms_management.models.Product;
import com.luan.dms_management.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luan.nt on 8/1/2017.
 */

public class ProductOrderAdapter extends BaseAdapter {
    private Context context;
    private List<OrderbyDoc> item;

    public ProductOrderAdapter(Context context, List<OrderbyDoc> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public int getCount() {
        return this.item.size();
    }

    @Override
    public Object getItem(int position) {
        return this.item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LinearLayout layout = (LinearLayout) convertView;
        if (convertView != null) {
            holder = (ViewHolder) layout.getTag();
        } else {
            layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_product_order, null);
            holder = new ViewHolder(layout);
            layout.setTag(holder);
        }

        holder.txtProduct.setText(item.get(position).getItemName());
        holder.txtDiscountPro.setText(item.get(position).getDiscount());
        holder.txtUnitPrice.setText(item.get(position).getPrice());
        holder.txtQuantity.setText(item.get(position).getQuantity());
        holder.Unit.setText(item.get(position).getUoM());

        return layout;
    }


    public class ViewHolder {
        @BindView(R.id.item_order_product)
        protected TextView txtProduct;
        @BindView(R.id.item_order_unit)
        protected TextView Unit;
        @BindView(R.id.item_order_unitprice)
        protected TextView txtUnitPrice;
        @BindView(R.id.item_order_discountPro)
        protected TextView txtDiscountPro;
        @BindView(R.id.item_order_quantity)
        protected TextView txtQuantity;
        @BindView(R.id.item_order_product_delete)
        protected TextView icdelete;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            CommonUtils.makeTextViewFont(context, icdelete);
        }
    }
}
