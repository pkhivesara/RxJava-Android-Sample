package com.app.materialdesigndemo_rxjava.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


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
                public String url;
            }
        }


    }
}
