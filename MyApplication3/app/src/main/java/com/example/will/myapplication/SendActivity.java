package com.example.will.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.example.will.myapplication.MainActivity.uploadFile;

public class SendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        final Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                String tst=msg.getData().getString("1");
                Toast.makeText(SendActivity.this,"Your emotion is:"+tst,Toast.LENGTH_SHORT).show();
                super.handleMessage(msg);
            }
        };
        final LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
        final Button button = (Button)findViewById(R.id.button);
        final ImageView img=(ImageView)findViewById(R.id.img) ;
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Bitmap bp=getLoacalBitmap("/mnt/sdcard/image02.jpg");
        img.setImageBitmap(bp);
        button.setText("Test");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Thread(){
                        public void run(){
                            try{
                                String response=uploadFile();
                                Message msg = new Message();
                                Bundle mBundle = new Bundle();
                                mBundle.putString("1",response);
                                msg.setData(mBundle);
                                mHandler.sendMessage(msg);
                                Log.d("TAG","成功");
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }

                    }.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}
