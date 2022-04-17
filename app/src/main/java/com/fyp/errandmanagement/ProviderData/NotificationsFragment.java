package com.fyp.errandmanagement.ProviderData;

import android.content.Intent;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fyp.errandmanagement.Adapters.NoticeAdapter;
import com.fyp.errandmanagement.Models.NotificationModel;
import com.fyp.errandmanagement.R;

public class NotificationsFragment extends Fragment {

    FirebaseAuth auth;
    RecyclerView recyclerView;
    ArrayList<NotificationModel> list;
    DatabaseReference reference;
    NoticeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_notifications, container, false);
        Intent sender = new Intent("custom-event-name");
        sender.putExtra("message", "Notifications");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(sender);

        recyclerView = itemView.findViewById(R.id.allNoticeRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();

        FetchNotices();

        return itemView;
    }

    private void FetchNotices() {
        reference = FirebaseDatabase.getInstance().getReference("Notifications");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String providerID = snapshot.child("providerID").getValue().toString();

                    if(auth.getCurrentUser().getUid().equals(providerID)) {
                        String notiTitle = snapshot.child("notiTitle").getValue().toString();
                        String notiBody = snapshot.child("notiBody").getValue().toString();
                        String dateTime = snapshot.child("dateTime").getValue().toString();
                        String customerID = snapshot.child("customerID").getValue().toString();

                        list.add(new NotificationModel(notiTitle,notiBody,providerID,customerID,dateTime));
                    }
                }

                adapter = new NoticeAdapter(getContext(),list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}