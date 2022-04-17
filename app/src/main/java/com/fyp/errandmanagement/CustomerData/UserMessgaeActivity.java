package com.fyp.errandmanagement.CustomerData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.errandmanagement.ProviderData.ProviderMessgaeActivity;
import com.fyp.errandmanagement.databinding.ActivityUserMessgaeBinding;
import com.fyp.errandmanagement.Adapters.MessgaeAdapter;
import com.fyp.errandmanagement.Models.Chat;
import com.fyp.errandmanagement.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skype.android.mobilesdk.SkypeSdkException;
import com.skype.android.mobilesdk.api.Modality;
import com.skype.android.mobilesdk.api.SkypeApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserMessgaeActivity extends AppCompatActivity {

    ActivityUserMessgaeBinding binding;

    String ProviderID = "";
    String name = "";
    String image = "";
    String address = "";
    String email = "";
    String phone = "";

    FirebaseAuth auth;

    List<Chat> list;
    RecyclerView recyclerView;
    MessgaeAdapter adapter;

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_messgae);
        binding = ActivityUserMessgaeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            ProviderID = bundle.getString("ProviderID");
            name = bundle.getString("name");
            image = bundle.getString("image");
            address = bundle.getString("address");
            email = bundle.getString("email");
            phone = bundle.getString("phone");
        }

        binding.imgCallSkype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences ProviderPreference = getSharedPreferences("providerSkypeID", MODE_PRIVATE);
//
//
//                String providerID = ProviderPreference.getString("providerID", "ashdaskj");
//                try {
//                    SkypeApi skypeApi = new SkypeApi(getApplicationContext());
//
//                    skypeApi.startConversation(phone, Modality.AudioCall);
//                } catch (SkypeSdkException e) {
//                    Toast.makeText(UserMessgaeActivity.this,"Couldnt Initage the call",Toast.LENGTH_LONG).show();
//                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(phone));
                startActivity(browserIntent);
//                Toast.makeText(UserMessgaeActivity.this, phone, Toast.LENGTH_LONG).show();
            }
        });



        auth = FirebaseAuth.getInstance();

        Glide.with(this)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(binding.personIMG);

        binding.personName.setText(name);

        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = binding.yourMsg.getText().toString().trim();

                if(!msg.isEmpty()){
                    sendMessage(auth.getCurrentUser().getUid(),
                            ProviderID,
                            msg);
                }
                else
                    Toast.makeText(UserMessgaeActivity.this, "Please Write Message", Toast.LENGTH_SHORT).show();

                binding.yourMsg.setText("");
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.chatCustomerRecycler.setLayoutManager(layoutManager);
        binding.chatCustomerRecycler.setHasFixedSize(true);

        readMessage(auth.getCurrentUser().getUid(), ProviderID, image);
    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessage(String myID, String userID, String imgURL){
        list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String message = snapshot.child("message").getValue().toString();
                    String receiver = snapshot.child("receiver").getValue().toString();
                    String sender = snapshot.child("sender").getValue().toString();
                    if(receiver.equals(myID) && sender.equals(userID) ||
                            receiver.equals(userID) && sender.equals(myID)){
                        list.add(new Chat(sender,receiver,message));
                    }
                    SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);

                    adapter = new MessgaeAdapter(UserMessgaeActivity.this,list,imgURL,shared.getString("image",""));
                    binding.chatCustomerRecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}