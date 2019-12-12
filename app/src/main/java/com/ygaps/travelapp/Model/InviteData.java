package com.ygaps.travelapp.Model;

public class InviteData {
    private String tourId;
    private String invitedUserId;
    private Boolean isInvited;

    public InviteData(String tourId, String invitedUserId, Boolean isInvited) {
        this.tourId = tourId;
        this.invitedUserId = invitedUserId;
        this.isInvited = isInvited;
    }
}
