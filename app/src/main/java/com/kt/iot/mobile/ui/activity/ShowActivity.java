package com.kt.iot.mobile.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.iot.mobile.android.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by User on 2017-02-02.
 */

public class ShowActivity extends AppCompatActivity {
    private final String TAG = ShowActivity.class.getSimpleName();
    ImageView image;
    TextView  Day, Month;
    TextView memo;
    String Date, RealDay, RealMonth;
    String filePath;
    Button calendar;

    File file, path;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);


        memo = (TextView) findViewById(R.id.memo_edit);
     //   Day = (EditText) findViewById(R.id.day);
       // Month = (EditText) findViewById(R.id.month);
        image = (ImageView) findViewById(R.id.picture);
        calendar = (Button) findViewById(R.id.calendar);


    }


    public void OnClick(View v) {


    //    RealDay = Day.getText().toString();
      // RealMonth = Month.getText().toString();
       //Day.setText(RealDay);
       //Month.setText(RealMonth);

        Date = RealMonth + "월" + RealDay + "일";
       // Toast.makeText(ShowActivity.this, Date, Toast.LENGTH_SHORT).show();

        filePath = "/storage/emulated/0/Android/data/com.kt.iot.mobile.android/files/Pictures/" + Date + ".jpg";
        switch (v.getId()) {
            case R.id.calendar: {
                showDialog(1);
                break;
            }
            case R.id.confirm: {


                /********image load***********/
                Matrix rotateMatrix = new Matrix();
               BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;

                Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
                if(bitmap!=null) {
                    rotateMatrix.postRotate(90);
                    Bitmap sideInversionImg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, false);
                    image.setImageBitmap(sideInversionImg);
                }
                else
                    Toast.makeText(ShowActivity.this, "해당 날짜의 기록이 없습니다. ", Toast.LENGTH_SHORT).show();



                /**************File load**************/

                StringBuffer buffer = new StringBuffer();

                path = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                file = new File(path, Date+".txt");

                try {
                    FileReader in = new FileReader(file);
                    BufferedReader reader = new BufferedReader(in);

                    String str = reader.readLine();

                    while (str != null) {
                        buffer.append(str + "\n");
                        str = reader.readLine();//한 줄씩 읽어오기

                    }


                    memo.setText(buffer.toString());
                    reader.close();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;

            }
        }


    }
/*********************************************************************************************/
@Override
@Deprecated
protected Dialog onCreateDialog(int id) {
    switch (id) {
        case 1:
            DatePickerDialog dpd = new DatePickerDialog
                    (ShowActivity.this, // 현재화면의 제어권자
                            new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker view,
                                                      int year, int monthOfYear, int dayOfMonth) {
                                 /*   Toast.makeText(getApplicationContext(),
                                            year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일 을 선택했습니다",
                                            Toast.LENGTH_SHORT).show();*/
                                    RealMonth=Integer.toString((monthOfYear + 1));
                                    RealDay=Integer.toString(dayOfMonth);
                                    Date=RealMonth+"월"+RealDay+"일";
                                    Toast.makeText(ShowActivity.this, Date, Toast.LENGTH_SHORT).show();

                                }
                            }
                            , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                            //    호출할 리스너 등록
                            2016, 1, 1); // 기본값 연월일
            return dpd;


    }
    return super.onCreateDialog(id);
}

}

