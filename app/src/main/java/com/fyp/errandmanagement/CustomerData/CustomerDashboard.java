package com.fyp.errandmanagement.CustomerData;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;
import com.fyp.errandmanagement.R;
import com.fyp.errandmanagement.SelectionScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CustomerDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CircleImageView dImage;
    TextView dName, dEmail, frag_name;

    FirebaseAuth auth;

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);

        auth = FirebaseAuth.getInstance();

        frag_name = findViewById(R.id.frag_name);
        frag_name.setText("Dashboard");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        dImage = headerView.findViewById(R.id.drawerImage);
        dName = headerView.findViewById(R.id.drawerName);
        dEmail = headerView.findViewById(R.id.drawerEmail);

        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(CustomerDashboard.this);

        dName.setText(shared.getString("name",""));
        dEmail.setText(shared.getString("email",""));
        new DownloadImageFromInternet(dImage).execute(shared.getString("image",""));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            frag_name.setText("Profile");
            ProfileFragment userFragment = new ProfileFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.replacingFragment,userFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_dashboard) {
            frag_name.setText("Dashboard");
            DashboardFragment userFragment = new DashboardFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.replacingFragment,userFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_bookings) {
            frag_name.setText("My Bookings");
            MyBookingFragment userFragment = new MyBookingFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.replacingFragment,userFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_cancel) {
            frag_name.setText("Cancel Order");
            CancleOrder userFragment = new CancleOrder();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.replacingFragment,userFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_chats) {
            frag_name.setText("Chats");
            ChatsFragment userFragment = new ChatsFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.replacingFragment,userFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_logout) {
            auth.signOut();
            startActivity(new Intent(CustomerDashboard.this, SelectionScreen.class));
            finishAffinity();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        CircleImageView imageView;

        public DownloadImageFromInternet(CircleImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String encoded = Base64.encodeToString(b, Base64.DEFAULT);
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("IMGURL", encoded);
            editor.apply();
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}