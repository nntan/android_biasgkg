package com.luan.dms_management.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luan.dms_management.R;
import com.luan.dms_management.adapter.TravelAdapter;
import com.luan.dms_management.models.Channel;
import com.luan.dms_management.models.Customer;
import com.luan.dms_management.models.CustomerGroup;
import com.luan.dms_management.models.Route;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.GridSpacingItemDecoration;
import com.luan.dms_management.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import io.realm.Realm;

import static com.luan.dms_management.utils.Constant.SALEID_PREF_KEY;
import static com.luan.dms_management.utils.Constant.USERNAME_PREF_KEY;
import static com.luan.dms_management.utils.Constant.WORKING_PREF_KEY;

public class Travel extends BaseActivity {
    @BindView(R.id.recycler_view_travel)
    protected RecyclerView view;
    @BindView(R.id.ic_search_travel)
    protected TextView icSearch;
    @BindView(R.id.inputSearchTravel)
    protected EditText edtSearch;
    @BindView(R.id.travelView)
    protected LinearLayout layout;

    private ApiService mService;
    private TravelAdapter adapter;
    private Realm realm;
    private ArrayList<String> listRoute, listChannel, listCusGrp, listSort;
    private ArrayList<String> listRouteValue, listChannelValue, listCusGrpValue, listSortValue;
    private int posroute, poschannel, posgrp, possort;
    private String sroute, schannel, sgrpcus, ssort;
    private Dialog mFilterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.travel);
        ButterKnife.bind(this);
        CommonUtils.makeTextViewFont(this, icSearch);
        this.realm = RealmController.with(this).getRealm();
        mService = ApiService.getInstance(this);
        posroute = 0;
        poschannel = 0;
        posgrp = 0;
        possort = 0;

        sroute = "00";
        schannel = "00";
        sgrpcus = "00";
        ssort = "00";
        RealmController.with(this).refresh();

        loadRoute();
        loadGrp();
        loadChannel();
        loadSort();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(RealmController.with(this).getAllCustomer());
    }

    @OnEditorAction(R.id.inputSearchTravel)
    protected boolean search(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchCus();
            return true;
        }
        return false;
    }

    @OnTextChanged(R.id.inputSearchTravel)
    protected void onTextChanged(Editable editable) {
        if (editable.toString().equals("")) {
            loadData(RealmController.with(this).getAllCustomer());
            CommonUtils.hideSoftKeyboard(this, layout);
        } else {
            searchCus();
        }
    }

    private void searchCus() {
        //CommonUtils.hideSoftKeyboard(this, layout);
        //showLoading(getString(R.string.searching));
        String text = edtSearch.getText().toString();
        int saleid = Integer.parseInt(PrefUtils.getPreference(this, SALEID_PREF_KEY));
        String workdate = PrefUtils.getPreference(this, WORKING_PREF_KEY);
        mService.loadListCustomer(PrefUtils.getPreference(this, USERNAME_PREF_KEY), sroute, schannel, sgrpcus, saleid, text, workdate, new ApiService.GetListCustomer() {
            @Override
            public void onSuccess(final List<Customer> data) {
                Travel.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //hideLoading();
                            loadData(data);
                        } catch (Exception e) {
                            //hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Travel.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //hideLoading();
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

    private void loadData(List<Customer> data) {
        adapter = new TravelAdapter(this, data);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        view.setLayoutManager(mLayoutManager);
        view.addItemDecoration(new GridSpacingItemDecoration(1, CommonUtils.dpToPx(this, 0), true));
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    private void loadRoute() {
        listRoute = new ArrayList<>();
        listRouteValue = new ArrayList<>();
        String first = getString(R.string.route);
        listRoute.add(first);
        listRouteValue.add("00");
        for (Route route : RealmController.with(this).getAllRoute()) {
            listRoute.add(route.getRouteName());
            listRouteValue.add(route.getRouteCode());
        }
    }

    private void loadGrp() {
        listCusGrp = new ArrayList<>();
        listCusGrpValue = new ArrayList<>();
        String first = getString(R.string.grpcus);
        listCusGrp.add(first);
        listCusGrpValue.add("00");
        for (CustomerGroup group : RealmController.with(this).getAllCusGrp()) {
            listCusGrp.add(group.getGrpName());
            listCusGrpValue.add(group.getGrpCode());
        }
    }

    private void loadChannel() {
        listChannel = new ArrayList<>();
        listChannelValue = new ArrayList<>();
        String first = getString(R.string.custype);
        listChannel.add(first);
        listChannelValue.add("00");
        for (Channel channel : RealmController.with(this).getAllChannel()) {
            listChannel.add(channel.getChannelName());
            listChannelValue.add(channel.getChannelCode());
        }
    }

    private void loadSort() {
        listSort = new ArrayList<>();
        String first = getString(R.string.sort);
        listSort.add(first);
        listSort.add("Tên");
    }

    private void initFilterDialog() {
        if (mFilterDialog == null) {
            mFilterDialog = new Dialog(this);
            mFilterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mFilterDialog.setContentView(R.layout.dialog_filter);
            final Spinner route = (Spinner) mFilterDialog.findViewById(R.id.filterRoute);
            final Spinner sort = (Spinner) mFilterDialog.findViewById(R.id.filterSort);
            final Spinner typecus = (Spinner) mFilterDialog.findViewById(R.id.filterTypeCus);
            final Spinner grpcus = (Spinner) mFilterDialog.findViewById(R.id.filterGrpCus);
            final Button ok = (Button) mFilterDialog.findViewById(R.id.linkapi_ok);
            final ImageButton close = (ImageButton) mFilterDialog.findViewById(R.id.ib_close);
            final Button reset = (Button) mFilterDialog.findViewById(R.id.btn_reset);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    Travel.this, android.R.layout.simple_spinner_item, listRoute);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            route.setAdapter(adapter);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                    Travel.this, android.R.layout.simple_spinner_item, listCusGrp);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            grpcus.setAdapter(adapter1);

            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                    Travel.this, android.R.layout.simple_spinner_item, listChannel);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typecus.setAdapter(adapter2);

            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                    Travel.this, android.R.layout.simple_spinner_item, listSort);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sort.setAdapter(adapter3);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilterDialog.dismiss();
                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    route.setSelection(0);
                    sort.setSelection(0);
                    typecus.setSelection(0);
                    grpcus.setSelection(0);
                }
            });

            route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    posroute = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    posroute = 0;
                }
            });

            grpcus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    posgrp = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    posroute = 0;
                }
            });

            typecus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    poschannel = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    posroute = 0;
                }
            });

            sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    possort = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    posroute = 0;
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if (posroute > 0) {
                        sroute = listRouteValue.get(posroute);
                    //}
                    //if (posgrp > 0) {
                        sgrpcus = listRouteValue.get(posgrp);
                    //}
                    //if (poschannel > 0) {
                        schannel = listRouteValue.get(poschannel);
                    //}
                    //if (possort > 0) {
                        ssort = listRoute.get(possort);
                    //}
                    searchCus();
                    mFilterDialog.dismiss();
                }
            });
        }
    }

    private void showDialogFilter() {
        initFilterDialog();
        mFilterDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            showDialogFilter();
            return true;
        }

        if (id == R.id.action_addcus) {
            requestAddNewCustomer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestAddNewCustomer() {
        Intent intent = new Intent(Travel.this, AddNewCustomer.class);
        startActivity(intent);
    }
}
