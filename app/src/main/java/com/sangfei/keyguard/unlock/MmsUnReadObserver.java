package com.sangfei.keyguard.unlock;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MmsUnReadObserver extends ContentObserver {
    //監聽的uri
    public static final Uri MMS_URI = Telephony.Threads.CONTENT_URI;

    private static final String ADDR_CONSTRAINT = "type=137";
    private static final String[] ADDR_PROJECTION;
    private static final String MMS_QUERY = "content://mms/";
    private static final Uri MMS_QUERY_URI;
    private static final String[] MMS_STATUS_PROJECTION;
    private static final String NEW_INCOMING_MM_CONSTRAINT = "(read = 0  AND (m_type <> 134 AND m_type <> 136) AND date >= ";
    private static final String NEW_INCOMING_SM_CONSTRAINT = "(type = 1 AND read = 0 AND date >= ";
    private static final String PART_CONSTRAINT = "ct='text/plain'";
    private static final String[] PART_PROJECTION;

    //cursor 查询的uri和column
    private static final Uri SMS_QUERY_URI = Telephony.Sms.CONTENT_URI;
    private static final String[] SMS_STATUS_PROJECTION = {"_id", "date", "address", "body"};
    private static final String TAG = "MmsUnReadObserver";
    final Context mContext;
    long mCreateTime;
    private ArrayList<NewEvent> mEventList = new ArrayList<NewEvent>();
    private OnNewMmsListener mOnNewMmsListener;

    static {
        MMS_QUERY_URI = Uri.parse("content://mms/inbox");
        MMS_STATUS_PROJECTION = new String[]{"date", "_id"};
        ADDR_PROJECTION = new String[]{"address"};
        PART_PROJECTION = new String[]{"text"};
    }

    public MmsUnReadObserver(Handler paramHandler, Context context) {
        super(paramHandler);
        this.mContext = context;
        this.mCreateTime = System.currentTimeMillis();
        //TBD
//        Contact.init(context);
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

    // ERROR //
    private void getMmsContent(Cursor paramCursor) {
    }


    private void startQueryUnreadMessage() {
        new AsyncTask<Void, Void, Integer>() {
            protected Integer doInBackground(Void[] params) {
                MmsUnReadObserver.this.clearEventByType(2);
                MmsUnReadObserver.this.clearEventByType(3);
                Cursor cursor = MmsUnReadObserver.this.mContext.getContentResolver().query(
                        MmsUnReadObserver.SMS_QUERY_URI,
                        MmsUnReadObserver.SMS_STATUS_PROJECTION,
                        "(type = 1 AND read = 0 AND date >= " + MmsUnReadObserver.this.mCreateTime + ")",
                        null, null);
                int i = 0;
                if (cursor != null) {
                    try {
                        i = cursor.getCount();
                        while (cursor.moveToNext()) {
                            int _id = cursor.getInt(0);
                            long date = cursor.getLong(1);
                            NewEvent localNewEvent = new NewEvent(2, cursor.getString(2), date, cursor.getString(3), _id);
                            localNewEvent.updateContact();
                            MmsUnReadObserver.this.mEventList.add(localNewEvent);
                        }
                    } finally {
                        cursor.close();
                    }
                    cursor.close();
                }

                long time = MmsUnReadObserver.this.mCreateTime / 1000L;
                cursor = MmsUnReadObserver.this.mContext.getContentResolver().query(
                        MmsUnReadObserver.MMS_QUERY_URI,
                        MmsUnReadObserver.MMS_STATUS_PROJECTION,
                        "(read = 0  AND (m_type <> 134 AND m_type <> 136) AND date >= " + time + ")",
                        null, null);
                int j = 0;
                if (cursor != null) {
                    try {
                        j = cursor.getCount();
                        while (cursor.moveToNext())
                            MmsUnReadObserver.this.getMmsContent(cursor);
                    } finally {
                        cursor.close();
                    }
                    cursor.close();
                }
                return j + i;
            }

            protected void onPostExecute(Integer result) {
                if (MmsUnReadObserver.this.mOnNewMmsListener != null) {
                    if (result > 0)
                        MmsUnReadObserver.this.mOnNewMmsListener.refreshUnReadMmsNumber(result);
                    if (MmsUnReadObserver.this.mEventList.size() > 0) {
                        MmsUnReadObserver.this.filterAndSortEventList();
                        MmsUnReadObserver.this.mOnNewMmsListener.onNewEvent(MmsUnReadObserver.this.mEventList.get(0));
                    }
                }
            }

        }.execute();

    }

    @Override
    public void onChange(boolean paramBoolean) {
        startQueryUnreadMessage();
    }

    public void setOnNewMmsListener(OnNewMmsListener paramOnNewMmsListener) {
        this.mOnNewMmsListener = paramOnNewMmsListener;
    }

    //回掉接口
    public static abstract interface OnNewMmsListener {
        public abstract void onNewEvent(NewEvent paramNewEvent);

        public abstract void refreshUnReadMmsNumber(int paramInt);
    }
}

