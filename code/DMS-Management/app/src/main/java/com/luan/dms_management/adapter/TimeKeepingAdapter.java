package com.luan.dms_management.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.models.TimeKeeping;
import com.luan.dms_management.models.TimeSheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luan.nt on 8/18/2017.
 */

public class TimeKeepingAdapter extends BaseAdapter {
    private Context context;
    private List<TimeSheet> items;

    public TimeKeepingAdapter(Context context, List<TimeSheet> items) {
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
        final TimeSheet item = items.get(position);
        View layout = convertView;
        if (layout != null) {
            holder = (ViewHolder) layout.getTag();
        } else {
            layout = LayoutInflater.from(context).inflate(R.layout.item_work_time, null);
            holder = new ViewHolder(layout);
            layout.setTag(holder);
        }

        holder.tvTimeIn.setText(convertToStringTime(item.getStarttime()));
        if (!TextUtils.isEmpty(item.getEndtime())) {
            holder.tvTimeOut.setText(convertToStringTime(item.getEndtime()));
        } else {
            holder.tvTimeOut.setText("");
        }
        return layout;
    }

    private String convertToStringTime(String source) {
        String returnValue = "";
        SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        SimpleDateFormat destinationDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date convertedDate = new Date();
        try {
            convertedDate = sourceDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        returnValue = destinationDateFormat.format(convertedDate);
        return returnValue;
    }

    public class ViewHolder {
        @BindView(R.id.item_timesheet_in)
        TextView tvTimeIn;
        @BindView(R.id.item_timesheet_out)
        TextView tvTimeOut;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
