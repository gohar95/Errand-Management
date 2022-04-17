package com.fyp.errandmanagement.CustomerData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fyp.errandmanagement.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditOrder extends AppCompatActivity {

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

    TextView eNAME,sName,sAdd,sPhone,dayText,dateText,timeText,checkPrice,orderFinal,sText;
    LinearLayout dDay,dTime;
    EditText insText;
    RadioGroup groupOrder;
    RadioButton radioOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);
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

        SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);

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

        sName.setText(shared.getString("name",""));
        sAdd.setText(shared.getString("address",""));
        sPhone.setText(shared.getString("phone",""));

        groupOrder = findViewById(R.id.radioPay);
        int selectedId = groupOrder.getCheckedRadioButtonId();
        radioOrder = findViewById(selectedId);

        dayText.setText(DAY);
        dateText.setText(DATE);
        timeText.setText(TIME);
        insText.setText(INS);
        checkPrice.setText(PRICE);

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
                String DPRICE = PRICE;
                String DORDER = radioOrder.getText().toString();
                if(TextUtils.isEmpty(DINS) || TextUtils.isEmpty(DDAY) || TextUtils.isEmpty(DDATE) || TextUtils.isEmpty(DPRICE) || TextUtils.isEmpty(DTIME)){
                    Toast.makeText(EditOrder.this, "Required Field", Toast.LENGTH_SHORT).show();
                }
                else{
                    UpdateOrder(DDAY,DDATE,DTIME,DINS,DPRICE,DORDER);
                }
            }
        });
    }

    private void UpdateOrder(final String dday, final String ddate, final String dtime, final String dins, final String dprice, final String dorder) {
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Updating Order...");
        p.show();
        p.setCancelable(false);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Orders").child(orderNO);
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("addr", ADDR);
        updates.put("date", ddate);
        updates.put("day", dday);
        updates.put("ins", dins);
        updates.put("name", NAME);
        updates.put("order", dorder);
        updates.put("orderNO", orderNO);
        updates.put("pdate", PDATE);
        updates.put("phone", PHONE);
        updates.put("price", dprice);
        updates.put("time", dtime);
        updates.put("oname", ONAME);
        updates.put("ostatus", STATUS);
        updates.put("customerID", CustomerID);
        updates.put("providerID", ProviderID);
        updates.put("serviceID", ServiceID);
        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditOrder.this, "Order Updated", Toast.LENGTH_SHORT).show();
                finish();
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
}