package com.gc.android_helper.view.picker.entity;

import com.gc.android_helper.view.picker.model.IPickerViewData;

import java.util.List;

/**
 * Created by 郭灿 on 2017/5/28.
 */

public class Province implements IPickerViewData {
    private String id;
    private String name;
    private List<City> datas;

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

    public List<City> getDatas() {
        return datas;
    }

    public void setDatas(List<City> datas) {
        this.datas = datas;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }
}
