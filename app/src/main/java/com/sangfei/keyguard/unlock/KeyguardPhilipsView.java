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
        init(context);
    }

    void init(Context context){
        Log.d("KeyguardPhilipsView", "init");
        ViewConfiguration localViewConfiguration = ViewConfiguration.get(context);
        this.mMaxVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
        this.mMinVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    }

    private ObjectAnimator mUnlockAnimator;
    private void startAnimation(int targetPosition) {
        if (this.mUnlockAnimator != null)
            this.mUnlockAnimator.cancel();
        doAnimation(targetPosition);
    }

    private AnimatorListenerAdapter animatorListen = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator paramAnimator) {
            Log.d("KeyguardPhilipsView", "onAnimationEnd");
//            KeyguardPhilipsView.this.doTriggerAction(KeyguardPhilipsView.this.mTriggerActionSave);
//            KeyguardPhilipsView.this.resetView();
        }

        public void onAnimationStart(Animator paramAnimator) {
            Log.d("KeyguardPhilipsView", "onAnimationStart");
//            KeyguardPhilipsView.access$102(KeyguardPhilipsView.this,KeyguardPhilipsView.this.mTriggerAction);
        }
    };

    //打开相应的app
    private void doTriggerAction(int paramInt) {
        Intent localIntent = new Intent();
        switch (paramInt) {
            default:
                return;
            case 0:
                Log.d("KeyguardPhilipsView", "TRIGGER_TARGET_NONE");
                return;
            case 1:
                Log.d("KeyguardPhilipsView", "TRIGGER_TARGET_UNLOCK");
                doUnlockAction();
                return;
            case 2:
                Log.d("KeyguardPhilipsView", "TRIGGER_TARGET_CAMERA");
//                this.mActivityLauncher.launchCamera(null, null);
                return;
            case 3:
                Log.d("KeyguardPhilipsView", "TRIGGER_TARGET_PHONE");
                localIntent.setComponent(new ComponentName("com.android.dialer",
                        "com.mediatek.dialer.calllogex.CallLogActivityEx"));
//                this.mActivityLauncher.launchActivity(localIntent, false, true, null, null);
                doUnlockAction();
                return;
            case 4:
        }
        Log.d("KeyguardPhilipsView", "TRIGGER_TARGET_SMS");
        localIntent.setComponent(new ComponentName("com.android.mms",
                "com.android.mms.ui.ConversationList"));
//        this.mActivityLauncher.launchActivity(localIntent, false, true, null, null);
        doUnlockAction();
    }

    private void doUnlockAction() {
//        this.mCallback.userActivity(0L);
//        this.mCallback.dismiss(false);
    }

    private void doAnimation(int paramInt) {
        new Interpolator() {
            public float getInterpolation(float paramFloat) {
                float f = paramFloat - 1.0F;
                return 1.0F + f * (f * (f * (f * f)));
            }
        };
        DragLayoutViewWrap localDragLayoutViewWrap = new DragLayoutViewWrap();
        int[] arrayOfInt = new int[2];
        arrayOfInt[0] = getDragLayoutPosition();
        arrayOfInt[1] = paramInt;
        this.mUnlockAnimator = ObjectAnimator.ofInt(localDragLayoutViewWrap,
                "Position", arrayOfInt);
        this.mUnlockAnimator.setInterpolator(mDecelerateInterpolator);
        this.mUnlockAnimator.setDuration(250L);
        this.mUnlockAnimator.addListener(this.animatorListen);
        this.mUnlockAnimator.start();
    }

    private class DragLayoutViewWrap {
        private DragLayoutViewWrap() {}

        public void setPosition(int paramInt) {
            KeyguardPhilipsView.this.moveDragLayoutViewTo(paramInt);
        }
    }

    private void moveDragLayoutViewTo(int paramInt) {
        this.mDragLayoutContainer.layout(this.mDragLayoutContainer.getLeft(),
                paramInt, this.mDragLayoutContainer.getRight(),
                this.mDragLayoutContainer.getBottom());
        setBackGroundViewPosition(paramInt);
        setBackGroundAlpha(paramInt);
    }
    private void setBackGroundViewPosition(int paramInt) {
        int i = (int) getContext().getResources().getDimension(
                R.dimen.kg_background_animation_view_corner_height);
        if ((this.mDragLayoutViewDefaultPosition - i <= paramInt)
                && (paramInt <= this.mDragLayoutViewDefaultPosition))
            this.mBackgroundView.setPosition(paramInt);
        do
            return;
        while (paramInt <= this.mDragLayoutViewDefaultPosition);
        this.mBackgroundView.setPosition(this.mDragLayoutViewDefaultPosition);
    }
    private void setBackGroundAlpha(int paramInt) {
        int[] arrayOfInt = new int[2];
        this.mDragLayoutView.getLocationOnScreen(arrayOfInt);
        this.mDragLayoutTopOnScreen = arrayOfInt[1];
        int i = paramInt + this.mDragLayoutTopOnScreen;
        int j = this.mDragLayoutViewDefaultPosition
                + this.mDragLayoutTopOnScreen;
        if ((i >= 0) && (i <= j)) {
            float f = 1.0F * ((j - i) / j);
            if (this.mScrim != null)
                this.mScrim.setAlpha(f);
        }
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("KeyguardPhilipsView", "onInterceptTouchEvent x="
                + ev.getX() + " y=" + ev.getY());
        if (this.mVelocityTracker == null)
            this.mVelocityTracker = VelocityTracker.obtain();
        this.mVelocityTracker.addMovement(ev);

        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                if (isInDragLayoutView(x,y)){
                    this.mGestureStartX = x;
                    this.mGestureStartY = y;
                    this.mGestureStartDragLayoutTop = getDragLayoutPosition();
                    this.mDragging = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("KeyguardPhilipsView", "onInterceptTouchEvent ACTION_MOVE");
                if (this.mDragging){
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mDragging){
                    return true;
                }
                break;
        }


        return super.onInterceptTouchEvent(ev);
    }

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
                if (isInDragLayoutView(x, y)){
                    this.mGestureStartX = x;
                    this.mGestureStartY = y;
                    this.mGestureStartDragLayoutTop = getDragLayoutPosition();
                    this.mDragging = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("KeyguardPhilipsView", "onTouchEvent ACTION_UP");
                if (this.mDragging){

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (this.mDragging) {
                    Log.d("KeyguardPhilipsView", "Velocity=" + velocityY);
                    handleDragLayoutViewMoveComplete(x, y, velocityY);
                }
                resetTouch();
                break;
        }

        return super.onTouchEvent(event);
    }


    private int mTriggerAction = 0;

    private void handleDragLayoutViewMoveComplete(int x, int y, int velocityY) {
        int i;
        if ((isMoveUp(y, velocityY))
                && (isMoveUpEnough(y, velocityY))) {
            i = this.mDragLayoutViewDoAnimationMaxToPPosition;
            this.mTriggerAction = 2;
            Log.d("KeyguardPhilipsView", "set TRIGGER_TARGET_CAMERA");
        }
        if ((isMoveDown(y, velocityY))
                && (isMoveDownEnough(y, velocityY))) {
            i = this.mDragLayoutViewDoAnimationMaxBottomPosition;
            this.mTriggerAction = 1;
            Log.d("KeyguardPhilipsView", "set TRIGGER_TARGET_UNLOCK");
        }
        startAnimation(i);
        i = this.mDragLayoutViewDefaultPosition;
        this.mTriggerAction = 0;
        Log.d("KeyguardPhilipsView", "set TRIGGER_TARGET_NONE");
    }

    private boolean isMoveDown(int paramInt1, int paramInt2) {
        return (paramInt2 >= 0)
                && (getDragLayoutPosition() > this.mDragLayoutViewDefaultPosition);
    }

    private boolean isMoveDownEnough(int paramInt1, int paramInt2) {
        if (Math.abs(paramInt2) > this.mMinVelocity){
            return true;
        }
        if (getDragLayoutPosition() - this.mDragLayoutViewDefaultPosition <= this.mDragLayoutMoveFastDownDistances) {
            return true;
        }
        if (getDragLayoutPosition() - this.mDragLayoutViewDefaultPosition > this.mDragLayoutMoveSlowDownDistances){
            return true;
        }
        return false;
    }

    private boolean isMoveUp(int y, int velocityY) {
        return (velocityY < 0)
                && (getDragLayoutPosition() < this.mDragLayoutViewDefaultPosition);
    }

    private boolean isMoveUpEnough(int y, int velocityY) {
        this.mDragLayoutMoveFastDownDistances = 100;
        this.mDragLayoutMoveSlowDownDistances = 100;

        if (Math.abs(velocityY) > this.mMinVelocity) {
            return true;
        }
        if (this.mDragLayoutViewDefaultPosition - getDragLayoutPosition() <= this.mDragLayoutMoveFastUpDistances) {
            return true;
        }

        if (this.mDragLayoutViewDefaultPosition - getDragLayoutPosition() > this.mDragLayoutMoveSlowUpDistances) {
            return true;
        }

        return false;
    }

    private void resetTouch() {
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        this.mDragging = false;
    }

    private int getDragLayoutPosition() {
        return this.mDragLayoutContainer.getTop();
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