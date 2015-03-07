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
import android.widget.LinearLayout;

import com.eastaeon.keyguardtest.R;

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

    private int mWidgetPagerBottom;


    public KeyguardPhilipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyguardPhilipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void init(Context context){
        Log.d("KeyguardPhilipsView", "init");
        ViewConfiguration localViewConfiguration = ViewConfiguration.get(context);
        this.mMaxVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
        this.mMinVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    }

    private ShineTextView mOpenCameraTipTextView;
    private ShineTextView mUnlockTipTextView;
    private Vibrator mVibrator;
    private BackgroundAnimationView mBackgroundView;
    private NewEventShowView mUnReadEventLayoutView;
    private PullUnlockView mPullUnlockView;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Log.d("KeyguardPhilipsView", "onFinishInflate");
        this.mDragLayoutView = findViewById(R.id.drag_layout);
        this.mUnlockTipTextView = ((ShineTextView)findViewById(R.id.unlock_tip));
        this.mOpenCameraTipTextView = ((ShineTextView)findViewById(R.id.open_camera_tip));
        this.mArrowView = ((ImageView)findViewById(R.id.arrow));
        this.mLockIconView = ((ImageView)findViewById(R.id.lock_icon));
        this.mVibrator = ((Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE));
        this.mBackgroundView = ((BackgroundAnimationView)findViewById(R.id.background_view));
        this.mUnReadEventLayoutView = ((NewEventShowView)findViewById(R.id.new_event_show_layout));
//        this.mUnReadEventLayoutView.setOnNewEvnetListener(this.mOnNewEvnetListener);
        this.mDragLayoutContainer = findViewById(R.id.drag_layout_container);
        this.mPullUnlockView = ((PullUnlockView)findViewById(R.id.pull_unlock_view));
        this.mPullUnlockView.setOnTriggerListener(this.mOnPullUnlockTriggerListener);
    }

    PullUnlockView.OnTriggerListener mOnPullUnlockTriggerListener = new PullUnlockView.OnTriggerListener()
    {
        public void onTrgger()
        {
            /*NewEvent localNewEvent = KeyguardPhilipsView.this.mUnReadEventLayoutView.getEvent();
            if (localNewEvent.type == 1)
            {
                KeyguardPhilipsView.this.makeACall(localNewEvent.getNumber());
                return;
            }
            KeyguardPhilipsView.this.sendMessageTo(localNewEvent.getNumber());*/
        }
    };



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("KeyguardPhilipsView", "onLayout");
        if (this.mIsNeedInitLayout){
            int dragLayoutHeight = mDragLayoutView.getHeight();
            int unReadEventLayoutViewHeight = this.mUnReadEventLayoutView.getMeasuredHeight();
            Log.d("KeyguardPhilipsView", "onLayout layoutHeight=" + getHeight() + " dragLayoutHeight=" + dragLayoutHeight + " unReadEventLayoutViewHeight=" + unReadEventLayoutViewHeight);

            int dragLayoutContainerHeight = dragLayoutHeight + unReadEventLayoutViewHeight;
            if (mDragLayoutContainerHeight != dragLayoutContainerHeight){
                this.mDragLayoutContainerHeight = dragLayoutContainerHeight;
                this.mDragLayoutViewDefaultPosition = (getHeight() - this.mDragLayoutContainerHeight);
                //设置topMargin
                ((LinearLayout.LayoutParams) mDragLayoutContainer.getLayoutParams()).topMargin = mDragLayoutViewDefaultPosition;
                mBackgroundView.setBottomHeight(mDragLayoutContainerHeight);

//                saveDragLayoutContainerHeight(this.mDragLayoutContainerHeight);
            }
        }

        int[] arrayOfInt = new int[2];
        getLocationOnScreen(arrayOfInt);

        this.mDragLayoutViewDoAnimationMaxToPPosition = (-(arrayOfInt[1] + this.mDragLayoutView.getHeight() - this.mWidgetPagerBottom));
        this.mDragLayoutViewDoAnimationMaxBottomPosition = getBottom();
        Log.d("KeyguardPhilipsView", "mDragLayoutViewDoAnimationMaxToPPosition=" + this.mDragLayoutViewDoAnimationMaxToPPosition + " mDragLayoutViewDoAnimationMaxBottomPosition=" + this.mDragLayoutViewDoAnimationMaxBottomPosition + " mDragLayoutViewDefaultPosition=" + this.mDragLayoutViewDefaultPosition);
    }

    private VelocityTracker mVelocityTracker;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mVelocityTracker == null){
            this.mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);
        mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);

        int velocityY = (int) mVelocityTracker.getYVelocity();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("KeyguardPhilipsView", "onTouchEvent ACTION_DOWN");
                if (!isInDragLayoutView(x, y)){

                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onTouchEvent(event);
    }

    private boolean isInDragLayoutView(int paramInt1, int paramInt2)
    {
        if (this.mDragLayoutView == null)
            return false;
        int i = this.mDragLayoutView.getLeft() + this.mDragLayoutContainer.getLeft();
        int j = this.mDragLayoutView.getTop() + this.mDragLayoutContainer.getTop();
        int k = i + this.mDragLayoutView.getWidth();
        int m = j + this.mDragLayoutView.getHeight();
        boolean isInDragLayoytView = false;
        if (i <= paramInt1)
        {
            isInDragLayoytView = false;
            if (paramInt1 < k)
            {
                isInDragLayoytView = false;
                if (j <= paramInt2)
                {
                    isInDragLayoytView = false;
                    if (paramInt2 < m)
                        isInDragLayoytView = true;
                }
            }
        }
        return isInDragLayoytView;
    }
}