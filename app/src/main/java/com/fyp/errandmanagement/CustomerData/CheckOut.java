package com.fyp.errandmanagement.CustomerData;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fyp.errandmanagement.Models.NotificationModel;
import com.fyp.errandmanagement.Models.OrderModel;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class CheckOut extends AppCompatActivity {

    String ProviderID = "";
    String ServiceID = "";
    String CustomerID = "";
    String AMOUNT= "";
    String ONAME= "";
    String ServicePriceType = "";
    String fcmToken = "";

    TextView sName,sAdd,sPhone,dayText,dateText,timeText,checkPrice,orderFinal;
    LinearLayout dDay,dTime;
    EditText insText;
    RadioGroup groupOrder;
    RadioButton radioOrder;
    int randomNumber;

    private APIService apiService;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            ProviderID = bundle.getString("ProviderID");
            ServiceID = bundle.getString("ServiceID");
            CustomerID = bundle.getString("CustomerID");
            AMOUNT = bundle.getString("TAMOUNT");
            ONAME = bundle.getString("ONAME");
            ServicePriceType = bundle.getString("ServicePriceType");
            fcmToken = bundle.getString("fcmToken");
        }

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(this);

        sName = findViewById(R.id.nameShipping);
        sAdd = findViewById(R.id.addShipping);
        sPhone = findViewById(R.id.phoneShipping);
        dDay = findViewById(R.id.checkDay);
        dayText = findViewById(R.id.dayText);
        dateText = findViewById(R.id.dateText);
        dTime = findViewById(R.id.checkTime);
        timeText = findViewById(R.id.timeText);
        insText = findViewById(R.id.instructionText);
        checkPrice = findViewById(R.id.checkPrice);
        orderFinal = findViewById(R.id.orderFinal);

        checkPrice.setText(AMOUNT);

        groupOrder = findViewById(R.id.radioPay);
        int selectedId = groupOrder.getCheckedRadioButtonId();
        radioOrder = findViewById(selectedId);

        sName.setText(shared.getString("name",""));
        sAdd.setText(shared.getString("address",""));
        sPhone.setText(shared.getString("phone",""));

        dDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDeliveryDay();
            }
        });
        dTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDeliveryTime();
            }
        });
        orderFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DDAY = dayText.getText().toString();
                String DDATE = dateText.getText().toString();
                String DTIME = timeText.getText().toString();
                String DINS = insText.getText().toString();
                String DPRICE = AMOUNT;
                String DORDER = radioOrder.getText().toString();
                if(TextUtils.isEmpty(DINS) || TextUtils.isEmpty(DDAY) || TextUtils.isEmpty(DDATE) || TextUtils.isEmpty(DPRICE) || TextUtils.isEmpty(DTIME)){
                    Toast.makeText(CheckOut.this, "Required Field", Toast.LENGTH_SHORT).show();
                }
                else{
                    ORDERNOW(DDAY,DDATE,DTIME,DINS,DPRICE,DORDER);
                }
            }
        });


    }

    private void OpenDeliveryTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (this).getLayoutInflater();
        builder.setCancelable(false);
        builder.setIcon(R.drawable.time_icon);
        final View dialogView = inflater.inflate(R.layout.time_dialog, null);
        final RadioGroup group = dialogView.findViewById(R.id.radioTime);

        builder.setView(dialogView);
        // Add action buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                int selectedId = group.getCheckedRadioButtonId();
                final RadioButton radioButton = dialogView.findViewById(selectedId);
                timeText.setText(radioButton.getText());
            }
        });
        builder.create();
        builder.show();
    }

    private void OpenDeliveryDay() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (this).getLayoutInflater();
        builder.setCancelable(false);
        builder.setIcon(R.drawable.calender_icon);
        final View dialogView = inflater.inflate(R.layout.day_dialog, null);

        final DatePicker picker = dialogView.findViewById(R.id.dayPick);

        builder.setView(dialogView);
        // Add action buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date myDate = inFormat.parse(picker.getDayOfMonth()+"-"+(picker.getMonth() + 1)+"-"+picker.getYear());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                    String dayName=simpleDateFormat.format(myDate);
                    dayText.setText(dayName);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dateText.setText(picker.getDayOfMonth()+"-"+ (picker.getMonth() + 1)+"-"+picker.getYear());
            }
        });
        builder.create();
        builder.show();
    }

    private void ORDERNOW(final String dday, final String ddate, final String dtime, final String dins, final String dprice, final String dorder) {
        final SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);
        progressDialog.setMessage("Order in Progress...");
        progressDialog.show();
        Random random = new Random();
        randomNumber = random.nextInt(99999);
        final String uid = "SC-"+Integer.toString(randomNumber);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        final String pdate = currentDate+" / "+currentTime;
        OrderModel values = new OrderModel(uid,dday,ddate,dtime,dins,dprice,dorder,sAdd.getText().toString(),sName.getText().toString(),sPhone.getText().toString(),pdate,ONAME,"Pending",ProviderID,ServiceID,CustomerID,shared.getString("image",""),ServicePriceType);
        FirebaseDatabase.getInstance().getReference("Orders").child(uid).setValue(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            sendNotification("New Order Placed",values.getONAME(),fcmToken,uid);

                        }else{
                            progressDialog.dismiss();
                            //Alert Dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                            alertDialog.setTitle("Order Failed");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Due to\nTechnical Issue\nTry Again");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                });
    }

    private void sendNotification(String title, String message, String usertoken, String uid) {

        //Toast.makeText(this, title+"\n"+message+"\n"+usertoken, Toast.LENGTH_SHORT).show();

        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        progressDialog.dismiss();
                        Toast.makeText(CheckOut.this, "Order Booked\nNotification Failed ", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(CheckOut.this,Booked.class);
                        i.putExtra("ORDER_ID","Order ID : "+uid);
                        startActivity(i);
                        finishAffinity();
                        finish();
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
                                            Toast.makeText(CheckOut.this, "Order Booked\nNotification Sent to Service Provider", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(CheckOut.this,Booked.class);
                                            i.putExtra("ORDER_ID","Order ID : "+uid);
                                            startActivity(i);
                                            finishAffinity();
                                            finish();
                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(CheckOut.this, "Order Booked\nNotification Failed ", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(CheckOut.this,Booked.class);
                                            i.putExtra("ORDER_ID","Order ID : "+uid);
                                            startActivity(i);
                                            finishAffinity();
                                            finish();
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