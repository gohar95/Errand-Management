package com.fyp.errandmanagement.ProviderData;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fyp.errandmanagement.Adapters.ProviderOrderAdapter;
import com.fyp.errandmanagement.Models.OrderModel;
import com.fyp.errandmanagement.R;

public class CompletedOrderFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<OrderModel> list;
    ProviderOrderAdapter adapter;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_completed_order, container, false);

        recyclerView = v.findViewById(R.id.completedRequestsRecycle);
        progressBar = v.findViewById(R.id.completedProgress);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        FetchData();

        return v;
    }

    private void FetchData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        String providerID = snapshot.child("providerID").getValue().toString();
                        String ostatus = snapshot.child("ostatus").getValue().toString();

                        if(auth.getCurrentUser().getUid().equals(providerID) && !ostatus.equals("Canceled") && ostatus.equals("Completed")){
                            String addr = snapshot.child("addr").getValue().toString();
                            String date = snapshot.child("date").getValue().toString();
                            String day = snapshot.child("day").getValue().toString();
                            String ins = snapshot.child("ins").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            String oname = snapshot.child("oname").getValue().toString();
                            String order = snapshot.child("order").getValue().toString();
                            String orderNO = snapshot.child("orderNO").getValue().toString();
                            String pdate = snapshot.child("pdate").getValue().toString();
                            String phone = snapshot.child("phone").getValue().toString();
                            String price = snapshot.child("price").getValue().toString();
                            String serviceID = snapshot.child("serviceID").getValue().toString();
                            String customerID = snapshot.child("customerID").getValue().toString();
                            String time = snapshot.child("time").getValue().toString();
                            String image = snapshot.child("image").getValue().toString();
                            String servicePriceType = snapshot.child("servicePriceType").getValue().toString();

                            list.add(new OrderModel(orderNO,day,date,time,ins,price,order,addr,name,phone,pdate,oname,ostatus,providerID,serviceID,customerID,image,servicePriceType));
                        }
                    }
                    adapter = new ProviderOrderAdapter(getContext(),list);
                    recyclerView.setAdapter(adapter);

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
}