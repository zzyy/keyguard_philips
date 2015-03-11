package com.sangfei.keyguard.unlock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

import com.sangfei.keyguard.R;

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
    private View mScrim;
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
    boolean mHasLoad = false;
    private ImageView mLockIconView;
    private int mMaxVelocity;
    private int mMinVelocity;

    private int mWidgetPagerBottom;


    public KeyguardPhilipsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyguardPhilipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
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
            KeyguardPhilipsView.this.doTriggerAction(KeyguardPhilipsView.this.mTriggerAction);
            mTriggerAction = 0;
            KeyguardPhilipsView.this.resetView();
        }

    };

    private void resetView() {
        //恢复 重新布局
        mHasLoad = false;
        requestLayout();

        showUnlockTip();
        if (this.mScrim != null)
            this.mScrim.setAlpha(0.0F);

        mBackgroundView.setBottomHeight(mDragLayoutView.getHeight() + mUnReadEventLayoutView.getHeight());
    }

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
        private DragLayoutViewWrap() {
        }

        public void setPosition(int paramInt) {
            KeyguardPhilipsView.this.moveDragLayoutViewTo(paramInt);
        }
    }

    private void moveDragLayoutViewTo(int y) {
        this.mDragLayoutContainer.layout(this.mDragLayoutContainer.getLeft(), y, this.mDragLayoutContainer.getRight(), this.mDragLayoutContainer.getBottom());
        setBackGroundViewPosition(y);
        setBackGroundAlpha(y);
    }

    private void setBackGroundViewPosition(int top) {
        int kg_background_animation_view_corner_height = (int) getContext().getResources().getDimension(R.dimen.kg_background_animation_view_corner_height);
        if (this.mDragLayoutViewDefaultPosition - kg_background_animation_view_corner_height < top
                && top <= this.mDragLayoutViewDefaultPosition) {
            this.mBackgroundView.setPosition(top);
        }
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
        this.mUnlockTipTextView = ((ShineTextView) findViewById(R.id.unlock_tip));
        this.mOpenCameraTipTextView = ((ShineTextView) findViewById(R.id.open_camera_tip));
        this.mArrowView = ((ImageView) findViewById(R.id.arrow));
        this.mLockIconView = ((ImageView) findViewById(R.id.lock_icon));
        this.mVibrator = ((Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE));
        this.mBackgroundView = ((BackgroundAnimationView) findViewById(R.id.background_view));
        this.mDragLayoutContainer = findViewById(R.id.drag_layout_container);

        this.mPullUnlockView = ((PullUnlockView)findViewById(R.id.pull_unlock_view));
        this.mPullUnlockView.setOnTriggerListener(this.mOnPullUnlockTriggerListener);
        this.mUnReadEventLayoutView = ((NewEventShowView) findViewById(R.id.new_event_show_layout));
        this.mUnReadEventLayoutView.setOnNewEvnetListener(this.mOnNewEvnetListener);
    }

    //有新消息后更新数据
    NewEventShowView.OnNewEventListener mOnNewEvnetListener = new NewEventShowView.OnNewEventListener()
    {
        public void notifyNewEvent()
        {
//            if (KeyguardPhilipsView.this.isEnableKeyguardNewEvent())
//            {
//                if (KeyguardPhilipsView.this.getLastWidgetPage() == 0)
                    KeyguardPhilipsView.this.mUnReadEventLayoutView.setVisibility(View.VISIBLE);
//            }
//            else{
//                KeyguardPhilipsView.this.mUnReadEventLayoutView.setVisibility(View.GONE);
//            }
        }
    };

    //消息下拉回调
    PullUnlockView.OnTriggerListener mOnPullUnlockTriggerListener = new PullUnlockView.OnTriggerListener() {
        public void onTrgger() {
            NewEvent localNewEvent = KeyguardPhilipsView.this.mUnReadEventLayoutView.getEvent();
            if (localNewEvent.type == 1)
            {
                KeyguardPhilipsView.this.makeACall(localNewEvent.getNumber());
                return;
            }
            KeyguardPhilipsView.this.sendMessageTo(localNewEvent.getNumber());
        }
    };
    //电话
    private void makeACall(String paramString) {
        Intent localIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + paramString));
        //todo
//        this.mActivityLauncher.launchActivity(localIntent, false, true, null, null);
        doUnlockAction();
    }
    //短信
    private void sendMessageTo(String paramString) {
        Intent localIntent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + paramString));
        //todo
//        this.mActivityLauncher.launchActivity(localIntent, false, true, null, null);
        doUnlockAction();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("KeyguardPhilipsView", "onLayout");
        if (!this.mHasLoad) {
            int dragLayoutHeight = mDragLayoutView.getHeight();
            int unReadEventLayoutViewHeight = this.mUnReadEventLayoutView.getMeasuredHeight();
            Log.d("KeyguardPhilipsView", "onLayout layoutHeight=" + getHeight() + " dragLayoutHeight=" + dragLayoutHeight + " unReadEventLayoutViewHeight=" + unReadEventLayoutViewHeight);

            int dragLayoutContainerHeight = dragLayoutHeight + unReadEventLayoutViewHeight;
            if (mDragLayoutContainerHeight != dragLayoutContainerHeight) {
                this.mDragLayoutContainerHeight = dragLayoutContainerHeight;
                this.mDragLayoutViewDefaultPosition = (getHeight() - this.mDragLayoutContainerHeight);
                //设置topMargin
                ((FrameLayout.LayoutParams) mDragLayoutContainer.getLayoutParams()).topMargin = mDragLayoutViewDefaultPosition;
                mBackgroundView.setBottomHeight(mDragLayoutContainerHeight);

//                saveDragLayoutContainerHeight(this.mDragLayoutContainerHeight);
            }

//            mHasLoad = true;
        }

        int[] arrayOfInt = new int[2];
        getLocationOnScreen(arrayOfInt);

        //动画运动的最终位置  上边界和下边界
        this.mDragLayoutViewDoAnimationMaxToPPosition = (-(arrayOfInt[1] + this.mDragLayoutView.getHeight() - this.mWidgetPagerBottom));
        this.mDragLayoutViewDoAnimationMaxBottomPosition = getBottom();
        Log.d("KeyguardPhilipsView", "mDragLayoutViewDoAnimationMaxToPPosition=" + this.mDragLayoutViewDoAnimationMaxToPPosition + " mDragLayoutViewDoAnimationMaxBottomPosition=" + this.mDragLayoutViewDoAnimationMaxBottomPosition + " mDragLayoutViewDefaultPosition=" + this.mDragLayoutViewDefaultPosition);
    }

    private VelocityTracker mVelocityTracker;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("KeyguardPhilipsView", "onInterceptTouchEvent x="
                + ev.getX() + " y=" + ev.getY() + " getAction=" + ev.getAction());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("KeyguardPhilipsView", "onInterceptTouchEvent ACTION_DOWN");
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                if (isInDragLayoutView(x, y)) {
                    this.mDragging = true;
                    Log.d("KeyguardPhilipsView", "onInterceptTouchEvent ACTION_DOWN ====== mDragging=" + mDragging);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.mDragging) {
                    Log.d("KeyguardPhilipsView", "onInterceptTouchEvent ACTION_MOVE");
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mDragging) {
                    Log.d("KeyguardPhilipsView", "onInterceptTouchEvent ACTION_CANCEL");
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int velocityY = -1;
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        velocityY = (int) mVelocityTracker.getYVelocity();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInDragLayoutView(x, y)) {
                    Log.d("KeyguardPhilipsView", "onTouchEvent ACTION_DOWN");
                    this.mGestureStartX = x;
                    this.mGestureStartY = y;
                    this.mGestureStartDragLayoutTop = getDragLayoutPosition();
                    this.mDragging = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("KeyguardPhilipsView", "onTouchEvent ACTION_MOVE  mGestureStartDragLayoutTop=" + mGestureStartDragLayoutTop);
                if (this.mDragging) {
                    moveDragLayoutViewTo(y - this.mGestureStartY + this.mDragLayoutContainer.getTop());
                    showUnlockTip();

                    this.mGestureStartX = x;
                    this.mGestureStartY = y;

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (this.mDragging) {
                    Log.d("KeyguardPhilipsView", "Velocity=" + velocityY);
                    resetTouch();
                    handleDragLayoutViewMoveComplete(x, y, velocityY);
                    return true;
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private void showUnlockTip() {
        if (this.mDragLayoutContainer.getTop() +10 >= this.mDragLayoutViewDefaultPosition) {
            this.mOpenCameraTipTextView.setVisibility(View.GONE);
            this.mUnlockTipTextView.setVisibility(View.VISIBLE);
            this.mArrowView.setImageResource(R.drawable.sf_unlock_down);
            this.mLockIconView.setVisibility(View.VISIBLE);
        } else {
            this.mOpenCameraTipTextView.setVisibility(View.VISIBLE);
            this.mUnlockTipTextView.setVisibility(View.GONE);
            this.mArrowView.setImageResource(R.drawable.sf_unlock_up);
            this.mLockIconView.setVisibility(View.INVISIBLE);
        }
    }

    private int mTriggerAction = 0;

    private void handleDragLayoutViewMoveComplete(int x, int y, int velocityY) {
        int targetAnimationPosition = 0;
        if (velocityY < -mMinVelocity || mDragLayoutContainer.getTop() < this.mDragLayoutViewDefaultPosition / 2) {
            targetAnimationPosition = this.mDragLayoutViewDoAnimationMaxToPPosition;
            this.mTriggerAction = 2;
            Log.d("KeyguardPhilipsView", "set TRIGGER_TARGET_CAMERA");
        } else if (velocityY > mMinVelocity) {
            targetAnimationPosition = this.mDragLayoutViewDoAnimationMaxBottomPosition;
            this.mTriggerAction = 1;
            Log.d("KeyguardPhilipsView", "set TRIGGER_TARGET_UNLOCK");
        } else {
            targetAnimationPosition = this.mDragLayoutViewDefaultPosition;
        }
        startAnimation(targetAnimationPosition);
        Log.d("KeyguardPhilipsView", "set TRIGGER_TARGET_NONE");
    }

    private void resetTouch() {
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        this.mDragging = false;
    }

    private int getDragLayoutPosition() {
        return this.mDragLayoutContainer.getTop();
    }

    private boolean isInDragLayoutView(int x, int y) {
        if (this.mDragLayoutView == null)
            return false;
        int left = this.mDragLayoutView.getLeft() + this.mDragLayoutContainer.getLeft();
        int top = this.mDragLayoutView.getTop() + this.mDragLayoutContainer.getTop();
        int right = left + this.mDragLayoutView.getWidth();
        int bottom = top + this.mDragLayoutView.getHeight();

        if (x >= left && x <= right && y >= top && y<= bottom){
            return true;
        }

        return false;
    }
}