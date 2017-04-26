package com.example.will.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener {
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;
    private CameraBridgeViewBase openCvCameraView;
    private CascadeClassifier cascadeClassifier;
    public static int saveP=0;
    //图像人脸小于高度的多少就不检测
    private int absoluteFaceSize;
    //临时图像对象
    private Mat matLin;
    //最终图像对象
    public Mat mat;
    public Mat mat1;

    private void initializeOpenCVDependencies() {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();


            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }


        // And we are ready to go
        openCvCameraView.enableView();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        openCvCameraView = new JavaCameraView(this, CameraBridgeViewBase.CAMERA_ID_FRONT);
        openCvCameraView.setCvCameraViewListener(this);
        requestPermission();
        final Button button = new Button(MainActivity.this);
        button.setText("Camera");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveP=1;
                    Thread.sleep(600);
                    Intent intent = new Intent();
                    //從MainActivity 到Main2Activity
                    intent.setClass(MainActivity.this , SendActivity.class);
                    //開啟Activity
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        LinearLayout linear=new LinearLayout(this);
        linear.addView(button);
        relativeLayout.addView(openCvCameraView);
        relativeLayout.addView(linear);


    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        matLin = new Mat(height, width, CvType.CV_8UC4);//临时图像

        // 人脸小于高度的百分之30就不检测
        absoluteFaceSize = (int) (height * 0.3);
    }


    @Override
    public void onCameraViewStopped() {
    }


    @Override
    public Mat onCameraFrame(Mat aInputFrame) {
        //转置函数,将图像翻转（顺时针90度）
        Core.transpose(aInputFrame, matLin);
         //转置函数,将图像翻转（对换）
            Core.flip(matLin, aInputFrame, 1);
            //转置函数,将图像顺时针顺转（对换）
            Core.flip(aInputFrame, matLin, 0);
            mat = matLin;
        MatOfRect faces = new MatOfRect();

        Log.i("123456", "absoluteFaceSize = " + absoluteFaceSize);
        // Use the classifier to detect faces
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(mat, faces, 1.1, 1, 1,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        try {
            if (faces !=null){
                // 检测出多少个
                final Rect facesArray = faces.toArray()[0];
                Core.rectangle(mat, facesArray.tl(), facesArray.br(), new Scalar(0, 255, 0, 255), 3);
                if (saveP==1) {
                    new Thread() {
                        public void run() {
                                try {
                                    Mat newmat=mat.submat(facesArray);
                                    mat1=newmat;
                                    Size displaySize= new Size(500, 500);
                                    Size Size= new Size(100, 100);
                                    Imgproc.resize(newmat, mat1, displaySize);
                                    Highgui.imwrite("/mnt/sdcard/image02.jpg", mat1);
                                    Imgproc.resize(newmat, mat1, Size);
                                    Highgui.imwrite("/mnt/sdcard/image01.jpg",changRGB(mat1));
                                    saveP=0;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                        };
                    }.start();
                }
            }
        }catch (Exception e){

        }

        return mat;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.e("log_wons", "OpenCV init error");
            // Handle initialization error
        }
        initializeOpenCVDependencies();
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
    }

    public void requestPermission(){
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this,"please give me the permission",Toast.LENGTH_SHORT).show();
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_REQ_CODE);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_REQ_CODE);
            }
        }
    }
    public static Mat changRGB(Mat image){
        Mat mat1=image;
        try {
            Imgproc.cvtColor(image,mat1, Imgproc.COLOR_RGB2GRAY);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mat1;
    }
    public static String uploadFile() {
        String RequestURL="http://54.68.237.224:5000/upload";
        File file=new File("/mnt/sdcard/image01.jpg");
        String TAG = "uploadFile";
        int TIME_OUT = 10*10000000; //超时时间
        String CHARSET = "utf-8"; //设置编码
        String SUCCESS="1";
        String FAILURE="0";
        /** * android上传文件到服务器
         * @param file 需要上传的文件
         * @param RequestURL 请求的rul
         * @return 返回响应的内容
         */
        String BOUNDARY = "----WebKitFormBoundary7MA4YWxkTrZu0gW"; //边界标识 随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; //内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); conn.setReadTimeout(TIME_OUT); conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false); //不允许使用缓存
            conn.setRequestMethod("POST"); //请求方式
            conn.setRequestProperty("Charset", CHARSET);
            //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + "; boundary=" + BOUNDARY);
            if(file!=null) {
                /** * 当文件不为空，把文件包装并且上传 */
                OutputStream outputSteam=conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY); sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"image01\"; filename=\""+file.getName()+"\""+LINE_END);
                sb.append("Content-Type: image/jpeg; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1)
                {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功
                 * 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                Log.e(TAG, "response code:"+res);
                if(res==200)
                {
                    StringBuffer sb2=new StringBuffer();
                    String readLine=new String();
                    BufferedReader responseReader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                    while((readLine=responseReader.readLine())!=null){
                        sb2.append(readLine).append("\n");
                    }
                    responseReader.close();
                    System.out.println(sb2.toString());
                    return sb2.toString();
                }

            }
        } catch (MalformedURLException e)
        { e.printStackTrace(); }
        catch (IOException e)
        { e.printStackTrace(); }
        return FAILURE;
    }
}