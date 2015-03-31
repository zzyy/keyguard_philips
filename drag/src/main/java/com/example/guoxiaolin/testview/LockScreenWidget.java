package com.example.guoxiaolin.testview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public abstract class LockScreenWidget
{
  private static Intent mLauncher;
  private ComponentName mComponentName;
  protected Context mContext;
  protected View mConttentView;
  private View mHeaderView;

  protected String tag;

  public LockScreenWidget(Context paramContext, String paramString)
  {
    this.mContext = paramContext;
    this.tag = paramString;
  }

  public abstract ComponentName createComponentName();

  public abstract View createContentView();

  public abstract View createHeaderView();

//  public View createPullView()
//  {
//    if (this.mConttentView == null)
//      this.mConttentView = createContentView();
//    if (this.mHeaderView == null)
//      this.mHeaderView = createHeaderView();
//    PullUnlockView localPullUnlockView = new PullUnlockView(this.mContext);
//    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
//    localPullUnlockView.setOnTriggerListener(this.mTrigger);
//    localPullUnlockView.setHeader(this.mHeaderView);
//    localPullUnlockView.addView(this.mConttentView, localLayoutParams);
//    return localPullUnlockView;
//  }

}
