package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandregister.Model.Create_Tour_Data;
import com.example.loginandregister.Model.Create_Tour_Result;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class CreateActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private TextView title;
    private EditText name_tour, start, end, adult, childs, min, max, image;
    private DatePickerDialog datePickerDialog;
    private Spinner isPrivate;
    private Spinner status;
    private LinearLayout linear_status;
    private Button create_button;
    public static final String URL = "http://35.197.153.192:3000/";
    private static final String TAG = "MAP";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token;
    private int ID;
    private int id_tour;
    private int mode=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_create);
        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        mode=bundle.getInt("mode");
        id_tour=bundle.getInt("id_tour");
        mapping();
        if (mode==1)
        {
            name_tour.setText(bundle.getString("name_temp"));
            title.setText("Update Tour");
            linear_status.setVisibility(View.VISIBLE);
            create_button.setText("Update");

        }
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addDateEvent();
        if (isServicesOK()) {
            init();
        }
    }

    private void addDateEvent() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                int mDay = cal.get(Calendar.DAY_OF_MONTH);
                int mMonth = cal.get(Calendar.MONTH);
                int mYear = cal.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(CreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        start.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }

                    ;
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle(R.string.choose);
                datePickerDialog.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                int mDay = cal.get(Calendar.DAY_OF_MONTH);
                int mMonth = cal.get(Calendar.MONTH);
                int mYear = cal.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(CreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        end.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }

                    ;
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private void mapping() {
        name_tour = findViewById(R.id.tour_name);
        start = findViewById(R.id.startDate);
        end = findViewById(R.id.endDate);
        adult = findViewById(R.id.adult);
        childs = findViewById(R.id.children);
        min = findViewById(R.id.minCost);
        max = findViewById(R.id.maxCost);
        create_button = findViewById(R.id.create_button);
        isPrivate = findViewById(R.id.isPrivate);
        status=findViewById(R.id.status);
        ArrayAdapter<CharSequence> adapter_byname = ArrayAdapter.createFromResource(CreateActivity.this, R.array.is_private, android.R.layout.simple_spinner_item);
        adapter_byname.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapter_status = ArrayAdapter.createFromResource(CreateActivity.this, R.array.status, android.R.layout.simple_spinner_item);
        adapter_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        isPrivate.setAdapter(adapter_byname);
        status.setAdapter(adapter_status);
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        imageViewBack = findViewById(R.id.back_create_tour);
        linear_status=findViewById(R.id.linear_status);
        title=findViewById(R.id.title_text);
    }

    private void init() {
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckEmpty()) {
                    String mName = name_tour.getText().toString();
                    Date sDate, eDate;
                    long mStart = 0, mEnd = 0;
                    //xu li
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        sDate = sdf.parse(start.getText().toString());
                        eDate = sdf.parse(end.getText().toString());
                        mStart = sDate.getTime();
                        mEnd = eDate.getTime();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int mAdult = Integer.parseInt(adult.getText().toString());
                    int mChild = Integer.parseInt(childs.getText().toString());
                    int mMin = Integer.parseInt(min.getText().toString());
                    int mMax = Integer.parseInt(max.getText().toString());
                    Boolean mIsPrivate;
                    if (isPrivate.getSelectedItem().toString() == "True") {
                        mIsPrivate = true;
                    } else {
                        mIsPrivate = false;
                    }
                    if (mode==1)
                    {

                        int mStatus;
                        if (status.getSelectedItem().toString().equals("Canceled"))
                        {
                            mStatus=-1;
                        }
                        else if (status.getSelectedItem().toString().equals("Open"))
                        {
                            mStatus=0;
                        }
                        else if (status.getSelectedItem().toString().equals("Started"))
                        {
                            mStatus=1;
                        }
                        else
                        {
                            mStatus=2;
                        }
                        Create_Tour_Data upadte_tour_data = new Create_Tour_Data(id_tour,mName, mStart, mEnd, mIsPrivate, mAdult, mChild, mMin, mMax,mStatus);
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", token);
                        Call<Create_Tour_Result> call=jsonPlaceHolderApi.updateTour(map,upadte_tour_data);
                        call.enqueue(new Callback<Create_Tour_Result>() {
                            @Override
                            public void onResponse(Call<Create_Tour_Result> call, Response<Create_Tour_Result> response) {
                                if (!response.isSuccessful())
                                {
                                    Toast.makeText(CreateActivity.this,"Ko thanh cong",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(CreateActivity.this,"Thanh cong",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Create_Tour_Result> call, Throwable t) {
                                Toast.makeText(CreateActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Create_Tour_Data create_tour_data = new Create_Tour_Data(mName, mStart, mEnd, mIsPrivate, mAdult, mChild, mMin, mMax);
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", token);

                        Call<Create_Tour_Result> call = jsonPlaceHolderApi.createTour(map, create_tour_data);
                        call.enqueue(new Callback<Create_Tour_Result>() {
                            @Override
                            public void onResponse(Call<Create_Tour_Result> call, Response<Create_Tour_Result> response) {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(CreateActivity.this, "Khong thanh cong", Toast.LENGTH_SHORT).show();
                                    Log.d("CREATE_TOUR", "Ko Thanh Cong");
                                } else {
                                    Toast.makeText(CreateActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                                    ID = response.body().getId();
                                    Log.d("CREATE_TOUR", "Thanh Cong" + ID);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("token", token);
                                    bundle.putInt("ID", ID);
                                    Intent intent = new Intent(CreateActivity.this, MapActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<Create_Tour_Result> call, Throwable t) {
                                Toast.makeText(CreateActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    Toast.makeText(CreateActivity.this, "Vui lòng nhập đầy đủ thông tin về tour", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(CreateActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Service is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServiceOK: an error occured but we can fic it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(CreateActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You cant make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean CheckEmpty() {
        if (TextUtils.isEmpty(name_tour.getText())
                || TextUtils.isEmpty(start.getText())
                || TextUtils.isEmpty(end.getText())
                || TextUtils.isEmpty(adult.getText())
                || TextUtils.isEmpty(childs.getText())
                || TextUtils.isEmpty(min.getText())
                || TextUtils.isEmpty(max.getText())) {
            return false;
        }
        return true;
    }

}
