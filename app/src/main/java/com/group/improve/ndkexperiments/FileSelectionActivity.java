package com.group.improve.ndkexperiments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.group.improve.ndkexperiments.file_selection.DirectoryFragment;

public class FileSelectionActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;
    private DirectoryFragment mDirectoryFragment;

    public static final String PATH="path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selection);

        ImageButton btnBack=(ImageButton)this.findViewById(R.id.imBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDirectoryFragment.onBackPressed_()) {
                    finish();
                }
            }
        });

        ImageButton btnOk=(ImageButton)this.findViewById(R.id.imOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String path=((DirectoryFragment)fragmentManager.getFragments().get(0)).getCurrentPath();

                if(path.isEmpty()){
                    mDirectoryFragment.showErrorBox(getString(R.string.file_manager_error_path_incorrect));
                    return;
                }

                Intent intent=new Intent(FileSelectionActivity.this,FileInfoActivity.class);
                intent.putExtra(PATH,path);
                FileSelectionActivity.this.startActivity(intent);

            }
        });


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        mDirectoryFragment = new DirectoryFragment();
        mDirectoryFragment.setDelegate(new DirectoryFragment.DocumentSelectActivityDelegate() {

            @Override
            public void startDocumentSelectActivity() {
            }

            @Override
            public void didSelectFiles(DirectoryFragment activity,
                                       ArrayList<String> files) {
                mDirectoryFragment.showErrorBox(files.get(0).toString());
            }

            @Override
            public void updateToolBarName(String name) {
            }
        });
        fragmentTransaction.add(R.id.fragment_container, mDirectoryFragment, "" + mDirectoryFragment.toString());
        fragmentTransaction.commit();
    }
	
    @Override
    protected void onDestroy() {
        mDirectoryFragment.onFragmentDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDirectoryFragment.onBackPressed_()) {
            super.onBackPressed();
        }
    }

}
