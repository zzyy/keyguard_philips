package com.example.guoxiaolin.testview;

import android.annotation.TargetApi;
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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalendarWiget extends LockScreenWidget {
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
    public CalendarWiget(Context paramContext) {
        super(paramContext, "CalendarWiget");
        Locale localLocale = Locale.getDefault();
        this.mShowLunar = localLocale.getLanguage().equals("zh");
        // this.mLunar = LunarUtil.getInstance(this.mContext);
        //  this.mGanZhiLiUtil = new GanZhiLiUtil(this.mContext);
        //   this.mLockPatternUtils = new LockPatternUtils(this.mContext);
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

    public ComponentName createComponentName() {
        return new ComponentName("com.android.calendar", "com.android.calendar.AllInOneActivity");
    }

    public View createContentView() {
        View localView = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.calendar_widget, null);
        this.mDateView = ((TextView) localView.findViewById(R.id.date));
        mDateView.setText("mDateView");
        this.mDayOfWeekView = ((TextView) localView.findViewById(R.id.day_of_week));
        mDayOfWeekView.setText("mDayOfWeekView");
        this.mLunarView = ((TextView) localView.findViewById(R.id.lunar));
        mLunarView.setText("mLunarView");
        this.mCalendarTime = ((TextView) localView.findViewById(R.id.calendar_time));
        mCalendarTime.setText("mCalendarTime");
        this.mCalendarAmPm = ((TextView) localView.findViewById(R.id.calendar_ampm));
        mCalendarAmPm.setText("mCalendarAmPm");
        this.mAlarmStatusView = ((TextView) localView.findViewById(R.id.alarm_status));
        this.mAlarmImageView = ((ImageView) localView.findViewById(R.id.image));
        this.mConttentView = localView;
        updateView();
        localView.invalidate();
        return localView;
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

