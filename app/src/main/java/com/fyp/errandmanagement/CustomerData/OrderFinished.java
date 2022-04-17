package com.fyp.errandmanagement.CustomerData;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.errandmanagement.Models.NotificationModel;
import com.fyp.errandmanagement.Models.ReviewModel;
import com.fyp.errandmanagement.R;
import com.fyp.errandmanagement.SendNotificationPack.APIService;
import com.fyp.errandmanagement.SendNotificationPack.Client;
import com.fyp.errandmanagement.SendNotificationPack.Data;
import com.fyp.errandmanagement.SendNotificationPack.MyResponse;
import com.fyp.errandmanagement.SendNotificationPack.NotificationSender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderFinished extends AppCompatActivity {

    String orderNO = "";
    String DAY = "";
    String DATE = "";
    String TIME = "";
    String INS = "";
    String PRICE = "";
    String ORDER = "";
    String ADDR = "";
    String NAME = "";
    String PHONE = "";
    String PDATE = "";
    String ONAME = "";
    String STATUS = "";
    String ProviderID = "";
    String CustomerID = "";
    String ServiceID = "";

    TextView orderCode;
    Button jobDone,reviewDone;
    String myREF = "";
    EditText ratingText;
    RatingBar ratingBar;

    RelativeLayout finish_card,review_card;
    ProgressDialog progressDialog;
    private APIService apiService;

    boolean bb = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_finished);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            orderNO = bundle.getString("OID");
            DAY = bundle.getString("DAY");
            DATE = bundle.getString("DATE");
            TIME = bundle.getString("TIME");
            INS = bundle.getString("INSTRUCTIONS");
            PRICE = bundle.getString("PRICE");
            ORDER = bundle.getString("METHOD");
            ADDR = bundle.getString("ADDRESS");
            NAME = bundle.getString("NAME");
            PHONE = bundle.getString("PHONE");
            PDATE = bundle.getString("ODATE");
            ONAME = bundle.getString("TITLE");
            STATUS = bundle.getString("STATUS");
            ProviderID = bundle.getString("ProviderID");
            CustomerID = bundle.getString("CustomerID");
            ServiceID = bundle.getString("ServiceID");
        }

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        orderCode = findViewById(R.id.orderCode);
        jobDone = findViewById(R.id.jobDone);
        finish_card = findViewById(R.id.finish_card);
        review_card = findViewById(R.id.review_card);
        reviewDone = findViewById(R.id.reviewDone);
        ratingText = findViewById(R.id.ratingText);
        ratingBar = findViewById(R.id.ratingBar);

        finish_card.setVisibility(View.VISIBLE);
        review_card.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);

        orderCode.setText(orderNO);

        jobDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStatus();
            }
        });

        reviewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strReview = ratingText.getText().toString().trim();
                float rating = ratingBar.getRating();
                if(strReview.isEmpty())
                    ratingText.setError("Enter Comment here");
                else if(rating == 0)
                    Toast.makeText(OrderFinished.this, "Please Give Rating First", Toast.LENGTH_SHORT).show();
                else{
                    PublishReviews(Float.toString(rating), strReview);
                }
            }
        });
    }

    private void PublishReviews(String rating, String strReview) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Publishing Your Review...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ReviewModel values = new ReviewModel(strReview, rating, ServiceID, ProviderID, CustomerID);
        FirebaseDatabase.getInstance().getReference("Reviews").child(orderNO).setValue(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(OrderFinished.this, "Thanks for Giving Feedback", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(OrderFinished.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void UpdateStatus() {
        progressDialog.setMessage("Updating Status...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Orders").child(orderNO);
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("ostatus", "Completed");
        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    UpdateServiceBooking();
                }
                else{
                    finish_card.setVisibility(View.VISIBLE);
                    review_card.setVisibility(View.GONE);
                    Toast.makeText(OrderFinished.this, "Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void UpdateServiceBooking(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        if(!bb) {
                            String key = snapshot.getKey();

                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Services");
                            reference1.child(key).child(ProviderID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot1) {
                                    if (dataSnapshot1 != null) {
                                        for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {

                                            //Toast.makeText(OrderFinished.this, snapshot1.getKey(), Toast.LENGTH_SHORT).show();

                                            String serviceID = snapshot1.child("serviceID").getValue().toString();
                                            String booked = snapshot1.child("booked").getValue().toString();
                                            if(serviceID.equals(ServiceID)) {
                                                int ii = Integer.parseInt(booked);
                                                ii++;
                                                String bookedValue = Integer.toString(ii);

                                                //Toast.makeText(OrderFinished.this, bookedValue, Toast.LENGTH_SHORT).show();

                                                UpdateBooking(key, bookedValue);
                                                bb = true;

                                                break;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    finish_card.setVisibility(View.VISIBLE);
                                    review_card.setVisibility(View.GONE);
                                    Toast.makeText(OrderFinished.this, "Failed", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                        else
                            break;
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finish_card.setVisibility(View.VISIBLE);
                review_card.setVisibility(View.GONE);
                Toast.makeText(OrderFinished.this, "Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    private void UpdateBooking(String key, String bookedValue){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Services").child(key).child(ProviderID).child(ServiceID);
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("booked", bookedValue);
        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    sendNotification("Job Finished",
                            "Your Job Finished by Customer with the following Order ID: "+orderNO);
                }
                else{
                    finish_card.setVisibility(View.VISIBLE);
                    review_card.setVisibility(View.GONE);
                    Toast.makeText(OrderFinished.this, "Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void sendNotification(String title, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("ServiceProviders").child(ProviderID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    String fcmToken = dataSnapshot.child("fcmToken").getValue().toString();

                    Data data = new Data(title, message);
                    NotificationSender sender = new NotificationSender(data, fcmToken);
                    apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success != 1) {
                                    progressDialog.dismiss();
                                    finish_card.setVisibility(View.GONE);
                                    review_card.setVisibility(View.VISIBLE);
                                    Toast.makeText(OrderFinished.this, "Order Updated\nNotification Failed", Toast.LENGTH_SHORT).show();
                                }
                                else{

                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                    Date date = new Date();
                                    final String DateTime = dateFormat.format(date);
                                    NotificationModel values = new NotificationModel(title,message,ProviderID,CustomerID,DateTime);
                                    FirebaseDatabase.getInstance().getReference("Notifications").child(UUID.randomUUID().toString()).setValue(values)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @RequiresApi(api = Build.VERSION_CODES.N)
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        progressDialog.dismiss();
                                                        finish_card.setVisibility(View.GONE);
                                                        review_card.setVisibility(View.VISIBLE);
                                                        Toast.makeText(OrderFinished.this, "Order Updated\nNotification Sent to Service Provider", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        progressDialog.dismiss();
                                                        finish_card.setVisibility(View.GONE);
                                                        review_card.setVisibility(View.VISIBLE);
                                                        Toast.makeText(OrderFinished.this, "Order Updated\nNotification Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}