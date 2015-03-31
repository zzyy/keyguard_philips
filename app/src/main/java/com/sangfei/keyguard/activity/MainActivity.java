package com.sangfei.keyguard.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.sangfei.keyguard.LockScreenMetroLayout;
import com.sangfei.keyguard.R;
import com.sangfei.keyguard.gallery.GalleryWidget;


public class MainActivity extends Activity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.keyguard_philips_view);

//        LockScreenMetroLayout localLockScreenMetroLayout = (LockScreenMetroLayout) findViewById(R.id.metro_layout);
    }


}
