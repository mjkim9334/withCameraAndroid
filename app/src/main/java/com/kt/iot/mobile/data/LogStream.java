package com.kt.iot.mobile.data;

import com.kt.gigaiot_sdk.data.TagStrm;
import com.kt.gigaiot_sdk.data.Log;
import java.util.ArrayList;

/**
 * Created by NP1014425901 on 2015-03-11.
 */
public class LogStream {

    private TagStrm tag;
    private ArrayList<Log> logList;

    public LogStream() {
        logList = new ArrayList<Log>();
    }

    public TagStrm getTag() {
        return tag;
    }

    public void setTag(TagStrm tag) {
        this.tag = tag;
    }

    public ArrayList<Log> getLogList() {
        return logList;
    }

    public void setLogList(ArrayList<Log> logList) {
        this.logList = logList;
    }

    public void addLog(Log log) {
        logList.add(log);
    }


}
