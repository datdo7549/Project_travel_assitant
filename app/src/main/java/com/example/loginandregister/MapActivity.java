package com.example.loginandregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.loginandregister.Adapter.CustomAdapterForListStopPoint;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private ImageView list_stop_point_selected;
    private Button pk;
    //vars
    private Boolean mLocationPermissionGranted=false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    Dialog dialog;
    Dialog add_Stop_Point_Dialog;
    Dialog list_stop_point_selected_dialog;
    private String token;
    private int id;
    private ArrayList<Stop_Point> arrayListStopPoint=new ArrayList<>();
    private ArrayList<Stop_Point> temp=new ArrayList<>();
    public static final String[] arrayProvince={"Hồ Chí Minh","Hà Nội","Đà Nẵng","Bình Dương","Đồng Nai","Khánh Hòa", "Hải Phòng", "Long An", "Quảng Nam", "Bà Rịa Vũng Tàu", "Đắk Lắk","Cần Thơ",
            "Bình Thuận", "Lâm Đồng", "Thừa Thiên Huế", "Kiên Giang", "Bắc Ninh", "Quảng Ninh", "Thanh Hóa", "Nghệ An", "Hải Dương", "Gia Lai", "Bình Phước", "Hưng Yên", "Bình Định", "Tiền Giang",
            "Thái Bình", "Bắc Giang", "Hòa Bình", "An Giang", "Vĩnh Phúc", "Tây Ninh", "Thái Nguyên", "Lào Cai","Nam Định","Quảng Ngãi", "Bến Tre", "Đắk Nông", "Cà Mau", "Vĩnh Long",
            " Ninh Bình", "Phú Thọ", "Ninh Thuận", "Phú Yên", "Hà Nam", "Hà Tĩnh", "Đồng Tháp", "Sóc Trăng", "Kon Tum", "Quảng Bình", "Quảng Trị", "Trà Vinh", "Hậu Giang", "Sơn La", "Bạc Liêu", "Yên Bái",
            "Tuyên Quang", "Điện Biên", "Lai Châu", "Lạng Sơn", "Hà Giang", "Bắc Kạn", "Cao Bằng"};
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    public static final String URL="http://35.197.153.192:3000/";
    private int TYPE;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_map);

        Bundle bundle=getIntent().getExtras();
        TYPE=bundle.getInt("type",0);
        mSearchText=findViewById(R.id.input_search);
        imageView=findViewById(R.id.magnifi);
        gps=findViewById(R.id.gps);
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


        token=bundle.getString("token");
        id=bundle.getInt("ID");
        dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);


        getLocationPermission();
        list_stop_point_selected_dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        list_stop_point_selected=findViewById(R.id.list_stop_point_selected);
        list_stop_point_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_stop_point_selected_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                list_stop_point_selected_dialog.setContentView(R.layout.list_stop_point_selected_popup);
                list_stop_point_selected_dialog.show();
                CustomAdapterForListStopPoint customAdapterForListStopPoint;
                ListView listView_stop_point=list_stop_point_selected_dialog.findViewById(R.id.list_stop_point_list_view);
                if (arrayListStopPoint.size()!=0)
                {
                    customAdapterForListStopPoint=new CustomAdapterForListStopPoint(list_stop_point_selected_dialog.getContext(),R.layout.list_stop_point_tour_info,arrayListStopPoint);
                    listView_stop_point.setAdapter(customAdapterForListStopPoint);
                    Button add_sp=list_stop_point_selected_dialog.findViewById(R.id.add_list_stop_point_to_tour);
                    ImageView exit=list_stop_point_selected_dialog.findViewById(R.id.exit_list_stop_point_review);
                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            list_stop_point_selected_dialog.dismiss();
                        }
                    });
                    add_sp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
                                        list_stop_point_selected_dialog.dismiss();
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
            }
        });

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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng arg0) {


                Geocoder geocoder=new Geocoder(MapActivity.this);
                List<Address> addresses = null;
                try {
                    addresses=geocoder.getFromLocation(arg0.latitude,arg0.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MarkerOptions options=new MarkerOptions()
                        .position(arg0)
                        .title(addresses.get(0).getAddressLine(0));
                mMap.addMarker(options);
            }
        });
      
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

    private void moveCamera(LatLng latLng, float zoom, String title) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (TYPE==1)
                {
                    LatLng mLatLng = marker.getPosition();
                    double mNewLat = mLatLng.latitude;
                    double mNewLong = mLatLng.longitude;
                    String mNewAddress=marker.getTitle();
                    Bundle bundle1=new Bundle();
                    bundle1.putDouble("mNewLat",mNewLat);
                    bundle1.putDouble("mNewLong",mNewLong);
                    bundle1.putString("mNewAddress",mNewAddress);
                    Intent intent=new Intent(MapActivity.this,Tour_Info.class);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                    return true;
                }
                else
                {
                    TextView name_country;
                    TextView stop_Point_Infor;

                    dialog.setContentView(R.layout.popup);
                    name_country = dialog.findViewById(R.id.name_country);
                    stop_Point_Infor = dialog.findViewById(R.id.stop_point_information);
                    stop_Point_Infor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            add_Stop_Point_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            add_Stop_Point_Dialog.setContentView(R.layout.add_stop_point);
                            add_Stop_Point_Dialog.show();
                            mMap.clear();
                            dialog.dismiss();
                            final EditText diemxuatphat = add_Stop_Point_Dialog.findViewById(R.id.diem_xuat_phat);
                            ImageView exit_add_stop_point = add_Stop_Point_Dialog.findViewById(R.id.exit_add_stop_point);
                            exit_add_stop_point.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    add_Stop_Point_Dialog.dismiss();
                                }
                            });
                            final Spinner type = add_Stop_Point_Dialog.findViewById(R.id.restaurant);
                            ArrayAdapter<CharSequence> adapter_byname1 = ArrayAdapter.createFromResource(MapActivity.this, R.array.service_type, android.R.layout.simple_spinner_item);
                            adapter_byname1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            type.setAdapter(adapter_byname1);
                            final EditText address = add_Stop_Point_Dialog.findViewById(R.id.addresstext);
                            address.setText(marker.getTitle());

                            final EditText province = add_Stop_Point_Dialog.findViewById(R.id.provincetext);
                            for (int i = 0; i < arrayProvince.length; i++) {
                                if (marker.getTitle().contains(arrayProvince[i])) {
                                    province.setText(i + 1 + "");
                                    break;
                                }
                            }

                            final EditText mMinCost = add_Stop_Point_Dialog.findViewById(R.id.min_cost);
                            final EditText mMaxCost = add_Stop_Point_Dialog.findViewById(R.id.max_cost);

                            // xu li time and date/*

                            final EditText mtimeArrive = add_Stop_Point_Dialog.findViewById(R.id.timeArrial);

                            final EditText mtimeLeave = add_Stop_Point_Dialog.findViewById(R.id.timeLeave);

                            final EditText mDateArrive = add_Stop_Point_Dialog.findViewById(R.id.dateArrial);

                            final EditText mDateLeave = add_Stop_Point_Dialog.findViewById(R.id.dateLeave);

                            final ImageButton btnArrive = add_Stop_Point_Dialog.findViewById(R.id.btndateArrive);

                            final ImageButton btnLeave = add_Stop_Point_Dialog.findViewById(R.id.btndateLeave);
                            final Button ok_button = add_Stop_Point_Dialog.findViewById(R.id.ok_btt);


                            btnArrive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Calendar cal = Calendar.getInstance();
                                    int mDay = cal.get(Calendar.DAY_OF_MONTH);
                                    int mMonth = cal.get(Calendar.MONTH);
                                    int mYear = cal.get(Calendar.YEAR);

                                    DatePickerDialog datePickerDialog = new DatePickerDialog(MapActivity.this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                            mDateArrive.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                        }

                                        ;
                                    }, mYear, mMonth, mDay);
                                    datePickerDialog.setTitle(R.string.choose);
                                    datePickerDialog.show();
                                }
                            });

                            btnLeave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Calendar cal = Calendar.getInstance();
                                    int mDay = cal.get(Calendar.DAY_OF_MONTH);
                                    int mMonth = cal.get(Calendar.MONTH);
                                    int mYear = cal.get(Calendar.YEAR);

                                    DatePickerDialog datePickerDialog = new DatePickerDialog(MapActivity.this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                            mDateLeave.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                        }

                                        ;
                                    }, mYear, mMonth, mDay);
                                    datePickerDialog.setTitle(R.string.choose);
                                    datePickerDialog.show();
                                }
                            });

                            mtimeArrive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Calendar cal = Calendar.getInstance();

                                    int mHour = cal.get(Calendar.HOUR_OF_DAY);
                                    int mMinute = cal.get(Calendar.MINUTE);

                                    TimePickerDialog timePickerDialog = new TimePickerDialog(MapActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int hourOfday, int minute) {
                                            mtimeArrive.setText(hourOfday + ":" + minute);
                                        }

                                        ;
                                    }, mHour, mMinute, false);
                                    timePickerDialog.setTitle(R.string.choose);
                                    timePickerDialog.show();
                                }
                            });


                            mtimeLeave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Calendar cal = Calendar.getInstance();
                                    int mHour = cal.get(Calendar.HOUR_OF_DAY);
                                    int mMinute = cal.get(Calendar.MINUTE);

                                    TimePickerDialog timePickerDialog = new TimePickerDialog(MapActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int hourOfday, int minute) {
                                            mtimeLeave.setText(hourOfday + ":" + minute);
                                        }

                                        ;
                                    }, mHour, mMinute, false);
                                    timePickerDialog.setTitle(R.string.chooseTime);
                                    timePickerDialog.show();
                                }
                            });

                            ok_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String mDiemXuatPhat = diemxuatphat.getText().toString();
                                    String mAddress = address.getText().toString();

                                    int mType;
                                    if (type.getSelectedItem().toString().equals("Restaurant")) {
                                        mType = 1;
                                    } else if (type.getSelectedItem().toString().equals("Hotel")) {
                                        mType = 2;
                                    } else if (type.getSelectedItem().toString().equals("Rest Station")) {
                                        mType = 3;
                                    } else {
                                        mType = 4;
                                    }
                                    int mProvince = Integer.parseInt(province.getText().toString());
                                    int mMin_Cots = Integer.parseInt(mMinCost.getText().toString());
                                    int mMax_Cost = Integer.parseInt(mMaxCost.getText().toString());

                                    Date arriveDate, leaveDate;
                                    long mAr_Date = 0;
                                    long mLe_Date = 0;
                                    //xu li covert to milisecond
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                                    try {
                                        arriveDate = sdf.parse(mDateArrive.getText().toString() + " " + mtimeArrive.getText().toString());
                                        leaveDate = sdf.parse(mDateLeave.getText().toString() + " " + mtimeLeave.getText().toString());
                                        mAr_Date = arriveDate.getTime();
                                        mLe_Date = leaveDate.getTime();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    LatLng mLatLng = marker.getPosition();
                                    double mLat = mLatLng.latitude;
                                    double mLong = mLatLng.longitude;


                                    Stop_Point stop_point = new Stop_Point(mDiemXuatPhat, mAddress, mProvince, mLat, mLong, mAr_Date, mLe_Date, mType, mMin_Cots, mMax_Cost);
                                    arrayListStopPoint.add(stop_point);
                                    add_Stop_Point_Dialog.dismiss();
                                    temp.add(stop_point);

                                    Add_Stop_Point_Data add_stop_point_data = new Add_Stop_Point_Data(id, arrayListStopPoint);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("Authorization", token);


                                    //Bat dau them
                                /*
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
                                */

                                }
                            });


                        }


                    });
                    name_country.setText(marker.getTitle());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                }
                return false;
            }
        });


    }


    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Toast.makeText(MapActivity.this, "Map is ready" + id + " va type= " + TYPE, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onMapReady: map is ready");
                mMap = googleMap;
                if (mLocationPermissionGranted) {
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



}
