package com.ygaps.travelapp.Model;

public class SendTokenFirebaseToSever_Data {
    private String fcmToken;
    private String deviceId;
    private int platform;
    private String appVersion;

    public SendTokenFirebaseToSever_Data(String fcmToken, String deviceId, int platform, String appVersion) {
        this.fcmToken = fcmToken;
        this.deviceId = deviceId;
        this.platform = platform;
        this.appVersion = appVersion;
    }
}
