package com.fyp.errandmanagement.ProviderData;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fyp.errandmanagement.R;


public class ProviderMapActivity<FusedLocationProviderClient> extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        //Log.d(TAG,"onMapReady: map is ready");
        mMap = googleMap;


        if (mLocationPermissionGranted) {

            //getDevicelocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                init();
            }
            else{
                getLocationPermission();
            }

        }
    }
    private  static final String TAG="ProviderMapActivity";
    private static final String FINE_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE=1234;
    private GoogleMap mMap;

    //widget
    EditText search_box;
    Button conform_start_point;
    TextView placepicker;
    ImageView modify_icon;

    List<Address> list;
    //VARIABLES
    private Boolean mLocationPermissionGranted =false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map_provider);
        search_box=findViewById(R.id.input_search);

        placepicker=findViewById(R.id.placepicker);
        conform_start_point=findViewById(R.id.conformbtn);
        modify_icon=findViewById(R.id.modify_icon);


        //Show_Location=findViewById(R.id.show_location);
        getLocationPermission();

    }
    private void init()
    {
        search_box.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER)
                {

                    geoLocate();
                }
                return false;
            }
        });

        modify_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();
            }
        });

        hideSoftKeyboard();
    }
    private void geoLocate()
    {
        final String searchStr=search_box.getText().toString();
        Geocoder geocoder=new Geocoder(ProviderMapActivity.this);
        list=new ArrayList<>();
        try {
            list=geocoder.getFromLocationName(searchStr,1);


        }catch (IOException e)
        {
            //Log.e(TAG,"geoLocate: IOException: "+e.getMessage());
        }
        if (list.size()>0)
        {
            final Address address=list.get(0);
            double longitude=address.getLongitude();
            double latitude=address.getLatitude();
            String adres=list.get(0).getAddressLine(0);

            String postalcode=list.get(0).getCountryCode();
            final String fulladdress=(adres+", "+postalcode);
            placepicker.setText(fulladdress);
            conform_start_point.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String  type=getIntent().getStringExtra("addressConform");
                    //Toast.makeText(ProviderMapActivity.this, ""+type, Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences=getSharedPreferences("Address",0);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("address",fulladdress);
                    editor.commit();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("address",fulladdress);
                    returnIntent.putExtra("longitude",Double.toString(longitude));
                    returnIntent.putExtra("latitude",Double.toString(latitude));
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();

                    Toast.makeText(ProviderMapActivity.this, fulladdress, Toast.LENGTH_SHORT).show();
                }
            });
            moveCamra(new LatLng(address.getLatitude(),address.getLongitude()),15f,address.getAddressLine(0));
        }
    }
    /*private void getDevicelocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted)
            {
                Task location=fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            //Log.d(TAG,"onComplete: found location");
                            Location currentlocation = (Location) task.getResult();
                            //Toast.makeText(ProviderMapActivity.this, "this is a current location "+currentlocation, Toast.LENGTH_LONG).show();

                            moveCamra(new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude()),15f,"My Location");
                        }else
                        {
                            //Log.d(TAG,"onComplete: found location null");
                        }
                    }
                });
            }

        }
        catch (SecurityException e){

            //Log.e(TAG, "get device location :SecurityException :" +e.getMessage());

        }

    }*/

    private void moveCamra(LatLng latLng, float zooms, String title)
    {

        //Toast.makeText(this, "moving the camra lat :"+latLng.latitude+"lag :"+latLng.longitude, Toast.LENGTH_SHORT).show();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zooms));
        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions().position(latLng).title(title).snippet(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    private void initMap()
    {
        //Log.d(TAG,"initMap: map is initial");
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(ProviderMapActivity.this);

    }
    private void getLocationPermission(){
        //Log.d(TAG,"getLocationPermission : get permission");
        String[] permission ={Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),

                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),

                    COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted =true;
                initMap();

            }else{
                ActivityCompat.requestPermissions(this,
                        permission,
                        LOCATION_PERMISSION_REQUEST_CODE);


            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permission,
                    LOCATION_PERMISSION_REQUEST_CODE);


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Log.d(TAG,"onRequestPermissionsResult :called");
        mLocationPermissionGranted =false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{

                if(grantResults.length >0){

                    for(int i=0;i< grantResults.length;i++){

                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){

                            mLocationPermissionGranted =false;
                            //Log.d(TAG,"onRequestPermissionsResult :failed permission");
                            return;

                        }
                    }
                    //Log.d(TAG,"onRequestPermissionsResult :granted permission");
                    mLocationPermissionGranted = true;

                    //Intialize our map
                    initMap();



                }
            }




        }
    }

    private void hideSoftKeyboard()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
