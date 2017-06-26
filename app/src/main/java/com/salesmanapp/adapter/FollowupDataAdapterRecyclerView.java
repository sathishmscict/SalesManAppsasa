package com.salesmanapp.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.salesmanapp.animation.FlipAnimation;
import com.salesmanapp.database.dbhandler;
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
    private Context context;
    ArrayList<FollowupData> list_FollowupData;
    LayoutInflater inflater;
    private String TAG = FollowupDataAdapterRecyclerView.class.getSimpleName();
    private int topic_approval_status;
    private ProgressDialog pDialog;
    private long DELAY_MILLIS=2500;


    public FollowupDataAdapterRecyclerView(Context context, ArrayList<FollowupData> TestMasterData, Activity activity) {
        this.context = context;
        this.list_FollowupData = TestMasterData;
        inflater = LayoutInflater.from(context);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();
        this.activity = activity;

        db = new dbhandler(context);
        sd = db.getWritableDatabase();


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

        public MyViewHolder(View itemView) {

            super(itemView);

            edtName = (EditText) itemView.findViewById(R.id.edtName);
            edtNote = (EditText) itemView.findViewById(R.id.edtNote);
            edtFollowupDate = (EditText) itemView.findViewById(R.id.edtFollowupDate);
            edtFollowupTime = (EditText) itemView.findViewById(R.id.edtFollowupTime);
            edtCompnayName = (EditText)itemView.findViewById(R.id.edtCompnayName);



            ivLocation  = (ImageView)itemView.findViewById(R.id.ivLocation);
            ivCall  = (ImageView)itemView.findViewById(R.id.ivCall);
            ivServices  = (ImageView)itemView.findViewById(R.id.ivServices);
            ivEdit  = (ImageView)itemView.findViewById(R.id.ivEdit);
            ivDelete  = (ImageView)itemView.findViewById(R.id.ivDelete);
            ivShow  = (ImageView)itemView.findViewById(R.id.ivShow);


        }

    }

    @Override
    public FollowupDataAdapterRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {


        View view = inflater.inflate(R.layout.row_single_followup, parent, false);

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






        holder.ivServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initFlip(holder.ivServices);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        sessionManager.setClientid(fd.getClientid());
                        Intent intent = new Intent(context , AddServicesActivity.class);
                        context.startActivity(intent);



                    }
                },DELAY_MILLIS);

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

                            sessionManager.setGPSLocations(fd.getLattitude() , fd.getLongtitude(),"");


                            Toast.makeText(activity, "Lattitude : "+fd.getLattitude()+" Longtitude : "+fd.getLongtitude(), Toast.LENGTH_SHORT).show();

                            sessionManager.setGPSLocations("21.2049887","72.8385114",fd.getClientname());

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

                        context.startActivity(intent);
                    }
                },DELAY_MILLIS);


            }
        });





        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initFlip(holder.ivDelete);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        sd.delete(dbhandler.TABLE_FOLLOWUP_MASTER,""+ dbhandler.CLIENT_ID +"='"+ fd.getClientid() +"' and "+ dbhandler.FOLLOWUP_ID +"="+ fd.getFollowupid()  +"",null);

                        Toast.makeText(activity, fd.getCompanyname()+"  has been deleted", Toast.LENGTH_SHORT).show();
                        list_FollowupData.remove(list_FollowupData.indexOf(fd));


                        notifyDataSetChanged();


                        //notifyItemRemoved();

                        if(list_FollowupData.size() == 0)
                        {
                            Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context , DashBoardActivity.class);
                            context.startActivity(intent);


                        }






                    }
                },DELAY_MILLIS);

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
