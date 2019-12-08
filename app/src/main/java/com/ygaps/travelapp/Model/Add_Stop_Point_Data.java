package com.ygaps.travelapp.Model;

import java.util.ArrayList;

public class Add_Stop_Point_Data {
    private int tourId;
    private ArrayList<Stop_Point> stopPoints;

    public Add_Stop_Point_Data(int tourId, ArrayList<Stop_Point> stopPoints) {
        this.tourId = tourId;
        this.stopPoints = stopPoints;
    }
}
