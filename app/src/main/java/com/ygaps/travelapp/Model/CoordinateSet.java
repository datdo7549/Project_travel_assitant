package com.ygaps.travelapp.Model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.SerializedName;


public class CoordinateSet {


    private Double lat;
    @SerializedName("long")
    private Double _long;

    public CoordinateSet(Double lat, Double _long) {
        this.lat = lat;
        this._long = _long;
    }
}

