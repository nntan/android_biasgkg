package com.luan.dms_management.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.luan.dms_management.R;
import com.luan.dms_management.models.InventoryProduct;
import com.luan.dms_management.models.NormalProduct;
import com.luan.dms_management.models.Product;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.service.SyncService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luan.nt on 7/28/2017.
 */

public class ProductChekinAdapter extends BaseSwipeAdapter {
    public static interface OnRequestBarCodeScannerListener {
        void onRequestBarCode(NormalProduct product, int index);
    }

    private Context context;
    private List<NormalProduct> items;
    private OnRequestBarCodeScannerListener onRequestBarCodeScannerListener;

    public void setOnRequestBarCodeScannerListener(OnRequestBarCodeScannerListener listener) {
        onRequestBarCodeScannerListener = listener;
    }

    public ProductChekinAdapter(Context context, List<NormalProduct> items) {
        this.context = context;

        this.items = new ArrayList<>();
        for (NormalProduct p : items) {
            this.items.add(p.clone());
        }
    }

    public void updateBarcodeFor(int index, String barcode) {
        items.get(index).setBarcode(barcode);
        notifyDataSetChanged();
    }

    private interface inputValue {
        void input(String value);
    }

    public void addNewProduct(Product data) {
        items.add(data.getNormalProduct());
        notifyDataSetChanged();
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
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_checkin_inventory, null);

        final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {

            }
        });

        v.findViewById(R.id.bottom_wrapper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //notifyDataSetChanged();
                swipeLayout.close();
            }
        });
        return v;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final ViewHolder holder;
        holder = new ViewHolder(convertView);
        convertView.setTag(holder);

        final NormalProduct product = items.get(position);

        holder.product.setText(product.getItemName());
        holder.expiry.setText(product.getExpDate());
        holder.even.setText(product.getEven() + "");
        holder.retail.setText(product.getRetail() + "");

        holder.even.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                createDialog("Tồn chẵn", holder.even.getText().toString(), new inputValue() {
                    @Override
                    public void input(String value) {
                        product.setEven(Integer.parseInt(value));
                        ProductChekinAdapter.this.notifyDataSetChanged();
                        //holder.even.setText(value);
                        //items.get(position).setEven(Integer.parseInt(value));
                    }
                });
            }
        });
        holder.retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog("Tồn lẻ", holder.retail.getText().toString(), new inputValue() {
                    @Override
                    public void input(String value) {
                        product.setRetail(Integer.parseInt(value));
                        ProductChekinAdapter.this.notifyDataSetChanged();
                        //holder.retail.setText(value);
                        //items.get(position).setRetail(Integer.parseInt(value));
                    }
                });
            }
        });

//        holder.expiry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onRequestBarCodeScannerListener.onRequestBarCode(product, position);
//            }
//        });
    }

    public void createDialog(String title, String value, final inputValue callback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_input_inventory, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.textdialog);
        editText.setText(value);
        dialogBuilder.setTitle(title);
        dialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.input(editText.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public class ViewHolder {
        @BindView(R.id.item_inventory_product)
        TextView product;
        @BindView(R.id.item_inventory_even)
        EditText even;
        @BindView(R.id.item_inventory_odd)
        EditText retail;
        @BindView(R.id.item_inventory_expiry)
        EditText expiry;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public List<NormalProduct> getChangedProducts(List<NormalProduct> rootProducts) {
        List<NormalProduct> returnValue = new ArrayList<>();

        for (int i = 0; i < rootProducts.size(); i++) {
            NormalProduct normalItem = items.get(i);
            NormalProduct item = rootProducts.get(i);
            if (normalItem.isDiffWith(item.clone())) {
                returnValue.add(normalItem);
            }
        }

        if (items.size() > rootProducts.size()) {
            for (int i = rootProducts.size(); i < items.size(); i++) {
                returnValue.add(items.get(i));
            }
        }

        return returnValue;
    }

    public void addProduct(InventoryProduct product) {
        NormalProduct normalProduct = new NormalProduct();
        normalProduct.setBarcode(product.getBarcode());
        normalProduct.setItemCode(product.getItemCode());
        normalProduct.setItemName(product.getItemName());
        normalProduct.setBuyUoM(product.getUom());
        normalProduct.setInvUom(product.getInvUom());
        normalProduct.setExpDate(product.getExpDate());
        this.items.add(normalProduct);
        notifyDataSetChanged();
    }

    public void addProduct(Product product, String expDate) {
        NormalProduct normalProduct = product.getNormalProduct().clone();
        normalProduct.setExpDate(expDate);
        this.items.add(product.getNormalProduct().clone());
        notifyDataSetChanged();
    }
}
