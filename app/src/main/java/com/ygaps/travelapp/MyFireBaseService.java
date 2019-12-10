package com.ygaps.travelapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ygaps.travelapp.Model.SendTokenFirebaseToServer_Result;
import com.ygaps.travelapp.Model.SendTokenFirebaseToSever_Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyFireBaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebase";
    public static final String URL="http://35.197.153.192:3000/";
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    private void showNotification(String title, String body) {
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANEL_ID="com.example.notificationtest.test";

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANEL_ID,"Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("DoThanhDat");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }
        NotificationCompat.Builder notificationBuildder=new NotificationCompat.Builder(this,NOTIFICATION_CHANEL_ID);
        notificationBuildder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(body)
                .setContentInfo("Info");
        notificationManager.notify(new Random().nextInt(),notificationBuildder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);


        String device_id= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences sharedPreferences = getSharedPreferences("com.ygaps.travel", Context.MODE_PRIVATE);
        String token_user = sharedPreferences.getString("token_user","");

        Map<String, String> map = new HashMap<>();
        map.put("Authorization",token_user);

        SendTokenFirebaseToSever_Data sendTokenFirebaseToSever_data=new SendTokenFirebaseToSever_Data(s,device_id,1,"1.0");

        Call<SendTokenFirebaseToServer_Result> call=jsonPlaceHolderApi.sendTokenToServer(map,sendTokenFirebaseToSever_data);
        call.enqueue(new Callback<SendTokenFirebaseToServer_Result>() {
            @Override
            public void onResponse(Call<SendTokenFirebaseToServer_Result> call, Response<SendTokenFirebaseToServer_Result> response) {
                if (!response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Send khong thanh cong",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Send thanh cong",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SendTokenFirebaseToServer_Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        Log.d(TAG, s);
    }
}
