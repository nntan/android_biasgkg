package com.luan.dms_management.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.activities.Checkin;
import com.luan.dms_management.models.Customer;
import com.luan.dms_management.models.LeaveLog;
import com.luan.dms_management.models.TimeSheet;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PrefUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luan.dms_management.utils.Constant.CUSTOMER_PREF_KEY;

/**
 * Created by hanbiro on 14/12/2017.
 */

public class LeaveLogAdapter extends RecyclerView.Adapter<LeaveLogAdapter.ViewHolder> {
    private Context context;
    private List<LeaveLog> items;
    private ApproveCallback callback;

    public interface ApproveCallback {
        void onApprove(final LeaveLog leaveLog);
    }

    public LeaveLogAdapter(Context context, List<LeaveLog> items, final ApproveCallback callback) {
        this.context = context;
        this.items = items;
        this.callback = callback;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public LeaveLogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leave_log, parent, false);
        return new LeaveLogAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LeaveLogAdapter.ViewHolder holder, int position) {
        final LeaveLog item = items.get(position);

        holder.tvID.setText(item.getLeaveId());
        holder.tvRequestDate.setText(convertToStringTime(item.getCreateDate()));
        holder.tvFromDate.setText(convertToStringTime(item.getFromDate()));
        holder.tvToDate.setText(convertToStringTime(item.getToDate()));
        holder.tvType.setText(item.getLeaveType());
        holder.tvStatus.setText(item.getStatus());
        if (!TextUtils.isEmpty(item.getApproveUser())) {
            holder.tvUserApprove.setText(item.getApproveUser());
        } else {
            holder.tvUserApprove.setText("");
        }
        if (item.getStatus().equals("0")) {
            holder.btnApprove.setEnabled(true);
            holder.btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogApprove(item);
                }
            });
        } else {
            holder.btnApprove.setEnabled(false);
            holder.btnApprove.setVisibility(View.INVISIBLE);
        }
    }

    private void showDialogApprove(final LeaveLog leaveLog) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        callback.onApprove(leaveLog);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(context.getString(R.string.mes_approve))
                .setPositiveButton(context.getString(R.string.button_ok), dialogClickListener)
                .setNegativeButton(context.getString(R.string.button_cancel), dialogClickListener).show();
    }

    private String convertToStringTime(String source) {
        String returnValue = "";
        SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        SimpleDateFormat destinationDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = sourceDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        returnValue = destinationDateFormat.format(convertedDate);
        return returnValue;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_leave_log_id)
        TextView tvID;
        @BindView(R.id.item_leave_log_request_date)
        TextView tvRequestDate;
        @BindView(R.id.item_leave_log_from_date)
        TextView tvFromDate;
        @BindView(R.id.item_leave_log_to_date)
        TextView tvToDate;
        @BindView(R.id.item_leave_log_type)
        TextView tvType;
        @BindView(R.id.item_leave_log_status)
        TextView tvStatus;
        @BindView(R.id.item_leave_log_user_approve)
        TextView tvUserApprove;
        @BindView(R.id.item_leave_log_approve)
        TextView btnApprove;

        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, this.view);
        }
    }
}
