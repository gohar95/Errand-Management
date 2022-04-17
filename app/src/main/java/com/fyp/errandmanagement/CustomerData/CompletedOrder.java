package com.fyp.errandmanagement.CustomerData;

import androidx.appcompat.app.AppCompatActivity;
import com.fyp.errandmanagement.R;

import android.os.Bundle;
import android.widget.TextView;

public class CompletedOrder extends AppCompatActivity {

    TextView orderCode;
    String orderNO = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_order);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            orderNO = bundle.getString("OID");
        }

        orderCode = findViewById(R.id.orderCode);

        orderCode.setText(orderNO);
    }
}