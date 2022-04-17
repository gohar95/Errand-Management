package com.fyp.errandmanagement.CustomerData;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import com.fyp.errandmanagement.MailSender;
import com.fyp.errandmanagement.R;

public class CancleOrder extends Fragment {

    EditText oID;
    RelativeLayout printOrder;
    TextView morderID,morderNAME,morderQUAN,morderPLAN,morderTIME,morderCOST;
    Button cancleButton,orderSearch;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_cancle_order, container, false);

        oID = v.findViewById(R.id.orderIID);
        printOrder = v.findViewById(R.id.printOrder);
        morderID = v.findViewById(R.id.morderID);
        morderNAME = v.findViewById(R.id.morderNAME);
        morderQUAN = v.findViewById(R.id.morderQUAN);
        morderPLAN = v.findViewById(R.id.morderPLAN);
        morderTIME = v.findViewById(R.id.morderTIME);
        morderCOST = v.findViewById(R.id.morderCOST);
        cancleButton = v.findViewById(R.id.cancleButton);
        orderSearch = v.findViewById(R.id.orderSearch);
        progressDialog = new ProgressDialog(getActivity());

        orderSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplyValidation();
            }
        });

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartCanceling();
            }
        });

        return v;
    }

    private void ApplyValidation() {
        if(TextUtils.isEmpty(oID.getText().toString().trim())){
            oID.setError("Field is Required");
        }
        else{
            progressDialog.setMessage("Searching...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
            reference2.child("Orders").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot !=null){
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                            if(snapshot.getKey().equals("SC-"+oID.getText().toString())){
                                String addr = snapshot.child("addr").getValue().toString();
                                String date = snapshot.child("date").getValue().toString();
                                String day = snapshot.child("day").getValue().toString();
                                String ins = snapshot.child("ins").getValue().toString();
                                String orderNO = snapshot.child("orderNO").getValue().toString();
                                String name = snapshot.child("name").getValue().toString();
                                String oname = snapshot.child("oname").getValue().toString();
                                String order = snapshot.child("order").getValue().toString();
                                String ostatus = snapshot.child("ostatus").getValue().toString();
                                String pdate = snapshot.child("pdate").getValue().toString();
                                String phone = snapshot.child("phone").getValue().toString();
                                String price = snapshot.child("price").getValue().toString();
                                String customerID = snapshot.child("customerID").getValue().toString();
                                String providerID = snapshot.child("providerID").getValue().toString();
                                String serviceID = snapshot.child("serviceID").getValue().toString();
                                String time = snapshot.child("time").getValue().toString();

                                printOrder.setVisibility(View.VISIBLE);
                                morderID.setText(orderNO);
                                morderNAME.setText(oname);
                                morderQUAN.setText(ostatus);
                                morderPLAN.setText(day+" "+date);
                                morderTIME.setText(time);
                                morderCOST.setText("Rs. "+price);

                                if(ostatus.equals("Canceled")){
                                    morderQUAN.setTextColor(Color.parseColor("#af0000"));
                                }
                                if(ostatus.equals("Pending")){
                                    morderQUAN.setTextColor(Color.parseColor("#0B67C0"));
                                }
                                if(ostatus.equals("Approved")){
                                    morderQUAN.setTextColor(Color.parseColor("#0ea71e"));
                                }
                                if(ostatus.equals("Completed")){
                                    morderQUAN.setTextColor(Color.parseColor("#FF7200"));
                                }
                            }
                        }
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    printOrder.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "There is No Order", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void StartCanceling() {
        if(TextUtils.isEmpty(oID.getText().toString().trim())){
            oID.setError("Field is Required");
        }
        else{
            progressDialog.setMessage("Canceling...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Orders").child("SC-"+oID.getText().toString());
            Map<String, Object> updates = new HashMap<String,Object>();
            updates.put("ostatus", "Canceled");
            ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        SharedPreferences shared = getActivity().getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);
                        SendSMS(shared.getString("email",""),
                                                "SC-"+oID.getText().toString(),
                                                    shared.getString("name",""));
                        Toast.makeText(getContext(), "Order Updated", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });

            /*FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("Orders").child("SC-"+oID.getText().toString());
            myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        printOrder.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Order Canceled", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.dismiss();
                        printOrder.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Canceling Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });*/
        }
    }

    public void SendSMS(String email, String pin, String name)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new SendMail(pin,email,name).execute();
    }

    private class SendMail extends AsyncTask<String, Void, Integer>
    {
        String error = null;
        Integer result;
        String pincode;
        String email;
        String name;

        public SendMail(String pincode, String email, String name) {
            this.pincode = pincode;
            this.email = email;
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub

                        MailSender sender = new MailSender("maradeveloper9@gmail.com", "#08mano*s");

            sender.setTo(new String[]{email});
            sender.setFrom("maradeveloper9@gmail.com");
            sender.setSubject("HAND-AID SUPPORT CENTER");
            sender.setBody("ORDER CANCELLATION of your HAND-AID Account\n\n" +
                    "Hey "+name+"\n\n" +
                    "Thanks for using hand-aid an Account with the following email address : "+email+"\n\n" +
                    "Your Order has been canceled with the following Order ID : "+pincode+"\n\n" +
                    "You can verify the status of your order in your account");
            try {
                if(sender.send()) {
                    System.out.println("Message sent");
                    return 1;
                } else {
                    return 2;
                }
            } catch (Exception e) {
                error = e.getMessage();
            }

            return 3;
        }

        protected void onPostExecute(Integer result) {
            progressDialog.dismiss();
            if(error!=null) {

            }
            if(result==1) {
                Toast.makeText(getContext(),
                        "Order Canceled successfully.", Toast.LENGTH_LONG).show();
            }
        }
    }
}