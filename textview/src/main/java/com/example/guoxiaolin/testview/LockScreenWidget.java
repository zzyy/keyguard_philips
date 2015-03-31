package com.example.guoxiaolin.testview;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public abstract class LockScreenWidget
{
  private static Intent mLauncher;
  private ComponentName mComponentName;
  protected Context mContext;
    protected Activity mActivity;

  protected String tag;
  protected int parentDragViewid;

  public LockScreenWidget(Context paramContex)
  {
    this.mContext = paramContex;

  }

  public  abstract  ComponentName createComponentName();

 // public abstract void initView();

  public abstract View createHeaderView();



}
