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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.salesmanapp.activity.AddClientActivity;
import com.salesmanapp.activity.AddServicesActivity;
import com.salesmanapp.activity.ContactUsActivity;
import com.salesmanapp.activity.DashBoardActivity;
import com.salesmanapp.R;
import com.salesmanapp.animation.FlipAnimation;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.pojo.ClientData;
import com.salesmanapp.pojo.FollowupData;
import com.salesmanapp.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Satish Gadde on 19-08-2016.
 */
public class ClientsDataAdapterRecyclerView extends RecyclerView.Adapter<ClientsDataAdapterRecyclerView.MyViewHolder> {


    private final SessionManager sessionManager;
    private final HashMap<String, String> userDetails;
    private final Activity activity;
    private final dbhandler db;
    private final SQLiteDatabase sd;
    private Context context;
    ArrayList<ClientData> list_ClientsData;
    LayoutInflater inflater;
    private String TAG = ClientsDataAdapterRecyclerView.class.getSimpleName();
    private int topic_approval_status;
    private ProgressDialog pDialog;
    private long DELAY_MILLIS=2500;
    private ArrayList<FollowupData> list_followups = new ArrayList<FollowupData>();


    public ClientsDataAdapterRecyclerView(Context context, ArrayList<ClientData> TestMasterData, Activity activity) {
        this.context = context;
        this.list_ClientsData = TestMasterData;
        inflater = LayoutInflater.from(context);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();
        this.activity = activity;

        db = new dbhandler(context);
        sd = db.getWritableDatabase();


    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        private final EditText edtName;
        private final EditText edtMobile;
        private final EditText edtEmail;
        private final EditText edtBussiness;
        private final EditText edtAddress;
        private final EditText edtNote;
        private final Button btncall;
        private final Button btnedit;
        private final Button btndelete;
        private final ImageView ivLocation;
        private final ImageView ivCall;
        private final ImageView ivServices;
        private final ImageView ivEdit;
        private final ImageView ivDelete;
        private final ImageView ivShow;
        private final EditText edtCompanyname;

        public MyViewHolder(View itemView) {

            super(itemView);

            edtName = (EditText) itemView.findViewById(R.id.edtName);
            edtMobile = (EditText) itemView.findViewById(R.id.edtMobile);
            edtEmail = (EditText) itemView.findViewById(R.id.edtEmail);
            edtBussiness = (EditText) itemView.findViewById(R.id.edtBussiness);
            edtAddress = (EditText) itemView.findViewById(R.id.edtAddress);
            edtNote = (EditText) itemView.findViewById(R.id.edtNote);
            edtCompanyname = (EditText)itemView.findViewById(R.id.edtCompanyname);


            btncall = (Button) itemView.findViewById(R.id.btncall);
            btnedit = (Button) itemView.findViewById(R.id.btnedit);
            btndelete = (Button) itemView.findViewById(R.id.btndelete);

            ivLocation  = (ImageView)itemView.findViewById(R.id.ivLocation);
            ivCall  = (ImageView)itemView.findViewById(R.id.ivCall);
            ivServices  = (ImageView)itemView.findViewById(R.id.ivServices);
            ivEdit  = (ImageView)itemView.findViewById(R.id.ivEdit);
            ivDelete  = (ImageView)itemView.findViewById(R.id.ivDelete);
            ivShow  = (ImageView)itemView.findViewById(R.id.ivShow);


        }

    }

    @Override
    public ClientsDataAdapterRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {


        View view = inflater.inflate(R.layout.row_single_client, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        final ClientData cd = list_ClientsData.get(position);


        holder.edtName.setText(cd.getClientname());


        holder.edtAddress.setText(cd.getAddress());
        holder.edtBussiness.setText(cd.getBussiness());
        holder.edtBussiness.setVisibility(View.GONE);
        holder.edtEmail.setText(cd.getEmail());
        holder.edtEmail.setVisibility(View.GONE);
        holder.edtMobile.setText(cd.getMoibleno1());
        holder.edtMobile.setVisibility(View.GONE);
        holder.edtNote.setText(cd.getNote());
        holder.edtCompanyname.setText(cd.getCompanyname());


        holder.btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Dexter.withActivity(activity)
                        .withPermission(Manifest.permission.READ_SMS)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91" + cd.getMoibleno1()));
                                context.startActivity(intent);


                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();

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

                            sessionManager.setGPSLocations(cd.getLattitude() , cd.getLongtitude(),"");


                            Toast.makeText(activity, "Lattitude : "+cd.getLattitude()+" Longtitude : "+cd.getLongtitude(), Toast.LENGTH_SHORT).show();

                           // sessionManager.setGPSLocations("21.2049887","72.8385114",cd.getCompanyname());

                            Intent intent = new Intent(context , ContactUsActivity.class);
                            context.startActivity(intent);





                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },DELAY_MILLIS);
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

                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91" + cd.getMoibleno1()));
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

        holder.ivServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initFlip(holder.ivServices);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        sessionManager.setClientid(cd.getClientid());
                        Intent intent = new Intent(context , AddServicesActivity.class);
                        context.startActivity(intent);



                    }
                },DELAY_MILLIS);

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
                        Intent intent = new Intent(context, AddClientActivity.class);
                        intent.putExtra(dbhandler.CLIENT_ID, cd.getClientid());
                        intent.putExtra(dbhandler.CLIENT_NAME, cd.getClientname());
                        intent.putExtra(dbhandler.CLIENT_EMAIL, cd.getEmail());
                        intent.putExtra(dbhandler.CLIENT_BUSSINESS, cd.getBussiness());
                        intent.putExtra(dbhandler.CLIENT_ADDRESS, cd.getAddress());
                        intent.putExtra(dbhandler.CLIENT_NOTE, cd.getNote());
                        intent.putExtra(dbhandler.CLIENT_MOBILE1, cd.getMoibleno1());
                        intent.putExtra(dbhandler.CLIENT_MOBILE2, cd.getMoibleno2());
                        intent.putExtra(dbhandler.CLIENT_LANDLINE, cd.getLandline());
                        intent.putExtra(dbhandler.CLIENT_COMPANYNAME, cd.getCompanyname());
                        intent.putExtra(dbhandler.CLIENT_TYPE, cd.getClienttype());
                        intent.putExtra(dbhandler.CLIENT_DEVICE_TYPE, cd.getDevicetype());
                        intent.putExtra(dbhandler.CLIENT_WEBSITE, cd.getWebsite());

                        context.startActivity(intent);
                    }
                },DELAY_MILLIS);


            }
        });

        holder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //        Toast.makeText(activity, "Edited", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AddClientActivity.class);
                intent.putExtra(dbhandler.CLIENT_ID, cd.getClientid());
                intent.putExtra(dbhandler.CLIENT_NAME, cd.getClientname());
                intent.putExtra(dbhandler.CLIENT_EMAIL, cd.getEmail());
                intent.putExtra(dbhandler.CLIENT_BUSSINESS, cd.getBussiness());
                intent.putExtra(dbhandler.CLIENT_ADDRESS, cd.getAddress());
                intent.putExtra(dbhandler.CLIENT_NOTE, cd.getNote());
                intent.putExtra(dbhandler.CLIENT_MOBILE1, cd.getMoibleno1());
                intent.putExtra(dbhandler.CLIENT_MOBILE2, cd.getMoibleno2());
                intent.putExtra(dbhandler.CLIENT_LANDLINE, cd.getLandline());
                intent.putExtra(dbhandler.CLIENT_COMPANYNAME, cd.getCompanyname());
                intent.putExtra(dbhandler.CLIENT_TYPE, cd.getClienttype());
                intent.putExtra(dbhandler.CLIENT_DEVICE_TYPE, cd.getDevicetype());

                context.startActivity(intent);


            }
        });


        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();

            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initFlip(holder.ivDelete);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        sd.delete(dbhandler.TABLE_CLIENTMASTER,""+ dbhandler.CLIENT_ID +"='"+ cd.getClientid() +"' and "+ dbhandler.CLIENT_DEVICE_TYPE +"='"+ cd.getDevicetype() +"'",null);

                        Toast.makeText(activity, cd.getCompanyname()+" has been deleted", Toast.LENGTH_SHORT).show();                        //notifyItemRemoved();

                        list_ClientsData.remove(list_ClientsData.indexOf(cd));


                        notifyDataSetChanged();



                        if(list_ClientsData.size() == 0)
                        {
                            Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context , DashBoardActivity.class);
                            context.startActivity(intent);


                        }

                    }
                },DELAY_MILLIS);

            }
        });


        holder.ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initFlip(holder.ivShow);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        final Dialog dialog = new Dialog(context);
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

                        edtName.setText(cd.getClientname());
                        edtCompanyname.setText(cd.getCompanyname());
                        edtMobile.setText(cd.getMoibleno1());
                        edtMobile2.setText(cd.getMoibleno2());
                        edtLandline.setText(cd.getLandline());
                        edtEmail.setText(cd.getEmail());
                        edtWebsite.setText(cd.getWebsite());
                        edtBusiness.setText(cd.getBussiness());
                        edtAddress.setText(cd.getAddress());
                        edtNote.setText(cd.getNote());


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

                            FollowupDataAdapterRecyclerView adapter = new FollowupDataAdapterRecyclerView(context,list_followups,activity,"clienstadapter");
                            rv_followup.setAdapter(adapter);


                        }
                        else
                        {
                            Toast.makeText(context, "No client records found", Toast.LENGTH_SHORT).show();

                            txtnodata.setVisibility(View.VISIBLE);
                            rv_followup.setVisibility(View.GONE);
                        }




                        dialog.getWindow().setLayout(RecyclerView.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
                        dialog.show();


                    }
                },DELAY_MILLIS);
            }
        });


    }


    @Override
    public int getItemCount() {
        return list_ClientsData.size();
    }

    private void initFlip(ImageView iv) {

        FlipAnimation.create().with(iv)
                .setDuration(1000)
                .setRepeatCount(1)
                .start();


    }


}
