package com.fyp.errandmanagement.CustomerData;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.errandmanagement.databinding.ActivityCustomerRegistrationBinding;
import com.fyp.errandmanagement.Models.CustomerModel;


public class CustomerRegistration extends AppCompatActivity {

    Uri selectedImage = null;
    Boolean status_pick = false;
    ProgressDialog progressDialog;

    Uri DownloadedUrl;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference reference;
    String Phone= "";

    ActivityCustomerRegistrationBinding binding;

    String longitude = "";
    String latitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_customer_registration);
        binding = ActivityCustomerRegistrationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(this);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        auth = FirebaseAuth.getInstance();
    }

    public void onBackBTNClicked(View v) {
        finish();
    }

    public void onCustomerLocationClicked(View v) {
        Intent i = new Intent(CustomerRegistration.this, CustomerMapActivity.class);
        i.putExtra("addressConform", "start");
        startActivityForResult(i, 15);
    }

    public void onCustomerImgPathClicked(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public void onCustomerSignUpClicked(View v) {
        CheckValidation();
    }

    public void onRedirectLoginClicked(View v) {
        Intent i = new Intent(CustomerRegistration.this, CustomerLogin.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 15 && resultCode == RESULT_OK) {
            binding.customerLocation.setText(data.getStringExtra("address"));
            longitude = data.getStringExtra("longitude");
            latitude = data.getStringExtra("latitude");
        }

        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            selectedImage = data.getData();

            float mb = 0.00f;
            Bitmap bitmap = null;
            long lengthbmp = 0;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            status_pick = true;
            binding.customerImage.setImageBitmap(bitmap);
            binding.customerImgPath.setText(selectedImage.toString());
        }
    }

    private void CheckValidation() {
        String Name = binding.customerName.getText().toString().trim();
        String Email = binding.customerEmail.getText().toString().trim();
        Phone = binding.customerPhone.getText().toString().trim();
        String Password = binding.customerPassword.getText().toString().trim();
        String CNIC = binding.customerCNIC.getText().toString().trim();
        String Location = binding.customerLocation.getText().toString();
        String Path = binding.customerImgPath.getText().toString();

        if (TextUtils.isEmpty(Name)) {
            binding.customerNameLay.setError("Enter Name");
        } else if (TextUtils.isEmpty(Email) || !(Patterns.EMAIL_ADDRESS.matcher(Email).matches())) {
            binding.customerEmailLay.setError("Enter Valid Email");
        } else if (TextUtils.isEmpty(Password)) {
            binding.customerPasswordLay.setError("Enter Password");
        } else if (TextUtils.isEmpty(Phone)) {
            binding.customerPhoneLay.setError("Enter Phone");
        } else if (TextUtils.isEmpty(CNIC)) {
            binding.customerCNICLay.setError("Enter CNIC");
        } else if (TextUtils.isEmpty(Location) || Location.equals("Choose Location")) {
            Toast.makeText(this, "Choose Your Location", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Path)) {
            Toast.makeText(this, "Choose Your Image", Toast.LENGTH_SHORT).show();
        } else {
            UploadCustomerData(Name, Email, Password, Phone, CNIC, Location);
        }
    }

    private void UploadCustomerData(final String name, final String email, final String pass, final String phone, final String cnic, final String address) {
        progressDialog.setMessage("Creating You Account\nPlease Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        if (status_pick) {
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //final String pk = UUID.randomUUID().toString();
                        final StorageReference ref = reference.child("CustomerImages/" + auth.getCurrentUser().getUid());
                        ref.putFile(selectedImage).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful())
                                    throw task.getException();
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    status_pick = false;
                                    DownloadedUrl = task.getResult();
                                    SharedPreferences UserSkypeID = getSharedPreferences("CustomerSkypeID", MODE_PRIVATE);


                                    SharedPreferences.Editor myEdit = UserSkypeID.edit();


                                    myEdit.putString("CustomerID", Phone);


                                    myEdit.apply();
                                    UploadDatainCustomerDatabase(name, email, pass, phone, cnic, address, DownloadedUrl);
                                } else {
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(email, pass);
                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    user.delete()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        progressDialog.dismiss();
                                                                        AlertDialog alertDialog = new AlertDialog.Builder(CustomerRegistration.this).create();
                                                                        alertDialog.setTitle("Registration Failed");
                                                                        alertDialog.setCancelable(false);
                                                                        alertDialog.setMessage("Try Again");
                                                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                });
                                                                        alertDialog.show();
                                                                    } else {
                                                                        progressDialog.dismiss();
                                                                        AlertDialog alertDialog = new AlertDialog.Builder(CustomerRegistration.this).create();
                                                                        alertDialog.setTitle("Registration Failed");
                                                                        alertDialog.setCancelable(false);
                                                                        alertDialog.setMessage("Try Again");
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
                                            });
                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(CustomerRegistration.this).create();
                        alertDialog.setTitle("Registration Failed");
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("Try Again");
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

        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Please Pick Image of less then 2-MB", Toast.LENGTH_SHORT).show();
            status_pick = false;
        }
    }

    private void UploadDatainCustomerDatabase(String name, String email, String pass, String phone, String cnic, String address, Uri downloadedUrl) {

        String token = FirebaseInstanceId.getInstance().getToken();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        final String DateTime = dateFormat.format(date);

        String PrimaryKey = auth.getCurrentUser().getUid();

        CustomerModel values = new CustomerModel(PrimaryKey, name, email, pass, phone, cnic, address, downloadedUrl.toString(), DateTime, latitude, longitude, token);
        FirebaseDatabase.getInstance().getReference("Customers").child(PrimaryKey).setValue(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            status_pick = false;
                            binding.customerImgPath.setText("");

                            Intent i = new Intent(CustomerRegistration.this, CustomerLogin.class);
                            startActivity(i);
                            finish();

                        } else {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(email, pass);
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            user.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                                AlertDialog alertDialog = new AlertDialog.Builder(CustomerRegistration.this).create();
                                                                alertDialog.setTitle("Registration Failed");
                                                                alertDialog.setCancelable(false);
                                                                alertDialog.setMessage("Try Again");
                                                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                alertDialog.show();
                                                            } else {

                                                            }
                                                        }
                                                    });

                                        }
                                    });
                        }
                    }
                });
    }
}