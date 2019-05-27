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
    private long sleepTimeTrueAll = -192;

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
                sleepTime = sleepTimeTrue = Integer.parseInt(editTextNumber.getText().toString());
                if (buttonSet) {
                    buttonOrder2.setText("关闭");
                    final Thread thread = new Thread() {
                        @Override
                        public void run() {
                            buttonSet = false;
                            Stop = false;

                            String input = editText.getText().toString();
                            Font16 font16 = new Font16(getApplication().getApplicationContext());

                            try {
                                final boolean[][][] arr = font16.drawString(input);
                                sendMessage(textHandler,"0xff");
                                boolean[] result = hexToBool(0xff);
                                sendCheckMsg(result);

                                sendMessage(textHandler,"0x55");
                                WhiteTime = System.currentTimeMillis();
                                BlackTime = System.currentTimeMillis();
                                result = hexToBool(0x55);
                                sendShortCheckMsg(result);
                                sendShortCheckMsg(result);

                                for (int j = 0; j < 2; j++) {
                                    for (int k = 0; k < 16; k++) {
                                        //转化数据
                                        for (int i = 0; i < 16 && !Stop; i++) {
                                            if (i%8 == 0) {
                                                sendMessage(textHandler, "0xE");
                                                sendShortCheckMsg(hexToBool(0xFE));
                                            }
                                            ChangeColor(arr[j][(i)][k],k * 16 + i + 1 + 8);
                                            sendMessage(textHandler, String.valueOf(j*256 + k * 16 + i));
                                            try {
                                                if (Stop) {
                                                   sendMessage(textHandler,"0x0");
                                                    return;
                                                }
                                                Thread.sleep(sleepTime);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }
                                if (checkStop(textHandler)) {
                                    return;
                                }

                                //报尾
                                result = hexToBool(0xFE);
                                sendMessage(textHandler,"0xE");
                                sendShortCheckMsg(result);

                                sendMessage(textHandler,"0x15");
                                sendShortCheckMsg(hexToBool(0x01));
                                sendShortCheckMsg(hexToBool(0x05));


                                Log.i(TAG, "sleepTimeAll: " + runTimeAll);
                                sendMessage(textHandler,"0x00");
                                sendMessage(textHandler, "Black");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


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

        buttonOrder2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sleepTime = sleepTimeTrue = Integer.parseInt(editTextNumber.getText().toString());
                if (buttonSet) {
                    buttonOrder2.setText("关闭");
                    final Thread thread = new Thread() {
                        @Override
                        public void run() {
                            buttonSet = false;
                            Stop = false;

                            String input = editText.getText().toString();
                            Font16 font16 = new Font16(getApplication().getApplicationContext());

                            try {
                                final boolean[][][] arr = font16.drawString(input);
                                sendMessage(textHandler,"0xff");
                                boolean[] result = hexToBool(0xff);
                                sendCheckMsg(result);

                                sendMessage(textHandler,"0x55");
                                WhiteTime = System.currentTimeMillis();
                                BlackTime = System.currentTimeMillis();
                                result = hexToBool(0x55);
                                sendShortCheckMsg(result);
                                sendShortCheckMsg(result);

                                for (int j = 0; j < 2; j++) {
                                    for (int k = 0; k < 32; k++) {
//                                        if (k%2 == 0){
                                        sendMessage(textHandler,"0xE");
                                        sendShortCheckMsg(hexToBool(0xFE));
//                                        }

                                        result = hexToBool(0x1A);
                                        //转化数据
                                        for (int i = 0; i < 8 && !Stop; i++) {
                                            ChangeColor(result[i],k * 8 + i + 1 + 8);
                                            sendMessage(textHandler, String.valueOf(j*256 + k * 8 + i));
                                            try {
                                                if (Stop) {
                                                    sendMessage(textHandler,"0x0");
                                                    return;
                                                }
                                                Thread.sleep(sleepTime);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }
                                if (checkStop(textHandler)) {
                                    return;
                                }

                                //报尾
                                result = hexToBool(0xFE);
                                sendMessage(textHandler,"0xE");
                                sendShortCheckMsg(result);

                                sendMessage(textHandler,"0x15");
                                sendShortCheckMsg(hexToBool(0x01));
                                sendShortCheckMsg(hexToBool(0x05));


                                Log.i(TAG, "sleepTimeAll: " + runTimeAll);
                                sendMessage(textHandler,"0x00");
                                sendMessage(textHandler, "Black");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


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
                if (buttonSet) {
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
                sendMessage(textHandler, "White");
            } else {
                sendMessage(textHandler, "Black");
            }

            try {
                if (Stop) {
                    sendMessage(textHandler,"0x0");
                    return;
                }
                Thread.sleep(sleepTimeTrue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    private void sendShortCheckMsg(boolean[] result){
        for (int i = 4; i < result.length && !Stop; i++) {
            if (result[i] == true) {
                WhiteTime = System.currentTimeMillis();
                runTime = Math.abs(WhiteTime - BlackTime);
                sendMessage(textHandler, "White");
            } else {
                BlackTime = System.currentTimeMillis();
                runTime = Math.abs(WhiteTime - BlackTime);
                sendMessage(textHandler, "Black");
            }

            WhiteTime = System.currentTimeMillis();
            BlackTime = System.currentTimeMillis();
            runTimeAll += runTime;
            sleepTimeTrueAll = sleepTimeTrueAll + sleepTimeTrue;
            sleepTime = sleepTimeTrue - (runTimeAll - sleepTimeTrueAll);


            Log.d(TAG,  "sleepTime " + Long.toString(sleepTime));
            Log.d(TAG,  "runTimeAll " + Long.toString(runTimeAll));
            Log.d(TAG,  "sleepTimeTrueAll " + Long.toString(sleepTimeTrueAll));
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



    private void ChangeColor(boolean result, int i)
    {
        if (result == true) {
            WhiteTime = System.currentTimeMillis();
            runTime = Math.abs(WhiteTime - BlackTime);
            sendMessage(textHandler, "White");
        } else {
            BlackTime = System.currentTimeMillis();
            runTime = Math.abs(WhiteTime - BlackTime);
            sendMessage(textHandler, "Black");
        }
        WhiteTime = System.currentTimeMillis();
        BlackTime = System.currentTimeMillis();
        runTimeAll += runTime;

        sleepTimeTrueAll = sleepTimeTrueAll + sleepTimeTrue;
        sleepTime = sleepTimeTrue - (runTimeAll - sleepTimeTrueAll);
        Log.d(TAG,  "sleepTime " + Long.toString(sleepTime));
        Log.d(TAG,  "runTimeAll " + Long.toString(runTimeAll));
        Log.d(TAG,  "sleepTimeTrueAll " + Long.toString(sleepTimeTrueAll));

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
