package com.ygaps.travelapp.Model;

import java.util.ArrayList;


public class GetSuggestStoppoint_Data {
    private Boolean hasOneCoordinate;
    private ArrayList<CoordList> coordList;

    public GetSuggestStoppoint_Data(Boolean hasOneCoordinate, ArrayList<CoordList> coordList) {
        this.hasOneCoordinate = hasOneCoordinate;
        this.coordList = coordList;
    }
}