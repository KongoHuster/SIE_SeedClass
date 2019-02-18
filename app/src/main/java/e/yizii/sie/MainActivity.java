package e.yizii.sie;

import android.graphics.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.Policy;

public class MainActivity extends AppCompatActivity {

    private Camera camera = null;
    private Policy.Parameters parameters = null;
    boolean buttonSet = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (buttonSet == true)
                {

                    button.setText("关闭闪关灯");
                    if (null == camera) {
                        ;
                    }



                    buttonSet = false;
                }else {

                    button.setText("打开闪光灯");
                    buttonSet = true;
                }

            }
        });
    }
}
