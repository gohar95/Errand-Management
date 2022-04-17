package com.fyp.errandmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import com.fyp.errandmanagement.CustomerData.CustomerDashboard;
import com.fyp.errandmanagement.ProviderData.ProviderDashboard;
import com.fyp.errandmanagement.R;

public class SplashScreen extends AppCompatActivity {

    LinearLayout mainContainer;
    FirebaseAuth auth;
    String UserStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();
        if(auth != null){
            SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);
            UserStatus = shared.getString("STATUS","");
        }

        //call for animation
        mainContainer = findViewById(R.id.animateLayout);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        mainContainer.setAnimation(myanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(auth.getCurrentUser() != null){
                    if(UserStatus.equals("Customers")){
                        Intent i = new Intent(SplashScreen.this, CustomerDashboard.class);
                        startActivity(i);
                        //Toast.makeText(SplashScreen.this, "Customer Dashboard", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Intent i = new Intent(SplashScreen.this, ProviderDashboard.class);
                        startActivity(i);
                        //Toast.makeText(SplashScreen.this, "Provider Dashboard", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    Intent i = new Intent(SplashScreen.this,WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        },3000);
    }
}