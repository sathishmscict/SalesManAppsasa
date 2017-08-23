package com.salesmanapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesmanapp.R;
import com.salesmanapp.animation.FlipAnimation;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.pojo.OrderData;
import com.salesmanapp.session.SessionManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Satish Gadde on 19-08-2016.
 */
public class OrderSummaryAdapterRecyclerView extends RecyclerView.Adapter<OrderSummaryAdapterRecyclerView.MyViewHolder> {


    private final SessionManager sessionManager;
    private final HashMap<String, String> userDetails;

    private final dbhandler db;
    private final SQLiteDatabase sd;

    private Context context;
    ArrayList<OrderData> list_OrderData;
    LayoutInflater inflater;
    private String TAG = OrderSummaryAdapterRecyclerView.class.getSimpleName();
    private int topic_approval_status;
    private ProgressDialog pDialog;
    private long DELAY_MILLIS=2500;


    public OrderSummaryAdapterRecyclerView(Context context, ArrayList<OrderData> TestMasterData) {
        this.context = context;
        this.list_OrderData = TestMasterData;
        inflater = LayoutInflater.from(context);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        db = new dbhandler(context);
        sd = db.getWritableDatabase();


    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        private final TextView tvOrderID;
        private final TextView tvOrderDate;
        private final EditText edtEmployee;
        private final EditText edtServiceName;
        private final EditText edtServiceQuntity;
        private final EditText edtRate;
        private final EditText edtDiscountAmount;
        private final EditText edtNetAmount;
        private final EditText edtServiceDescr;

        public MyViewHolder(View itemView) {

            super(itemView);

            tvOrderID = (TextView) itemView.findViewById(R.id.tvOrderID);
            tvOrderDate = (TextView)itemView.findViewById(R.id.tvOrderDate);
            edtServiceName = (EditText) itemView.findViewById(R.id.edtServiceName);
            edtServiceQuntity = (EditText) itemView.findViewById(R.id.edtServiceQuntity);
            edtRate = (EditText) itemView.findViewById(R.id.edtRate);
            edtDiscountAmount = (EditText)itemView.findViewById(R.id.edtDiscountAmount);
            edtNetAmount = (EditText)itemView.findViewById(R.id.edtNetAmount);
            edtEmployee = (EditText)itemView.findViewById(R.id.edtEmployee);
            edtServiceDescr = (EditText)itemView.findViewById(R.id.edtServiceDescr);






        }

    }

    @Override
    public OrderSummaryAdapterRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {


        View view = inflater.inflate(R.layout.row_single_order, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        final OrderData od = list_OrderData.get(position);








        holder.tvOrderID.setText("Order Id : "+od.getOrderid());
        holder.tvOrderDate.setText(od.getDate());
        holder.edtServiceName.setText(od.getServiceid());
        holder.edtServiceQuntity.setText("\u20b9"+od.getQuantity());
        holder.edtRate.setText("\u20b9"+od.getRate());
        holder.edtDiscountAmount.setText("\u20b9"+od.getDiscountamount());
        holder.edtNetAmount.setText("\u20b9"+od.getNetamount());
        holder.edtEmployee.setText(od.getEmployeeid());
        holder.edtEmployee.setVisibility(View.GONE);
        if(od.getDescr().toString().equals(""))
        {
            holder.edtServiceDescr.setVisibility(View.GONE);
        }
        else
        {
            holder.edtServiceDescr.setVisibility(View.VISIBLE);

            holder.edtServiceDescr.setText(od.getDescr());
        }






        try {
            /*DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = originalFormat.parse("August 21, 2012");
            String formattedDate = targetFormat.format(date);  // 20120821*/

            //DateFormat originalFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd Hh:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date date = originalFormat.parse(od.getDate());
            String formattedDate = targetFormat.format(date);  // 20120821

            holder.tvOrderDate.setText(Html.fromHtml("DATE : <b>" + formattedDate + "</b>"));





        } catch (ParseException e) {
            e.printStackTrace();
        }
















    }


    @Override
    public int getItemCount() {
        return list_OrderData.size();
    }

    private void initFlip(ImageView iv) {

        FlipAnimation.create().with(iv)
                .setDuration(1000)
                .setRepeatCount(1)
                .start();


    }


}
