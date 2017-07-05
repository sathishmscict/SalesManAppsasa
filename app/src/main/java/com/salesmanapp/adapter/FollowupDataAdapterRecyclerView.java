package com.salesmanapp.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.salesmanapp.activity.AddFollowupActivity;
import com.salesmanapp.activity.AddServicesActivity;
import com.salesmanapp.activity.ContactUsActivity;
import com.salesmanapp.activity.DashBoardActivity;
import com.salesmanapp.R;
import com.salesmanapp.activity.FollowupResponseActivity;
import com.salesmanapp.activity.ViewClientAndFollwupDataActivity;
import com.salesmanapp.animation.FlipAnimation;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.helper.AllKeys;
import com.salesmanapp.pojo.FollowupData;
import com.salesmanapp.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Satish Gadde on 19-08-2016.
 */
public class FollowupDataAdapterRecyclerView extends RecyclerView.Adapter<FollowupDataAdapterRecyclerView.MyViewHolder> {


    private final SessionManager sessionManager;
    private final HashMap<String, String> userDetails;
    private final Activity activity;
    private final dbhandler db;
    private final SQLiteDatabase sd;
    private final String displayType;

    private Context context;
    ArrayList<FollowupData> list_FollowupData;
    LayoutInflater inflater;
    private String TAG = FollowupDataAdapterRecyclerView.class.getSimpleName();
    private int topic_approval_status;
    private ProgressDialog pDialog;
    private long DELAY_MILLIS = 1500;

    private ArrayList<FollowupData> list_followups = new ArrayList<FollowupData>();


    public FollowupDataAdapterRecyclerView(Context context, ArrayList<FollowupData> TestMasterData, Activity activity, String displayType) {
        this.context = context;
        this.list_FollowupData = TestMasterData;
        inflater = LayoutInflater.from(context);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();
        this.activity = activity;

        db = new dbhandler(context);
        sd = db.getWritableDatabase();

        this.displayType = displayType;


    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        private final EditText edtName;

        private final EditText edtNote;

        private final ImageView ivLocation;
        private final ImageView ivCall;
        private final ImageView ivServices;
        private final ImageView ivEdit;
        private final ImageView ivDelete;
        private final ImageView ivShow;
        private final EditText edtFollowupDate;
        private final EditText edtFollowupTime;
        private final EditText edtCompnayName;
        private final TextInputLayout edtFollowupDateWrapper;
        private final LinearLayout llMenu;
        private final ImageView ivStatus;
        private final EditText edtScheduleReason;

        public MyViewHolder(View itemView) {

            super(itemView);

            edtName = (EditText) itemView.findViewById(R.id.edtName);
            edtNote = (EditText) itemView.findViewById(R.id.edtNote);
            edtFollowupDate = (EditText) itemView.findViewById(R.id.edtFollowupDate);
            edtFollowupTime = (EditText) itemView.findViewById(R.id.edtFollowupTime);
            edtCompnayName = (EditText) itemView.findViewById(R.id.edtCompnayName);
            edtFollowupDateWrapper = (TextInputLayout) itemView.findViewById(R.id.edtFollowupDateWrapper);

            edtScheduleReason = (EditText) itemView.findViewById(R.id.edtScheduleReason);


            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);


            llMenu = (LinearLayout) itemView.findViewById(R.id.llMenu);


            ivLocation = (ImageView) itemView.findViewById(R.id.ivLocation);
            ivCall = (ImageView) itemView.findViewById(R.id.ivCall);
            ivServices = (ImageView) itemView.findViewById(R.id.ivServices);
            ivEdit = (ImageView) itemView.findViewById(R.id.ivEdit);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            ivShow = (ImageView) itemView.findViewById(R.id.ivShow);


        }

    }

    @Override
    public FollowupDataAdapterRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View view;

        if (displayType.equals("followup") || displayType.equals("dashboard")) {

            view = inflater.inflate(R.layout.row_single_followup, parent, false);
        } else {
            view = inflater.inflate(R.layout.row_single_followup_for_other, parent, false);

        }


        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        final FollowupData fd = list_FollowupData.get(position);


        holder.edtName.setText(fd.getClientname());
        holder.edtNote.setText(fd.getFollowupnote());

        holder.edtFollowupDate.setText(fd.getFollowupdate());
        holder.edtFollowupTime.setText(fd.getFollowuptime());


        holder.edtCompnayName.setText(fd.getCompanyname());

        holder.edtScheduleReason.setText(fd.getFollowup_reason());


        if (fd.getFollowup_reason().equals("")) {
            holder.edtScheduleReason.setVisibility(View.GONE);
        } else {

            holder.edtScheduleReason.setVisibility(View.VISIBLE);
        }


        holder.ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.setFollowupId(fd.getFollowupid());

                Intent intent = new Intent(context, FollowupResponseActivity.class);
                context.startActivity(intent);


                Toast.makeText(activity, "Status   : " + fd.getFollowupstatus(), Toast.LENGTH_SHORT).show();
            }
        });

        //Toast.makeText(activity, "Status   : " + fd.getFollowupstatus(), Toast.LENGTH_SHORT).show();
        if (fd.getFollowupstatus().equals(String.valueOf(AllKeys.YES))) {
            holder.ivStatus.setVisibility(View.VISIBLE);
            holder.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_yes));

        } else if (fd.getFollowupstatus().equals(String.valueOf(AllKeys.CANCEL))) {
            holder.ivStatus.setVisibility(View.VISIBLE);
            holder.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_cancel));

        } else if (fd.getFollowupstatus().equals(String.valueOf(AllKeys.SCHEDULE))) {
            holder.ivStatus.setVisibility(View.VISIBLE);
            holder.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_schedule));

        } else {

            holder.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_pending));

            holder.ivStatus.setVisibility(View.VISIBLE);


        }

        if (displayType.equals("dashboard")) {
            holder.edtFollowupDateWrapper.setVisibility(View.GONE);

        } else {
            holder.edtFollowupDateWrapper.setVisibility(View.VISIBLE);

        }

/*
        if(displayType.equals("dialog") || displayType.equals("followup"))
        {
            //holder.edtCompnayName.setHint("Followup Date");
            holder.edtCompnayName.setHint(context.getString(R.string.str_followup_date));
            holder.edtCompnayName.setText(fd.getFollowupdate());



            holder.edtFollowupDate.setVisibility(View.GONE);
            holder.edtName.setVisibility(View.GONE);
            holder.llMenu.setVisibility(View.GONE);

        }
        else
        {
            holder.edtCompnayName.setHint(context.getString(R.string.str_company_name));

        }*/


        holder.ivServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initFlip(holder.ivServices);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        sessionManager.setClientid(fd.getClientid());
                        Intent intent = new Intent(context, AddServicesActivity.class);
                        context.startActivity(intent);


                    }
                }, DELAY_MILLIS);

            }
        });


        holder.ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initFlip(holder.ivLocation);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            sessionManager.setGPSLocations(fd.getLattitude(), fd.getLongtitude(), "");


                            Toast.makeText(activity, "Lattitude : " + fd.getLattitude() + " Longtitude : " + fd.getLongtitude(), Toast.LENGTH_SHORT).show();

                            sessionManager.setGPSLocations("21.2049887", "72.8385114", fd.getClientname());

                            Intent intent = new Intent(context, ContactUsActivity.class);
                            context.startActivity(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, DELAY_MILLIS);
            }
        });


        holder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initFlip(holder.ivCall);

                new Handler().postDelayed(new Runnable() {


        /*      Showing splash screen with a timer. This will be useful when you
             want to show case your app logo / company*/


                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity


                        Dexter.withActivity(activity)
                                .withPermission(Manifest.permission.READ_SMS)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {

                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91" + fd.getMoibleno1()));
                                        context.startActivity(intent);


                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                                }).check();


                    }
                }, DELAY_MILLIS);


            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initFlip(holder.ivEdit);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //        Toast.makeText(activity, "Edited", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, AddFollowupActivity.class);
                        intent.putExtra(dbhandler.FOLLOWUP_ID, Integer.parseInt(fd.getFollowupid()));
                        intent.putExtra(dbhandler.CLIENT_ID, fd.getClientid());
                        intent.putExtra(dbhandler.CLIENT_NAME, fd.getClientname());

                        intent.putExtra(dbhandler.FOLLOWUP_DESCR, fd.getFollowupnote());

                        intent.putExtra(dbhandler.FOLLOWUP_DATE, fd.getFollowupdate());
                        intent.putExtra(dbhandler.FOLLOWUP_TIME, fd.getFollowuptime());

                        intent.putExtra(dbhandler.CLIENT_DEVICE_TYPE, fd.getDevicetype());

                        intent.putExtra(dbhandler.FOLLOWUP_STATUS, fd.getFollowupstatus());

                        context.startActivity(intent);
                    }
                }, DELAY_MILLIS);


            }
        });


        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initFlip(holder.ivDelete);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        sd.delete(dbhandler.TABLE_FOLLOWUP_MASTER, "" + dbhandler.CLIENT_ID + "='" + fd.getClientid() + "' and " + dbhandler.FOLLOWUP_ID + "=" + fd.getFollowupid() + "", null);

                        Toast.makeText(activity, fd.getCompanyname() + "  has been deleted", Toast.LENGTH_SHORT).show();
                        list_FollowupData.remove(list_FollowupData.indexOf(fd));


                        notifyDataSetChanged();


                        //notifyItemRemoved();

                        if (list_FollowupData.size() == 0) {
                            Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, DashBoardActivity.class);
                            context.startActivity(intent);


                        }


                    }
                }, DELAY_MILLIS);

            }
        });


        holder.ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initFlip(holder.ivShow);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        sessionManager.setClientid(fd.getClientid());
                        Intent intent = new Intent(context, ViewClientAndFollwupDataActivity.class);
                        //Intent intent = new Intent(context , DemoTabActivity.class);
                        context.startActivity(intent);

                      /*  final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_client_details);

                        dialog.setCancelable(true);

                        EditText  edtName = (EditText)dialog. findViewById(R.id.edtName);
                        EditText   edtCompanyname = (EditText)dialog. findViewById(R.id.edtCompanyname);
                        EditText   edtMobile = (EditText)dialog. findViewById(R.id.edtMobile);
                        EditText  edtMobile2 = (EditText)dialog. findViewById(R.id.edtMobile2);
                        EditText  edtLandline = (EditText)dialog. findViewById(R.id.edtMobile3);
                        EditText  edtEmail = (EditText) dialog.findViewById(R.id.edtEmail);
                        EditText  edtWebsite = (EditText) dialog.findViewById(R.id.edtWebsite);
                        EditText edtBusiness = (EditText) dialog.findViewById(R.id.edtBussiness);
                        EditText edtAddress = (EditText)dialog. findViewById(R.id.edtAddress);
                        EditText edtNote = (EditText) dialog.findViewById(R.id.edtNote);

                        edtName.setText(fd.getClientname());
                        edtCompanyname.setText(fd.getCompanyname());
                        edtMobile.setText(fd.getMoibleno1());
                        edtMobile2.setText(fd.getMoibleno2());
                        edtLandline.setText(fd.getLandline());
                        edtEmail.setText(fd.getEmail());
                        edtWebsite.setText(fd.getWebsite());
                        edtBusiness.setText(fd.getBussiness());
                        edtAddress.setText(fd.getAddress());
                        edtNote.setText(fd.getNote());


                        TextView tvClose = (TextView)dialog.findViewById(R.id.tvClose);

                        tvClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.cancel();
                                dialog.dismiss();
                            }
                        });

                        TextView  txtnodata = (TextView)dialog.findViewById(R.id.txtnodata);

                        RecyclerView rv_followup = (RecyclerView) dialog.findViewById(R.id.rv_followup);

                        RecyclerView.LayoutManager lmanagr = new LinearLayoutManager(context);
                        rv_followup.setLayoutManager(lmanagr);
                        rv_followup.setItemAnimator(new DefaultItemAnimator());

                        String query = "select * from " + dbhandler.TABLE_FOLLOWUP_MASTER + "";
                        query = "select *,fm."+ dbhandler.CLIENT_DEVICE_TYPE +" as DevicType  from "+ dbhandler.TABLE_FOLLOWUP_MASTER +" as fm,"+ dbhandler.TABLE_CLIENTMASTER +"  as cm where cm."+ dbhandler.CLIENT_ID +" =fm."+ dbhandler.CLIENT_ID +"";
                        Log.d(TAG, " Query : " + query);

                        Cursor c = sd.rawQuery(query, null);

                        Log.d(TAG, "Client Records : " + c.getCount() + "  found");

                        list_followups.clear();
                        if (c.getCount() > 0)
                        {
                            while (c.moveToNext())
                            {
                                //FollowupData(String followupid, String followupdate, String followuptime, String followupnote, String clientid, String devicetype, String clientname, String moibleno1, String bussiness, String address, String note, String email, String moibleno2, String landline, String companyname, String clienttype, String lattitude, String longtitude) {
                                FollowupData followupData = new FollowupData(c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_ID)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_TIME)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)),c.getString(c.getColumnIndex(dbhandler.CLIENT_ID)),c.getString(c.getColumnIndex("DevicType")),c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)),c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE1)),c.getString(c.getColumnIndex(dbhandler.CLIENT_BUSSINESS)),c.getString(c.getColumnIndex(dbhandler.CLIENT_ADDRESS)),c.getString(c.getColumnIndex(dbhandler.CLIENT_NOTE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_EMAIL)),c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE2)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LANDLINE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_COMPANYNAME)),c.getString(c.getColumnIndex(dbhandler.CLIENT_TYPE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LATTITUDE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LONGTITUDE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_WEBSITE)));
                                //ClientData cd = new ClientData(c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)));
                                list_followups.add(followupData);


                            }


                            txtnodata.setVisibility(View.GONE);
                            rv_followup.setVisibility(View.VISIBLE);

                            FollowupDataAdapterRecyclerView adapter = new FollowupDataAdapterRecyclerView(context,list_followups,activity,"dialog");
                            rv_followup.setAdapter(adapter);


                        }
                        else
                        {
                            Toast.makeText(context, "No client records found", Toast.LENGTH_SHORT).show();

                            txtnodata.setVisibility(View.VISIBLE);
                            rv_followup.setVisibility(View.GONE);
                        }




                        dialog.getWindow().setLayout(RecyclerView.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
                        dialog.show();*/


                    }
                }, DELAY_MILLIS);
            }
        });


    }


    @Override
    public int getItemCount() {
        return list_FollowupData.size();
    }

    private void initFlip(ImageView iv) {

        FlipAnimation.create().with(iv)
                .setDuration(1000)
                .setRepeatCount(1)
                .start();


    }


}
