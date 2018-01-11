package com.example.android.pictureviewer;

/**
 * Created by zhuangzhili on 2018-01-10.
 */

public class Picture {

    private String caption;
    private String imageUrl;

    public Picture(String url, String title) {
        caption = title;
        imageUrl = url;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
