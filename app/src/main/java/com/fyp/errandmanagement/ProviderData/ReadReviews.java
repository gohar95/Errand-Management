package com.fyp.errandmanagement.ProviderData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fyp.errandmanagement.Adapters.ReadReviewAdapter;
import com.fyp.errandmanagement.Models.ReadReviewModel;
import com.fyp.errandmanagement.R;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadReviews extends AppCompatActivity {

    RecyclerView recyclerView;
    ReadReviewAdapter adapter;
    ArrayList<ReadReviewModel> list;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_reviews);

        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.readReviewRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        FetchData();
    }

    private void FetchData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching All Reviews...\nPlease Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String provider = auth.getCurrentUser().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Reviews").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        String providerID = snapshot.child("providerID").getValue().toString();

                        if(provider.equals(providerID)){
                            String custID = snapshot.child("customerID").getValue().toString();
                            String rating = snapshot.child("rating").getValue().toString();
                            String reviewText = snapshot.child("reviewText").getValue().toString();
                            String serviceID = snapshot.child("serviceID").getValue().toString();

                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                            reference2.child("Customers").child(custID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot1) {
                                    if(dataSnapshot1 !=null){
                                        String name = dataSnapshot1.child("name").getValue().toString();
                                        String image = dataSnapshot1.child("image").getValue().toString();

                                        list.add(new ReadReviewModel(reviewText, rating, serviceID, providerID, custID, name, image));

                                        adapter = new ReadReviewAdapter(ReadReviews.this,list);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
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