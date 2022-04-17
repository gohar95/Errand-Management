package com.fyp.errandmanagement.ProviderData;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.fyp.errandmanagement.databinding.ActivityPostingServicesBinding;
import com.fyp.errandmanagement.Models.AnyServiceModel;
import com.fyp.errandmanagement.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PostingServices extends AppCompatActivity {

    ActivityPostingServicesBinding binding;
    String TYPE = "";
    ProgressDialog progressDialog;
    String serviceType = "";
    String priceType = "";


    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_posting_services);
        binding = ActivityPostingServicesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            TYPE = bundle.getString("TYPE");
        }

        progressDialog = new ProgressDialog(this);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        auth = FirebaseAuth.getInstance();

        if("Check Events".equals(TYPE)){
            binding.tit.setText("Add Ne w\nFood Errand");
        }else  if("Book A Mission".equals(TYPE)){
            binding.tit.setText("Add New\nGrocerry Errand");
        }else  if("Find a Avatar".equals(TYPE)){
            binding.tit.setText("Add New\nDocument Errand");
        }else  if("Gifts".equals(TYPE)){
            binding.tit.setText("Add New\nGifts Errand");
        }else  if("Goods".equals(TYPE)){
            binding.tit.setText("Add New\nGoods Errand");
        }else  if("Furniture".equals(TYPE)){
            binding.tit.setText("Add New\nFurniture Errand");
        }else  if("Luggage".equals(TYPE)){
            binding.tit.setText("Add New\nLuggage Errand");
        }   else{
            binding.tit.setText("Add New\nService");
            binding.headingImage.setImageResource(R.drawable.app_logo);
        }

        binding.headingName.setText(TYPE);

        MaterialSpinner spinnerPriceType = findViewById(R.id.spinnerPriceType);
        spinnerPriceType.setItems("Select","per hour", "visit charges", "per day", "overall");
        spinnerPriceType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                priceType = item;
            }
        });

        MaterialSpinner spinnerServiceType = findViewById(R.id.spinnerServiceType);
        spinnerServiceType.setItems("Home Delivery","Custom Location");
        spinnerServiceType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                serviceType = item;
            }
        });


    }



    public void onPostServiceClicked(View v){

        String serviceName = binding.serviceName.getText().toString().trim();
        String serviceDesc = binding.serviceDesc.getText().toString().trim();
        String servicePrice = binding.servicePrice.getText().toString().trim();
        //Adding more Services to this App

        if(TextUtils.isEmpty(serviceName))
            binding.serviceNameLay.setError("Enter Title");
        else if(TextUtils.isEmpty(serviceDesc))
            binding.serviceDescLay.setError("Enter Description");
        else if(TextUtils.isEmpty(servicePrice))
            binding.servicePriceLay.setError("Enter Price");
        else if("Select".equals(serviceType))
            Toast.makeText(this, "Please Choose Service Type", Toast.LENGTH_SHORT).show();
        else if("Select".equals(priceType))
            Toast.makeText(this, "Please Choose Price Type", Toast.LENGTH_SHORT).show();
        else{
            if("Check Events".equals(TYPE)){
                UploadService(serviceName,serviceDesc,serviceType,servicePrice,priceType);
            }else  if("Book A Mission".equals(TYPE)){
                UploadService(serviceName,serviceDesc,serviceType,servicePrice,priceType);
            }else  if("Find a Avatar".equals(TYPE)){
                UploadService(serviceName,serviceDesc,serviceType,servicePrice,priceType);
            }else  if("Gifts".equals(TYPE)){
                UploadService(serviceName,serviceDesc,serviceType,servicePrice,priceType);
            }else  if("Goods".equals(TYPE)){
                UploadService(serviceName,serviceDesc,serviceType,servicePrice,priceType);
            }else  if("Furniture".equals(TYPE)){
                UploadService(serviceName,serviceDesc,serviceType,servicePrice,priceType);
            }else  if("Luggage".equals(TYPE)){
                UploadService(serviceName,serviceDesc,serviceType,servicePrice,priceType);
            }else{
                Toast.makeText(this, "Failed to Add Service", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void UploadService(final String name, final String desc, final String type, final String price, final String ptype) {
        progressDialog.setMessage("Creating Your Service\nPlease Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String ServiceKey = UUID.randomUUID().toString();
        UploadDatainServiceDatabase(name, desc, type, price, ptype, ServiceKey);

    }

    private void UploadDatainServiceDatabase(String name, String desc, String type, String price, String ptype, String serviceKey) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        final String DateTime = dateFormat.format(date);

        String PrimaryKey = auth.getCurrentUser().getUid();
        String sTYPE = "";
        String sCAT = "";

        if("Check Events".equals(TYPE)){
            sTYPE = "Check Events";
            sCAT = "Check Events";
        }if("Book A Mission".equals(TYPE)){
            sTYPE = "Book A Mission";
            sCAT = "Book A Mission";
        }if("Find a Avatar".equals(TYPE)){
            sTYPE = "Find a Avatar";
            sCAT = "Find a Avatar";
        }if("Gifts".equals(TYPE)){
            sTYPE = "Gifts";
            sCAT = "Gifts";
        }if("Goods".equals(TYPE)){
            sTYPE = "Goods";
            sCAT = "Goods";
        }if("Furniture".equals(TYPE)){
            sTYPE = "Furniture";
            sCAT = "Furniture";
        }if("Luggage".equals(TYPE)){
            sTYPE = "Luggage";
            sCAT = "Luggage";
        }


        AnyServiceModel values = new AnyServiceModel(serviceKey,auth.getCurrentUser().getUid(),name,desc,type,price,ptype,DateTime,sCAT,"0");
        FirebaseDatabase.getInstance().getReference("Services").child(sTYPE).child(PrimaryKey).child(serviceKey).setValue(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();


                            finish();

                        }else{
                            progressDialog.dismiss();
                            //Alert Dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                            alertDialog.setTitle("Uploading Failed");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Due to\nTechnical Issue\nTry Again");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
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
}