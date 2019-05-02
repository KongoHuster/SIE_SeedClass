package e.yizii.sie;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
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
    private Button button1, button2,button3,button4;
    private ImageView imageView;
    private Boolean Stop = false;
    private EditText editText,editText3,editTextNumber;
    private Camera m_Camera;
    private String Withe = "white";
    private String Black = "black";
    private Camera.Parameters mParameters;

    private long sleepTime = 500;
    private long sleepTimeTrue = sleepTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }

        editText = findViewById(R.id.editText2);
        imageView = findViewById(R.id.image1);

        editText3 = findViewById(R.id.editText3);
        editTextNumber = findViewById(R.id.editText);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sleepTime = Integer.parseInt(editTextNumber.getText().toString());
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
                                        msgStr[0] = Withe;
                                    } else {
                                        msgStr[0] = Black;
                                    }
                                    Message m = mHandler.obtainMessage(1, 1, 1, msgStr[0]);//构造要传递的消息
                                    mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息
                                    try {
                                        Thread.sleep(sleepTime);
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
                                            msgStr[0] = Withe;
//                                            OpenCamera();
                                        }else {
                                            msgStr[0] = Black;
//                                            CloseCamera();
                                        }
                                        Message m = mHandler.obtainMessage(1, 1, 1, msgStr[0]);//构造要传递的消息
                                        mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息
                                        try {
                                            Thread.sleep(sleepTime);
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
                                        msgStr[0] = Withe;
                                    } else {
                                        msgStr[0] = Black;
                                    }

                                    try {
                                        Thread.sleep(sleepTime);
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
//                    CloseCamera();
                    imageView.setVisibility(View.INVISIBLE);
                    buttonSet = true;
                    Stop = true;
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sleepTime = Integer.parseInt(editTextNumber.getText().toString());
                Looper looper = Looper.myLooper();//取得当前线程里的looper
                final textHandler textHandler = new textHandler(looper);//构造一个handler使之可与looper通信
                if (buttonSet == true) {
                    button2.setText("关闭");
                    imageView.setVisibility(View.VISIBLE);
                    Font16 font16 = new Font16(getApplication().getApplicationContext());

                    boolean[][] arr = new boolean[0][];
                    try {
                        arr = font16.drawString(editText.getText().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final boolean[][] finalArr = arr;
                    final Thread thread = new Thread() {
                        @Override
                        public void run() {
                            Log.i(TAG, "run: ");
                            buttonSet = false;

                            int texthead = 0xff;
                            long tureTime = System.currentTimeMillis();
                            long falseTime = System.currentTimeMillis();
                            boolean[] result = hexToBool(texthead);
                            Stop = false;

                            textHandler.removeMessages(0);
                            Message message = textHandler.obtainMessage(1, 1, 1, "0xff");
                            textHandler.sendMessage(message);
                            for (int i = 0; i < result.length && !Stop; i++) {
                                if (result[i] == true) {
                                    tureTime = System.currentTimeMillis();
                                    Log.i(TAG, "run: " + Long.toString(Math.abs(tureTime - falseTime)));
                                    button4.setBackgroundColor(Color.WHITE);
                                } else {
                                    falseTime = System.currentTimeMillis();
                                    Log.i(TAG, "run: " + Long.toString(Math.abs(tureTime - falseTime)));
                                    button4.setBackgroundColor(Color.BLACK);
                                }

                                try {
                                    if(Stop == true)
                                    {
                                        textHandler.removeMessages(0);
                                        message = textHandler.obtainMessage(1, 1, 1, "0x00");
                                        textHandler.sendMessage(message);
                                        return;
                                    }
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            if(Stop == true)
                            {
                                textHandler.removeMessages(0);
                                message = textHandler.obtainMessage(1, 1, 1, "0x00");
                                textHandler.sendMessage(message);
                                return;
                            }

                            textHandler.removeMessages(0);
                            message = textHandler.obtainMessage(1, 1, 1, "0x55");
                            textHandler.sendMessage(message);
                            texthead = 0x55;
                            result = hexToBool(texthead);

                            long runTime = 0;
                            long runTimeAll = 0;

                            for (int k = 0; k < 1; k++) {
                                Log.d(TAG, "run: " + k);
                                for (int i = 0; i < result.length && !Stop; i++) {
                                    if (result[i] == true) {
                                        tureTime = System.currentTimeMillis();
                                        Log.i(TAG, "runHead: " + Long.toString(Math.abs(tureTime-falseTime)));
                                        button4.setBackgroundColor(Color.WHITE);
                                    } else {
                                        falseTime = System.currentTimeMillis();
                                        Log.i(TAG, "runHead: " + Long.toString(Math.abs(tureTime-falseTime)));
                                        button4.setBackgroundColor(Color.BLACK);
                                    }


                                    try {
                                        if(Stop == true)
                                        {
                                            textHandler.removeMessages(0);
                                            message = textHandler.obtainMessage(1, 1, 1, "0x00");
                                            textHandler.sendMessage(message);
                                            return;
                                        }
                                        Thread.sleep(sleepTime);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }


                            if(Stop == true)
                            {
                                textHandler.removeMessages(0);
                                message = textHandler.obtainMessage(1, 1, 1, "0x00");
                                textHandler.sendMessage(message);
                                return;
                            }


                            textHandler.removeMessages(0);
                            message = textHandler.obtainMessage(1, 1, 1, "0x15");
                            textHandler.sendMessage(message);
                            texthead = 0x15;
                            result = hexToBool(texthead);
                            for (int k = 0; k <= 15; k++) {
                                Log.d(TAG, "run: " + k);
                                for (int i = 0; i < result.length && !Stop; i++) {
                                    if (result[i] == true) {
                                        tureTime = System.currentTimeMillis();
                                        runTime = Math.abs(tureTime-falseTime);
                                        Log.i(TAG, "runHead: " + Long.toString(Math.abs(runTime)));
                                        editText.setText(Long.toString(Math.abs(runTime)));
                                        button4.setBackgroundColor(Color.WHITE);
                                    } else {
                                        falseTime = System.currentTimeMillis();
                                        runTime = Math.abs(tureTime-falseTime);
                                        Log.i(TAG, "runHead: " + Long.toString(Math.abs(runTime)));
                                        editText.setText(Long.toString(Math.abs(runTime)));
                                        button4.setBackgroundColor(Color.BLACK);
                                    }
                                    tureTime = System.currentTimeMillis();
                                    falseTime = System.currentTimeMillis();

                                    runTimeAll = runTimeAll + runTime;
                                    sleepTime = sleepTime - (runTimeAll - (k*8+i+1) * sleepTimeTrue);
                                    Log.i(TAG, "runTimeAll: " + runTimeAll);
                                    Log.i(TAG, "runTrueTime: " + (k*8+i+1) * sleepTimeTrue);
                                    Log.d(TAG, "sleepTime: " + sleepTime);

                                    textHandler.removeMessages(0);
                                    message = textHandler.obtainMessage(1, 1, 1, String.valueOf(k*8+i));
                                    textHandler.sendMessage(message);
                                    try {
                                        if(Stop == true)
                                        {
                                            textHandler.removeMessages(0);
                                            message = textHandler.obtainMessage(1, 1, 1, "0x00");
                                            textHandler.sendMessage(message);
                                            return;
                                        }
                                    Thread.sleep(sleepTime);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }


                            if(Stop == true)
                            {
                                textHandler.removeMessages(0);
                                message = textHandler.obtainMessage(1, 1, 1, "0x00");
                                textHandler.sendMessage(message);
                                return;
                            }

//                            textHandler.removeMessages(0);
//                            message = textHandler.obtainMessage(1, 1, 1, "body");
//                            textHandler.sendMessage(message);
//                            //body
//                            for (int i = 0; i < finalArr.length && Stop == false; i++)
//                            {
//
//                                for (int j = 0; j < finalArr[0].length; j++) {
//                                    if (finalArr[i][j] == true) {
//                                        tureTime = System.currentTimeMillis();
//                                        Log.i(TAG, "runBody " + Integer.toString(i*16+j)  + " " + Long.toString(Math.abs(tureTime-falseTime)));
//                                        button4.setBackgroundColor(Color.WHITE);
//                                    } else {
//                                        falseTime = System.currentTimeMillis();
//                                        Log.i(TAG, "runBody " + Integer.toString(i*16+j)  + " " + Long.toString(Math.abs(tureTime-falseTime)));
//                                        button4.setBackgroundColor(Color.BLACK);
//                                    }
//
//                                    tureTime = System.currentTimeMillis();
//                                    falseTime = System.currentTimeMillis();
//
//                                    if(Stop == true)
//                                    {
//                                        textHandler.removeMessages(0);
//                                        message = textHandler.obtainMessage(1, 1, 1, "0x00");
//                                        textHandler.sendMessage(message);
//                                        return;
//                                    }
//
//                                    try {
//                                        Thread.sleep(sleepTime);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }

                            if(Stop == true)
                            {
                                textHandler.removeMessages(0);
                                message = textHandler.obtainMessage(1, 1, 1, "0x00");
                                textHandler.sendMessage(message);
                                return;
                            }

                            textHandler.removeMessages(0);
                            message = textHandler.obtainMessage(1, 1, 1, "0x0D");
                            textHandler.sendMessage(message);


                            for (int i = 0; i < result.length && !Stop; i++) {
                                if (result[i] == true) {
                                    tureTime = System.currentTimeMillis();
                                    Log.i(TAG, "runEnd: " + Long.toString(Math.abs(tureTime-falseTime)));
                                    button4.setBackgroundColor(Color.WHITE);
                                } else {
                                    falseTime = System.currentTimeMillis();
                                    Log.i(TAG, "runEnd: " + Long.toString(Math.abs(tureTime-falseTime)));
                                    button4.setBackgroundColor(Color.BLACK);
                                }


                                try {
                                    if(Stop == true)
                                    {
                                        textHandler.removeMessages(0);
                                        message = textHandler.obtainMessage(1, 1, 1, "0x00");
                                        textHandler.sendMessage(message);
                                        return;
                                    }
                                    Thread.sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            textHandler.removeMessages(0);
                            message = textHandler.obtainMessage(1, 1, 1, "0x00");
                            textHandler.sendMessage(message);
                            button4.setBackgroundColor(Color.BLACK);

                        }
                    };
                    thread.start();
                }else {
                    button2.setText("测试");
                    editText3.setText("");
                    buttonSet = true;
                    Stop = true;

                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    Looper looper = Looper.myLooper();//取得当前线程里的looper
                    final MyHandler mHandler = new MyHandler(looper);//构造一个handler使之可与looper通信
                    final textHandler textHandler = new textHandler(looper);//构造一个handler使之可与looper通信
                    if (buttonSet == true) {
                        button3.setText("关闭");
                        button4.setBackgroundColor(Color.BLACK);
                        buttonSet = false;

                    }else {
                        button3.setText("常亮");
                        button4.setBackgroundColor(Color.WHITE);
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
         if (msg.obj.toString().equals(Withe))
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
        for (int i = 7; i > 0; i--) {
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



    public void OpenCamera() {
        try{
            m_Camera = Camera.open();
            Camera.Parameters mParameters;
            mParameters = m_Camera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            m_Camera.setParameters(mParameters);
        } catch(Exception ex){}


    }

    public void CloseCamera() {
        try{
            Camera.Parameters mParameters;
            mParameters = m_Camera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            m_Camera.setParameters(mParameters);
            m_Camera.release();
        } catch(Exception ex){}
    }

    public void OpenCameraTure()
    {
        try{
            m_Camera = Camera.open();
            mParameters = m_Camera.getParameters();

        } catch(Exception ex){}
    }

    public void CloseCameraTure()
    {
        try{
            m_Camera.release();

        } catch(Exception ex){}
    }
}
