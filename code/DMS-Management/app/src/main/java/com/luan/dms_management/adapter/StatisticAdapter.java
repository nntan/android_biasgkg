package com.luan.dms_management.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.activities.Checkin;
import com.luan.dms_management.activities.CreateOrderDetail;
import com.luan.dms_management.activities.StatisticsViewActivity;
import com.luan.dms_management.activities.ViewOnMap;
import com.luan.dms_management.models.Customer;
import com.luan.dms_management.models.Statistic;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.service.SyncService;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PrefUtils;
import com.luan.dms_management.utils.TrackerGPS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.luan.dms_management.utils.Constant.CUSTOMER_PREF_KEY;

/**
 * Created by luan.nt on 7/27/2017.
 */

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.CustomerViewHolder> {

    private Context context;
    private List<Statistic> items;
    private Realm realm;

    public StatisticAdapter(Context context, List<Statistic> items) {
        this.context = context;
        this.items = items;
        this.realm = RealmController.with(context).getRealm();
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_statistic, parent, false);
        return new CustomerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        final Statistic statistic = this.items.get(position);

        holder.index.setText((position + 1) + "");
        holder.checkInTime.setText(convertToStringTime(statistic.getCheckInTime()));

        if (statistic.getCheckOutTime() == null || statistic.getCheckOutTime().isEmpty()) {
            holder.checkOutTime.setText("NA");
            holder.checkOutTime.setTextColor(Color.RED);

        }
        else {
            holder.checkOutTime.setText(convertToStringTime(statistic.getCheckOutTime()));
            holder.checkOutTime.setTextColor(Color.GRAY);
        }

        holder.order.setText(statistic.getNoCustOrder());
        holder.pics.setText(statistic.getNoPicTake());
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_index)
        protected TextView index;
        @BindView(R.id.tv_check_in_time)
        protected TextView checkInTime;
        @BindView(R.id.tv_check_out_time)
        protected TextView checkOutTime;
        @BindView(R.id.tv_order)
        protected TextView order;
        @BindView(R.id.tv_pics)
        protected TextView pics;

        private View view;

        public CustomerViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, this.view);
        }
    }

}
