package com.kt.iot.mobile.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.iot.mobile.android.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by User on 2017-02-02.
 */

public class WriteActivity extends AppCompatActivity {

    private final String TAG = WriteActivity.class.getSimpleName();
    Button takePicture, confirm, calendar;
    EditText mMemoEdit;
    TextView Title;
    String memoData, RealDay, RealMonth, RealDate;
    ImageView imageView;
    String Path, folderPath, filePath;


    File temp, StoredFile, path, file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        takePicture = (Button) findViewById(R.id.TakeaPicture);
        confirm = (Button) findViewById(R.id.confirm);
        calendar = (Button) findViewById(R.id.calendar);

        mMemoEdit = (EditText) findViewById(R.id.memo_edit);
        Title = (TextView) findViewById(R.id.title);
      //  day = (TextView) findViewById(R.id.day);
    }

    /***********************************************/

    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.calendar: {
                showDialog(1);
                break;
            }


            case R.id.TakeaPicture: {


                mMemoEdit.setText(memoData);

              /*  RealMonth = month.getText().toString();
                month.setText(RealMonth);

                RealDay = day.getText().toString();
                day.setText(RealDay);*/

                RealDate = RealMonth + "월" + RealDay + "일";


                // File storeDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "FlowerOreMemo");
                //    File storeDir=new File("/storage/emulated/0/FlowerOreMemo");
               /* if(!storeDir.exists()){
                    if(!storeDir.mkdirs()){
                        Log.d("FlowerOre", "failed to create directory");
                        return ;
                    }
                }*/


             /*   temp=new File(storeDir.getPath()+File.separator+RealDate+".txt");
                Toast.makeText(WriteActivity.this, storeDir.getPath()+File.separator+RealDate+".txt", Toast.LENGTH_LONG).show();
                FileOutputStream fos=null;
                try{
                    fos=new FileOutputStream(temp);
                    fos.write(memoData.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

/******************************text sotre *************************************************************/
             /*   memoData = mMemoEdit.getText().toString(); //EditText의 Text 얻어오기

                //SDcard에 데이터 종류(Type)에 따라 자동으로 분류된 저장 폴더 경로선택
                //Environment.DIRECTORY_DOWNLOADS : 사용자에의해 다운로드가 된 파일들이 놓여지는 표준 저장소
                path= Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageDirectory()
                        .getAbsolutePath()+"/Please");

                file= new File(path, RealDate+".txt"); //파일명까지 포함함 경로의 File 객체 생성

                try {
                    //데이터 추가가 가능한 파일 작성자(FileWriter 객체생성)
                    FileWriter wr= new FileWriter(file,true); //두번째 파라미터 true: 기존파일에 추가할지 여부를 나타냅니다.

                    PrintWriter writer= new PrintWriter(wr);
                    writer.println(memoData);
                    Toast.makeText(WriteActivity.this,"쓰여진다!!!", Toast.LENGTH_SHORT).show();
                    writer.close();


                } catch (IOException e) {

                    e.printStackTrace();
                }
*/
             /**********************************image Store****************************/
                imageView = (ImageView) findViewById(R.id.picture);

                StoredFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), RealDate + ".jpg");
                Log.i(TAG, StoredFile.getPath() + " 성공!!!!!!!!!!!!!!!!!!!!!!");


                /********************************/
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(StoredFile));
                startActivityForResult(intent, 1001);
                break;

            }
            case R.id.storeMemo:


                memoData = mMemoEdit.getText().toString(); //EditText의 Text 얻어오기
                mMemoEdit.setText(memoData);

               // RealMonth = month.getText().toString();
               // month.setText(RealMonth);

               // RealDay = day.getText().toString();
               // day.setText(RealDay);

                RealDate = RealMonth + "월" + RealDay + "일";

                //SDcard에 데이터 종류(Type)에 따라 자동으로 분류된 저장 폴더 경로선택
                //Environment.DIRECTORY_DOWNLOADS : 사용자에의해 다운로드가 된 파일들이 놓여지는 표준 저장소
                path = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                Toast.makeText(WriteActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();


                file = new File(path.getPath() + "/" + RealDate + ".txt"); //파일명까지 포함함 경로의 File 객체 생성


                try {
                    //데이터 추가가 가능한 파일 작성자(FileWriter 객체생성)
                    FileWriter wr = new FileWriter(file, true); //두번째 파라미터 true: 기존파일에 추가할지 여부를 나타냅니다.

                    Log.i(TAG, file.getPath() + " 성공");
                    PrintWriter writer = new PrintWriter(wr);
                    writer.println(memoData);

                    writer.close();


                } catch (IOException e) {

                    e.printStackTrace();
                }
                break;

            case R.id.confirm:
                Intent intent2 = new Intent(getApplicationContext(), RecordActivity.class);
                // intent2.putExtra(.EXTRA_MEMBER_ID, mMbrId);
                startActivity(intent2);
                finish();
                break;

        }
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                DatePickerDialog dpd = new DatePickerDialog
                        (WriteActivity.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear, int dayOfMonth) {
                                        Toast.makeText(getApplicationContext(),
                                                year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일 을 선택했습니다",
                                                Toast.LENGTH_SHORT).show();
                                        RealMonth=Integer.toString((monthOfYear + 1));
                                        RealDay=Integer.toString(dayOfMonth);
                                        RealDate=RealMonth+"월"+RealDay+"일";
                                        Title.setText(RealDate);


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
