package com.ygaps.travelapp.Model;

public class InviteMemberData {
    private String tourID;
    private String invitedUserID;
    private boolean isInvited;

    public InviteMemberData(String tourID, String invitedUserID, boolean isInvited) {
        this.tourID = tourID;
        this.invitedUserID = invitedUserID;
        this.isInvited = isInvited;
    }
}
