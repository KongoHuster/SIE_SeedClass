package e.yizii.sie;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private Camera mCamera = null;
    private Camera.Parameters mParameters = null;
    boolean buttonSet = true;
    private static final String TAG = "MainActivity";
    private Button button;
    private ImageView imageView;
    private Boolean Stop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }


        imageView = findViewById(R.id.image1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (buttonSet == true)
                {
                    button.setText("关闭");
                    Stop = false;
                    Font16 font16 = new Font16(getApplication().getApplicationContext());
                    try {
                        final boolean[][] arr = font16.drawString("我们");

                        imageView.setVisibility(View.VISIBLE);
                        Looper looper = Looper.myLooper();//取得当前线程里的looper

                        final MyHandler mHandler = new MyHandler(looper);//构造一个handler使之可与looper通信
                        final String[] msgStr = {""};
                        Thread thread = new Thread(){
                            @Override
                            public void run(){
                                for (int i = 0; i < arr.length && Stop == false; i++)
                                {
                                    for (int j = 0; j < arr[0].length; j++) {
                                        mHandler.removeMessages(0);
                                        if (arr[i][j] = true)
                                        {
                                            msgStr[0] = "black";
                                        }else {
                                            msgStr[0] = "white";
                                        }
                                        Message m = mHandler.obtainMessage(1, 1, 1, msgStr[0]);//构造要传递的消息
                                        mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        };

                        thread.start();
                        buttonSet = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else {

                    button.setText("打开");
                    imageView.setVisibility(View.INVISIBLE);
//                    closeCamera();
                    buttonSet = true;
                    Stop = true;
                }

            }
        });
    }

    private void startCamera(){
        mCamera = Camera.open();
        mParameters = mCamera.getParameters();
        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(mParameters);

    }

    private void closeCamera(){
        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(mParameters);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    private class MyHandler extends Handler{
     public MyHandler(Looper looper){
         super(looper);
        }

        @Override
        public void handleMessage(Message msg) {//处理消息
         if (msg.obj.toString().equals("white"))
         {
             imageView.setImageResource(R.drawable.withe);
         }else {
             imageView.setImageResource(R.drawable.timg);
         }
//            Log.d(TAG, "handleMessage: " + msg.obj.toString());
//            text.setText(msg.obj.toString());
        }

    }

//    class Mythread extends Thread{
//        Mythread(){
//
//        }
//
//        @Override
//        public void run(){
//
//        }
//    }
//

}
