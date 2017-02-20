package com.kt.iot.mobile.data;

/**
 * Created by ceoko on 15. 4. 29..
 */
public class BgSettingItem {

    private int resId;
    private String menuName;

    public BgSettingItem(int resId) {
        this.resId = resId;
//        this.menuName = menuName;
    }

    public int getResId() {
        return resId;
    }

    public String getMenuName() {
        return menuName;
    }
}
