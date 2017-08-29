package com.salesmanapp.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.salesmanapp.R;
import com.salesmanapp.adapter.OrderSummaryAdapterRecyclerView;
import com.salesmanapp.app.MyApplication;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.helper.AllKeys;
import com.salesmanapp.pojo.OrderData;
import com.salesmanapp.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class AddServicesActivity extends AppCompatActivity {

    private String TAG = AddServicesActivity.class.getSimpleName();
    private dbhandler db;
    private Context context = this;
    private SQLiteDatabase sd;
    private SpotsDialog pDialog;
    private RecyclerView rv_services;
    private ArrayList<OrderData> listOrders = new ArrayList<OrderData>();
    private OrderSummaryAdapterRecyclerView adapter;
    private Spinner spnServices;
    private ArrayList<String> list_service_id = new ArrayList<String>();

    private ArrayList<String> list_service = new ArrayList<String>();
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        pDialog = new SpotsDialog(context);
        pDialog.setCancelable(false);


        db = new dbhandler(context);
        sd = db.getReadableDatabase();
        sd = db.getWritableDatabase();


        try {

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        } catch (Exception e) {
            Log.d("Error Actionbar", "" + e.getMessage());

        }


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                setDialogForService(false, 0);
            }
        });


        FillDataOnspinner(false);

        rv_services = (RecyclerView) findViewById(R.id.rv_services);

        LinearLayoutManager lManager = new LinearLayoutManager(context);
        rv_services.setLayoutManager(lManager);


        rv_services.addOnItemTouchListener(new dbhandler.RecyclerTouchListener(context, rv_services, new dbhandler.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                try {


                    //Get selected service info and fill on dialog for update


                    setDialogForService(true, position);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        getServiceDetailsFromServer();
        FillDataOnRecyclerView();


    }

    private void setDialogForService(boolean IsEditRecords, final int servicePos) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);


        dialog.setContentView(R.layout.dialog_services);

        TextView tvClose = (TextView) dialog.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FillDataOnRecyclerView();
                dialog.cancel();
                dialog.dismiss();
            }
        });

        spnServices = (Spinner) dialog.findViewById(R.id.spnServices);


        TextInputLayout edtServiceQuntityWrapper = (TextInputLayout) dialog.findViewById(R.id.edtServiceQuntityWrapper);
        TextInputLayout edtRateWrapper = (TextInputLayout) dialog.findViewById(R.id.edtRateWrapper);
        TextInputLayout edtDiscountAmountWrapper = (TextInputLayout) dialog.findViewById(R.id.edtDiscountAmountWrapper);
        TextInputLayout edtNetAmountWrapper = (TextInputLayout) dialog.findViewById(R.id.edtNetAmountWrapper);


        final EditText edtServiceQuntity = (EditText) dialog.findViewById(R.id.edtServiceQuntity);
        final EditText edtRate = (EditText) dialog.findViewById(R.id.edtRate);
        final EditText edtDiscountAmount = (EditText) dialog.findViewById(R.id.edtDiscountAmount);
        final EditText edtNetAmount = (EditText) dialog.findViewById(R.id.edtNetAmount);
        final EditText edtDescr = (EditText) dialog.findViewById(R.id.edtDescr);


        final TextView tvError = (TextView) dialog.findViewById(R.id.tvError);
        tvError.setVisibility(View.GONE);


        edtServiceQuntity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.equals("")) {
                    if (!edtRate.getText().toString().equals("")) {
                        Double rate = 0.0;
                        try {
                            rate = Double.parseDouble(edtRate.getText().toString());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        Double qunatity = 0.0;
                        try {
                            qunatity = Double.parseDouble(edtServiceQuntity.getText().toString());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        Double discountamount = 0.0;
                        if (!edtDiscountAmount.getText().toString().equals("")) {
                            discountamount = Double.parseDouble(edtDiscountAmount.getText().toString());

                        } else {
                            discountamount = 0.0;
                        }

                        Double netamount = (rate * qunatity) - discountamount;

                        edtNetAmount.setText(String.valueOf(netamount));
                        edtNetAmount.setText(String.format("%.02f", netamount));
                        float f = 2.3455f;
                        String test = String.format("%.02f", f);


                    }

                } else {
                    edtNetAmount.setText("0");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.equals("")) {
                    if (!edtServiceQuntity.getText().toString().equals("")) {
                        Double rate = 0.0;
                        try {
                            rate = Double.parseDouble(edtRate.getText().toString());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        Double qunatity = 0.0;
                        try {
                            qunatity = Double.parseDouble(edtServiceQuntity.getText().toString());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        Double discountamount = 0.0;
                        if (!edtDiscountAmount.getText().toString().equals("")) {
                            try {
                                discountamount = Double.parseDouble(edtDiscountAmount.getText().toString());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();

                            }

                        }

                        Double netamount = (rate * qunatity) - discountamount;


                        edtNetAmount.setText(String.valueOf(netamount));
                        edtNetAmount.setText(String.format("%.02f", netamount));
                    }

                } else {
                    edtNetAmount.setText("0");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtDiscountAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {

                    if (!edtServiceQuntity.getText().toString().equals("")) {


                        Double rate = 0.0;
                        if (!edtRate.getText().toString().equals("")) {
                            try {
                                rate = Double.parseDouble(edtRate.getText().toString());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }


                        Double qunatity = 0.0;
                        if (!edtServiceQuntity.getText().toString().equals("")) {
                            try {
                                qunatity = Double.parseDouble(edtServiceQuntity.getText().toString());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }


                        Double discountamount = 0.0;
                        if (!edtDiscountAmount.getText().toString().equals("")) {
                            try {
                                discountamount = Double.parseDouble(edtDiscountAmount.getText().toString());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }

                        Double netamount = (rate * qunatity) - discountamount;


                        edtNetAmount.setText(String.valueOf(netamount));
                        edtNetAmount.setText(String.format("%.02f", netamount));
                    }
                } else {
                    edtNetAmount.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        final Button btnAddService = (Button) dialog.findViewById(R.id.btnAddService);


        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isError = false;


                String error = "";
                if (spnServices.getSelectedItemPosition() == 0) {
                    isError = true;
                    error = "Please select service";

                }

                if (edtServiceQuntity.getText().toString().equals("")) {
                    error = error + "\n Enter Quantity";
                }
                if (edtRate.getText().toString().equals("")) {
                    error = error + "\n Enter Rate";


                }


                if (isError == false) {


                    try {


                        ContentValues cv = new ContentValues();
                        cv.put(dbhandler.ORDER_SERVICEID, list_service_id.get(spnServices.getSelectedItemPosition()));
                        cv.put(dbhandler.ORDER_QUANTITY, edtServiceQuntity.getText().toString());
                        cv.put(dbhandler.ORDER_RATE, edtRate.getText().toString());

                        Double discountamount = 0.0;
                        if (!edtDiscountAmount.getText().toString().equals("")) {
                            discountamount = Double.parseDouble(edtDiscountAmount.getText().toString());

                        } else {
                            discountamount = 0.0;
                        }


                        cv.put(dbhandler.ORDER_DISCOUNT_AMOUNT, discountamount.intValue());
                        cv.put(dbhandler.ORDER_NET_AMOUNT, edtNetAmount.getText().toString());
                        cv.put(dbhandler.ORDER_CLIENT_ID, userDetails.get(SessionManager.KEY_CLIENTID));
                        cv.put(dbhandler.ORDER_EMPLOYEE_ID, userDetails.get(SessionManager.KEY_EMP_ID));
                        cv.put(dbhandler.ORDER_DATE, dbhandler.getDateTime());
                        cv.put(dbhandler.ORDER_DESCR, edtDescr.getText().toString());
                        cv.put(dbhandler.CLIENT_DEVICE_TYPE, "and");
                        cv.put(dbhandler.SYNC_STATUS, "0");

                        Log.d(TAG, "OrderMaster Insert Data : " + cv.toString());

                        if (btnAddService.getText().toString().toLowerCase().equals("update service")) {

                            sd.update(dbhandler.TABLE_ORDER_MASTER, cv, "" + dbhandler.ORDER_CLIENT_ID + "='" + userDetails.get(SessionManager.KEY_CLIENTID) + "' and " + dbhandler.ORDER_ID + "='" + listOrders.get(servicePos).getOrderid() + "'", null);
                        } else {


                            Cursor cur_max_orderid = sd.rawQuery("SELECT * FROM " + dbhandler.TABLE_ORDER_MASTER + " where " + dbhandler.ORDER_ID + " like '%" + AllKeys.KEYWORD_ORDER + userDetails.get(SessionManager.KEY_EMP_UNIQUE_CODE) + "%'", null);
                            //cur_max_orderid.moveToFirst();
                            int max_orderid = cur_max_orderid.getCount();
                            ++max_orderid;
                            Log.d("Max Id By OrderMst : ", "" + max_orderid);


                            cv.put(dbhandler.ORDER_ID, AllKeys.KEYWORD_ORDER + userDetails.get(SessionManager.KEY_EMP_UNIQUE_CODE) + max_orderid);


                            sd.insert(dbhandler.TABLE_ORDER_MASTER, null, cv);

                        }


                        Toast.makeText(context, "Record has been saved", Toast.LENGTH_SHORT).show();


                        edtDiscountAmount.setText("");
                        edtServiceQuntity.setText("");
                        edtDescr.setText("");
                        edtRate.setText("");
                        edtNetAmount.setText("");
                        spnServices.setSelection(0);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Try again..." + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    tvError.setText(error);
                    tvError.setVisibility(View.VISIBLE);
                }


            }
        });


        FillDataOnspinner(true);

        if (IsEditRecords == true) {

            edtDescr.setText(listOrders.get(servicePos).getDescr());
            edtRate.setText(listOrders.get(servicePos).getRate());
            edtDiscountAmount.setText(listOrders.get(servicePos).getDiscountamount());
            edtNetAmount.setText(listOrders.get(servicePos).getNetamount());
            edtServiceQuntity.setText(listOrders.get(servicePos).getQuantity());
            spnServices.setSelection(list_service.indexOf(listOrders.get(servicePos).getServiceid()));

            btnAddService.setText("update service");
        }

        dialog.getWindow().setLayout(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    private void FillDataOnspinner(boolean status) {


        String query = "select * from " + dbhandler.TABLE_SERVICES + "  order by " + dbhandler.SERVICE_NAME + "";
        Log.d(TAG, "Query  : ");

        Cursor cc = sd.rawQuery(query, null);
        Log.d(TAG, "Total 0 Records found");
        list_service_id.clear();
        list_service.clear();

        list_service_id.add("0");
        list_service.add("Select Service");


        if (cc.getCount() > 0) {
            while (cc.moveToNext()) {
                list_service.add(cc.getString(cc.getColumnIndex(dbhandler.SERVICE_NAME)));

                list_service_id.add(cc.getString(cc.getColumnIndex(dbhandler.SERVICE_ID)));

            }

        }

        if (status == true) {

            //Set Client details to adapter
            ArrayAdapter<String> adater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_service);
            spnServices.setAdapter(adater);

        }


    }

    private void FillDataOnRecyclerView() {
        try {
            String query = "Select * from " + dbhandler.TABLE_ORDER_MASTER + " where " + dbhandler.CLIENT_ID + " ='" + userDetails.get(SessionManager.KEY_CLIENTID) + "'";
            Log.d(TAG, "Query   : " + query);

            Cursor cc = sd.rawQuery(query, null);
            listOrders.clear();
            if (cc.getCount() > 0) {
                while (cc.moveToNext()) {
                    //OrderData(String orderid, String serviceid, String quantity, String rate, String discountamount, String netamount, String cleintid, String employeeid, String date) {

                    OrderData od = new OrderData(cc.getString(cc.getColumnIndex(dbhandler.ORDER_ID)), list_service.get(list_service_id.indexOf(cc.getString(cc.getColumnIndex(dbhandler.ORDER_SERVICEID)))), cc.getString(cc.getColumnIndex(dbhandler.ORDER_QUANTITY)), cc.getString(cc.getColumnIndex(dbhandler.ORDER_RATE)), cc.getString(cc.getColumnIndex(dbhandler.ORDER_DISCOUNT_AMOUNT)), cc.getString(cc.getColumnIndex(dbhandler.ORDER_NET_AMOUNT)), cc.getString(cc.getColumnIndex(dbhandler.ORDER_CLIENT_ID)), cc.getString(cc.getColumnIndex(dbhandler.ORDER_EMPLOYEE_ID)), cc.getString(cc.getColumnIndex(dbhandler.ORDER_DATE)), cc.getString(cc.getColumnIndex(dbhandler.CLIENT_DEVICE_TYPE)), cc.getString(cc.getColumnIndex(dbhandler.ORDER_DESCR)));
                    listOrders.add(od);
                }

                adapter = new OrderSummaryAdapterRecyclerView(context, listOrders);
                rv_services.setAdapter(adapter);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //onCreate Completed

    private void getServiceDetailsFromServer() {

        String url = AllKeys.WEBSITE + "ViewServiceMst?type=service";
        Log.d(TAG, "URL ViewServiceMst " + url);
        JsonObjectRequest reuqest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response ViewServiceMst  : " + response);
                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false) {
                        if (record_status == true) {
                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_LOGINDATA);
                            sd.delete(dbhandler.TABLE_SERVICES, null, null);
                            for (int i = 0; i < arr.length(); i++) {

                                JSONObject c = arr.getJSONObject(i);
                                ContentValues cv = new ContentValues();
                                cv.put(dbhandler.SERVICE_ID, c.getString(AllKeys.TAG_SERVICEID));
                                cv.put(dbhandler.SERVICE_NAME, c.getString(AllKeys.TAG_SERVICENAME));
                                sd.insert(dbhandler.TABLE_SERVICES, null, cv);

                            }


                            FillDataOnRecyclerView();
                            hideDialog();


                        }

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(context, "Try again...", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, "ViewServiceMst " + error.getMessage());
                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {

                    getServiceDetailsFromServer();
                }


            }
        });
        MyApplication.getInstance().addToRequestQueue(reuqest);


    }


    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.cancel();
            pDialog.dismiss();


        }
    }

    private void showDialog() {
        if (pDialog.isShowing()) {
            pDialog.show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(context, DashBoardActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_left, R.anim.slide_right);


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(context, DashBoardActivity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

    }


}
