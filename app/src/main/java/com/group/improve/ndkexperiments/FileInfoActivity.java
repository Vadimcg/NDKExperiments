package com.group.improve.ndkexperiments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FileInfoActivity extends AppCompatActivity {

    TextView tv;

    static {
        System.loadLibrary("fileIO");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle p = getIntent().getExtras();
        this.encryptFile("/storage/emulated/0/Download/Test","(tes");
    }


    public native void encryptFile(String path,String code);

}
