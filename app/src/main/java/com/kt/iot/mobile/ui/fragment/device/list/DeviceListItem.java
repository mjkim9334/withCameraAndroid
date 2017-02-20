package com.kt.iot.mobile.ui.fragment.device.list;

public class DeviceListItem {
    private int Image_ID;
    private String Main_Title;
    private String Sub_Title;

    public DeviceListItem(int image_ID, String main_Title, String sub_Title) {
        Image_ID = image_ID;
        Main_Title = main_Title;
        Sub_Title = sub_Title;
    }

    public int getImage_ID() {
        return Image_ID;
    }

    public void setImage_ID(int image_ID) {
        Image_ID = image_ID;
    }

    public String getMain_Title() {
        return Main_Title;
    }

    public void setMain_Title(String main_Title) {
        Main_Title = main_Title;
    }

    public String getSub_Title() {
        return Sub_Title;
    }

    public void setSub_Title(String sub_Title) {
        Sub_Title = sub_Title;
    }

}