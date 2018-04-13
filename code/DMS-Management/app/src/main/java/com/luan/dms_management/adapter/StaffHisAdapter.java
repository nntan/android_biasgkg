package com.luan.dms_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.models.Customer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luan.nt on 8/4/2017.
 */

public class StaffHisAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Customer> item;

    public StaffHisAdapter(Context context, ArrayList<Customer> item) {
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
        LinearLayout layout = (LinearLayout) convertView;
        if (layout != null) {
            holder = (ViewHolder) layout.getTag();
        } else {
            layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_historystaff, null);
            holder = new ViewHolder(layout);
            layout.setTag(holder);
        }

        return layout;
    }

    public class ViewHolder {
        @BindView(R.id.avatarStaffHis)
        ImageView avatar;
        @BindView(R.id.nameStaffHis)
        TextView name;
        @BindView(R.id.phoneStaffHis)
        TextView phone;
        @BindView(R.id.nameCusHis)
        TextView nameCus;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
