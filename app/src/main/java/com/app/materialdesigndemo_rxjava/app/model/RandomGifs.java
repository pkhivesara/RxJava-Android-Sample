package com.app.materialdesigndemo_rxjava.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pratik on 6/26/16.
 */
public class RandomGifs {

    @SerializedName("data")
    public DataObjectForRandom data;
    public class DataObjectForRandom{
        String type;
        String id;
        String url;
        String image_frames;
    }
}
