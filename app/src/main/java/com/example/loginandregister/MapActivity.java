package com.example.loginandregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandregister.Model.Add_Stop_Point_Data;
import com.example.loginandregister.Model.Add_Stop_Point_Result;
import com.example.loginandregister.Model.Stop_Point;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCAITON_PERMISSION_REQUEST_CODE=1234;
    private static final float DEFAULT_ZOOM=15f;
    private final static int PLACE_PICKER_REQUEST = 111;
    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView imageView;
    private ImageView gps;
    private Button pk;
    //vars
    private Boolean mLocationPermissionGranted=false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    Dialog dialog;
    Dialog add_Stop_Point_Dialog;
    private String token;
    private int id;
    private ArrayList<Stop_Point> arrayListStopPoint=new ArrayList<>();
    private ArrayList<Stop_Point> temp=new ArrayList<>();

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    public static final String URL="http://35.197.153.192:3000/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSearchText=findViewById(R.id.input_search);
        imageView=findViewById(R.id.magnifi);
        gps=findViewById(R.id.gps);
        pk=findViewById(R.id.pk);
        Bundle bundle=getIntent().getExtras();
        token=bundle.getString("token");
        id=bundle.getInt("ID");
        dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        add_Stop_Point_Dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                getDeviceLocation();
            }
        });
        pk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlacePicker();
            }
        });
        getLocationPermission();
    }
    private void init()
    {
        Log.d(TAG,"init: initializing alo123");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText.setFocusable(true);
                hideSoftKeyBoard();
                String temp=mSearchText.getText().toString();
                geoLocate(temp);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PLACE_PICKER_REQUEST && requestCode==RESULT_OK)
        {
            Place place = PlacePicker.getPlace(this, data);
            String placeName = String.format("Place: %s", place.getName());
            double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;
            LatLng coordinate = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(coordinate);
            markerOptions.title(placeName); //Here Total Address is address which you want to show on marker
            mMap.clear();
            markerOptions.icon(
                    BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            markerOptions.getPosition();
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    private void geoLocate(String temp) {
        Log.d(TAG,"geoLocate: geolocating");
        String searchString=temp;
        Geocoder geocoder=new Geocoder(MapActivity.this);
        List<Address> list=new ArrayList<>();
        try {
            list=geocoder.getFromLocationName(searchString,1);
        }
        catch (IOException e){
            Log.e(TAG,"geoLocate: IOException "+e.getMessage());
        }
        if (list.size()>0)
        {
            Address address=list.get(0);
            Log.d(TAG,"geoLocate:  found a location "+address.toString());
            Toast.makeText(MapActivity.this,address.getFeatureName()+", "+address.getCountryName()+" ,Lat: "+address.getLatitude()+" ,Long: "+address.getLongitude(),Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));

        }
    }

    private void getDeviceLocation()
    {
        Log.d(TAG,"getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted)
            {
                Task location=mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            Log.d(TAG,"onComplete: found location");
                            Location currentLocation=(Location)task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM,"My Location");
                        }
                        else
                        {
                            Log.d(TAG,"onComplete: current location is null");
                            Toast.makeText(MapActivity.this,"unable to get current location",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e)
        {
            Log.e(TAG,"getDeviceLocation: SecurityException+ "+e.getMessage());
        }
    }
    private void moveCamera(LatLng latLng,float zoom,String title)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        MarkerOptions options=new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                TextView name_country;
                TextView stop_Point_Infor;
                dialog.setContentView(R.layout.popup);
                name_country=dialog.findViewById(R.id.name_country);
                stop_Point_Infor=dialog.findViewById(R.id.stop_point_information);
                stop_Point_Infor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_Stop_Point_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        add_Stop_Point_Dialog.setContentView(R.layout.add_stop_point);
                        final EditText diemxuatphat=add_Stop_Point_Dialog.findViewById(R.id.diem_xuat_phat);
                        final EditText type=add_Stop_Point_Dialog.findViewById(R.id.restaurant);
                        final EditText address=add_Stop_Point_Dialog.findViewById(R.id.addresstext);
                        address.setText(marker.getTitle()+" / "+ marker.getSnippet());
                        final EditText province=add_Stop_Point_Dialog.findViewById(R.id.provincetext);
                        //province.setText(ID);
                        final EditText mMinCost=add_Stop_Point_Dialog.findViewById(R.id.min_cost);
                        final EditText mMaxCost=add_Stop_Point_Dialog.findViewById(R.id.max_cost);
                        final EditText mArDate=add_Stop_Point_Dialog.findViewById(R.id.date);
                        final EditText mLeDate=add_Stop_Point_Dialog.findViewById(R.id.leave_at);
                        final Button ok_button=add_Stop_Point_Dialog.findViewById(R.id.ok_btt);
                        add_Stop_Point_Dialog.show();
                        ok_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String mDiemXuatPhat=diemxuatphat.getText().toString();
                                String mAddress=address.getText().toString();
                                int mType=Integer.parseInt(type.getText().toString());
                                int mProvince=Integer.parseInt(province.getText().toString());
                                int mMin_Cots=Integer.parseInt(mMinCost.getText().toString());
                                int mMax_Cost=Integer.parseInt(mMaxCost.getText().toString());
                                long mAr_Date=Long.parseLong(mArDate.getText().toString());
                                long mLe_Date=Long.parseLong(mLeDate.getText().toString());

                                LatLng mLatLng=marker.getPosition();
                                double mLat=mLatLng.latitude;
                                double mLong=mLatLng.longitude;


                                Stop_Point stop_point=new Stop_Point(1,mDiemXuatPhat,mAddress,1,mLat,mLong,mAr_Date,mLe_Date,mType,mMin_Cots,mMax_Cost);
                                arrayListStopPoint.add(stop_point);
                                temp.add(stop_point);

                                Add_Stop_Point_Data add_stop_point_data=new Add_Stop_Point_Data(id,arrayListStopPoint);
                                Map<String,String> map=new HashMap<>();
                                map.put("Authorization",token);


                                Call<Add_Stop_Point_Result> call=jsonPlaceHolderApi.addStopPoint(map,add_stop_point_data);
                                call.enqueue(new Callback<Add_Stop_Point_Result>() {
                                    @Override
                                    public void onResponse(Call<Add_Stop_Point_Result> call, Response<Add_Stop_Point_Result> response) {
                                        if(!response.isSuccessful())
                                        {
                                            Toast.makeText(MapActivity.this,"Ko Thanh cong"+response.code()+response.body(),Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(MapActivity.this,"Thanh cong",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Add_Stop_Point_Result> call, Throwable t) {
                                        Toast.makeText(MapActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                arrayListStopPoint.clear();

                            }
                        });


                    }
                });
                name_country.setText(marker.getTitle());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                return false;
            }
        });
    }
    private void initMap()
    {
        Log.d(TAG,"initMap: initializing map");
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Toast.makeText(MapActivity.this,"Map is ready" +id,Toast.LENGTH_SHORT).show();
                Log.d(TAG,"onMapReady: map is ready");
                mMap=googleMap;
                if (mLocationPermissionGranted)
                {
                    getDeviceLocation();
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    init();
                }
            }
        });
    }
    private void getLocationPermission()
    {
        Log.d(TAG,"getLocationPermission: getting location permission ");
        String[] permission={Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                mLocationPermissionGranted=true;
                initMap();
            }
            else
            {
                ActivityCompat.requestPermissions(this,permission, LOCAITON_PERMISSION_REQUEST_CODE);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this,permission, LOCAITON_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult: called");
        mLocationPermissionGranted=false;
        switch (requestCode)
        {
            case LOCAITON_PERMISSION_REQUEST_CODE:
            {
                if(grantResults.length>0)
                {
                    for (int i=0;i<grantResults.length;i++)
                    {
                        if (grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationPermissionGranted=false;
                            Log.d(TAG,"onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG,"onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted=true;
                    initMap();
                }
            }
        }
    }
    private void hideSoftKeyBoard()
    {
        if (mSearchText.isFocusable() && mSearchText.onCheckIsTextEditor()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(MapActivity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void openPlacePicker() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            // for activty
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            // for fragment
            //startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
