package e.yizii.sie;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
    private Button buttonOrder1, buttonOrder2, buttonOrder3,buttonOrder4;
    private ImageView imageView;
    private Boolean Stop = false;
    private EditText editText, editText3, editTextNumber;
    private String Withe = "white";
    private String Black = "black";
    private long WhiteTime = System.currentTimeMillis();
    private long BlackTime = System.currentTimeMillis();
    private long sleepTime = 192;
    private long sleepTimeTrue = 192;
    private long sleepTimeTrueAll = 0;

    private long runTime = 0;
    private long runTimeAll = 0;
    Looper looper = Looper.myLooper();//取得当前线程里的looper
    final textHandler textHandler = new textHandler(looper);//构造一个handler使之可与looper通信

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOrder1 = findViewById(R.id.button);
        buttonOrder2 = findViewById(R.id.button2);
        buttonOrder3 = findViewById(R.id.button3);
        buttonOrder4 = findViewById(R.id.button4);
        sendMessage(textHandler, "White");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
        }

        editText = findViewById(R.id.editText2);
        imageView = findViewById(R.id.image1);

        editText3 = findViewById(R.id.editText3);
        editTextNumber = findViewById(R.id.editText);

        buttonOrder1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sleepTime = Integer.parseInt(editTextNumber.getText().toString());
                if (buttonSet == true) {
                    String input = editText.getText().toString();

                    if (!checkInputIsEmpty(input)) {
                        return;
                    }

                    buttonOrder1.setText("关闭");
                    Stop = false;
                    Font16 font16 = new Font16(getApplication().getApplicationContext());
                    try {
                        final boolean[][] arr = font16.drawString(input);
                        imageView.setVisibility(View.VISIBLE);
                        Looper looper = Looper.myLooper();//取得当前线程里的looper
                        final MyHandler mHandler = new MyHandler(looper);//构造一个handler使之可与looper通信
                        final String[] msgStr = {""};
                        Thread thread = new Thread() {
                            @Override
                            public void run() {

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
                                for (int i = 0; i < arr.length && Stop == false; i++) {

                                    for (int j = 0; j < arr[0].length; j++) {
                                        mHandler.removeMessages(0);
                                        if (arr[i][j] == true) {
                                            msgStr[0] = Withe;
//                                            OpenCamera();
                                        } else {
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

                } else {
                    buttonOrder1.setText("打开");
                    imageView.setVisibility(View.INVISIBLE);
                    buttonSet = true;
                    Stop = true;
                }

            }
        });

        buttonOrder2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sleepTime = sleepTimeTrue = Integer.parseInt(editTextNumber.getText().toString());
                if (buttonSet == true) {
                    buttonOrder2.setText("关闭");
//                    imageView.setVisibility(View.VISIBLE);
                    final Thread thread = new Thread() {
                        @Override
                        public void run() {
                            buttonSet = false;
                            int texthead = 0xff;
                            Stop = false;

                            sendMessage(textHandler,"0xff");
                            boolean[] result = hexToBool(texthead);
                            sendCheckMsg(result);
                            for (int i = 0; i < result.length && !Stop; i++) {
                                if (result[i] == true) {
                                    WhiteTime = System.currentTimeMillis();
                                    sendMessage(textHandler, "White");
                                } else {
                                    BlackTime = System.currentTimeMillis();
//                                    buttonOrder4.setBackgroundColor(Color.BLACK);
                                    sendMessage(textHandler, "Black");
                                }
                                try {
                                    if (checkStop(textHandler)) {
                                        return;
                                    }
                                    Thread.sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }


                            if (checkStop(textHandler)){
                                return;
                            }

                            //报头
                            sendMessage(textHandler,"0x55");
                            texthead = 0x55;
                            result = hexToBool(texthead);

                            for (int k = 0; k < 1; k++) {
                                Log.d(TAG, "run: " + k);
                                for (int i = 0; i < result.length && !Stop; i++) {
                                    ChangeColor(result[i],i + 1);
                                    try {
                                        if (checkStop(textHandler)) {
                                            return;
                                        }
                                        Thread.sleep(sleepTime);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            if (checkStop(textHandler)) {
                                return;
                            }

                            //报文
//                            sendMessage(textHandler, "0x25");
                            texthead = 0x25;
                            result = hexToBool(texthead);
                            for (int k = 0; k <= 15; k++) {
                                Log.d(TAG, "run: " + k);
                                for (int i = 0; i < result.length && !Stop; i++) {
                                    ChangeColor(result[i],k * 8 + i + 1 + 8);
                                    sendMessage(textHandler, String.valueOf(k * 8 + i));
                                    try {
                                        if (Stop == true) {
                                            sendMessage(textHandler,"0x0");
                                            return;
                                        }
                                        Thread.sleep(sleepTime);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if (checkStop(textHandler)) {
                                return;
                            }
                            //报尾
                            texthead = 0x15;
                            result = hexToBool(texthead);
                            sendMessage(textHandler,"0x15");
                            for (int i = 0; i < result.length && !Stop; i++) {
                                ChangeColor(result[i],8 + i + 129);
                                try {
                                    if (checkStop(textHandler)) {
                                        return;
                                    }
                                    Thread.sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.i(TAG, "sleepTimeAll: " + runTimeAll);
                            sendMessage(textHandler,"0x00");
//                            buttonOrder4.setBackgroundColor(Color.BLACK);
                            sendMessage(textHandler, "Black");
                        }
                    };
                    thread.start();
                } else {
                    buttonOrder2.setText("测试");
                    editText3.setText("");
                    buttonSet = true;
                    Stop = true;

                }
            }
        });

        buttonOrder3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (buttonSet == true) {
                    buttonOrder3.setText("常亮");
                    buttonOrder4.setBackgroundColor(Color.BLACK);
                    buttonSet = false;

                } else {
                    Looper looper = Looper.myLooper();//取得当前线程里的looper
                    final textHandler textHandler = new textHandler(looper);//构造一个handler使之可与looper通信
                    buttonOrder3.setText("关闭");
                    buttonOrder4.setBackgroundColor(Color.WHITE);
                    sendMessage(textHandler,"0x00");
                    buttonSet = true;
                }
            }

        });

    }


    private void sendCheckMsg(boolean[] result){
        for (int i = 0; i < result.length && !Stop; i++) {
            if (result[i] == true) {
                WhiteTime = System.currentTimeMillis();
                sendMessage(textHandler, "White");
            } else {
                BlackTime = System.currentTimeMillis();
//                                    buttonOrder4.setBackgroundColor(Color.BLACK);
                sendMessage(textHandler, "Black");
            }
            try {
                if (checkStop(textHandler)) {
                    return;
                }
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void ChangeColor(boolean result, int i)
    {
        if (result == true) {
            WhiteTime = System.currentTimeMillis();
            runTime = Math.abs(WhiteTime - BlackTime);
            runTimeAll = runTimeAll + runTime;
            Log.i(TAG, "runTime: " + Long.toString(Math.abs(runTime)));
//            editText.setText(Long.toString(Math.abs(runTime)));
//            buttonOrder4.setBackgroundColor(Color.WHITE);
            sendMessage(textHandler, "White");
        } else {
            BlackTime = System.currentTimeMillis();
            runTime = Math.abs(WhiteTime - BlackTime);
            runTimeAll = runTimeAll + runTime;
            Log.i(TAG, "runTime: " + Long.toString(Math.abs(runTime)));
//            editText.setText(Long.toString(Math.abs(runTime)));
//            buttonOrder4.setBackgroundColor(Color.BLACK);
            sendMessage(textHandler, "Black");
        }
        WhiteTime = System.currentTimeMillis();
        BlackTime = System.currentTimeMillis();
        sleepTimeTrueAll = sleepTimeTrueAll + sleepTimeTrue;
        sleepTime = sleepTimeTrue - (runTimeAll - sleepTimeTrueAll);

        Log.d(TAG, "sleepTime: " + sleepTime + " ruTime " + Long.toString(runTime) + " run: " + Integer.toString(i));
        Log.d(TAG, "runTimeAll: " + Long.toString(runTimeAll) + " sleepTimeTrueAll: " + Long.toString(sleepTimeTrueAll));

    }

    private void sendMessage(textHandler textHandler, String msg) {
        textHandler.removeMessages(0);
        Message message = textHandler.obtainMessage(1, 1, 1, msg);
        textHandler.sendMessage(message);
    }


    private boolean checkStop(textHandler textHandler) {
        if (Stop == true) {
            textHandler.removeMessages(0);
            Message message = textHandler.obtainMessage(1, 1, 1, "0x00");
            textHandler.sendMessage(message);
            return true;
        }
        return false;
    }

    private boolean checkInputIsEmpty(String input) {
        if (input.isEmpty()) {
            Toast.makeText(getApplication().getApplicationContext(), "输入字符为空，请输入两个汉字", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (input.length() != 2) {
            Toast.makeText(getApplication().getApplicationContext(), "请输入两个汉字", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isChinese(input)) {
            Toast.makeText(getApplication().getApplicationContext(), "请输入两个汉字", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {//处理消息
            Log.d(TAG, "handleMessage: ");
            if (msg.obj.toString().equals(Withe)) {
                imageView.setImageResource(R.mipmap.withe);
            } else {
                imageView.setImageResource(R.mipmap.timg);
            }

        }
    }

    private class textHandler extends Handler {
        public textHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {//处理消息
            Log.d(TAG, "handleMessage: ");
            if (msg.obj.toString().equals("White"))
            {
                buttonOrder4.setBackgroundColor(Color.WHITE);

            }else if (msg.obj.toString().equals("Black"))
            {
                buttonOrder4.setBackgroundColor(Color.BLACK);

            }else
            {
                editText3.setText(msg.obj.toString());
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

    private boolean[] hexToBool(int hex) {
        boolean[] result = new boolean[8];
        for (int i = 7; i >= 0; i--) {
            if ((hex & 0x01) == 0x01) {
                result[i] = true;
            } else {
                result[i] = false;
            }
            hex = hex >> 1;
        }
        return result;
    }


}
