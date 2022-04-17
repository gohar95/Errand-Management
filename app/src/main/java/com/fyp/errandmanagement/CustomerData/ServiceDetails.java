package com.fyp.errandmanagement.CustomerData;

import androidx.appcompat.app.AppCompatActivity;
import com.fyp.errandmanagement.databinding.ActivityServiceDetailsBinding;
import com.fyp.errandmanagement.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceDetails extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ActivityServiceDetailsBinding binding;

    String ServiceID = "";
    String Booked = "";
    String Category = "";
    String DateTime = "";
    String ServiceDescription = "";
    String ServiceImage = "";
    String ServiceName = "";
    String ServicePrice = "";
    String ServicePriceType = "";
    String ServiceType = "";
    String ProviderID = "";


    String name = "";
    String image = "";
    String address = "";
    String email = "";
    String phone = "";
    String fcmToken = "";
    double latt = 0;
    double lngg = 0;

    FirebaseAuth auth;

    TextView reviewCount;
    RatingBar ratingBar;

    float myRating = 0.0f;
    List<Float> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_service_details);
        binding = ActivityServiceDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            ServiceID = bundle.getString("ServiceID");
            Booked = bundle.getString("Booked");
            Category = bundle.getString("Category");
            DateTime = bundle.getString("DateTime");
            ServiceDescription = bundle.getString("ServiceDescription");
            ServiceImage = bundle.getString("ServiceImage");
            ServiceName = bundle.getString("ServiceName");
            ServicePrice = bundle.getString("ServicePrice");
            ServicePriceType = bundle.getString("ServicePriceType");
            ServiceType = bundle.getString("ServiceType");
            ProviderID = bundle.getString("ProviderID");
            name = bundle.getString("name");
            image = bundle.getString("image");
            address = bundle.getString("address");
            email = bundle.getString("email");
            phone = bundle.getString("phone");
            fcmToken = bundle.getString("fcmToken");
            latt = bundle.getDouble("latt");
            lngg = bundle.getDouble("lngg");
        }

        reviewCount = findViewById(R.id.reviewCount);
        ratingBar = findViewById(R.id.ratingBar);

        list = new ArrayList<>();

        findRating();

        auth = FirebaseAuth.getInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Glide.with(this).load(ServiceImage).into(binding.serviceImage);
        binding.serviceName.setText(ServiceName);
        binding.serviceDesc.setText(ServiceDescription);
        binding.serviceType.setText(ServiceType);
        binding.servicePrice.setText(ServicePrice);
        binding.servicePriceType.setText(ServicePriceType);

        binding.providerName.setText(name);
        binding.providerAddr.setText(address);
        Glide.with(this).load(image).into(binding.providerIMG);

        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServiceDetails.this, UserMessgaeActivity.class);
                i.putExtra("ServiceID",ServiceID);
                i.putExtra("Booked",Booked);
                i.putExtra("Category",Category);
                i.putExtra("DateTime",DateTime);
                i.putExtra("ServiceDescription",ServiceDescription);
                i.putExtra("ServiceImage",ServiceImage);
                i.putExtra("ServiceName",ServiceName);
                i.putExtra("ServicePrice",ServicePrice);
                i.putExtra("ServicePriceType",ServicePriceType);
                i.putExtra("ServiceType",ServiceType);
                i.putExtra("ProviderID",ProviderID);
                i.putExtra("name",name);
                i.putExtra("image",image);
                i.putExtra("address",address);
                i.putExtra("email",email);
                i.putExtra("phone",phone);
                startActivity(i);
            }
        });

        binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServiceDetails.this, CheckOut.class);
                i.putExtra("ServiceID",ServiceID);
                i.putExtra("ProviderID",ProviderID);
                i.putExtra("TAMOUNT",ServicePrice);
                i.putExtra("ONAME",ServiceName);
                i.putExtra("ServicePriceType",ServicePriceType);
                i.putExtra("fcmToken",fcmToken);
                i.putExtra("CustomerID",auth.getCurrentUser().getUid());
                startActivity(i);
            }
        });

    }

    private void findRating() {
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        reference2.child("Reviews").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        String serviceID = snapshot.child("serviceID").getValue().toString();

                        if(ServiceID.equals(serviceID)){
                            String customerID = snapshot.child("customerID").getValue().toString();
                            String providerID = snapshot.child("providerID").getValue().toString();
                            String rating = snapshot.child("rating").getValue().toString();
                            String reviewText = snapshot.child("reviewText").getValue().toString();

                            float rate = Float.parseFloat(rating);
                            list.add(rate);
                        }
                    }

                    if(list.size() > 0){

                        for(int i=0; i<list.size(); i++){
                            float myRate = list.get(i);
                            myRating = myRating + myRate;
                        }

                        myRating = myRating/list.size();
                        if(myRating >= 5.0) {
                            reviewCount.setText("5.0");
                            ratingBar.setRating(myRating);
                        }
                        else {
                            reviewCount.setText(Float.toString(myRating));
                            ratingBar.setRating(myRating);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                reviewCount.setText("0.0");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng Points = new LatLng(latt, lngg);
        mMap.addMarker(new
                MarkerOptions().position(Points).title("Provider Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Points));
    }
}