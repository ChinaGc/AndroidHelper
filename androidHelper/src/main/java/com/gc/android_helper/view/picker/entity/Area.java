package com.gc.android_helper.view.picker.entity;

import com.gc.android_helper.view.picker.model.IPickerViewData;

/**
 * Created by 郭灿 on 2017/5/29.
 */

public class Area implements IPickerViewData{
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }
}
