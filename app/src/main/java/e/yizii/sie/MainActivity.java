package e.yizii.sie;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.util.Measure;
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
    private Button button1, button2;
    private ImageView imageView;
    private Boolean Stop = false;
    private EditText editText,editText3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }

        button2 = findViewById(R.id.button2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }

        editText = findViewById(R.id.editText2);
        imageView = findViewById(R.id.image1);

        editText3 = findViewById(R.id.editText3);

        button1.setOnClickListener(new View.OnClickListener() {
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

                    button1.setText("关闭");
                    Stop = false;
                    Font16 font16 = new Font16(getApplication().getApplicationContext());
                    try {
                        final boolean[][] arr = font16.drawString(input);

                        imageView.setVisibility(View.VISIBLE);
                        Looper looper = Looper.myLooper();//取得当前线程里的looper

                        final MyHandler mHandler = new MyHandler(looper);//构造一个handler使之可与looper通信
                        final String[] msgStr = {""};
                        Thread thread = new Thread(){
                            @Override
                            public void run(){

                                //添加0x55
                                int head = 0x55;
                                boolean[] result = hexToBool(head);
                                mHandler.removeMessages(0);

                                for (int i = 0; i < result.length; i++) {
                                    if (result[i] == true) {
                                        msgStr[0] = "black";
                                    } else {
                                        msgStr[0] = "white";
                                    }
                                    Message m = mHandler.obtainMessage(1, 1, 1, msgStr[0]);//构造要传递的消息
                                    mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }


                                //body
                                for (int i = 0; i < arr.length && Stop == false; i++)
                                {

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
                                            Thread.sleep(25);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                //添加0x0D
                                int end = 0x0D;
                                result = hexToBool(end);
                                mHandler.removeMessages(0);

                                for (int i = 0; i < result.length; i++) {
                                    if (result[i] == true) {
                                        msgStr[0] = "black";
                                    } else {
                                        msgStr[0] = "white";
                                    }
                                    Message m = mHandler.obtainMessage(1, 1, 1, msgStr[0]);//构造要传递的消息
                                    mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
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

                    button1.setText("打开");
                    imageView.setVisibility(View.INVISIBLE);
                    buttonSet = true;
                    Stop = true;
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Looper looper = Looper.myLooper();//取得当前线程里的looper
                final MyHandler mHandler = new MyHandler(looper);//构造一个handler使之可与looper通信
                final textHandler textHandler = new textHandler(looper);//构造一个handler使之可与looper通信
                if (buttonSet == true) {
                    button2.setText("关闭");
                    imageView.setVisibility(View.VISIBLE);

                    final String[] msgStr = {""};
                    Log.i(TAG, "run: ");

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            Log.i(TAG, "run: ");
                            buttonSet = false;

                            int texthead = 0x55;
                            boolean[] result = hexToBool(texthead);
                            mHandler.removeMessages(0);
                            textHandler.removeMessages(0);
                            Message message = textHandler.obtainMessage(1, 1, 1, "0x" + Integer.toHexString(0x55));
                            textHandler.sendMessage(message);

                            for (int i = 0; i < result.length; i++) {
                                if (result[i] == true) {
                                    msgStr[0] = "black";
                                } else {
                                    msgStr[0] = "white";
                                }
                                Message m = mHandler.obtainMessage(1, 1, 1, msgStr[0]);//构造要传递的消息
                                mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (buttonSet == false)
                            {
                                for (int head = 0x01; buttonSet==false; head++) {
                                    result = hexToBool(head);
                                    mHandler.removeMessages(0);
                                    textHandler.removeMessages(0);
                                    message = textHandler.obtainMessage(1, 1, 1, "0x" + Integer.toHexString(head));
                                    textHandler.sendMessage(message);

                                    for (int i = 0; i < result.length; i++) {
                                        if (result[i] == true) {
                                            msgStr[0] = "black";
                                        } else {
                                            msgStr[0] = "white";
                                        }
                                        Message m = mHandler.obtainMessage(1, 1, 1, msgStr[0]);//构造要传递的消息
                                        mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if(head == 0x40)
                                        buttonSet=true;
                                }
                            }

                            //添加0x0D
                            int end = 0x0D;
                            result = hexToBool(end);
                            mHandler.removeMessages(0);
                            textHandler.removeMessages(0);
                            message = textHandler.obtainMessage(1, 1, 1, "0x" + Integer.toHexString(0x0D));
                            textHandler.sendMessage(message);

                            for (int i = 0; i < result.length; i++) {
                                if (result[i] == true) {
                                    msgStr[0] = "black";
                                } else {
                                    msgStr[0] = "white";
                                }
                                Message m = mHandler.obtainMessage(1, 1, 1, msgStr[0]);//构造要传递的消息
                                mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            message = textHandler.obtainMessage(1, 1, 1, "结束");
                            textHandler.sendMessage(message);

                        }
                    };
                    thread.start();
                }else {
                    button2.setText("测试");
                    imageView.setVisibility(View.INVISIBLE);
                    textHandler.removeMessages(0);
                    Message message = textHandler.obtainMessage(1, 1, 1, "0x0");
                    textHandler.sendMessage(message);
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

    private class textHandler extends Handler{
        public textHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {//处理消息
            Log.d(TAG, "handleMessage: ");
            editText3.setText(msg.obj.toString());

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

    private boolean[] hexToBool(int hex)
    {
        boolean[] result = new boolean[8];
        for (int i = 0; i < 8; i++) {
            if ((hex & 0x01) == 0x01) {
                result[i] = true;
            }else
            {
                result[i] = false;
            }
            hex = hex >> 1;
        }
        return result;
    }

}
