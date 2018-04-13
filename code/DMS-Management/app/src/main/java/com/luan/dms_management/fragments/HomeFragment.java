package com.luan.dms_management.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.luan.dms_management.R;
import com.luan.dms_management.activities.AccountSetting;
import com.luan.dms_management.activities.Login;
import com.luan.dms_management.activities.Monitoring;
import com.luan.dms_management.activities.OrderActivity;
import com.luan.dms_management.activities.Travel;
import com.luan.dms_management.activities.Work;
import com.luan.dms_management.models.Channel;
import com.luan.dms_management.models.Customer;
import com.luan.dms_management.models.CustomerGroup;
import com.luan.dms_management.models.NoteType;
import com.luan.dms_management.models.Orders;
import com.luan.dms_management.models.Product;
import com.luan.dms_management.models.Route;
import com.luan.dms_management.models.StatusOrder;
import com.luan.dms_management.models.TypeOrder;
import com.luan.dms_management.models.TypeVisit;
import com.luan.dms_management.models.WareHouse;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.SyncService;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PrefUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.luan.dms_management.utils.Constant.PASSWORD_PREF_KEY;
import static com.luan.dms_management.utils.Constant.USERNAME_PREF_KEY;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.txtNameUser)
    protected TextView txtNameUser;
    @BindView(R.id.travel)
    protected LinearLayout travel;
    @BindView(R.id.order)
    protected LinearLayout order;
    @BindView(R.id.monitoring)
    protected LinearLayout monitoring;
    @BindView(R.id.sync)
    protected LinearLayout sync;
    @BindView(R.id.work)
    protected LinearLayout work;
    @BindView(R.id.logout)
    protected LinearLayout logout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Realm realm;
    private ProgressDialog mProgressDialog;
    private ApiService mService;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        mService = ApiService.getInstance(getContext());
        loadData();
        return view;
    }

    private void loadData() {
        txtNameUser.setText(getString(R.string.welcome) + ": " + PrefUtils.getPreference(getActivity(), USERNAME_PREF_KEY));
    }

    @OnClick(R.id.ib_acc_setting)
    protected void accountSetting(View view) {
        Intent intent = new Intent(getActivity(), AccountSetting.class);
        startActivity(intent);
    }

    @OnClick(R.id.travel)
    protected void travelClick(View view) {
        Intent intent = new Intent(getActivity(), Travel.class);
        startActivity(intent);
    }

    @OnClick(R.id.order)
    protected void orderClick(View view) {
        Intent intent = new Intent(getActivity(), OrderActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.monitoring)
    protected void monitoringClick(View view) {
        Intent intent = new Intent(getActivity(), Monitoring.class);
        startActivity(intent);
    }

    @OnClick(R.id.work)
    protected void workClick(View view) {
        Intent intent = new Intent(getActivity(), Work.class);
        startActivity(intent);
    }

    private void doLogout() {
        PrefUtils.savePreference(getActivity(), USERNAME_PREF_KEY, "");
        PrefUtils.savePreference(getActivity(), PASSWORD_PREF_KEY, "");
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }

    void showDialogLogout() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        doLogout();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.logouttitle))
                .setPositiveButton(getString(R.string.button_ok), dialogClickListener)
                .setNegativeButton(getString(R.string.button_cancel), dialogClickListener).show();
    }

    @OnClick(R.id.sync)
    protected void syncClick(View view) {
        showLoading();
        SyncService.getInstance(getContext()).sync(new ApiService.CommonCallback() {
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        syncData();
                        //CommonUtils.showDialog(getContext(), getString(R.string.app_name), getString(R.string.sync_success));
                    }
                });
            }

            @Override
            public void onFail(String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        CommonUtils.showDialog(getContext(), getString(R.string.app_name), getString(R.string.sync_fail));
                    }
                });
            }
        });

    }

    protected void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getString(R.string.progress_bar_message));
        }
        else {
            mProgressDialog.setMessage(getString(R.string.progress_bar_message));
        }


        mProgressDialog.show();
    }

    protected void showLoading(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(message);
        }
        else {
            mProgressDialog.setMessage(message);
        }

        mProgressDialog.show();
    }

    protected void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    protected void showErrorDialog(String message) {
        CommonUtils.showErrorDialog(getContext(), message);
    }

    @OnClick(R.id.logout)
    protected void logoutClick(View view) {
        showDialogLogout();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void syncData() {
        this.realm = RealmController.with(this).getRealm();

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        loadListProduct();
    }

    private void loadListProduct() {
        hideLoading();
        showLoading(getString(R.string.load_products_message));
        String sUsername = mService.getUserCode();
        mService.loadListProduct(sUsername, "", new ApiService.GetListProduct() {
            @Override
            public void onSuccess(final List<Product> data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListCustomerTravel();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_product));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListCustomerTravel() {
        hideLoading();
        showLoading(getString(R.string.load_customers_message));
        String sUsername = mService.getUserCode();
        mService.loadListCustomerTravel(sUsername, new ApiService.GetListCustomerTravel() {
            @Override
            public void onSuccess(final List<Customer> data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListChannel();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_cus));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListChannel() {
        String sUsername = mService.getUserCode();
        mService.loadListChannel(sUsername, new ApiService.GetListChannel() {
            @Override
            public void onSuccess(final List<Channel> data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListGrpCus();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_cus));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListGrpCus() {
        String sUsername = mService.getUserCode();
        mService.loadListCusGrp(sUsername, new ApiService.GetListCusGrp() {
            @Override
            public void onSuccess(final List<CustomerGroup> data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListOrder();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_cus));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListOrder() {
        hideLoading();
        showLoading(getString(R.string.load_order_message));
        String sUsername = mService.getUserCode();
        mService.loadListOrder(sUsername, "2016/01/01", CommonUtils.getCurrentdate(), "01", "01", "01", "01", "", new ApiService.GetListOrder() {
            @Override
            public void onSuccess(final List<Orders> data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListTypeVisit();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_order));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListTypeVisit() {
        String sUsername = mService.getUserCode();
        mService.loadListTypeVisit(sUsername, new ApiService.GetListTypeVisit() {
            @Override
            public void onSuccess(final List<TypeVisit> data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListTypeOrder();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_order));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListTypeOrder() {
        String sUsername = mService.getUserCode();
        mService.loadListTypeOrder(sUsername, new ApiService.GetListTypeOrder() {
            @Override
            public void onSuccess(final List<TypeOrder> data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListStatusOrder();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_order));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListStatusOrder() {
        String sUsername = mService.getUserCode();
        mService.loadListStatusOrder(sUsername, new ApiService.GetListStatusOrder() {
            @Override
            public void onSuccess(final List<StatusOrder> data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListWareHouse();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_order));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListWareHouse() {
        String sUsername = mService.getUserCode();
        mService.loadListWareHouse(sUsername, new ApiService.GetListWareHouse() {
            @Override
            public void onSuccess(final List<WareHouse> data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListRoute();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_order));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListRoute() {
        hideLoading();
        showLoading(getString(R.string.load_route_message));
        String sUsername = mService.getUserCode();
        mService.loadListRoute(sUsername, new ApiService.GetListRoute() {
            @Override
            public void onSuccess(final List<Route> data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListNotesGroup();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_cus));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListNotesGroup() {
        String sUsername = mService.getUserCode();
        mService.loadNoteTypes(sUsername, new ApiService.GetListNoteType() {
            @Override
            public void onSuccess(final List<NoteType> data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            doAfterLoginCompleted();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_note_group));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    void doAfterLoginCompleted() {
        hideLoading();
        CommonUtils.showDialog(getContext(), getString(R.string.app_name), getString(R.string.sync_success));
    }
}
