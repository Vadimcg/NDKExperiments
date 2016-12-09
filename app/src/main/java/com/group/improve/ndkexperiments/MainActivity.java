package com.group.improve.ndkexperiments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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


        Log.e("native",String.valueOf(this.passBooleanReturnBoolean(false)));
        Log.e("native",String.valueOf(this.passIntReturnint(1)));
        Log.e("native",String.valueOf(this.passDoubleReturnDouble(0.3)));
        Log.e("native",String.valueOf(this.passByteReturnByte((byte)43)));
        Log.e("native",String.valueOf(this.passStringReturnString("bla bla bla")));
       // this.localReference("bla bla bla",true);
       // this.globalReference("bla bla bla",false);
        this.weakReference("bla bla bla",false);
        this.allocObjectDemo();
    }


    public native boolean passBooleanReturnBoolean(boolean value);
    public native int passIntReturnint(int value);
    public native double passDoubleReturnDouble(double value);
    public native byte passByteReturnByte(byte value);
    public native String passStringReturnString(String value);
    public native void localReference(String value,boolean flag);
    public native void globalReference(String value,boolean flag);
    public native void weakReference(String value,boolean flag);
    public native void allocObjectDemo();

    public native String stringFromJNI();


}
