package com.group.improve.ndkexperiments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        this.encryptFile(p.getString(FileSelectionActivity.PATH));
    }


    public native void encryptFile(String path);

}
