package com.fyp.errandmanagement.CustomerData;

import androidx.appcompat.app.AppCompatActivity;
import com.fyp.errandmanagement.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Booked extends AppCompatActivity {

    TextView orderNO;
    String ORDER_ID = "";
    TextView okPlaced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ORDER_ID = bundle.getString("ORDER_ID");
        }

        orderNO = findViewById(R.id.orderNO);
        orderNO.setText(ORDER_ID);

        okPlaced = findViewById(R.id.okPlaced);
        okPlaced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Booked.this,CustomerDashboard.class));
                finishAffinity();
                finish();
            }
        });
    }
}