package com.kt.iot.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kt.iot.mobile.android.R;

/**
 * Created by User on 2017-02-02.
 */

public class RecordActivity extends AppCompatActivity {
    Button record, show;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);


    }

    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.record: {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
                //finish();
                break;
            }

            case R.id.show: {
                Intent intent1 = new Intent(getApplicationContext(), ShowActivity.class);
                startActivity(intent1);
                //finish();
                break;
            }

        }
    }
}
