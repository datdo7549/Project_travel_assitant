package com.ygaps.travelapp.Model;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "coordinateSet"
})
public class CoordList {

    @JsonProperty("coordinateSet")
    private ArrayList<CoordinateSet> coordinateSet;


    public CoordList(ArrayList<CoordinateSet> coordinateSet) {
        this.coordinateSet = coordinateSet;
    }
}