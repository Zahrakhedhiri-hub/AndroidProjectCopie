package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMap extends AppCompatActivity {
//Initialize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    Button button;
    String myCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        button=findViewById(R.id.buttonMap);
        Intent intent=getIntent();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("myLog","Complete Adresse: "+myCity);

                Intent ReponseIntent =new Intent();
                ReponseIntent.putExtra("reponse",myCity);
                setResult(RESULT_OK,ReponseIntent);
                finish();
            }
        });

        //Assign variable
        supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        //Initialize fused location
        client= LocationServices.getFusedLocationProviderClient(this);
        //check permission
        if(ActivityCompat.checkSelfPermission(GoogleMap.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

getCurrentLocation();
        }else {
            //when permission denied
            ////////request permission
            ActivityCompat.requestPermissions(GoogleMap.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},44);

        }
    }

    private void getCurrentLocation() {
        Task<Location> task =client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if(location !=null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
                            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
String cityName=getCityName(latLng);
                            //Create maker options
                            MarkerOptions options=new MarkerOptions().position(latLng).title("I am here");

                            ////Zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }
public String getCityName(LatLng lng){

    Geocoder geocoder= new Geocoder(GoogleMap.this, Locale.getDefault());
    try {
        List<Address> adresses=geocoder.getFromLocation(lng.latitude,lng.longitude,1);
        String adress=adresses.get(0).getAddressLine(0);
        myCity=adresses.get(0).getLocality();
        Log.d("myLog","Complete Adresse: "+adresses.toString());
        Log.d("myLog"," Adresse: "+adress);


    } catch (IOException e) {
        e.printStackTrace();
    }
    return  myCity;}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==44){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                //when permission grated
                //call method
                getCurrentLocation();
            }
        }

    }


}
