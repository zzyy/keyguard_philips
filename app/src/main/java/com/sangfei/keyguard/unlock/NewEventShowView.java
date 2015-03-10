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

import com.sangfei.keyguard.R;

@TargetApi(19)
public class NewEventShowView extends LinearLayout {
    private static final String TAG = "MmsUnReadEventView";
    private boolean isHasNewEvent;
    private NewEvent mEvent;
    private ImageView mImageView;
    private MissCallObserver mMissCallObserver;
    private MmsUnReadObserver mMmsUnreadObserver;
    private TextView mName;
    private OnNewEventListener mNewEventListener;

    MissCallObserver.OnMissCallListener mOnMissCallListener = new MissCallObserver.OnMissCallListener() {
        public void onNewEvent(NewEvent event) {
            Log.d(TAG , "Miss call onNewEvent; event=" + event);
//            NewEventShowView.access$002(NewEventShowView.this, true);
            NewEventShowView.this.notifyNewEvent();
            NewEventShowView.this.update(event);
        }

        public void refreshMissCallNumber(int paramInt) {
        }
    };
    MmsUnReadObserver.OnNewMmsListener mOnNewMmsListener = new MmsUnReadObserver.OnNewMmsListener() {
        public void onNewEvent(NewEvent event) {
            Log.d(TAG, "NewMms onNewEvent; event=" + event);
//            NewEventShowView.access$002(NewEventShowView.this, true);
            NewEventShowView.this.notifyNewEvent();
            NewEventShowView.this.update(event);
        }

        public void refreshUnReadMmsNumber(int paramInt) {
        }
    };


    private TextView mTime;
    private TextView mUnReadMessage;


    public NewEventShowView(Context paramContext) {
        this(paramContext, null);
    }

    public NewEventShowView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public NewEventShowView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    // paramBoolean不知道干嘛的
    private static String formatTimeStampString(Context context, long date, boolean paramBoolean) {
        Time localTime1 = new Time();
        localTime1.set(date);
        Time localTime2 = new Time();
        localTime2.setToNow();
        int formatFlag = DateUtils.FORMAT_NO_NOON_MIDNIGHT |
                DateUtils.FORMAT_ABBREV_ALL |
                DateUtils.FORMAT_CAP_AMPM;
        if (localTime1.year != localTime2.year)
            formatFlag |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
        else if (localTime1.yearDay != localTime2.yearDay){
            formatFlag |= DateUtils.FORMAT_SHOW_DATE;
        }else {
            formatFlag |= DateUtils.FORMAT_SHOW_TIME;
        }

        return DateUtils.formatDateTime(context, date, formatFlag);
    }

    private void setMessage(String paramString) {
        if (paramString == null) {
            this.mUnReadMessage.setVisibility(View.GONE);
            return;
        }
        this.mUnReadMessage.setText(paramString);
        this.mUnReadMessage.setVisibility(View.VISIBLE);
    }

    private void setName(String paramString) {
        this.mName.setText(paramString);
    }

    private void setTime(String paramString) {
        this.mTime.setText(paramString);
    }

    private void update(NewEvent event) {
        String name = event.getName();
        String number = event.getNumber();
        long date = event.date;
        int type = event.type;
        this.mEvent = event;

        if (null==name || TextUtils.isEmpty(name)){
            setName(name);
        }else {
            setName(number);
        }

        setTime(formatTimeStampString(getContext(), date, false));

        switch (type){
            case NewEvent.TYPE_CALL:
                setMessage(null);
                this.mImageView.setImageResource(R.drawable.sf_ic_newevent_phone);
                break;
            case NewEvent.TYPE_MESSAGE_SMS:
                setMessage(event.getMessage());
                this.mImageView.setImageResource(R.drawable.sf_ic_newevent_smsmms);
                break;
        }

    }



    public NewEvent getEvent() {
        return this.mEvent;
    }

    public boolean isHasNewEvent() {
        return this.isHasNewEvent;
    }

    protected void notifyNewEvent() {
        if (this.mNewEventListener != null)
            this.mNewEventListener.notifyNewEvent();
    }

    //注册监听, 对未接电话 短信监听
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mMmsUnreadObserver = new MmsUnReadObserver(new Handler(), getContext());
        getContext().getContentResolver().registerContentObserver(MmsUnReadObserver.MMS_URI, true, this.mMmsUnreadObserver);
        this.mMmsUnreadObserver.setOnNewMmsListener(this.mOnNewMmsListener);
        this.mMissCallObserver = new MissCallObserver(new Handler(), getContext());
        getContext().getContentResolver().registerContentObserver(MissCallObserver.MISS_CALL_URI, true, this.mMissCallObserver);
        this.mMissCallObserver.setOnMissCallListener(this.mOnMissCallListener);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mMmsUnreadObserver != null)
            getContext().getContentResolver().unregisterContentObserver(this.mMmsUnreadObserver);
        if (this.mMmsUnreadObserver != null)
            getContext().getContentResolver().unregisterContentObserver(this.mMmsUnreadObserver);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mUnReadMessage = ((TextView) findViewById(R.id.message));
        this.mName = ((TextView) findViewById(R.id.name));
        this.mTime = ((TextView) findViewById(R.id.time));
        this.mImageView = ((ImageView) findViewById(R.id.new_event_img));
        setViewVisibility(View.GONE);

//        NewEvent event = new NewEvent(1,"123", 153458,"asdf", 1);
//        update(event);
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams localLayoutParams = getLayoutParams();
        Log.d("MmsUnReadEventView", "getHeight()=" + getHeight() + " height=" + height);
        localLayoutParams.height = height;
        setLayoutParams(localLayoutParams);
    }

    public void setOnNewEvnetListener(OnNewEventListener paramOnNewEventListener) {
        this.mNewEventListener = paramOnNewEventListener;
    }

    public void setViewVisibility(int paramInt) {
        setVisibility(paramInt);
    }

    public static abstract interface OnNewEventListener {
        public abstract void notifyNewEvent();
    }
}

