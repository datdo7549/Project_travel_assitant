package com.ygaps.travelapp;

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
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ygaps.travelapp.Adapter.CustomAdapterForListStopPoint;
import com.ygaps.travelapp.Adapter.CustomAdapter_Notification_2;
import com.ygaps.travelapp.Model.Add_Stop_Point_Data;
import com.ygaps.travelapp.Model.Add_Stop_Point_Result;
import com.ygaps.travelapp.Model.ChatMemberOnRoad_Data;
import com.ygaps.travelapp.Model.CoordList;
import com.ygaps.travelapp.Model.Coordinate;
import com.ygaps.travelapp.Model.CoordinateSet;
import com.ygaps.travelapp.Model.GetNotification_Result;
import com.ygaps.travelapp.Model.GetSugestStopPoint_Result;
import com.ygaps.travelapp.Model.GetSuggestStoppoint_Data;
import com.ygaps.travelapp.Model.InviteMember_Result;
import com.ygaps.travelapp.Model.Notification_2;
import com.ygaps.travelapp.Model.SendCoordinate_Data;
import com.ygaps.travelapp.Model.SendSpeed_Data;
import com.ygaps.travelapp.Model.Send_noti_on_road_result;
import com.ygaps.travelapp.Model.Speed_Result;
import com.ygaps.travelapp.Model.Stop_Point;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MapTemp extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleMap mMap;
    private Marker stopPointTemp;
    private Marker currentUserLocationMarker;
    private FusedLocationProviderClient client;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker markerSelect;
    public static final int Request_User_Location_Code=99;
    private Geocoder geocoder;

    private String fullAddress="HCM";
    private String provinceGet="HCM";
    private String[] arrayProvince={"Hồ Chí Minh","Hà Nội","Đà Nẵng","Bình Dương","Đồng Nai","Khánh Hòa", "Hải Phòng", "Long An", "Quảng Nam", "Bà Rịa Vũng Tàu", "Đắk Lắk","Cần Thơ",
            "Bình Thuận", "Lâm Đồng", "Thừa Thiên Huế", "Kiên Giang", "Bắc Ninh", "Quảng Ninh", "Thanh Hóa", "Nghệ An", "Hải Dương", "Gia Lai", "Bình Phước", "Hưng Yên", "Bình Định", "Tiền Giang",
            "Thái Bình", "Bắc Giang", "Hòa Bình", "An Giang", "Vĩnh Phúc", "Tây Ninh", "Thái Nguyên", "Lào Cai","Nam Định","Quảng Ngãi", "Bến Tre", "Đắk Nông", "Cà Mau", "Vĩnh Long",
            " Ninh Bình", "Phú Thọ", "Ninh Thuận", "Phú Yên", "Hà Nam", "Hà Tĩnh", "Đồng Tháp", "Sóc Trăng", "Kon Tum", "Quảng Bình", "Quảng Trị", "Trà Vinh", "Hậu Giang", "Sơn La", "Bạc Liêu", "Yên Bái",
            "Tuyên Quang", "Điện Biên", "Lai Châu", "Lạng Sơn", "Hà Giang", "Bắc Kạn", "Cao Bằng"};

    private ArrayList<CoordList> coordLists = new ArrayList<>();
    private String token;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    public static final String URL="http://35.197.153.192:3000/";
    private int TYPE;
    private Dialog dialog;
    private Dialog add_Stop_Point_Dialog;
    private Dialog send_speed;
    private int id;
    private ArrayList<Stop_Point> arrayListStopPoint = new ArrayList<>();
    private ArrayList<Stop_Point> temp = new ArrayList<>();
    private ImageView list_stop_point_selected;
    private Dialog list_stop_point_selected_dialog;
    private Dialog notifiaction_dialog;
    private Dialog chat_member_dialog;
    private ImageView get_suggest;
    private int mode=0;
    private String tour_id;
    private String user_id;
    private String token_1;
    private ImageView chat_member;
    private ImageView notification_text;
    private CustomAdapter_Notification_2 customAdapter_notification_2;
    private ArrayList<Notification_2> arrayList;
    private Double mLat;
    private Double mLong;
    private ImageButton recorder;
    private ImageButton play;
    private ImageView bike;
    private TextView speed_info;
    private ImageView change_view;
    Random random=new Random();
    String AudioSavePathInDevice = null;
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    private ArrayList<Speed_Result> arrayList12=new ArrayList<>();
    private static int change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_map_temp);
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Bundle bundle = getIntent().getExtras();
        TYPE = bundle.getInt("type", 0);
        if (TYPE==0)
        {
            Toast.makeText(getApplicationContext(),"La 0",Toast.LENGTH_SHORT).show();
        }
        if (TYPE==1)
        {
            Toast.makeText(getApplicationContext(),"La 1",Toast.LENGTH_SHORT).show();
        }
        token = bundle.getString("token","");
        id = bundle.getInt("ID",0);
        dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        add_Stop_Point_Dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        list_stop_point_selected_dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        chat_member_dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        send_speed = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        list_stop_point_selected=findViewById(R.id.list_stop_point_selected);
        recorder=findViewById(R.id.recorder);
        play=findViewById(R.id.play);
        bike=findViewById(R.id.bike);
        speed_info=findViewById(R.id.speed_info);
        change_view=findViewById(R.id.change_view);
        change_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change==0)
                {
                    change=1;
                    Toast.makeText(getApplicationContext(),"Xem thong tin toc do",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Xem thong tin vi tri",Toast.LENGTH_SHORT).show();
                    change=0;
                }
            }
        });
        bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_speed.setContentView(R.layout.speed_popup);

                Button send=send_speed.findViewById(R.id.send_speed);
                final RadioButton km_40=(RadioButton) send_speed.findViewById(R.id.km40);
                final RadioButton km_50=(RadioButton) send_speed.findViewById(R.id.km50);
                final RadioButton km_60=(RadioButton) send_speed.findViewById(R.id.km60);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int speed;
                        if (km_40.isChecked())
                        {
                            speed =40;
                        }
                        else if (km_50.isChecked())
                        {
                            speed =50;
                        }
                        else
                        {
                            speed=60;
                        }
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", token_1);
                        SendSpeed_Data sendSpeed_data=new SendSpeed_Data(mLat,mLong,Integer.parseInt(tour_id),Integer.parseInt(user_id),3,speed);
                        Call<InviteMember_Result>call=jsonPlaceHolderApi.send_notification_on_road(map,sendSpeed_data);
                        call.enqueue(new Callback<InviteMember_Result>() {
                            @Override
                            public void onResponse(Call<InviteMember_Result> call, Response<InviteMember_Result> response) {
                                if (!response.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),"k thanh",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"thanh cong",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<InviteMember_Result> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                send_speed.show();
            }
        });
        recorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN: {
                        if (checkPermission()) {
                            AudioSavePathInDevice =
                                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                            CreateRandomAudioFileName(5) + "AudioRecording.3gp";
                            MediaRecorderReady();
                            try {
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                            } catch (IllegalStateException e) {

                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            requestPermission();
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mediaRecorder.stop();
                        Toast.makeText(MapTemp.this, "Recording Completed",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                return false;
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(MapTemp.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapTemp);
        mapFragment.getMapAsync(MapTemp.this);

        mode=bundle.getInt("mode",0);
        tour_id=bundle.getString("tour_id");
        user_id=bundle.getString("user_id");
        token_1=bundle.getString("token_1");
        chat_member=findViewById(R.id.chat_member);
        notification_text=findViewById(R.id.notification_text);
        notifiaction_dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Objects.requireNonNull(notifiaction_dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (mode==0)
        {
            //find_suggest_stop_point();
        }
        else {
            chat_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    chat_member_dialog.setContentView(R.layout.chat_member_pop_up);
                    final EditText chat_mess=chat_member_dialog.findViewById(R.id.chat_text);
                    Button send_mess=chat_member_dialog.findViewById(R.id.send_chat_message);
                    send_mess.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String mess=chat_mess.getText().toString();
                            Map<String, String> map = new HashMap<>();
                            map.put("Authorization", token_1);

                            ChatMemberOnRoad_Data chatMemberOnRoad_data=new ChatMemberOnRoad_Data(tour_id,user_id,mess);
                            Call<InviteMember_Result> call=jsonPlaceHolderApi.chat_member(map,chatMemberOnRoad_data);
                            call.enqueue(new Callback<InviteMember_Result>() {
                                @Override
                                public void onResponse(Call<InviteMember_Result> call, Response<InviteMember_Result> response) {
                                    if (!response.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),"k thanh"+tour_id+"  "+user_id+ "  "+token_1+"   "+response.code(),Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getApplicationContext(),"thanh cong",Toast.LENGTH_SHORT).show();
                                        chat_member_dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<InviteMember_Result> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    chat_member_dialog.show();
                }
            });

            notification_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifiaction_dialog.setContentView(R.layout.notification_popup_text);
                    final ListView listView=notifiaction_dialog.findViewById(R.id.list_notification_text);
                    Map<String, String> map = new HashMap<>();
                    map.put("Authorization", token_1);
                    Call<GetNotification_Result> call=jsonPlaceHolderApi.get_notification(map,tour_id,1,"100");
                    call.enqueue(new Callback<GetNotification_Result>() {
                        @Override
                        public void onResponse(Call<GetNotification_Result> call, Response<GetNotification_Result> response) {
                            if (!response.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Khong thanh",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"thanh cong",Toast.LENGTH_SHORT).show();
                                arrayList=response.body().getNotiList();
                                if (arrayList.size()!=0) {

                                    customAdapter_notification_2 = new CustomAdapter_Notification_2(getApplicationContext(), R.layout.custom_item_notification, arrayList);
                                    listView.setAdapter(customAdapter_notification_2);
                                }
                                else {
                                    Toast.makeText(getApplicationContext()," rong",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetNotification_Result> call, Throwable t) {

                        }
                    });
                    notifiaction_dialog.show();
                }
            });
        }

    }




    private void requestPermission() {
        ActivityCompat.requestPermissions(MapTemp.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    private String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();

    }

    private void MediaRecorderReady() {
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public boolean checkUserLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);

            }
            return  false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case Request_User_Location_Code: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setBuildingsEnabled(true);
                    } else {
                        Toast.makeText(this, "Permission Denied !!", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                break;
            }
            case RequestPermissionCode: {
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MapTemp.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MapTemp.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            }
        }
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (currentUserLocationMarker!=null)
        {
            currentUserLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        mLat=location.getLatitude();
        mLong=location.getLongitude();
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Location");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        currentUserLocationMarker = map.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            if (mode==0)
            {
                find_suggest_stop_point();
            }
            if (mode==1)
            {
                final Handler ha=new Handler();
                ha.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //call function
                        mMap.clear();
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", token_1);

                        SendCoordinate_Data sendCoordinate_data=new SendCoordinate_Data(user_id,tour_id,mLat+"",mLong+"");

                        Call<ArrayList> call=jsonPlaceHolderApi.send_get_coor(map,sendCoordinate_data);
                        call.enqueue(new Callback<ArrayList>() {
                            @Override
                            public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
                                if (!response.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),"Gui len k thanh",Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<Coordinate>>(){}.getType();
                                    List<Coordinate> contactList = gson.fromJson(response.body().toString(), type);
                                    for (Coordinate contact : contactList){
                                        //Toast.makeText(getApplicationContext(),"Gui len thanh cong: "+contact.getLat()+" --- "+contact.getMlong()+"---- "+contactList.size(),Toast.LENGTH_SHORT).show();
                                        MarkerOptions options=new MarkerOptions();
                                        options.position(new LatLng(Double.parseDouble(contact.getLat()),Double.parseDouble(contact.getMlong())));
                                        options.title(contact.getId()+"");
                                        if (change==0) {
                                            mMap.addMarker(options);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                        Call<Send_noti_on_road_result> call1_1=jsonPlaceHolderApi.get_notification_on_road(map,Integer.parseInt(tour_id),1,"10");
                       call1_1.enqueue(new Callback<Send_noti_on_road_result>() {
                           @Override
                           public void onResponse(Call<Send_noti_on_road_result> call, Response<Send_noti_on_road_result> response) {
                               if (!response.isSuccessful())
                               {
                                   Toast.makeText(getApplicationContext(),"k thanh",Toast.LENGTH_SHORT).show();
                               }else {
                                   arrayList12=response.body().getNotiList();
                                   String str="";
                                   for (int i=0;i<arrayList12.size();i++)
                                   {
                                      str=str+"Canh bao toc do: "+arrayList12.get(i).getSpeed()+"km/h\n";
                                       MarkerOptions options=new MarkerOptions();
                                       options.position(new LatLng(arrayList12.get(i).getLat(),arrayList12.get(i).getmLong()));
                                       options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                       options.title(arrayList12.get(i).getSpeed()+"km/h");
                                       if (change==1)
                                       {
                                           mMap.addMarker(options);
                                       }


                                   }
                                   speed_info.setText(str);
                                   arrayList12.clear();
                               }
                           }

                           @Override
                           public void onFailure(Call<Send_noti_on_road_result> call, Throwable t) {

                           }
                       });


                        ha.postDelayed(this, 10000);
                    }
                }, 9500);
            }


        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions temp = new MarkerOptions();
                String address_click = "";
                temp.position(latLng);
                List<Address> addressList;
                geocoder = new Geocoder(MapTemp.this, Locale.getDefault());
                try {
                    addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    address_click = addressList.get(0).getAddressLine(0);

                    Toast.makeText(MapTemp.this, address_click, Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp.title(address_click);
                mMap.addMarker(temp);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (TYPE == 1) {
                    Toast.makeText(getApplicationContext(),"Alo",Toast.LENGTH_SHORT).show();
                    LatLng mLatLng = marker.getPosition();
                    double mNewLat = mLatLng.latitude;
                    double mNewLong = mLatLng.longitude;
                    String mNewAddress = marker.getTitle();
                    Bundle bundle1 = new Bundle();
                    bundle1.putDouble("mNewLat", mNewLat);
                    bundle1.putDouble("mNewLong", mNewLong);
                    bundle1.putString("mNewAddress", mNewAddress);
                    //Toast.makeText(getApplicationContext(),mNewLat+" "+mNewLong,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapTemp.this, Tour_Info.class);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                    return false;
                } else {
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
                            dialog.dismiss();
                            final EditText diemxuatphat = add_Stop_Point_Dialog.findViewById(R.id.diem_xuat_phat);
                            if (marker.getTitle().contains("+"))
                            {
                                diemxuatphat.setText(marker.getTitle().substring(0,marker.getTitle().indexOf('+')));
                            }

                            ImageView exit_add_stop_point = add_Stop_Point_Dialog.findViewById(R.id.exit_add_stop_point);
                            exit_add_stop_point.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    add_Stop_Point_Dialog.dismiss();
                                }
                            });
                            final Spinner type = add_Stop_Point_Dialog.findViewById(R.id.restaurant);
                            ArrayAdapter<CharSequence> adapter_byname1 = ArrayAdapter.createFromResource(MapTemp.this, R.array.service_type, android.R.layout.simple_spinner_item);
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

                                    DatePickerDialog datePickerDialog = new DatePickerDialog(MapTemp.this, new DatePickerDialog.OnDateSetListener() {
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

                                    DatePickerDialog datePickerDialog = new DatePickerDialog(MapTemp.this, new DatePickerDialog.OnDateSetListener() {
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

                                    TimePickerDialog timePickerDialog = new TimePickerDialog(MapTemp.this, new TimePickerDialog.OnTimeSetListener() {
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

                                    TimePickerDialog timePickerDialog = new TimePickerDialog(MapTemp.this, new TimePickerDialog.OnTimeSetListener() {
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
                                }
                            });
                        }


                    });
                    name_country.setText(marker.getTitle());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                }
                return true;
            }
        });


        list_stop_point_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_stop_point_selected_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                list_stop_point_selected_dialog.setContentView(R.layout.list_stop_point_selected_popup);
                list_stop_point_selected_dialog.show();
                CustomAdapterForListStopPoint customAdapterForListStopPoint;
                final ListView listView_stop_point = list_stop_point_selected_dialog.findViewById(R.id.list_stop_point_list_view);
                listView_stop_point.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        add_Stop_Point_Dialog.show();
                        final EditText diemxuatphat = add_Stop_Point_Dialog.findViewById(R.id.diem_xuat_phat);
                        diemxuatphat.setText("");
                        ImageView exit_add_stop_point = add_Stop_Point_Dialog.findViewById(R.id.exit_add_stop_point);
                        exit_add_stop_point.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                add_Stop_Point_Dialog.dismiss();
                            }
                        });
                        final Spinner type1 = add_Stop_Point_Dialog.findViewById(R.id.restaurant);
                        ArrayAdapter<CharSequence> adapter_byname1 = ArrayAdapter.createFromResource(MapTemp.this, R.array.service_type, android.R.layout.simple_spinner_item);
                        adapter_byname1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        type1.setAdapter(adapter_byname1);
                        final EditText address1 = add_Stop_Point_Dialog.findViewById(R.id.addresstext);
                        address1.setText("");

                        final EditText province1 = add_Stop_Point_Dialog.findViewById(R.id.provincetext);
                        province1.setText("");

                        final EditText mMinCost = add_Stop_Point_Dialog.findViewById(R.id.min_cost);
                        mMinCost.setText("");
                        final EditText mMaxCost = add_Stop_Point_Dialog.findViewById(R.id.max_cost);
                        mMaxCost.setText("");
                        // xu li time and date/*

                        final EditText mtimeArrive = add_Stop_Point_Dialog.findViewById(R.id.timeArrial);

                        mtimeArrive.setText("");
                        final EditText mtimeLeave = add_Stop_Point_Dialog.findViewById(R.id.timeLeave);
                        mtimeLeave.setText("");
                        final EditText mDateArrive = add_Stop_Point_Dialog.findViewById(R.id.dateArrial);
                        mDateArrive.setText("");
                        final EditText mDateLeave = add_Stop_Point_Dialog.findViewById(R.id.dateLeave);
                        mDateLeave.setText("");
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

                                DatePickerDialog datePickerDialog = new DatePickerDialog(MapTemp.this, new DatePickerDialog.OnDateSetListener() {
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

                                DatePickerDialog datePickerDialog = new DatePickerDialog(MapTemp.this, new DatePickerDialog.OnDateSetListener() {
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

                                TimePickerDialog timePickerDialog = new TimePickerDialog(MapTemp.this, new TimePickerDialog.OnTimeSetListener() {
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

                                TimePickerDialog timePickerDialog = new TimePickerDialog(MapTemp.this, new TimePickerDialog.OnTimeSetListener() {
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
                                String mAddress = address1.getText().toString();

                                int mType;
                                if (type1.getSelectedItem().toString().equals("Restaurant")) {
                                    mType = 1;
                                } else if (type1.getSelectedItem().toString().equals("Hotel")) {
                                    mType = 2;
                                } else if (type1.getSelectedItem().toString().equals("Rest Station")) {
                                    mType = 3;
                                } else {
                                    mType = 4;
                                }
                                int mProvince = Integer.parseInt(province1.getText().toString());
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

                                double mLat = arrayListStopPoint.get(position).getLat();
                                double mLong = arrayListStopPoint.get(position).getMlong();
                                Stop_Point stop_point = new Stop_Point(mDiemXuatPhat, mAddress, mProvince, mLat, mLong, mAr_Date, mLe_Date, mType, mMin_Cots, mMax_Cost);
                                arrayListStopPoint.remove(position);
                                arrayListStopPoint.add(position, stop_point);
                                CustomAdapterForListStopPoint customAdapterForListStopPoint1 = new CustomAdapterForListStopPoint(list_stop_point_selected_dialog.getContext(), R.layout.list_stop_point_tour_info, arrayListStopPoint);
                                listView_stop_point.setAdapter(customAdapterForListStopPoint1);
                                add_Stop_Point_Dialog.dismiss();
                            }
                        });
                        //Xoa cai cu va them cai moi

                    }
                });

                listView_stop_point.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        arrayListStopPoint.remove(position);
                        CustomAdapterForListStopPoint customAdapterForListStopPoint1 = new CustomAdapterForListStopPoint(list_stop_point_selected_dialog.getContext(), R.layout.list_stop_point_tour_info, arrayListStopPoint);
                        listView_stop_point.setAdapter(customAdapterForListStopPoint1);
                        return false;
                    }
                });
                ImageView exit = list_stop_point_selected_dialog.findViewById(R.id.exit_list_stop_point_review);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list_stop_point_selected_dialog.dismiss();
                    }
                });
                if (arrayListStopPoint.size() != 0) {
                    customAdapterForListStopPoint = new CustomAdapterForListStopPoint(list_stop_point_selected_dialog.getContext(), R.layout.list_stop_point_tour_info, arrayListStopPoint);
                    listView_stop_point.setAdapter(customAdapterForListStopPoint);
                    Button add_sp = list_stop_point_selected_dialog.findViewById(R.id.add_list_stop_point_to_tour);

                    add_sp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Add_Stop_Point_Data add_stop_point_data = new Add_Stop_Point_Data(id, arrayListStopPoint);
                            Map<String, String> map = new HashMap<>();
                            map.put("Authorization", token);
                            Call<Add_Stop_Point_Result> call = jsonPlaceHolderApi.addStopPoint(map, add_stop_point_data);
                            call.enqueue(new Callback<Add_Stop_Point_Result>() {
                                @Override
                                public void onResponse(Call<Add_Stop_Point_Result> call, Response<Add_Stop_Point_Result> response) {
                                    if (!response.isSuccessful()) {
                                        Toast.makeText(MapTemp.this, "Ko Thanh cong" + response.code() + response.body(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        list_stop_point_selected_dialog.dismiss();
                                        Toast.makeText(MapTemp.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Add_Stop_Point_Result> call, Throwable t) {
                                    Toast.makeText(MapTemp.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            arrayListStopPoint.clear();
                        }
                    });
                }
            }
        });
    }
    private void find_suggest_stop_point() {
        CoordinateSet coordinateSet_1 = new CoordinateSet(48.033090, 69.814943);
        CoordinateSet coordinateSet_2 = new CoordinateSet(2.739697, 43.903915);
        CoordinateSet coordinateSet_3 = new CoordinateSet(2.739697, 43.903915);
        CoordinateSet coordinateSet_4 = new CoordinateSet(-6.658560, 143.575005);
        CoordinateSet coordinateSet_5 = new CoordinateSet(-6.658560, 143.575005);
        CoordinateSet coordinateSet_6 = new CoordinateSet(49.555488, 143.322819);
        CoordinateSet coordinateSet_7 = new CoordinateSet(49.555488, 143.322819);
        CoordinateSet coordinateSet_8 = new CoordinateSet(48.033090, 69.814943);


        ArrayList<CoordinateSet> cs1 = new ArrayList<>();
        cs1.add(coordinateSet_1);
        cs1.add(coordinateSet_2);

        ArrayList<CoordinateSet> cs2 = new ArrayList<>();
        cs2.add(coordinateSet_3);
        cs2.add(coordinateSet_4);

        ArrayList<CoordinateSet> cs3 = new ArrayList<>();
        cs3.add(coordinateSet_5);
        cs3.add(coordinateSet_6);

        ArrayList<CoordinateSet> cs4 = new ArrayList<>();
        cs4.add(coordinateSet_7);
        cs4.add(coordinateSet_8);


        CoordList cl1 = new CoordList(cs1);
        CoordList cl2 = new CoordList(cs2);
        CoordList cl3 = new CoordList(cs3);
        CoordList cl4 = new CoordList(cs4);


        coordLists.add(cl1);
        coordLists.add(cl2);
        coordLists.add(cl3);
        coordLists.add(cl4);
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjU3LCJwaG9uZSI6IiIsImVtYWlsIjoiMTcxMjMyMUBzdHVkZW50LmhjbXVzLmVkdS52biIsImV4cCI6MTU3Nzk1OTI0MTgyNiwiYWNjb3VudCI6InVzZXIiLCJpYXQiOjE1NzUzNjcyNDF9.UTAry051vTyGSq4UJL4PSsudKcgDk64_DfSUN5JRNc0");


        final GetSuggestStoppoint_Data getSuggestStoppoint_data = new GetSuggestStoppoint_Data(false, coordLists);
        Call<GetSugestStopPoint_Result> call1_44 = jsonPlaceHolderApi.get_suggets_stop_point(map, getSuggestStoppoint_data);

        call1_44.enqueue(new Callback<GetSugestStopPoint_Result>() {
            @Override
            public void onResponse(Call<GetSugestStopPoint_Result> call, Response<GetSugestStopPoint_Result> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Lay suggest stop point k thanh cong " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < 20; i++) {
                        MarkerOptions options = new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(response.body().getStopPoints().get(i).getLat()), Double.parseDouble(response.body().getStopPoints().get(i).getmLong())))
                                .title(response.body().getStopPoints().get(i).getName()+"+"+ response.body().getStopPoints().get(i).getAddress());
                        mMap.addMarker(options);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetSugestStopPoint_Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }






}

