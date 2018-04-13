package com.luan.dms_management.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.adapter.OrderAdapter;
import com.luan.dms_management.models.Orders;
import com.luan.dms_management.models.Route;
import com.luan.dms_management.models.StatusOrder;
import com.luan.dms_management.models.TypeOrder;
import com.luan.dms_management.models.TypeVisit;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.GridSpacingItemDecoration;
import com.luan.dms_management.utils.PrefUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

import static com.luan.dms_management.utils.Constant.USERNAME_PREF_KEY;

public class OrderActivity extends BaseActivity {
    @BindView(R.id.recycler_view_order)
    protected RecyclerView view;
    @BindView(R.id.ic_search)
    protected TextView icSearch;
    @BindView(R.id.inputOrderSearch)
    protected EditText edtSearch;
    @BindView(R.id.orderView)
    protected LinearLayout layout;

    private OrderAdapter adapter;
    private ApiService mService;
    private String fromdate, todate, sdocstatus, svisitstatus, sordertype, sroute, free;
    private int posdocstatus, posvisitstatus, posordertype, posroute, dayFrom, monthFrom, yearFrom, dayTo, monthTo, yearTo;
    private DatePicker datePicker;
    private Calendar calendar;

    private ArrayList<String> listRoute, listDoc, listOrderType, listVisit;
    private ArrayList<String> listRouteValue, listDocValue, listOrderTypeValue, listVisitValue;
    private SetDateCallback setDateCallback;

    private interface SetDateCallback {
        void onSetFrom(int day, int month, int year);

        void onSetTo(int day, int month, int year);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.order);
        ButterKnife.bind(this);
        CommonUtils.makeTextViewFont(this, icSearch);
        RealmController.with(this).refresh();
        mService = ApiService.getInstance(this);
        setUpData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(RealmController.with(this).getAllOrder());
    }

    private void setUpData() {

        calendar = Calendar.getInstance();

        yearFrom = 2017;
        monthFrom = 9;
        dayFrom = 1;

        yearTo = calendar.get(Calendar.YEAR);
        monthTo = calendar.get(Calendar.MONTH);
        dayTo = calendar.get(Calendar.DAY_OF_MONTH);

        fromdate = yearFrom + "/" + (monthFrom + 1) + "/" + dayFrom;
        todate = yearTo + "/" + (monthTo + 1) + "/" + dayTo;
        sdocstatus = "01";
        svisitstatus = "01";
        sordertype = "01";
        sroute = "01";
        free = "";

        loadDocStatus();
        loadOrderType();
        loadVisitStatus();
        loadRoute();
    }

    private void loadData(List<Orders> ordersList) {
        adapter = new OrderAdapter(OrderActivity.this, ordersList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(OrderActivity.this, 1);
        view.setLayoutManager(mLayoutManager);
        view.addItemDecoration(new GridSpacingItemDecoration(1, CommonUtils.dpToPx(OrderActivity.this, 0), true));
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    private void searchOrder() {
        free = edtSearch.getText().toString();
        mService.loadListOrder(PrefUtils.getPreference(this,
                USERNAME_PREF_KEY), fromdate, todate,
                sdocstatus, svisitstatus, sordertype, sroute, free, new ApiService.GetListOrder() {
                    @Override
                    public void onSuccess(final List<Orders> data) {
                        OrderActivity.this.runOnUiThread(new Runnable() {
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
                        OrderActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //hideLoading();
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

    private void loadDocStatus() {
        listDoc = new ArrayList<>();
        listDocValue = new ArrayList<>();
        String first = getString(R.string.docstatus);
        listDoc.add(first);
        listDocValue.add("00");
        for (StatusOrder order : RealmController.with(this).getStatusOrder()) {
            listDoc.add(order.getStatusName());
            listDocValue.add(order.getStatusCode());
        }
    }

    private void loadVisitStatus() {
        listVisit = new ArrayList<>();
        listVisitValue = new ArrayList<>();
        String first = getString(R.string.visitstatus);
        listVisit.add(first);
        listVisitValue.add("00");
        for (TypeVisit typeVisit : RealmController.with(this).getTypeVisit()) {
            listVisit.add(typeVisit.getStatusName());
            listVisitValue.add(typeVisit.getStatusCode());
        }
    }

    private void loadOrderType() {
        listOrderType = new ArrayList<>();
        listOrderTypeValue = new ArrayList<>();
        String first = getString(R.string.typeorder);
        listOrderType.add(first);
        listOrderTypeValue.add("00");
        for (TypeOrder typeOrder : RealmController.with(this).getTypeOrder()) {
            listOrderType.add(typeOrder.getTypeName());
            listOrderTypeValue.add(typeOrder.getTypeCode());
        }
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

    private void showDialogFilter() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter_order);
        final Spinner docstatus = (Spinner) dialog.findViewById(R.id.filterDocStatus);
        final Spinner visit = (Spinner) dialog.findViewById(R.id.filterVisitStatus);
        final Spinner typeorder = (Spinner) dialog.findViewById(R.id.filterOrderType);
        final Spinner route = (Spinner) dialog.findViewById(R.id.filterOrderRoute);

        final Button ok = (Button) dialog.findViewById(R.id.linkapi_ok);
        final ImageButton close = (ImageButton) dialog.findViewById(R.id.ib_close);
        final Button reset = (Button) dialog.findViewById(R.id.btn_reset);

        final LinearLayout dialogfromdate = (LinearLayout) dialog.findViewById(R.id.filterFromdate);
        final TextView dateFrom = (TextView) dialog.findViewById(R.id.txtfilterFromdate);
        final TextView icFrom = (TextView) dialog.findViewById(R.id.ictriangleFromdate);

        final LinearLayout dialogtodate = (LinearLayout) dialog.findViewById(R.id.filterTodate);
        final TextView dateTo = (TextView) dialog.findViewById(R.id.txtfilterTodate);
        final TextView icTo = (TextView) dialog.findViewById(R.id.ictriangleTodate);

        CommonUtils.makeTextViewFont(OrderActivity.this, icFrom);
        CommonUtils.makeTextViewFont(OrderActivity.this, icTo);

        dateFrom.setText(fromdate);
        dateTo.setText(todate);

        setDateCallback = new SetDateCallback() {
            @Override
            public void onSetFrom(int day, int month, int year) {
                dateFrom.setText(year + "/" + month + "/" + day);
            }

            @Override
            public void onSetTo(int day, int month, int year) {
                dateTo.setText(year + "/" + month + "/" + day);
            }
        };

        dialogfromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(111);
            }
        });

        dialogtodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(222);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                OrderActivity.this, android.R.layout.simple_spinner_item, listRoute);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        route.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                OrderActivity.this, android.R.layout.simple_spinner_item, listDoc);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        docstatus.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                OrderActivity.this, android.R.layout.simple_spinner_item, listVisit);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        visit.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                OrderActivity.this, android.R.layout.simple_spinner_item, listOrderType);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeorder.setAdapter(adapter3);

        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                route.setSelection(0);
                docstatus.setSelection(0);
                visit.setSelection(0);
                typeorder.setSelection(0);
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

        docstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posdocstatus = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                posdocstatus = 0;
            }
        });

        visit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posvisitstatus = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                posvisitstatus = 0;
            }
        });

        typeorder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posordertype = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                posordertype = 0;
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posdocstatus > 1) {
                    sdocstatus = listDocValue.get(posdocstatus);
                } else {
                    sdocstatus = listDocValue.get(1);
                }
                if (posvisitstatus > 1) {
                    svisitstatus = listVisitValue.get(posvisitstatus);
                } else {
                    svisitstatus = listVisitValue.get(1);
                }
                if (posordertype > 1) {
                    sordertype = listOrderTypeValue.get(posordertype);
                } else {
                    sordertype = listOrderTypeValue.get(1);
                }
                if (posroute > 1) {
                    sroute = listRouteValue.get(posroute);
                } else {
                    sroute = listRouteValue.get(1);
                }
                fromdate = dateFrom.getText().toString();
                todate = dateTo.getText().toString();
                searchOrder();
                dialog.dismiss();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 111) {
            return new DatePickerDialog(this,
                    dateFromListener, yearFrom, monthFrom, dayFrom);
        }
        if (id == 222) {
            return new DatePickerDialog(this,
                    dateToListener, yearTo, monthTo, dayTo);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateFromListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setDateCallback.onSetFrom(dayOfMonth, month + 1, year);
        }
    };

    private DatePickerDialog.OnDateSetListener dateToListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setDateCallback.onSetTo(dayOfMonth, month + 1, year);
        }
    };


    @OnEditorAction(R.id.inputOrderSearch)
    protected boolean search(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchOrder();
            return true;
        }
        return false;
    }

    @OnTextChanged(R.id.inputOrderSearch)
    protected void onTextChanged(Editable editable) {
        if (editable.toString().equals("")) {
            loadData(RealmController.with(this).getAllOrder());
            CommonUtils.hideSoftKeyboard(this, layout);
        } else {
            searchOrder();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        menu.getItem(1).setVisible(false);
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

        return super.onOptionsItemSelected(item);
    }
}
