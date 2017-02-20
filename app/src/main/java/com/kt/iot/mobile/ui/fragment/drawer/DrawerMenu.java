package com.kt.iot.mobile.ui.fragment.drawer;

/**
 * Created by ceoko on 15. 4. 15..
 */
public class DrawerMenu {

    private String menuName;
    private int menuImgRsrcId;

    public DrawerMenu(String menuName, int menuImgRsrcId) {
        this.menuName = menuName;
        this.menuImgRsrcId = menuImgRsrcId;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getMenuImgRsrcId() {
        return menuImgRsrcId;
    }

}
