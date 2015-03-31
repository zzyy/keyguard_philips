package com.example.guoxiaolin.testview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;


public class ContactWidget extends LockScreenWidget
{
  public ContactWidget(Context paramContext )
  {
    super(paramContext, "ContactWidget");
  }

  public ComponentName createComponentName()
  {
    return new ComponentName("com.android.contacts", "com.android.contacts.activities.PeopleActivity");
  }

  public View createContentView()
  {

      ImageView localImageView = new ImageView(this.mContext);
    localImageView.setBackgroundResource(R.drawable.contact_widget_bg);

    return localImageView;
  }

  public View createHeaderView()
  {

    ImageView localImageView = new ImageView(this.mContext);
    localImageView.setBackgroundResource(R.drawable.contact_widget_icon);
    localImageView.setScaleType(ScaleType.CENTER);

    return localImageView;
  }
}
