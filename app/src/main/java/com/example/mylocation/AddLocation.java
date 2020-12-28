package com.example.mylocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddLocation extends AppCompatActivity implements View.OnClickListener, LocationListener {

    EditText etaddlocationname;
    TextView tvlatitudevalue, tvlongitudevalue;
    Button btnSave;

    LocationManager locman;
    Location loc;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        etaddlocationname = findViewById(R.id.etaddlocationname);
        tvlatitudevalue = findViewById(R.id.tvlatitudevalue);
        tvlongitudevalue = findViewById(R.id.tvlongitudevalue);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        locman = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // set Location
        if (locman.isProviderEnabled(LocationManager.GPS_PROVIDER))
            provider = LocationManager.GPS_PROVIDER;
        else if (locman.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            provider = LocationManager.NETWORK_PROVIDER;

        // request location update from provider
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locman.requestLocationUpdates(provider, 0, 0, this);
        // request location value from a provider
        loc=locman.getLastKnownLocation(provider);
        if(loc!=null){
            tvlatitudevalue.setText(String.format("%.4f degress",loc.getLatitude()));
            tvlongitudevalue.setText(String.format("%.4f degress",loc.getLongitude()));
        }else{
            tvlatitudevalue.setText("Error Location Value");
            tvlongitudevalue.setText("Error Location Value");
        }
    }

    @Override
    public void onClick(View view) {
        int id= view.getId();
        if(id==R.id.btnSave){
            String locname = etaddlocationname.getText().toString();
            if(!locname.equals("") && loc!=null){
                Intent intent = new Intent();
                intent.putExtra("name", locname);
                intent.putExtra("la", loc.getLatitude());
                intent.putExtra("lo", loc.getLongitude());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onLocationChanged(@NonNull Location location) {}
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    public void onProviderEnabled(@NonNull String provider) {}
    public void onProviderDisabled(@NonNull String provider) {}
}