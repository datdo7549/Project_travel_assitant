package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
    private EditText name_tour,start,end,adult,childs,min,max,image;
    private DatePickerDialog datePickerDialog;
    private Spinner isPrivate;
    private Button create_button;
    public static final String URL="http://35.197.153.192:3000/";
    private static final String TAG="MAP";
    private  static final int ERROR_DIALOG_REQUEST=9001;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token;
    private int ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Bundle bundle=getIntent().getExtras();
        token=bundle.getString("token");
        mapping();
        addDateEvent();
        if (isServicesOK())
        {
            init();
        }
    }

    private void addDateEvent() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal=Calendar.getInstance();
                int mDay=cal.get(Calendar.DAY_OF_MONTH);
                int mMonth=cal.get(Calendar.MONTH);
                int mYear=cal.get(Calendar.YEAR);

                datePickerDialog=new DatePickerDialog(CreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        start.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    };
                },mYear,mMonth,mDay);
                datePickerDialog.setTitle(R.string.choose);
                datePickerDialog.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal=Calendar.getInstance();
                int mDay=cal.get(Calendar.DAY_OF_MONTH);
                int mMonth=cal.get(Calendar.MONTH);
                int mYear=cal.get(Calendar.YEAR);

                datePickerDialog=new DatePickerDialog(CreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        end.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    };},mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });
    }

    private void mapping() {
        name_tour=findViewById(R.id.tour_name);
        start=findViewById(R.id.startDate);
        end=findViewById(R.id.endDate);
        adult=findViewById(R.id.adult);
        childs=findViewById(R.id.children);
        min=findViewById(R.id.minCost);
        max=findViewById(R.id.maxCost);
        create_button=findViewById(R.id.create_button);
        isPrivate=findViewById(R.id.isPrivate);
        ArrayAdapter<CharSequence> adapter_byname = ArrayAdapter.createFromResource(CreateActivity.this, R.array.is_private, android.R.layout.simple_spinner_item);
        adapter_byname.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isPrivate.setAdapter(adapter_byname);
        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
    }

    private void init()
    {
        Button btnMap=findViewById(R.id.create_button);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mName=name_tour.getText().toString();
                Date sDate,eDate;
                long mStart=0,mEnd=0;
                //xu li
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                try
                {
                    sDate=sdf.parse(start.getText().toString());
                    eDate=sdf.parse(end.getText().toString());
                    mStart=sDate.getTime();
                    mEnd=eDate.getTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int mAdult=Integer.parseInt(adult.getText().toString());
                int mChild=Integer.parseInt(childs.getText().toString());
                int mMin=Integer.parseInt(min.getText().toString());
                int mMax=Integer.parseInt(max.getText().toString());
                Boolean mIsPrivate;
                if (isPrivate.getSelectedItem().toString()=="True")
                {
                    mIsPrivate=true;
                }
                else
                {
                    mIsPrivate=false;
                }
                Create_Tour_Data create_tour_data=new Create_Tour_Data(mName,mStart,mEnd,mIsPrivate,mAdult,mChild,mMin,mMax);
                Map<String,String> map=new HashMap<>();
                map.put("Authorization",token);

                Call<Create_Tour_Result> call=jsonPlaceHolderApi.createTour(map,create_tour_data);
                call.enqueue(new Callback<Create_Tour_Result>() {
                    @Override
                    public void onResponse(Call<Create_Tour_Result> call, Response<Create_Tour_Result> response) {
                        if (!response.isSuccessful())
                        {
                            Toast.makeText(CreateActivity.this,"Khong thanh cong",Toast.LENGTH_SHORT).show();
                            Log.d("CREATE_TOUR","Ko Thanh Cong");
                        }
                        else
                        {
                            Toast.makeText(CreateActivity.this,"Thanh cong",Toast.LENGTH_SHORT).show();
                            ID=response.body().getId();
                            Log.d("CREATE_TOUR","Thanh Cong"+ID);
                            Bundle bundle=new Bundle();
                            bundle.putString("token",token);
                            bundle.putInt("ID",ID);
                            Intent intent=new Intent(CreateActivity.this,MapActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Create_Tour_Result> call, Throwable t) {
                        Toast.makeText(CreateActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    private boolean isServicesOK()
    {
        Log.d(TAG,"isServicesOK: checking google services version");
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(CreateActivity.this);
        if(available== ConnectionResult.SUCCESS)
        {
            //everything is fine and the user can make map requests
            Log.d(TAG,"isServicesOK: Google Play Service is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            //an error occured but we can resolve it
            Log.d(TAG,"isServiceOK: an error occured but we can fic it");
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(CreateActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(this,"You cant make map requests",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
