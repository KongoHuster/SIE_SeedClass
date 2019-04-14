package e.yizii.sie;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    boolean buttonSet = true;
    private static final String TAG = "MainActivity";
    private Button button;
    private ImageView imageView;
    private Boolean Stop = false;
    private EditText editText;
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

        editText = findViewById(R.id.editText2);
        imageView = findViewById(R.id.image1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (buttonSet == true)
                {
                    String input = editText.getText().toString();
                    if (input.isEmpty())
                    {
                        Toast.makeText(getApplication().getApplicationContext(), "输入字符为空，请输入两个汉字", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (input.length() != 2)
                    {
                        Toast.makeText(getApplication().getApplicationContext(), "请输入两个汉字", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!isChinese(input))
                    {
                        Toast.makeText(getApplication().getApplicationContext(), "请输入两个汉字", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    button.setText("关闭");
                    Stop = false;
                    Font16 font16 = new Font16(getApplication().getApplicationContext());
                    try {
                        final boolean[][] arr = font16.drawString(input);

//                        for (int i = 0; i < arr.length; i++) {
//                            if (i%2 == 0)
//                            {
//                                arr[i] = false;
//                            }else
//                            {
//                                arr[i] = Ture;
//                            }
//                        }
                        imageView.setVisibility(View.VISIBLE);
                        Looper looper = Looper.myLooper();//取得当前线程里的looper

                        final MyHandler mHandler = new MyHandler(looper);//构造一个handler使之可与looper通信
                        final String[] msgStr = {""};
                        Thread thread = new Thread(){
                            @Override
                            public void run(){
                                for (int i = 0; i < arr.length && Stop == false; i++)
                                {
                                    //添加0x55
                                    int head = 0x55;
                                    while (head != 0x00)
                                    {
                                        mHandler.removeMessages(0);
                                        if ((head & 0x01) == 0x01)
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
                                        head = head >> 1;
                                    }


                                    for (int j = 0; j < arr[0].length; j++) {
                                        mHandler.removeMessages(0);
                                        if (arr[i][j] == true)
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

                                    //添加0x55
                                    int end = 0x0D;
                                    while (end != 0x00)
                                    {
                                        mHandler.removeMessages(0);
                                        if ((end & 0x01) == 0x01)
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
                                        head = head >> 1;
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
                    buttonSet = true;
                    Stop = true;
                }

            }
        });
    }

    private class MyHandler extends Handler{
     public MyHandler(Looper looper){
         super(looper);
        }

        @Override
        public void handleMessage(Message msg) {//处理消息
         Log.d(TAG, "handleMessage: ");
         if (msg.obj.toString().equals("black"))
         {
             imageView.setImageResource(R.mipmap.withe);
         }else {
             imageView.setImageResource(R.mipmap.timg);
         }

        }
    }

    public static boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())
            flg = true;

        return flg;
    }

}
