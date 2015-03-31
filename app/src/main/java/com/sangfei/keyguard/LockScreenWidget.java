package com.sangfei.keyguard;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public abstract class LockScreenWidget {
    //  private static KeyguardActivityLauncher mLauncher;
    private ComponentName mComponentName;
    protected Context mContext;
    protected View mConttentView;
    private View mHeaderView;
    //PullUnlockView 的回掉函数
    private PullUnlockView.OnTriggerListener mTrigger = new PullUnlockView.OnTriggerListener() {
        public void onTrgger() {
            if (LockScreenWidget.this.mComponentName == null) {
                LockScreenWidget.this.mComponentName = LockScreenWidget.this.createComponentName();
            }
            Intent localIntent = new Intent();
            localIntent.setComponent(LockScreenWidget.this.mComponentName);
//      localIntent.addFlags(880803840);
//      LockScreenWidget.mLauncher.launchActivity(localIntent, false, false, null, null);
        }
    };
    protected String tag;

    /* public LockScreenWidget(Context paramContext, KeyguardActivityLauncher paramKeyguardActivityLauncher, String paramString)
     {
       this.mContext = paramContext;
       mLauncher = paramKeyguardActivityLauncher;
       this.tag = paramString;
     }*/
    public LockScreenWidget(Context paramContext, String paramString) {
        this.mContext = paramContext;
        this.tag = paramString;
    }

    //创建用于启动的Component
    public abstract ComponentName createComponentName();

    public abstract View createContentView();

    public abstract View createHeaderView();

    public View createPullView() {
        if (this.mConttentView == null)
            this.mConttentView = createContentView();
        if (this.mHeaderView == null)
            this.mHeaderView = createHeaderView();
        PullUnlockView localPullUnlockView = new PullUnlockView(this.mContext);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
        localPullUnlockView.setOnTriggerListener(this.mTrigger);
        localPullUnlockView.setHeader(this.mHeaderView);
        localPullUnlockView.addView(this.mConttentView, localLayoutParams);
        return localPullUnlockView;
    }

    public void onAttachedToWindow(){}
    public void onDetachedFromWindow(){}

    public String getTag() {
        return this.tag;
    }

    protected void updateBackground(int[] paramArrayOfInt) {
        if (this.mConttentView != null) {
            GradientDrawable localGradientDrawable = new GradientDrawable(Orientation.TL_BR, paramArrayOfInt);
            this.mConttentView.setBackgroundDrawable(localGradientDrawable);
        }
    }
}
