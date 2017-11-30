package com.gc.android_helper.bean;

/**
 * Created by 郭灿 on 2017/3/18.
 */

public class Banner {
    private String imagesUrl;
    private String title;
    public Banner(String imagesUrl,String title){
        this.title = title;
        this.imagesUrl = imagesUrl;
    }
    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
