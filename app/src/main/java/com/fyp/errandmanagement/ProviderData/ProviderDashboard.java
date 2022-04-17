package com.fyp.errandmanagement.ProviderData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.fyp.errandmanagement.AppService;
import com.fyp.errandmanagement.R;
import com.fyp.errandmanagement.SelectionScreen;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProviderDashboard extends AppCompatActivity {

    ImageView side_menu;
    TextView main_heading;
    ImageView header_icon;

    FirebaseAuth auth;


    public void startService() {
        startService(new Intent(getBaseContext(), AppService.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), AppService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_dashboard);

        //startService();

        auth = FirebaseAuth.getInstance();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        BottomNavigationView navView = findViewById(R.id.navigationBottom);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_request, R.id.navigation_notifications, R.id.navigation_reviews,R.id.navigation_chat,R.id.navigation_call,R.id.navigation_account)
                .build();*/
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        main_heading = findViewById(R.id.main_heading);
        header_icon = findViewById(R.id.header_icon);

        side_menu = findViewById(R.id.side_menu);
        side_menu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                MenuBuilder menuBuilder =new MenuBuilder(ProviderDashboard.this);
                MenuInflater inflater = new MenuInflater(ProviderDashboard.this);
                inflater.inflate(R.menu.main_page, menuBuilder);
                MenuPopupHelper optionsMenu = new MenuPopupHelper(ProviderDashboard.this, menuBuilder, view);
                optionsMenu.setForceShowIcon(true);

                // Set Item Click Listener
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.side_profile: // Handle option1 Click
                                Intent i1 = new Intent(ProviderDashboard.this, ProviderProfile.class);
                                startActivity(i1);
                                return true;
                            case R.id.side_logout: // Handle option4 Click
                                auth.signOut();
                                Intent i2 = new Intent(ProviderDashboard.this, SelectionScreen.class);
                                finishAffinity();
                                startActivity(i2);
                                finish();
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onMenuModeChange(MenuBuilder menu) {}
                });

                optionsMenu.show();
            }
        });

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            main_heading.setText(message);
            if(message.equals("Requests"))
                header_icon.setImageResource(R.drawable.request_icon);
            else if(message.equals("Notifications"))
                header_icon.setImageResource(R.drawable.ic_notifications_black_24dp);
            else if(message.equals("Reviews"))
                header_icon.setImageResource(R.drawable.ic_baseline_rate_review_24);
            else if(message.equals("Chat"))
                header_icon.setImageResource(R.drawable.ic_baseline_forum_24);
            else if(message.equals("Post Services"))
                header_icon.setImageResource(R.drawable.ic_baseline_insert_invitation_24);
            else if(message.equals("Account"))
                header_icon.setImageResource(R.drawable.ic_baseline_person_24);
            else
                header_icon.setImageResource(R.drawable.splash_logo);
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}