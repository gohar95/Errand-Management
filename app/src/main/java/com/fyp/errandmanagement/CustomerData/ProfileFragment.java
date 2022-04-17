package com.fyp.errandmanagement.CustomerData;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import de.hdodenhof.circleimageview.CircleImageView;
import com.fyp.errandmanagement.R;

public class ProfileFragment extends Fragment {

    ImageView bBtn;
    CircleImageView pImage;
    EditText pName,pPhone,pCnic,pAdd;
    TextView update,updateToken;
    int ID = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_profile, container, false);

        pImage = itemView.findViewById(R.id.profileImage);
        pName = itemView.findViewById(R.id.profileName);
        pPhone = itemView.findViewById(R.id.profilePhone);
        pCnic = itemView.findViewById(R.id.profileCnic);
        pAdd = itemView.findViewById(R.id.profileAddress);
        update = itemView.findViewById(R.id.updateButton);
        updateToken = itemView.findViewById(R.id.updateToken);

        SharedPreferences shared = getActivity().getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);

        Glide.with(getActivity()).load(shared.getString("image","")).into(pImage);
        pName.setText(shared.getString("name",""));
        pPhone.setText(shared.getString("phone",""));
        pCnic.setText(shared.getString("cnic",""));
        pAdd.setText(shared.getString("address",""));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(pName.getText().toString()) || TextUtils.isEmpty(pPhone.getText().toString()) || TextUtils.isEmpty(pCnic.getText().toString()) || TextUtils.isEmpty(pAdd.getText().toString())){
                    Toast.makeText(getContext(), "Enter Required Fields", Toast.LENGTH_SHORT).show();
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
                final ProgressDialog p = new ProgressDialog(getContext());
                p.setMessage("Updating Token...");
                p.show();
                p.setCancelable(false);
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Customers").child(auth.getCurrentUser().getUid());
                Map<String, Object> updates = new HashMap<String,Object>();
                updates.put("fcmToken", Token);
                ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            p.dismiss();
                            Toast.makeText(getContext(), "Token Updated", Toast.LENGTH_SHORT).show();
                            SharedPreferences shared = getActivity().getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putString("fcmToken", Token);
                            editor.commit();
                        }
                        else {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            p.dismiss();
                        }
                    }
                });
            }
        });

        return itemView;
    }

    private void UpdateinFirebase(final String name, final String cnic, final String phone, final String address) {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final ProgressDialog p = new ProgressDialog(getContext());
        p.setMessage("Updating...");
        p.show();
        p.setCancelable(false);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Customers").child(auth.getCurrentUser().getUid());
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
                    Toast.makeText(getContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                    SharedPreferences shared = getActivity().getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("name", name);
                    editor.putString("cnic", cnic);
                    editor.putString("phone", phone);
                    editor.putString("address", address);
                    editor.commit();
                }
                else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    p.dismiss();
                }
            }
        });
    }
}