package com.luan.dms_management.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.activities.CreateOrderDetail;
import com.luan.dms_management.activities.OrderDetail;
import com.luan.dms_management.models.Orders;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luan.nt on 7/25/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private Context context;
    private List<Orders> items;

    public OrderAdapter(Context context, List<Orders> item) {
        this.context = context;
        this.items = item;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Orders item = this.items.get(position);
        holder.id.setText(item.getCardName() + " - " + item.getPONo());
        holder.cost.setText(context.getString(R.string.cost) + " " + item.getDocTotal());
        holder.dateorder.setText(context.getString(R.string.dateorder) + " " + CommonUtils.formatDate(item.getPODate()));
        holder.address.setText(item.getAddress());
        holder.phone.setText(item.getOrderTypeName());
        holder.status.setText(item.getDocStatusName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicService.orders = items.get(position);
                Intent intent = new Intent(context, OrderDetail.class);
                intent.putExtra("ModeDetail", "View");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtIdOrder)
        protected TextView id;
        @BindView(R.id.txtCostOrder)
        protected TextView cost;
        @BindView(R.id.txtDateOrder)
        protected TextView dateorder;
        @BindView(R.id.icon_order_address)
        protected TextView icAddress;
        @BindView(R.id.icon_order_phone)
        protected TextView icphone;
        @BindView(R.id.icon_order_status)
        protected TextView icstatus;
        @BindView(R.id.order_address)
        protected TextView address;
        @BindView(R.id.order_phone)
        protected TextView phone;
        @BindView(R.id.order_status)
        protected TextView status;

        private View view;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
            CommonUtils.makeTextViewFont(context, icAddress);
            CommonUtils.makeTextViewFont(context, icphone);
            CommonUtils.makeTextViewFont(context, icstatus);
        }
    }
}
