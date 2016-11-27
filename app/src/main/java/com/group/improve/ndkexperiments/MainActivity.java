package com.group.improve.ndkexperiments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tv;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        this. callNativeMethods();
    }


    private void callNativeMethods() {
        int a = 10, b = 100;
        int c = NativeAddition(a, b);
        tv.setText(a + "+" + b + "=" + c);
        c = NativeMultiplication(a, b);
        tv.append("\n" + a + "x" + b + "=" + c);
    }

    public native int NativeAddition(int a, int b);
    public native int NativeMultiplication(int a, int b);
    public native String stringFromJNI();


}
