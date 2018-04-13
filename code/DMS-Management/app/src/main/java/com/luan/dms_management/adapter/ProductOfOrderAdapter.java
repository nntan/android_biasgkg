package com.luan.dms_management.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.models.OrderbyDoc;
import com.luan.dms_management.models.ProductCheck;
import com.luan.dms_management.models.ProductCheckForOrder;
import com.luan.dms_management.models.PromotionProduct;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luan.nt on 8/1/2017.
 */

public class ProductOfOrderAdapter extends BaseAdapter {

    public interface OnPriceChanged {
        void onPriceChanged();
    }

    private OnPriceChanged onPriceChanged;
    private Context context;
    private List<ProductCheckForOrder> item;

    public ProductOfOrderAdapter(Context context, List<ProductCheckForOrder> item) {
        this.context = context;
        this.item = item;
    }

    public void setOnPriceChanged(OnPriceChanged onPriceChanged) {
        this.onPriceChanged = onPriceChanged;
    }

    public List<ProductCheckForOrder> getItems() {
        return item;
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
            layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_product_of_order, null);
            holder = new ViewHolder(layout);
            layout.setTag(holder);
        }

        final ProductCheckForOrder data = item.get(position);

        holder.tvName.setText(data.getItem().getItemName());
        holder.tvCode.setText(data.getItem().getItemCode());
        holder.tvType.setText(isPromotionProduct(data) ? "1 (Hàng khuyến mãi)" : "0 (Hàng bán)");
        holder.tvQuantity.setText(data.getQuantity() == null ? "0" : data.getQuantity());
        holder.tvUoM.setText(data.getItem().getBuyUoM());
        holder.tvPrice.setText(data.getPrice());
        holder.tvVAT.setText(data.getVAT());

        holder.tvQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.setQuantity(charSequence.toString());
                onPriceChanged.onPriceChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return layout;
    }


    public class ViewHolder {
        @BindView(R.id.tv_name)
        protected TextView tvName;
        @BindView(R.id.tv_code)
        protected TextView tvCode;
        @BindView(R.id.tv_type)
        protected TextView tvType;
        @BindView(R.id.et_quantity)
        protected EditText tvQuantity;
        @BindView(R.id.tv_uom)
        protected TextView tvUoM;
        @BindView(R.id.tv_price)
        protected TextView tvPrice;
        @BindView(R.id.tv_vat)
        protected TextView tvVAT;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void updateData(List<ProductCheckForOrder> data) {
        for (ProductCheckForOrder p : data) {
            for (ProductCheckForOrder i : item) {
                if (i.getItem().getItemCode().equalsIgnoreCase(p.getItem().getItemCode())) {
                    p.setQuantity(i.getQuantity());
                }
            }
        }

        item = data;

        BasicService.currentChosenOrderProduct = item;
        notifyDataSetChanged();
    }

    public String getCurrentPrice() {
        int price = 0;
        for (ProductCheckForOrder p : item) {
            int cp = Integer.parseInt(p.getPrice());
            int qt = (p.getQuantity() == null || p.getQuantity().isEmpty()) ? 0 : Integer.parseInt(p.getQuantity());
            price += cp * qt;
        }

        return price + "";
    }

    private boolean isPromotionProduct(ProductCheckForOrder data) {
        List<PromotionProduct> rootData = BasicService.currentPromotionProduct;
        for (PromotionProduct p : rootData) {
            if (p.getItemCode().equalsIgnoreCase(data.getItem().getItemCode())) {
                return true;
            }
        }

        return false;
    }
}
