package com.example.guoxiaolin.testview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalendarWiget {
    private static final CharSequence DEFAULT_FORMAT_AMPM="a";
    private static final CharSequence DEFAULT_FORMAT_HOUR_12 = "hh";
    private static final CharSequence DEFAULT_FORMAT_HOUR_24 = "kk";
    private static final CharSequence DEFAULT_FORMAT_MINUTE = "mm";
    private ImageView mAlarmImageView;
    private TextView mAlarmStatusView;
    private TextView mCalendarAmPm;
    private TextView mCalendarTime;
    private String mDateFormat;
    private TextView mDateView;
    private String mDayFormat;
    private TextView mDayOfWeekView;
    private CharSequence mFormatAmPm;
    private CharSequence mFormatHour;
    private CharSequence mFormatMinute;
    protected Context mContext;
    //  private GanZhiLiUtil mGanZhiLiUtil;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramContext, Intent paramIntent) {
            //   CalendarWiget.this.updateView();
        }
    };
    //  private LunarUtil mLunar;
    private TextView mLunarView;
    private boolean mShowLunar;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public CalendarWiget(Context mmContext) {
        mContext=mmContext;
        Locale localLocale = Locale.getDefault();
        this.mShowLunar = localLocale.getLanguage().equals("zh");
        this.mDayFormat = DateFormat.getBestDateTimePattern(localLocale, "EEEE");
        this.mDateFormat = DateFormat.getBestDateTimePattern(localLocale, "MMMd");
    }

    private boolean is24HourModeEnabled() {
        return DateFormat.is24HourFormat(this.mContext);
    }

    private void registerReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.TIME_SET");
        localIntentFilter.addAction("android.intent.action.TIME_TICK");
        localIntentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        this.mContext.registerReceiver(this.mIntentReceiver, localIntentFilter);
    }

    private void unregisterReceiver() {
        this.mContext.unregisterReceiver(this.mIntentReceiver);
    }

    public  ComponentName createComponentName() {
        return new ComponentName("com.android.calendar", "com.android.calendar.AllInOneActivity");
    }

    public void initView(Activity mActivity) {

        this.mDateView = ((TextView) mActivity.findViewById(R.id.date));
        mDateView.setText("mDateView");
        this.mDayOfWeekView = ((TextView) mActivity.findViewById(R.id.day_of_week));
        mDayOfWeekView.setText("mDayOfWeekView");
        this.mLunarView = ((TextView) mActivity.findViewById(R.id.lunar));
        mLunarView.setText("mLunarView");
        this.mCalendarTime = ((TextView) mActivity.findViewById(R.id.calendar_time));
        mCalendarTime.setText("mCalendarTime");
        this.mCalendarAmPm = ((TextView) mActivity.findViewById(R.id.calendar_ampm));
        mCalendarAmPm.setText("mCalendarAmPm");
        this.mAlarmStatusView = ((TextView) mActivity.findViewById(R.id.alarm_status));
        this.mAlarmImageView = ((ImageView) mActivity.findViewById(R.id.image));


    }


    public View createHeaderView() {
        ImageView localImageView = new ImageView(this.mContext);
        localImageView.setBackgroundResource(R.drawable.sf_calender_icon);
        localImageView.setScaleType(ScaleType.CENTER);
        return localImageView;
    }


    protected void updateView() {
        this.mFormatAmPm = DEFAULT_FORMAT_AMPM;
        this.mFormatHour = DEFAULT_FORMAT_HOUR_12;
        this.mFormatMinute = DEFAULT_FORMAT_MINUTE;
        Calendar localCalendar = Calendar.getInstance();
        this.mDateView.setText(DateFormat.format(this.mDateFormat, localCalendar));
        this.mDayOfWeekView.setText(DateFormat.format(this.mDayFormat, localCalendar));
        String str2 = (String)DateFormat.format(this.mFormatHour, localCalendar);
        String str3 = (String)DateFormat.format(this.mFormatMinute, localCalendar);
        String str4 = (String)DateFormat.format(this.mFormatAmPm, localCalendar);

        this.mCalendarTime.setText(str2 + ":" + str3);
        this.mCalendarAmPm.setText(str4);


    }

}

