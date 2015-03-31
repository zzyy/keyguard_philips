package com.sangfei.keyguard;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.sangfei.keyguard.gallery.GalleryWidget;

public class MetroWidgetFrame
        extends FrameLayout
//        extends KeyguardWidgetFrame
{
//  public static final boolean SUPPORT_WEATHER;

    //  public MetroWidgetFrame(Context context, KeyguardActivityLauncher paramKeyguardActivityLauncher)
    public MetroWidgetFrame(Context context) {
        super(context);
        LockScreenMetroLayout localLockScreenMetroLayout = (LockScreenMetroLayout) View.inflate(context, R.layout.keyguard_metro_frame2, this)
                .findViewById(R.id.metro_layout);

        for (int i = 0; i < 5; i++) {
            if (i == 0) {
//        localLockScreenMetroLayout.addWidget(new CalendarWiget(context, paramKeyguardActivityLauncher));
                localLockScreenMetroLayout.addWidget(new GalleryWidget(context));
            }
            if (i == 1) {
//          localLockScreenMetroLayout.addWidget(new BatteryWidget(context, paramKeyguardActivityLauncher));
                localLockScreenMetroLayout.addWidget(new GalleryWidget(context));
            }
            if (i == 2) {
//          localLockScreenMetroLayout.addWidget(new ContactWidget(context, paramKeyguardActivityLauncher));
                localLockScreenMetroLayout.addWidget(new GalleryWidget(context));
            }
            if (i == 3) {
//          localLockScreenMetroLayout.addWidget(new GalleryWidget(context, paramKeyguardActivityLauncher));
                localLockScreenMetroLayout.addWidget(new GalleryWidget(context));
                continue;
            }
            if (i == 4) {
//        localLockScreenMetroLayout.addWidget(new KeyguardMusicWidget(context, paramKeyguardActivityLauncher));
                localLockScreenMetroLayout.addWidget(new GalleryWidget(context));
            }
        }
    }

/*  public static MetroWidgetFrame create(Context paramContext, KeyguardActivityLauncher paramKeyguardActivityLauncher)
  {
    MetroWidgetFrame localMetroWidgetFrame = new MetroWidgetFrame(paramContext, paramKeyguardActivityLauncher);
    localMetroWidgetFrame.setBackgroundColor(-1);
    return localMetroWidgetFrame;
  }*/

    public static MetroWidgetFrame create(Context context) {
        MetroWidgetFrame localMetroWidgetFrame = new MetroWidgetFrame(context);
        localMetroWidgetFrame.setBackgroundColor(-1);
        return localMetroWidgetFrame;
    }
}
