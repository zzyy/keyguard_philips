package com.sangfei.keyguard.gallery;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.sangfei.keyguard.LockScreenWidget;
import com.sangfei.keyguard.R;

import java.io.File;
import java.util.Random;

public class GalleryWidget extends LockScreenWidget {
    private static final String TAG = "GalleryWidget";

    private static final int UPDATE_TIME = 3000;
    private String mCurrentFile = getListFile();
    private Handler mHandler;
    private ImageView mPhotoView;
    private Runnable mTicker;
    private boolean mTickerStopped = false;

    /*public GalleryWidget(Context paramContext, KeyguardActivityLauncher paramKeyguardActivityLauncher)
    {
      super(paramContext, paramKeyguardActivityLauncher, "GalleryWidget");
    }*/
    public GalleryWidget(Context paramContext) {
        super(paramContext, "GalleryWidget");
    }


    //获取图片路径  随机获取
    public static String getListFile() {
        Random localRandom = new Random();
        File localFile1 = new File("/storage/sdcard1/DCIM/Camera/");
        File localFile2 = new File("/storage/sdcard0/DCIM/Camera/");
        File[] arrayOfFile1 = localFile1.listFiles();
        File[] arrayOfFile2 = localFile2.listFiles();
        String str = null;

        if ((arrayOfFile1 != null) && (arrayOfFile1.length > 0)) {
            str = arrayOfFile1[localRandom.nextInt(arrayOfFile1.length)].getPath();
            return str;
        }

        if ((arrayOfFile2 != null) && (arrayOfFile2.length > 0)) {
            str = arrayOfFile2[localRandom.nextInt(arrayOfFile2.length)].getPath();
            return str;
        }
        Log.d(TAG, "arrayOfFile1=" + arrayOfFile1 + " arrayOfFile2=" + arrayOfFile2 + " str=" + str);
        return null;
    }

    private void update() {
        this.mCurrentFile = getListFile();
        if (TextUtils.isEmpty(this.mCurrentFile)) {
            this.mTickerStopped = true;
            return;
        }
        new ImageLoader(this.mPhotoView, this.mCurrentFile, this.mContext).execute(new Void[0]);
    }

    public ComponentName createComponentName() {
        return new ComponentName("com.android.gallery3d", "com.android.gallery3d.app.GalleryActivity");
    }

    @Override
    public View createContentView() {
        View localView = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.keyguard_gallery_widget_small, null);
        this.mPhotoView = ((ImageView) localView.findViewById(R.id.color));
        new ImageLoader(this.mPhotoView, this.mCurrentFile, this.mContext).execute();
        return localView;
    }

    @Override
    public View createHeaderView() {
        ImageView localImageView = new ImageView(this.mContext);
        localImageView.setBackgroundResource(R.drawable.sf_gallery_icon);
        localImageView.setScaleType(ScaleType.CENTER);
        return localImageView;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mTickerStopped = false;
        this.mHandler = new Handler();
        this.mTicker = new Runnable() {
            public void run() {
                if (GalleryWidget.this.mTickerStopped)
                    return;
                GalleryWidget.this.update();
                GalleryWidget.this.mHandler.postDelayed(GalleryWidget.this.mTicker, 3000L);
            }
        };
        this.mTicker.run();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mTickerStopped = true;
    }
}
