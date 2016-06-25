package com.app.materialdesigndemo_rxjava.app;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pratik on 6/25/16.
 */
public class GifsData {
    @SerializedName("data")
    public List<DataObject> data;

    public class DataObject {
        String bitly_gif_url;
        @SerializedName("images")
        public ImagesObject imagesObject;

        public class ImagesObject{
            @SerializedName("fixed_height")
            public FixedHeight fixedHeight;

            public class FixedHeight{
                String url;
            }
        }


    }
}
