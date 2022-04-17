package com.fyp.errandmanagement.CustomerData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fyp.errandmanagement.Adapters.CategoryServicesAdapter;
import com.fyp.errandmanagement.Models.AnyServiceModel;
import com.fyp.errandmanagement.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServicesCategory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<AnyServiceModel> list;
    CategoryServicesAdapter adapter;
    private TextView nothingHere;
    ProgressDialog progressDialog;

    ImageView headerImage;

    String CATEGORY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_category);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            CATEGORY = bundle.getString("CATEGORY");
        }

        headerImage = findViewById(R.id.headerImage);
        if(CATEGORY.equals("Food"))
            headerImage.setImageResource(R.color.mainColor3);
        if(CATEGORY.equals("Groccery"))
            headerImage.setImageResource(R.color.mainColor3);
        if(CATEGORY.equals("Document"))
            headerImage.setImageResource(R.color.mainColor3);
        if(CATEGORY.equals("Gifts"))
            headerImage.setImageResource(R.color.mainColor3);
        if(CATEGORY.equals("Goods"))
            headerImage.setImageResource(R.color.mainColor3);
        if(CATEGORY.equals("Furniture"))
            headerImage.setImageResource(R.color.mainColor3);
        if(CATEGORY.equals("Luggage"))
            headerImage.setImageResource(R.color.mainColor3);


        recyclerView = findViewById(R.id.categoryServiceRecycler);
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nothingHere = findViewById(R.id.nothingHere);

        nothingHere.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching "+CATEGORY+" Services");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FetchingServices(CATEGORY);
    }

    private void FetchingServices(String stype) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Services").child(stype).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                        reference2.child("Services").child(stype).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot !=null){
                                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        String category = snapshot.child("category").getValue().toString();
                                        String dateTime = snapshot.child("dateTime").getValue().toString();
                                        String serviceDescription = snapshot.child("serviceDescription").getValue().toString();
                                        String serviceID = snapshot.child("serviceID").getValue().toString();
                                        //String serviceImage = snapshot.child("serviceImage").getValue().toString();
                                        String serviceName = snapshot.child("serviceName").getValue().toString();
                                        String servicePrice = snapshot.child("servicePrice").getValue().toString();
                                        String servicePriceType = snapshot.child("servicePriceType").getValue().toString();
                                        String serviceType = snapshot.child("serviceType").getValue().toString();
                                        String booked = snapshot.child("booked").getValue().toString();
                                        String providerID = snapshot.child("providerID").getValue().toString();

                                        list.add(new AnyServiceModel(serviceID,providerID,serviceName,serviceDescription,serviceType,servicePrice,servicePriceType,dateTime,category,booked));
                                    }
                                    adapter = new CategoryServicesAdapter(ServicesCategory.this,list);
                                    recyclerView.setAdapter(adapter);

                                    nothingHere.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                            }
                        });
                    }
                    progressDialog.dismiss();
                }
                else{
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}