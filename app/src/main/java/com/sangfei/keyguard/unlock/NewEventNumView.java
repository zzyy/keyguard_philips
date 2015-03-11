package com.sangfei.keyguard.unlock;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sangfei.keyguard.R;

@SuppressLint({"NewApi"})
public class NewEventNumView extends LinearLayout {
    private static final String TAG = "NewEventNumView";
    private static final int MAX_COUNT = 99;
    private static final String MAX_COUNT_STRING = "99+";
    boolean mAttachedToWindow = false;
    private MissCallObserver mMissCallObserver;
    private MmsUnReadObserver mMmsUnreadObserver;
    private TextView mNewEventNumView;
    private int mNewMissCallNum;
    private int mNewUnReadMmsNum;

    MissCallObserver.OnMissCallListener mOnMissCallListener = new MissCallObserver.OnMissCallListener() {
        public void onNewEvent(NewEvent paramNewEvent) {
        }

        public void refreshMissCallNumber(int paramInt) {
            mNewMissCallNum = paramInt;
            NewEventNumView.this.updateNewEventNumber();
        }
    };
    MmsUnReadObserver.OnNewMmsListener mOnNewMmsListener = new MmsUnReadObserver.OnNewMmsListener() {
        public void onNewEvent(NewEvent paramNewEvent) {
        }

        public void refreshUnReadMmsNumber(int paramInt) {
            mNewUnReadMmsNum = paramInt;
            NewEventNumView.this.updateNewEventNumber();
        }
    };

    Runnable mSetNumberRunnable = new Runnable() {
        public void run() {
            NewEventNumView.this.setNumberImp(NewEventNumView.this.mNewUnReadMmsNum + NewEventNumView.this.mNewMissCallNum);
        }
    };

    public NewEventNumView(Context paramContext) {
        this(paramContext, null);
    }
    public NewEventNumView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }
    public NewEventNumView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    private final void setNumberImp(int num) {
        if (num <= 0) {
            setVisibility(View.INVISIBLE);
            return;
        } else {
            setVisibility(View.VISIBLE);
        }

        TextView localTextView = this.mNewEventNumView;
        if (num > MAX_COUNT) {
            localTextView.setText(MAX_COUNT_STRING);
        } else {
            localTextView.setText( String.valueOf(num) );
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        this.mMmsUnreadObserver = new MmsUnReadObserver(new Handler(), getContext());
        getContext().getContentResolver().registerContentObserver(MmsUnReadObserver.MMS_URI, true, this.mMmsUnreadObserver);
        this.mMmsUnreadObserver.setOnNewMmsListener(this.mOnNewMmsListener);
        this.mMissCallObserver = new MissCallObserver(new Handler(), getContext());
        getContext().getContentResolver().registerContentObserver(MissCallObserver.MISS_CALL_URI, true, this.mMissCallObserver);
        this.mMissCallObserver.setOnMissCallListener(this.mOnMissCallListener);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mSetNumberRunnable);
        this.mAttachedToWindow = false;
        if (this.mMissCallObserver != null)
            getContext().getContentResolver().unregisterContentObserver(this.mMissCallObserver);
        if (this.mMmsUnreadObserver != null)
            getContext().getContentResolver().unregisterContentObserver(this.mMmsUnreadObserver);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mNewUnReadMmsNum = 0;
        this.mNewMissCallNum = 0;
        this.mNewEventNumView = ((TextView) findViewById(R.id.new_event_num));
        setVisibility(View.INVISIBLE);
    }

    public void updateNewEventNumber() {
        if (this.mAttachedToWindow) {
            Log.d("NewEventNumView", "mNewUnReadMmsNum=" + this.mNewUnReadMmsNum + " mNewMissCallNum=" + this.mNewMissCallNum);
            post(this.mSetNumberRunnable);
        }
    }
}
