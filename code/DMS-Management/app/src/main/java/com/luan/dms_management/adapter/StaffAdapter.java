package com.luan.dms_management.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luan.dms_management.R;
import com.luan.dms_management.activities.ViewOnMap;
import com.luan.dms_management.models.StaffLocation;
import com.luan.dms_management.models.StaffMonitor;
import com.luan.dms_management.service.BasicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luan.nt on 8/4/2017.
 */

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {
    private Context context;
    private List<StaffMonitor> item;
    private List<StaffLocation> locations;
    private ArrayList<StaffMonitor> search;

    public StaffAdapter(Context context, List<StaffMonitor> item, List<StaffLocation> locations) {
        this.context = context;
        this.item = item;
        this.locations = locations;
        this.search = new ArrayList<>();
        this.search.addAll(this.item);
    }

    @Override
    public StaffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StaffViewHolder holder, final int position) {
        final StaffMonitor monitor = this.item.get(position);

        holder.name.setText(monitor.getEmpFullName());
        holder.phone.setText(context.getString(R.string.travel) + ": " + monitor.getLastCustName() + monitor.getLastCustAdd());
        holder.travelplan.setText(monitor.getCheckInPlan() + "");
        holder.travelexc.setText(monitor.getCheckInFinish() + "");
        holder.totalorder.setText(context.getString(R.string.totalorder) + ": " + monitor.getNoOrder());
        if (!monitor.getNoofMetter().equals("")) {
            holder.distance.setText(monitor.getNoofMetter() + " m");
        }

        Glide.with(this.context).load(item.get(position).getPicLink())
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.avatar);

        holder.viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicService.currentStaff = locations.get(position);
                Intent intent = new Intent(context, ViewOnMap.class);
                intent.putExtra(ViewOnMap.PRESENT_MODE_KEY, ViewOnMap.PRESENT_MODE_STAFF);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        this.item.clear();
        if (charText.length() == 0) {
            item.addAll(search);

        } else {
            for (StaffMonitor staffMonitor : search) {
                if (charText.length() != 0 && staffMonitor.getEmpFullName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    item.add(staffMonitor);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class StaffViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatarStaff)
        ImageView avatar;
        @BindView(R.id.nameStaff)
        TextView name;
        @BindView(R.id.phoneStaff)
        TextView phone;
        @BindView(R.id.travelPlan)
        TextView travelplan;
        @BindView(R.id.travelExecute)
        TextView travelexc;
        @BindView(R.id.orderStaff)
        TextView totalorder;
        @BindView(R.id.locationStaff)
        TextView viewMap;
        @BindView(R.id.distanceStaff)
        TextView distance;

        private View view;

        public StaffViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, this.view);
        }
    }
}
