package com.luan.dms_management.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luan.dms_management.R;
import com.luan.dms_management.activities.AddProduct;
import com.luan.dms_management.activities.Checkin;
import com.luan.dms_management.adapter.ProductChekinAdapter;
import com.luan.dms_management.models.InventoryProduct;
import com.luan.dms_management.models.NormalProduct;
import com.luan.dms_management.models.Product;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.service.SyncService;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.GetQRCodeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InventoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final int ADD_NEW_PRODUCT = 999;
    private static int GET_QRCODE_REQUEST_CODE = 888;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.lv_checkin_inventory)
    protected ListView listView;
    @BindView(R.id.btnAddPro)
    protected LinearLayout btnAddPro;
    @BindView(R.id.btnSaveDoc)
    protected LinearLayout btnSaveDoc;
    @BindView(R.id.icAddPro)
    protected TextView icAddPro;
    @BindView(R.id.icQRCode)
    protected TextView icQRCode;
    @BindView(R.id.icSaveDoc)
    protected TextView icSaveDoc;
    @BindView(R.id.progressBar)
    protected ProgressBar progressBar;

    private ProductChekinAdapter adapter;
    private LinearLayout header;

    private OnFragmentInteractionListener mListener;

    private List<NormalProduct> mRootProduct;

    public InventoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventoryFragment newInstance(String param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        header = (LinearLayout) inflater.inflate(R.layout.header_inventory_checkin, listView, false);
        ButterKnife.bind(this, view);
        listView.addHeaderView(header);
        CommonUtils.makeTextViewFont(getActivity(), icAddPro);
        CommonUtils.makeTextViewFont(getActivity(), icQRCode);
        CommonUtils.makeTextViewFont(getActivity(), icSaveDoc);
        setupUI();
        initData();
        return view;
    }

    private void initData() {
        String custCode = BasicService.currentCus.getCardCode();
        String userCode = ApiService.getInstance(getContext()).getUserCode();

        progressBar.setVisibility(View.GONE);
        mRootProduct = new ArrayList<>();
        loadData(mRootProduct);

//        ApiService.getInstance(getContext()).loadProductsFromCustomer(custCode, userCode, new ApiService.GetListNormalProduct() {
//            @Override
//            public void onSuccess(final List<NormalProduct> data) {
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        mRootProduct = data;
//                        loadData(data);
//                    }
//                });
//            }
//
//            @Override
//            public void onFail(String error) {
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        final AlertDialog dialog = new AlertDialog.Builder(getContext())
//                                .setTitle(R.string.app_name)
//                                .setMessage(R.string.get_products_from_customer_fail)
//                                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                    }
//                                }).create();
//                        if (!dialog.isShowing()) {
//                            dialog.show();
//                        }
//                    }
//                });
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btnAddPro)
    protected void btnAddProClick() {
        BasicService.currentProductList = mRootProduct;
        Intent intent = new Intent(getActivity(), AddProduct.class);
        startActivityForResult(intent, ADD_NEW_PRODUCT);
    }

    @OnClick(R.id.btnScanQR)
    protected void btnAddProQRClick() {
        Intent intent = new Intent(getActivity(), GetQRCodeActivity.class);
        startActivityForResult(intent, GET_QRCODE_REQUEST_CODE);
    }

    @OnClick(R.id.btnSaveDoc)
    protected void btnSaveDocClick() {
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.app_name)
                .setMessage(R.string.checkin_savedocs)
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doSaveDocs();
                        dialogInterface.dismiss();
                    }
                }).create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void doSaveDocs() {
        List<NormalProduct> changes = adapter.getChangedProducts(mRootProduct);
        String custCode = BasicService.currentCus.getCardCode();
        String userCode = ApiService.getInstance(getContext()).getUserCode();
        for (NormalProduct product : changes) {
            SyncService.getInstance(getContext()).offlineCheckinInStockAdd(custCode, userCode, product.getItemCode(), product.getBarcode(), product.getEven() + "", product.getRetail() + "", "");
        }
    }

    int mCurrentQRCodeIndex;
    NormalProduct mCurrentQRProduct;
    private void loadData(List<NormalProduct> list) {
        adapter = new ProductChekinAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        adapter.setOnRequestBarCodeScannerListener(new ProductChekinAdapter.OnRequestBarCodeScannerListener() {
            @Override
            public void onRequestBarCode(NormalProduct product, int index) {
                mCurrentQRCodeIndex = index;
                mCurrentQRProduct = product;

                Intent intent = new Intent(getActivity(), GetQRCodeActivity.class);
                startActivityForResult(intent, GET_QRCODE_REQUEST_CODE);
            }
        });
    }

    private void setupUI() {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NEW_PRODUCT && resultCode == RESULT_OK) {
            adapter.addNewProduct(BasicService.justAddedProduct);
        }

        if (requestCode == GET_QRCODE_REQUEST_CODE && resultCode == RESULT_OK) {
            String code = data.getStringExtra("result");
            final String codeProduct = code.split("/")[0];
            final String codeExpDate = code.split("/")[1];

            showLoading();
            ApiService.getInstance(getContext()).getInventoryProduct(codeProduct, new ApiService.GetInventoryProductCallback() {
                @Override
                public void onSuccess(final InventoryProduct data) {
                    Handler handler =new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading();
                            adapter.addProduct(data);
                        }
                    });
                }

                @Override
                public void onFail(String error) {
                    Handler handler =new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading();
                            List<Product> products = RealmController.getInstance().getAllProduct();
                            for (Product product : products) {
                                if (product.getBarcode().equalsIgnoreCase(codeProduct)) {
                                    adapter.addProduct(product, codeExpDate);
                                    return;
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    ProgressDialog mProgressDialog;
    protected void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getString(R.string.progress_bar_message));
        } else {
            mProgressDialog.setMessage(getString(R.string.progress_bar_message));
        }


        mProgressDialog.show();
    }

    protected void showLoading(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(message);
        } else {
            mProgressDialog.setMessage(message);
        }

        mProgressDialog.show();
    }

    protected void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
