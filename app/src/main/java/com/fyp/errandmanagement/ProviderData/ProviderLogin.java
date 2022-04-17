package com.fyp.errandmanagement.ProviderData;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.fyp.errandmanagement.databinding.ActivityProviderLoginBinding;
import com.fyp.errandmanagement.Notifications;

public class ProviderLogin extends AppCompatActivity {

    ProgressDialog progressDialog;
    FirebaseAuth auth;
    SharedPreferences shared;
    boolean status = false;

    ActivityProviderLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_provider_login);
        binding = ActivityProviderLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        auth = FirebaseAuth.getInstance();
        shared = getSharedPreferences("USER_PROFILE", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
    }

    public void onBackBTNClicked(View v) {
        finish();
    }

    public void onProviderSignInClicked(View v) {
        String Email = binding.providerEmail.getText().toString().trim();
        String Password = binding.providerPassword.getText().toString().trim();

        if(TextUtils.isEmpty(Email) || !(Patterns.EMAIL_ADDRESS.matcher(Email).matches())){
            binding.providerEmailLay.setError("Enter Valid Email");
        } else if(TextUtils.isEmpty(Password)){
            binding.providerPasswordLay.setError("Enter Password Here");
        }else{
            LoginProvider(Email,Password);
        }
    }

    private void LoginProvider(final String email, String password) {
        progressDialog.setMessage("Login\nPlease Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FETCH_USER_DETAILS("ServiceProviders",email);
                        }
                        else{
                            progressDialog.dismiss();
                            //Alert Dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(ProviderLogin.this).create();
                            alertDialog.setTitle("Login Failed");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Incorrect\nEmail or Password\nTry Again");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                });
    }

    private void FETCH_USER_DETAILS(final String serviceProviders, final String Email) {
        progressDialog.setTitle("Fetching User Data");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(serviceProviders).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String email = snapshot.child("email").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String image = snapshot.child("image").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();
                        String cnic = snapshot.child("cnic").getValue().toString();
                        String dateTime = snapshot.child("dateTime").getValue().toString();
                        String key = snapshot.child("key").getValue().toString();
                        String password = snapshot.child("password").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String fcmToken = snapshot.child("fcmToken").getValue().toString();

                        if(email.equals(Email)){
                            status = true;
                            progressDialog.dismiss();
                            Intent i = new Intent(ProviderLogin.this, ProviderDashboard.class);
                            SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putString("STATUS", serviceProviders);
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("image", image);
                            editor.putString("address", address);
                            editor.putString("cnic", cnic);
                            editor.putString("dateTime", dateTime);
                            editor.putString("key", key);
                            editor.putString("password", password);
                            editor.putString("phone", phone);
                            editor.putString("fcmToken", fcmToken);
                            editor.apply();

                            Notifications.createNotificationChannel(ProviderLogin.this);

                            startActivity(i);
                            Toast.makeText(ProviderLogin.this, "Success", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    if(!status){
                        progressDialog.dismiss();
                        //Alert Dialog

                        auth.signOut();
                        AlertDialog alertDialog = new AlertDialog.Builder(ProviderLogin.this).create();
                        alertDialog.setTitle("Login Failed");
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("Incorrect\nEmail or Password\nTry Again");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
                else{
                    progressDialog.dismiss();
                    //Alert Dialog
                    auth.signOut();
                    AlertDialog alertDialog = new AlertDialog.Builder(ProviderLogin.this).create();
                    alertDialog.setTitle("Login Failed");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Incorrect\nEmail or Password\nTry Again");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                //Alert Dialog
                auth.signOut();
                AlertDialog alertDialog = new AlertDialog.Builder(ProviderLogin.this).create();
                alertDialog.setTitle("Login Failed");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Incorrect\nEmail or Password\nTry Again");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    public void onRedirectRegisterClicked(View v) {
        Intent i = new Intent(ProviderLogin.this, ProviderRegistration.class);
        startActivity(i);
        finish();
    }

    public void onForgotClicked(View v) {
        String Email = binding.providerEmail.getText().toString().trim();
        if(TextUtils.isEmpty(Email) || !(Patterns.EMAIL_ADDRESS.matcher(Email).matches())){
            binding.providerEmailLay.setError("Enter Valid Email");
        }
        else {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Password Resetting");
            dialog.setMessage("Please Wait");
            dialog.setCancelable(false);
            dialog.show();
            FirebaseAuth.getInstance().sendPasswordResetEmail(Email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(ProviderLogin.this, "Password Reset Email sent to your Mail Address", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                dialog.dismiss();
                                Toast.makeText(ProviderLogin.this, "Password Reset Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}