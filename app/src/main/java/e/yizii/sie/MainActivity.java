package e.yizii.sie;


import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Camera mCamera = null;
    private Camera.Parameters mParameters = null;
    boolean buttonSet = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.button);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (buttonSet == true)
                {
                    button.setText("关闭闪关灯");
                    for (int i =0; i < 10; i++)
                    {
                        startCamera();
//                        SystemClock.sleep(1000);


                    }

                    buttonSet = false;
                }else {

                    button.setText("打开闪光灯");
                    closeCamera();
                    buttonSet = true;
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
}
