package com.sangfei.keyguard.unlock;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MissCallObserver extends ContentObserver {
    private static final String[] MISS_CALL_PROJECTION;
    private static final String MISS_CALL_SELECTION = "(new = ? AND type = ? AND is_read = ? AND date >= ";
    private static final String[] MISS_CALL_SELECTION_ARGS;
    //  public static final Uri MISS_CALL_URI = CallLog.Calls.CONTENT_URI;
    public static final Uri MISS_CALL_URI = Uri.parse("content://call_log/calls");
    private static final String TAG = "MissCallObserver";
    private final Context mContext;
    private long mCreateTime;
    private ArrayList<NewEvent> mEventList = new ArrayList<NewEvent>();
    private OnMissCallListener mOnMissCallListener;

    static {
        MISS_CALL_PROJECTION = new String[]{"_id", "new", "date", "number"};
        String[] arrayOfString = new String[3];
        arrayOfString[0] = "1";
        arrayOfString[1] = Integer.toString(3);
        arrayOfString[2] = Integer.toString(0);
        MISS_CALL_SELECTION_ARGS = arrayOfString;
    }

    public MissCallObserver(Handler paramHandler, Context paramContext) {
        super(paramHandler);
        this.mContext = paramContext;
        this.mCreateTime = System.currentTimeMillis();
    }

    private void clearEventByType(int paramInt) {
        Iterator<NewEvent> iterator = mEventList.iterator();
        while (iterator.hasNext()) {
            NewEvent event = iterator.next();
            if (event.type == paramInt) {
                iterator.remove();
            }
        }
    }

    private void filterAndSortEventList() {
        Collections.sort(this.mEventList);
    }

    public void onChange(boolean paramBoolean) {
        refreshUnReadNumber();
    }

    public void refreshUnReadNumber() {
        new AsyncTask<Void, Void, Integer>() {
            public Integer doInBackground(Void[] params) {
                MissCallObserver.this.clearEventByType(1);
                Cursor cursor = MissCallObserver.this.mContext.getContentResolver().query(
                        MissCallObserver.MISS_CALL_URI,
                        MissCallObserver.MISS_CALL_PROJECTION,
                        "(new = ? AND type = ? AND is_read = ? AND date >= " + MissCallObserver.this.mCreateTime + " )",
                        MissCallObserver.MISS_CALL_SELECTION_ARGS, null);
                int i = 0;
                if (cursor != null) {
                    try {
                        i = cursor.getCount();
                        while (cursor.moveToNext()) {
                            NewEvent event = new NewEvent();
                            event.ids = cursor.getInt(0) + "";
                            event.date = cursor.getLong(2);
                            event.number = cursor.getString(3);
                            event.type = 1;
                            event.count = 1;
                            event.updateContact();
                            MissCallObserver.this.mEventList.add(event);
                        }
                    } finally {
                        cursor.close();
                    }
                    cursor.close();
                }
                return Integer.valueOf(i);
            }

            public void onPostExecute(Integer paramInteger) {
                if (MissCallObserver.this.mOnMissCallListener != null) {
                    if (paramInteger.intValue() > 0)
                        MissCallObserver.this.mOnMissCallListener.refreshMissCallNumber(paramInteger.intValue());
                    if (MissCallObserver.this.mEventList.size() > 0) {
                        MissCallObserver.this.filterAndSortEventList();
                        MissCallObserver.this.mOnMissCallListener.onNewEvent(MissCallObserver.this.mEventList.get(0));
                    }
                }
            }
        }.execute();
    }

    public void setOnMissCallListener(OnMissCallListener paramOnMissCallListener) {
        this.mOnMissCallListener = paramOnMissCallListener;
    }

    public static abstract interface OnMissCallListener {
        public abstract void onNewEvent(NewEvent paramNewEvent);

        public abstract void refreshMissCallNumber(int paramInt);
    }
}
