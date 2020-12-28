package com.example.mylocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText etSearch;
    ListView lvResult;

    ArrayList<MyLocation> locationList = new ArrayList<MyLocation>();
    ArrayList<MyLocation> locationFilter = new ArrayList<MyLocation>();
    MyAdapterLocation adapterLocation;

    String regex = "";
    int cnt = 0;

    MyDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database =new MyDatabase(this);

        etSearch = findViewById(R.id.etSearch);
        lvResult = findViewById(R.id.lvResult);

        adapterLocation = new MyAdapterLocation(this, locationFilter);
        lvResult.setAdapter(adapterLocation);
        lvResult.setOnItemClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regex=charSequence.toString();
                refreshFilter();
            }
            public void afterTextChanged(Editable editable) {}
        });

        runtime_permission();

        locationList = database.getAllContact();
        refreshFilter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

            }else{
                runtime_permission();
            }
        }
    }

    private boolean runtime_permission() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }

    public void refreshFilter(){
        locationFilter.clear();
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        cnt = 0;
        for(MyLocation loc:locationList){
            Matcher m = p.matcher(loc.n);
            if(m.find()){
                locationFilter.add(loc);
                cnt++;
            }
        }
        adapterLocation.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_location_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, AddLocation.class);
        startActivityForResult(intent, 0);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 0){
                Bundle b =data.getExtras();
                String name =b.getString("name");
                double la = b.getDouble("la");
                double lo = b.getDouble("lo");

                MyLocation myLocation= new MyLocation(name, la, lo);
                database.addContact(myLocation);
                locationList = database.getAllContact();

                refreshFilter();
                Toast.makeText(this, "Location saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MyLocation loc = locationFilter.get(i);
        String name = loc.n;
        double la = loc.la;
        double lo = loc.lo;

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("la", la);
        intent.putExtra("lo", lo);

        startActivity(intent);
    }
}