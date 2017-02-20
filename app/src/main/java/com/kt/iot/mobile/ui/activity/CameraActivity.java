package com.kt.iot.mobile.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kt.iot.mobile.android.R;

import java.io.File;

/**
 * Created by User on 2017-01-28.
 */

public class CameraActivity extends ActionBarActivity implements View.OnClickListener {

    ImageView imageView;
    File outputFile;

    Button button, opencvButton, StoreButton;
    String filePath;
    final int number=0;
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_camera);


        button=(Button)findViewById(R.id.TakeaPicture);
        button.setOnClickListener(this);

        opencvButton=(Button)findViewById(R.id.opencv);
        opencvButton.setOnClickListener(this);



        imageView=(ImageView)findViewById(R.id.picture);
        String Path= Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderPath=Path;


        filePath= Path+File.separator+"output.jpg";

        Toast.makeText(CameraActivity.this,filePath, Toast.LENGTH_SHORT).show();

        outputFile=new File(filePath);





    }
    @Override
    public void onClick(View v)
    {


        switch (v.getId()) {
            case R.id.TakeaPicture:

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));

                startActivityForResult(intent, 1001);
                break;
            case R.id.opencv:
                Intent intent2 = new Intent(getApplicationContext(), OpencvActivity.class);
                // intent2.putExtra(.EXTRA_MEMBER_ID, mMbrId);
                startActivity(intent2);
                //finish();
                break;

        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1001)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=8;

            Bitmap bitmap=BitmapFactory.decodeFile(filePath, options);
            imageView.setImageBitmap(bitmap);

        }
    }
}
