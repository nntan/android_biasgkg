package com.luan.dms_management.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.service.SyncService;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PrefUtils;
import com.luan.dms_management.utils.TrackerGPS;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.luan.dms_management.utils.Constant.CUSTOMER_PREF_KEY;
import static com.luan.dms_management.utils.Constant.LAT_PREF_KEY;
import static com.luan.dms_management.utils.Constant.LONG_PREF_KEY;

/**
 * Created by luan.nt on 7/27/2017.
 */

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.CustomerViewHolder> {

    private Context context;
    private List<Customer> items;
    private Realm realm;

    public TravelAdapter(Context context, List<Customer> items) {
        this.context = context;
        this.items = items;
        this.realm = RealmController.with(context).getRealm();
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_travel, parent, false);
        return new CustomerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        final Customer customer = this.items.get(position);

        holder.nameCus.setText(customer.getCardName());
        holder.nameContact.setText(customer.getContactPerson());
        holder.address.setText(customer.getAddress());
        holder.phone.setText(customer.getTel());
        holder.lastVisit.setText(customer.getLastVisit());
        if (customer.getVisitStatus() == 1) {
            holder.status.setText(context.getString(R.string.icstatustravel) + " " + context.getString(R.string.status));
            holder.viewtitle.setBackgroundColor(context.getResources().getColor(R.color.title));
        } else {
            holder.status.setText("");
            holder.viewtitle.setBackgroundColor(context.getResources().getColor(R.color.titleopa));
        }

        holder.btnShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PrefUtils.savePreference(context, CUSTOMER_PREF_KEY, customer.getCardCode());
//                BasicService.getInstance().currentCus = customer;
//                Intent intent = new Intent(context, CreateOrderDetail.class);
//                context.startActivity(intent);
            }
        });

        holder.btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicService.currentCus = customer;
                //get current location and then compare with store's location
                doGetCurrentLocation(customer, false);
            }
        });

        holder.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.savePreference(context, CUSTOMER_PREF_KEY, customer.getCardCode());
                BasicService.currentCus = customer;
                Intent intent = new Intent(context, CreateOrderDetail.class);
                context.startActivity(intent);
            }
        });

        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicService.currentCus = customer;
                doGetCurrentLocation(customer, true);
            }
        });

        holder.icNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoteInfo(customer);
            }
        });

        holder.viewnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoteInfo(customer);
            }
        });

        holder.icStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatisticsInfo(customer);
            }
        });

        holder.viewstatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatisticsInfo(customer);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void showDialog(final Customer cust) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        PrefUtils.savePreference(context, CUSTOMER_PREF_KEY, cust.getCardCode());
                        Customer temp = new Customer(cust);
                        temp.setVisitStatus(Short.parseShort("1"));
                        temp.setVisitStartDate(CommonUtils.getHour() + ":" + CommonUtils.getMinute());
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(temp);
                        realm.commitTransaction();
                        Intent intent = new Intent(context, Checkin.class);
                        intent.putExtra("visited", false);
                        context.startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(
                context.getString(R.string.cus)
                        + " " + cust.getCardName()
                        + " " + context.getString(R.string.checkin))
                .setPositiveButton(context.getString(R.string.button_ok), dialogClickListener)
                .setNegativeButton(context.getString(R.string.button_cancel), dialogClickListener).show();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtNamCus)
        protected TextView nameCus;
        @BindView(R.id.txtNameContact)
        protected TextView nameContact;
        @BindView(R.id.txtStatus)
        protected TextView status;
        @BindView(R.id.icon_travel_address)
        protected TextView icAddress;
        @BindView(R.id.icon_travel_phone)
        protected TextView icphone;
        @BindView(R.id.icon_travel_status)
        protected TextView icstatus;
        @BindView(R.id.icon_travel_note)
        protected TextView icNote;
        @BindView(R.id.icon_travel_statistics)
        protected TextView icStatistics;
        @BindView(R.id.travel_note)
        protected TextView viewnote;
        @BindView(R.id.travel_statistics)
        protected TextView viewstatistics;
        @BindView(R.id.travel_address)
        protected TextView address;
        @BindView(R.id.travel_phone)
        protected TextView phone;
        @BindView(R.id.travel_last)
        protected TextView lastVisit;
        @BindView(R.id.travel_map)
        protected TextView map;
        @BindView(R.id.icOrder)
        protected TextView icOrder;
        @BindView(R.id.icShip)
        protected TextView icShip;
        @BindView(R.id.icCheckin)
        protected TextView icCheckin;
        @BindView(R.id.btnTravelOrder)
        protected LinearLayout btnOrder;
        @BindView(R.id.btnTravelShip)
        protected LinearLayout btnShip;
        @BindView(R.id.btnTravelCheckin)
        protected LinearLayout btnCheckin;
        @BindView(R.id.viewTravelStatus)
        protected LinearLayout viewtitle;

        private View view;

        public CustomerViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, this.view);
            CommonUtils.makeTextViewFont(context, icAddress);
            CommonUtils.makeTextViewFont(context, icphone);
            CommonUtils.makeTextViewFont(context, icstatus);
            CommonUtils.makeTextViewFont(context, status);
            CommonUtils.makeTextViewFont(context, icOrder);
            CommonUtils.makeTextViewFont(context, icShip);
            CommonUtils.makeTextViewFont(context, icCheckin);
            CommonUtils.makeTextViewFont(context, icNote);
            CommonUtils.makeTextViewFont(context, icStatistics);
        }
    }

    int retryCount = 0;

    private void doGetCurrentLocation(Customer customer, boolean isViewMap) {
        if (retryCount > 5) {
            afterGotCurrentLocation(customer, 0, 0, isViewMap);
            return;
        }
        retryCount++;

        TrackerGPS mTrackerGPS = new TrackerGPS(context);
        if (mTrackerGPS.canGetLocation()) {

            double longitude = mTrackerGPS.getLongitude();
            double latitude = mTrackerGPS.getLatitude();

            if (longitude == 0 && latitude == 0) {
                doGetCurrentLocation(customer, isViewMap);
                return;
            }

            afterGotCurrentLocation(customer, latitude, longitude, isViewMap);
        } else {
            mTrackerGPS.showSettingsAlert();
        }
    }

    protected void afterGotCurrentLocation(Customer customer, double lat, double lon, boolean isViewMap) {
        if (!isViewMap) {
            Location loc1 = new Location("");
            loc1.setLatitude(lat);
            loc1.setLongitude(lon);

            Location loc2 = new Location("");
            loc2.setLatitude(Double.parseDouble(customer.getLatitudeValue()));
            loc2.setLongitude(Double.parseDouble(customer.getLongitudeValue()));

            float distanceInMeters = loc1.distanceTo(loc2);
            if (distanceInMeters > 200) {
                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle(R.string.app_name)
                        .setMessage("Khoảng cách chênh lệch: " + Math.round(distanceInMeters) + "m")
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                doCheckin();
                                dialogInterface.dismiss();
                            }
                        }).create();
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            } else {
                doCheckin();
            }
        } else {
            PrefUtils.savePreference(context, LAT_PREF_KEY, lat + "");
            PrefUtils.savePreference(context, LONG_PREF_KEY, lon + "");
            PrefUtils.savePreference(context, CUSTOMER_PREF_KEY, customer.getCardCode());
            Intent intent = new Intent(context, ViewOnMap.class);
            intent.putExtra(ViewOnMap.PRESENT_MODE_KEY, ViewOnMap.PRESENT_MODE_STORE);
            context.startActivity(intent);
        }
    }

    void doCheckin() {
        String custCode = BasicService.currentCus.getCardCode();
        String userCode = ApiService.getInstance(context).getUserCode();
        String deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        SyncService.getInstance(context).offlineCheckin(custCode, userCode, deviceID);
        Intent intent = new Intent(context, Checkin.class);
        intent.putExtra("visited", true);
        context.startActivity(intent);
    }

    void showNoteInfo(final Customer customer) {
        //TODO 2.0 Tại màn hình viếng tham - danh sách khách hang đó. Hiển thị thông tin khi user click vào link "Xem Ghi chú"
        String custCode = customer.getCardCode();
        String userCode = ApiService.getInstance(context).getUserCode();
        ApiService.getInstance(context).getNoteFromUser(custCode, userCode, new ApiService.GetNoteCallback() {
            @Override
            public void onSuccess(final String data) {
                Handler mainHandler = new Handler(Looper.getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        showNoteInfo(customer.getCardName(), data);
                    }
                };
                mainHandler.post(myRunnable);
            }

            @Override
            public void onFail(String error) {
                Handler mainHandler = new Handler(Looper.getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String title = context.getString(R.string.app_name);
                        String mess = context.getString(R.string.get_note_fail);
                        CommonUtils.showDialog(context, title, mess);
                    }
                };
                mainHandler.post(myRunnable);
            }
        });
    }

    void showNoteInfo(String storeName, String data) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notes);

        TextView name = (TextView) dialog.findViewById(R.id.store_name);
        TextView note = (TextView) dialog.findViewById(R.id.store_note);


        name.setText(storeName);
        note.setText(data);

        dialog.show();
    }

    void showStatisticsInfo(Customer customer) {
        Intent intent = new Intent(context, StatisticsViewActivity.class);
        intent.putExtra("customer", customer.getCardCode());
        intent.putExtra("name", customer.getCardName());
        context.startActivity(intent);
    }
}
