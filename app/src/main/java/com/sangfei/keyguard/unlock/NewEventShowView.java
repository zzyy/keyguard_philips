package com.sangfei.keyguard.unlock;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(19)
public class NewEventShowView extends LinearLayout
{
  private static final String TAG = "MmsUnReadEventView";
  private boolean isHasNewEvent;
  private NewEvent mEvent;
  private ImageView mImageView;
  private MissCallObserver mMissCallObserver;
  private MmsUnReadObserver mMmsUnreadObserver;
  private TextView mName;
  private OnNewEventListener mNewEventListener;
  MissCallObserver.OnMissCallListener mOnMissCallListener = new MissCallObserver.OnMissCallListener()
  {
    public void onNewEvent(NewEvent paramNewEvent)
    {
      Log.d("MmsUnReadEventView", "Miss call onNewEvent");
      NewEventShowView.access$002(NewEventShowView.this, true);
      NewEventShowView.this.notifyNewEvent();
      NewEventShowView.this.update(paramNewEvent);
    }

    public void refreshMissCallNumber(int paramInt)
    {
    }
  };
  MmsUnReadObserver.OnNewMmsListener mOnNewMmsListener = new MmsUnReadObserver.OnNewMmsListener()
  {
    public void onNewEvent(NewEvent paramNewEvent)
    {
      Log.d("MmsUnReadEventView", "NewMms onNewEvent");
      NewEventShowView.access$002(NewEventShowView.this, true);
      NewEventShowView.this.notifyNewEvent();
      NewEventShowView.this.update(paramNewEvent);
    }

    public void refreshUnReadMmsNumber(int paramInt)
    {
    }
  };
  private TextView mTime;
  private TextView mUnReadMessage;

  public NewEventShowView(Context paramContext)
  {
    this(paramContext, null);
  }

  public NewEventShowView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public NewEventShowView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private static String formatTimeStampString(Context paramContext, long paramLong, boolean paramBoolean)
  {
    Time localTime1 = new Time();
    localTime1.set(paramLong);
    Time localTime2 = new Time();
    localTime2.setToNow();
    int i;
    if (localTime1.year != localTime2.year)
      i = 0x80B00 | 0x15;
    while (true)
    {
      if (paramBoolean)
        i |= 17;
      return DateUtils.formatDateTime(paramContext, paramLong, i);
      if (localTime1.yearDay != localTime2.yearDay)
      {
        i = 0x80B00 | 0x11;
        continue;
      }
      i = 0x80B00 | 0x1;
    }
  }

  private void setMessage(String paramString)
  {
    if (paramString == null)
    {
      this.mUnReadMessage.setVisibility(8);
      return;
    }
    this.mUnReadMessage.setText(paramString);
    this.mUnReadMessage.setVisibility(0);
  }

  private void setName(String paramString)
  {
    this.mName.setText(paramString);
  }

  private void setTime(String paramString)
  {
    this.mTime.setText(paramString);
  }

  private void update(NewEvent paramNewEvent)
  {
    String str1 = paramNewEvent.getName();
    String str2 = paramNewEvent.getNumber();
    long l = paramNewEvent.date;
    int i = paramNewEvent.type;
    if (TextUtils.isEmpty(str1))
    {
      setName(str2);
      setTime(formatTimeStampString(getContext(), l, false));
      if (i == 1)
        break label85;
      setMessage(paramNewEvent.getMessage());
      this.mImageView.setImageResource(R.drawable.sf_ic_newevent_smsmms);
    }
    while (true)
    {
      this.mEvent = paramNewEvent;
      return;
      setName(str1);
      break;
      label85: setMessage(null);
      this.mImageView.setImageResource(R.drawable.sf_ic_newevent_phone);
    }
  }

  public NewEvent getEvent()
  {
    return this.mEvent;
  }

  public boolean isHasNewEvent()
  {
    return this.isHasNewEvent;
  }

  protected void notifyNewEvent()
  {
    if (this.mNewEventListener != null)
      this.mNewEventListener.notifyNewEvent();
  }

  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mMmsUnreadObserver = new MmsUnReadObserver(new Handler(), getContext());
    getContext().getContentResolver().registerContentObserver(MmsUnReadObserver.MMS_URI, true, this.mMmsUnreadObserver);
    this.mMmsUnreadObserver.setOnNewMmsListener(this.mOnNewMmsListener);
    this.mMissCallObserver = new MissCallObserver(new Handler(), getContext());
    getContext().getContentResolver().registerContentObserver(MissCallObserver.MISS_CALL_URI, true, this.mMissCallObserver);
    this.mMissCallObserver.setOnMissCallListener(this.mOnMissCallListener);
  }

  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.mMmsUnreadObserver != null)
      getContext().getContentResolver().unregisterContentObserver(this.mMmsUnreadObserver);
    if (this.mMmsUnreadObserver != null)
      getContext().getContentResolver().unregisterContentObserver(this.mMmsUnreadObserver);
  }

  public void onFinishInflate()
  {
    super.onFinishInflate();
    this.mUnReadMessage = ((TextView)findViewById(R.id.message));
    this.mName = ((TextView)findViewById(R.id.name));
    this.mTime = ((TextView)findViewById(R.id.time));
    this.mImageView = ((ImageView)findViewById(R.id.new_event_img));
    setViewVisibility(8);
  }

  public void setHeight(int paramInt)
  {
    ViewGroup.LayoutParams localLayoutParams = getLayoutParams();
    Log.d("MmsUnReadEventView", "getHeight()=" + getHeight() + " height=" + paramInt);
    localLayoutParams.height = paramInt;
    setLayoutParams(localLayoutParams);
  }

  public void setOnNewEvnetListener(OnNewEventListener paramOnNewEventListener)
  {
    this.mNewEventListener = paramOnNewEventListener;
  }

  public void setViewVisibility(int paramInt)
  {
    setVisibility(paramInt);
  }

  public static abstract interface OnNewEventListener
  {
    public abstract void notifyNewEvent();
  }
}

/* Location:           D:\土豆ROM工具箱\classes_dex2jar.jar
 * Qualified Name:     com.sangfei.keyguard.unlock.NewEventShowView
 * JD-Core Version:    0.6.0
 */