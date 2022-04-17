package com.fyp.errandmanagement.ProviderData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import com.fyp.errandmanagement.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class ProviderProfile extends AppCompatActivity {

    ImageView bBtn;
    CircleImageView pImage;
    EditText pName,pPhone,pCnic,pAdd;
    TextView update,updateToken;
    int ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_profile);

        pImage = findViewById(R.id.profileImage);
        pName = findViewById(R.id.profileName);
        pPhone = findViewById(R.id.profilePhone);
        pCnic = findViewById(R.id.profileCnic);
        pAdd = findViewById(R.id.profileAddress);
        update = findViewById(R.id.updateButton);
        updateToken = findViewById(R.id.updateToken);

        SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);

        Glide.with(this).load(shared.getString("image","")).into(pImage);
        pName.setText(shared.getString("name",""));
        pPhone.setText(shared.getString("phone",""));
        pCnic.setText(shared.getString("cnic",""));
        pAdd.setText(shared.getString("address",""));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(pName.getText().toString()) || TextUtils.isEmpty(pPhone.getText().toString()) || TextUtils.isEmpty(pCnic.getText().toString()) || TextUtils.isEmpty(pAdd.getText().toString())){
                    Toast.makeText(ProviderProfile.this, "Enter Required Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    UpdateinFirebase(pName.getText().toString(),
                            pCnic.getText().toString(),
                            pPhone.getText().toString(),
                            pAdd.getText().toString());
                }
            }
        });

        updateToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Token = FirebaseInstanceId.getInstance().getToken();
                FirebaseAuth auth;
                auth = FirebaseAuth.getInstance();
                final ProgressDialog p = new ProgressDialog(ProviderProfile.this);
                p.setMessage("Updating Token...");
                p.show();
                p.setCancelable(false);
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child(auth.getCurrentUser().getUid());
                Map<String, Object> updates = new HashMap<String,Object>();
                updates.put("fcmToken", Token);
                ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            p.dismiss();
                            Toast.makeText(ProviderProfile.this, "Token Updated", Toast.LENGTH_SHORT).show();
                            SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putString("fcmToken", Token);
                            editor.commit();
                        }
                        else {
                            Toast.makeText(ProviderProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                            p.dismiss();
                        }
                    }
                });
            }
        });
    }

    private void UpdateinFirebase(final String name, final String cnic, final String phone, final String address) {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Updating...");
        p.show();
        p.setCancelable(false);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child(auth.getCurrentUser().getUid());
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("name", name);
        updates.put("cnic", cnic);
        updates.put("phone", phone);
        updates.put("address", address);
        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    p.dismiss();
                    Toast.makeText(ProviderProfile.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("name", name);
                    editor.putString("cnic", cnic);
                    editor.putString("phone", phone);
                    editor.putString("address", address);
                    editor.commit();
                }
                else {
                    Toast.makeText(ProviderProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                    p.dismiss();
                }
            }
        });
    }
}