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
import android.os.Bundle;
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
    private String title ="";
    private String body="";
    private String id_tour;
    private String token_user;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map data=remoteMessage.getData();
        RemoteMessage.Notification notification = remoteMessage.getNotification();



        SharedPreferences sharedPreferences=getSharedPreferences("com.ygaps.travel",MODE_PRIVATE);
        token_user=sharedPreferences.getString("token_user","");

        if (data.isEmpty()) { // message type is notification.
            Log.d("data","isNull");
            showNotification("Invite to join a Tour",remoteMessage.getNotification().getBody());
        } else { // message type is data.

            StringBuilder temp=new StringBuilder();

            temp.append(data.get("hostName")).append(" invites you to Tour: ").append(data.get("name"));

            id_tour=data.get("id").toString();
            body= temp.toString();

            showNotification("Invite to join a Tour",body);

        }

    }

    private Intent createIntent(String actionName,int notificationID,String messaggeBody)
    {
        Intent intent=new Intent(this,Temp.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(actionName);
        Bundle guiThongbao=new Bundle();
        guiThongbao.putInt("NOTIFICATION_ID",notificationID);
        guiThongbao.putString("MESSAGE_BODY",messaggeBody);
        guiThongbao.putString("token_user",token_user);
        guiThongbao.putString("id_tour",id_tour);
        intent.putExtras(guiThongbao);

        return intent;
    }

    private void showNotification(String title,String messageBody) {
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANEL_ID="dulich";
        int notificationID=new Random().nextInt();

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANEL_ID,"Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("DoThanhDat");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent acceptIntent=createIntent(Temp.ACCEPT_ACTION,notificationID,messageBody);
        Intent decline=createIntent(Temp.DECLINE_ACTION,notificationID,messageBody);
        Intent show=createIntent(Temp.SHOW_ACTION,notificationID,messageBody);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,show,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuildder=new NotificationCompat.Builder(this,NOTIFICATION_CHANEL_ID);
        notificationBuildder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.baseline_message_black_18dp)
                .setContentIntent(pendingIntent)
                .setContentText(messageBody)
                .setContentTitle(title)
                .setContentInfo("Info")
                .setAutoCancel(true)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .addAction(new NotificationCompat.Action(android.R.drawable.sym_call_outgoing,
                        "Accept",
                        PendingIntent.getActivity(this,0,acceptIntent,PendingIntent.FLAG_CANCEL_CURRENT)))
                .addAction(new NotificationCompat.Action(android.R.drawable.sym_call_missed,
                        "Decline",
                        PendingIntent.getActivity(this,0,decline,PendingIntent.FLAG_CANCEL_CURRENT)));
        notificationManager.notify(notificationID,notificationBuildder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

//        Gson gson=new GsonBuilder().serializeNulls().create();
//        Retrofit retrofit=new Retrofit.Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
//
//
//        String device_id= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        SharedPreferences sharedPreferences = getSharedPreferences("com.ygaps.travel", Context.MODE_PRIVATE);
//        String token_user = sharedPreferences.getString("token_user","");
//
//        Map<String, String> map = new HashMap<>();
//        map.put("Authorization",token_user);
//
//        SendTokenFirebaseToSever_Data sendTokenFirebaseToSever_data=new SendTokenFirebaseToSever_Data(s,device_id,1,"1.0");
//
//        Call<SendTokenFirebaseToServer_Result> call=jsonPlaceHolderApi.sendTokenToServer(map,sendTokenFirebaseToSever_data);
//        call.enqueue(new Callback<SendTokenFirebaseToServer_Result>() {
//            @Override
//            public void onResponse(Call<SendTokenFirebaseToServer_Result> call, Response<SendTokenFirebaseToServer_Result> response) {
//                if (!response.isSuccessful())
//                {
//                    Toast.makeText(getApplicationContext(),"Send khong thanh cong",Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),"Send thanh cong",Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<SendTokenFirebaseToServer_Result> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//        Log.d(TAG, s);
    }
}
