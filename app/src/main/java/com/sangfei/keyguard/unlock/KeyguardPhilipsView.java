package com.sangfei.keyguard.unlock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

@TargetApi(11)
public class KeyguardPhilipsView extends FrameLayout
//  implements KeyguardSecurityView
{
    private static final int ANIMATION_DURATION = 250;
    private static final String CALL_LOG_CLASS_NAME = "com.mediatek.dialer.calllogex.CallLogActivityEx";
    private static final String CALL_LOG_PACKAGE_NAME = "com.android.dialer";
    private static final String MMS_CLASS_NAME = "com.android.mms.ui.ConversationList";
    private static final String MMS_PACKAGE_NAME = "com.android.mms";
    private static final int MOVE_FAST_DOWN_DISTANCES = 100;
    private static final int MOVE_FAST_UP_DISTANCES = 200;
    private static final int MOVE_SLOW_DOWN_DISTANCES = 100;
    private static final int MOVE_SLOW_UP_DISTANCES = 300;
    private static final String NEW_EVENT_SHOW_STATUS = "new_event_show_status";
    static final String TAG = "KeyguardPhilipsView";
    private static final int TRIGGER_TARGET_CAMERA = 2;
    private static final int TRIGGER_TARGET_NONE = 0;
    private static final int TRIGGER_TARGET_PHONE = 3;
    private static final int TRIGGER_TARGET_SMS = 4;
    private static final int TRIGGER_TARGET_UNLOCK = 1;
    static final Interpolator mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
    static final Interpolator mDecelerateInterpolator = new DecelerateInterpolator();
    private ImageView mArrowView;
    private Context mContext;
    private View mDragLayoutContainer;
    private int mDragLayoutContainerHeight;
    private int mDragLayoutMoveFastDownDistances;
    private int mDragLayoutMoveFastUpDistances;
    private int mDragLayoutMoveSlowDownDistances;
    private int mDragLayoutMoveSlowUpDistances;
    private int mDragLayoutTopOnScreen;
    private View mDragLayoutView;
    private int mDragLayoutViewDefaultPosition;
    private int mDragLayoutViewDoAnimationMaxBottomPosition;
    private int mDragLayoutViewDoAnimationMaxToPPosition;
    private boolean mDragging;
    private int mGestureStartDragLayoutTop;
    private int mGestureStartX;
    private int mGestureStartY;
    boolean mIsNeedInitLayout;
    private ImageView mLockIconView;
    private int mMaxVelocity;
    private int mMinVelocity;


    public KeyguardPhilipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public KeyguardPhilipsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public KeyguardPhilipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}