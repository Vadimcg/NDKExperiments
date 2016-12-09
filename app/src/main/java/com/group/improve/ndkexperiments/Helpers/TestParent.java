package com.group.improve.ndkexperiments.Helpers;

import android.util.Log;

/**
 *
 */
 public class TestParent {

    protected String mMessege="test123";

    public void showMessege(){
        Log.d("NDKTest",this.mMessege);
    }
}


