package com.fyp.errandmanagement.ProviderData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fyp.errandmanagement.databinding.ActivityOrderDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OrderDetails extends AppCompatActivity {

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
    String PriceType = "";
    String IMG = "";

    private APIService apiService;
   ActivityOrderDetailsBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_order_details);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
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
            PriceType = bundle.getString("PriceType");
            IMG = bundle.getString("Image");
        }


        progressDialog = new ProgressDialog(this);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        checkFinishStatus();

        SharedPreferences shared1 = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.doChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetails.this,ProviderMessgaeActivity.class);
                intent.putExtra("name",shared1.getString("name",""));
                intent.putExtra("image",IMG);
                intent.putExtra("address",shared1.getString("address",""));
                intent.putExtra("email",shared1.getString("email",""));
                intent.putExtra("phone",shared1.getString("phone",""));
                intent.putExtra("CustomerID",CustomerID);
                startActivity(intent);
            }
        });

        binding.customerName.setText(NAME);
        Glide.with(this).load(IMG).into(binding.customerImage);
        binding.custName.setText(NAME);
        binding.orderTitle.setText(ONAME);
        binding.orderDate.setText(DATE);
        binding.orderTime.setText(TIME);
        binding.orderPrice.setText(PRICE);
        binding.priceType.setText(PriceType);
        binding.customerAddress.setText(ADDR);

        SharedPreferences shared = getSharedPreferences("ORDER_STATUS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(orderNO, STATUS);
        String cdate = shared.getString("currentDate","");

        long days = getDaysBetweenDates(cdate,DATE);

        if(shared != null){
            if(shared.getString(orderNO,"").equals("Approved") || shared.getString(orderNO,"").equals("Completed")){
                binding.img2.setColorFilter(ContextCompat.getColor(this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.img2Text.setTextColor(this.getResources().getColorStateList(R.color.green));
                binding.img1.setColorFilter(ContextCompat.getColor(this, R.color.GreyColor), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.img1Text.setTextColor(this.getResources().getColorStateList(R.color.lightBlack));
                binding.currentDate.setText(shared.getString("currentDate",""));

                binding.acceptDesign.setBackground(ContextCompat.getDrawable(this, R.drawable.status_back));
                binding.t14.setTextColor(this.getResources().getColorStateList(R.color.colorBlack));
                binding.t15.setTextColor(this.getResources().getColorStateList(R.color.GreyColor));
                binding.currentDate.setTextColor(this.getResources().getColorStateList(R.color.GreyColor));
                binding.firstDot.setBackground(ContextCompat.getDrawable(this, R.drawable.circle2));
                if(days <= 0){
                    binding.pregressAccept.setBackground(ContextCompat.getDrawable(this, R.drawable.status_back));
                    binding.t21.setTextColor(this.getResources().getColorStateList(R.color.colorBlack));
                    binding.secondDot.setBackground(ContextCompat.getDrawable(this, R.drawable.circle2));
                }

                checkFinishStatus();

            }
            else{
                binding.img2.setColorFilter(ContextCompat.getColor(this, R.color.GreyColor), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.img2Text.setTextColor(this.getResources().getColorStateList(R.color.lightBlack));
                binding.img1.setColorFilter(ContextCompat.getColor(this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.img1Text.setTextColor(this.getResources().getColorStateList(R.color.red));

                binding.acceptDesign.setBackground(ContextCompat.getDrawable(this, R.drawable.status_back_empty));
                binding.t14.setTextColor(this.getResources().getColorStateList(R.color.GreyColor));
                binding.t15.setTextColor(this.getResources().getColorStateList(R.color.GreyColor));
                binding.currentDate.setTextColor(this.getResources().getColorStateList(R.color.GreyColor));
                binding.currentDate.setText("");
                binding.firstDot.setBackground(ContextCompat.getDrawable(this, R.drawable.circle));

                binding.pregressAccept.setBackground(ContextCompat.getDrawable(this, R.drawable.status_back_empty));
                binding.t21.setTextColor(this.getResources().getColorStateList(R.color.GreyColor));
                binding.secondDot.setBackground(ContextCompat.getDrawable(this, R.drawable.circle));

                binding.finishAccept.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.status_back_empty));
                binding.t31.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.GreyColor));
                binding.thirdDot.setBackground(ContextCompat.getDrawable(this, R.drawable.circle));
            }
        }

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(shared.getString(orderNO,"").equals("Pending") || shared.getString(orderNO,"").equals("Canceled")))
                    UpdateOrderStatus("Canceled");
            }
        });

        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(shared.getString(orderNO,"").equals("Approved") || shared.getString(orderNO,"").equals("Completed")))
                    UpdateOrderStatus("Approved");
            }
        });
    }

    private void checkFinishStatus() {
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        reference2.child("Orders").child(orderNO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    String ostatus = dataSnapshot.child("ostatus").getValue().toString();

                    if(ostatus.equals("Completed")) {

                        SharedPreferences shared = getSharedPreferences("ORDER_STATUS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString(orderNO, ostatus);
                        editor.apply();

                        binding.acceptDesign.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.status_back));
                        binding.t14.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.colorBlack));
                        binding.t15.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.GreyColor));
                        binding.currentDate.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.GreyColor));
                        binding.firstDot.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.circle2));

                        binding.finishAccept.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.status_back));
                        binding.t31.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.colorBlack));
                        binding.thirdDot.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.circle2));

                        binding.pregressAccept.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.status_back));
                        binding.t21.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.colorBlack));
                        binding.secondDot.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.circle2));
                    }
                    else{

                        SharedPreferences shared = getSharedPreferences("ORDER_STATUS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString(orderNO, ostatus);
                        editor.apply();

                        binding.finishAccept.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.status_back_empty));
                        binding.t31.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.GreyColor));
                        binding.thirdDot.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.circle));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void UpdateOrderStatus(String sts) {
        progressDialog.setMessage("Canceling...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Orders").child(orderNO);
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("ostatus", sts);
        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();

                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                    SharedPreferences shared = getSharedPreferences("ORDER_STATUS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString(orderNO, sts);
                    editor.putString("currentDate", currentDate);
                    editor.apply();

                    long days = getDaysBetweenDates(currentDate,DATE);

                    if(shared.getString(orderNO,"").equals("Approved") || shared.getString(orderNO,"").equals("Completed")){
                        binding.img2.setColorFilter(ContextCompat.getColor(OrderDetails.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                        binding.img2Text.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.green));
                        binding.img1.setColorFilter(ContextCompat.getColor(OrderDetails.this, R.color.GreyColor), android.graphics.PorterDuff.Mode.SRC_IN);
                        binding.img1Text.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.lightBlack));
                        binding.currentDate.setText(currentDate);

                        binding.acceptDesign.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.status_back));
                        binding.t14.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.colorBlack));
                        binding.t15.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.GreyColor));
                        binding.currentDate.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.GreyColor));
                        binding.firstDot.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.circle2));

                        if(days <= 0){
                            binding.pregressAccept.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.status_back));
                            binding.t21.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.colorBlack));
                            binding.secondDot.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.circle2));
                        }

                        checkFinishStatus();
                    }
                    else{
                        binding.img2.setColorFilter(ContextCompat.getColor(OrderDetails.this, R.color.GreyColor), android.graphics.PorterDuff.Mode.SRC_IN);
                        binding.img2Text.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.lightBlack));
                        binding.img1.setColorFilter(ContextCompat.getColor(OrderDetails.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                        binding.img1Text.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.red));

                        binding.acceptDesign.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.status_back_empty));
                        binding.t14.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.GreyColor));
                        binding.t15.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.GreyColor));
                        binding.currentDate.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.GreyColor));
                        binding.currentDate.setText("");
                        binding.firstDot.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.circle));

                        binding.pregressAccept.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.status_back_empty));
                        binding.t21.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.GreyColor));
                        binding.secondDot.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.circle));

                        binding.finishAccept.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.status_back_empty));
                        binding.t31.setTextColor(OrderDetails.this.getResources().getColorStateList(R.color.GreyColor));
                        binding.thirdDot.setBackground(ContextCompat.getDrawable(OrderDetails.this, R.drawable.circle));
                    }

                    sendNotification("Order "+sts,
                            "Your Order "+sts+" with the following Order ID: "+orderNO);
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(OrderDetails.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNotification(String title, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Customers").child(CustomerID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    Toast.makeText(OrderDetails.this, title+"\n"+"Notification Failed", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(OrderDetails.this, title+"\n"+"Notification Sent to Customer", Toast.LENGTH_SHORT).show();
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

    public static final String DATE_FORMAT = "dd-MM-yyyy";  //or use "M/d/yyyy"

    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }
}