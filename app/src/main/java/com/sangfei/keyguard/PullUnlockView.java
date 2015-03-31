package com.sangfei.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressLint({"NewApi"})
public class PullUnlockView extends ViewGroup {
    static final String TAG = "DragMovingLayout";
    static final Interpolator mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
    static final Interpolator mDecelerateInterpolator = new DecelerateInterpolator();
    private final int ANIMATION_DURATION = 300;

    @SuppressLint({"NewApi"})
    private AnimatorListenerAdapter animatorListen = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator paramAnimator) {
            Log.d("DragMovingLayout", "onAnimationEnd");
        }

        public void onAnimationStart(Animator paramAnimator) {
            Log.d("DragMovingLayout", "onAnimationStart");
        }
    };
    private ObjectAnimator mAnimator;
    //触摸弹动的距离
    private int mClickSpringDistance;
    private int mDragDistance;
    private View mDragView;
    private boolean mDragging;
    private int mGestureStartDragViewTop;
    private int mGestureStartX;
    private int mGestureStartY;
    //最大下滑距离
    private int mMaxScoll;
    private int mMaxVelocity;
    private int mMinVelocity;
    private OnTriggerListener mOnTriggerListener;

    public PullUnlockView(Context paramContext) {
        this(paramContext, null);
    }

    public PullUnlockView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramContext);
        this.mMinVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaxVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
        this.mMaxScoll = 0;
        this.mDragDistance = 600;
    }

    @TargetApi(11)
    private void doAnimation(View paramView, int paramInt1, int paramInt2) {
        new Interpolator() {
            public float getInterpolation(float paramFloat) {
                float f = paramFloat - 1.0F;
                return 1.0F + f * (f * (f * (f * f)));
            }
        };
        DragLayoutViewWrap localDragLayoutViewWrap = new DragLayoutViewWrap(paramView);
        int[] arrayOfInt = new int[2];
        arrayOfInt[0] = getScrollY();
        arrayOfInt[1] = intScrollY;
        this.mAnimator = ObjectAnimator.ofInt(localDragLayoutViewWrap, "Position", arrayOfInt);
        this.mAnimator.setInterpolator(mDecelerateInterpolator);
        this.mAnimator.setDuration(ANIMATION_DURATION);
        this.mAnimator.addListener(this.animatorListen);
        this.mAnimator.start();
    }

    private void doClickAnimation(View paramView, int paramInt1, int paramInt2) {
        new Interpolator() {
            public float getInterpolation(float paramFloat) {
                float f = paramFloat - 1.0F;
                return 1.0F + f * (f * (f * (f * f)));
            }
        };
        DragLayoutViewWrap localDragLayoutViewWrap = new DragLayoutViewWrap(paramView);
        int[] arrayOfInt = new int[2];
        arrayOfInt[0] = intScrollY;
        arrayOfInt[1] = (intScrollY - this.mClickSpringDistance);
        this.mAnimator = ObjectAnimator.ofInt(localDragLayoutViewWrap, "Position", arrayOfInt);
        this.mAnimator.setInterpolator(mDecelerateInterpolator);
        this.mAnimator.setRepeatCount(1);
        this.mAnimator.setRepeatMode(2);
        this.mAnimator.setDuration(200L);
        this.mAnimator.start();
    }

    //达到足够的距离后  回调, 启动相应的app
    private void handleDragViewMoveComplete(View paramView, int paramInt1, int paramInt2, int velocityY) {
        if ((isMoveDownEnough(paramView, paramInt1, paramInt2, velocityY)) && (this.mOnTriggerListener != null)) {
            Log.d("DragMovingLayout", "handleDragViewMoveComplete startActivity()");
            this.mOnTriggerListener.onTrgger();
            Toast.makeText(PullUnlockView.this.getContext(), "start", Toast.LENGTH_SHORT).show();
        }
        startAnimation(paramView, paramInt1, this.mGestureStartDragViewTop);
    }

    //判定是否达到启动条件
    private boolean isMoveDownEnough(View paramView, int paramInt1, int paramInt2, int velocityY) {
        return (Math.abs(velocityY) > this.mMinVelocity) || (Math.abs(intScrollY - getScrollY()) <= this.mMaxScoll / 2);
    }

    private void movingDragView(View paramView, int x, int y) {
        scrollTo(getScrollX(), y);
    }

    private void startAnimation(View paramView, int paramInt1, int paramInt2) {
        if (this.mAnimator != null)
            this.mAnimator.cancel();
        doAnimation(paramView, paramInt1, paramInt2);
    }

    private void stopAnimation() {
        if (this.mAnimator != null)
            this.mAnimator.cancel();
    }

    private int intScrollY;

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            Log.d("DragMovingLayout", "onLayout");
            if (getChildCount() == 0)
                return;
            this.mMaxScoll = 0;
            int childCount = getChildCount();
            int firstViewHeight = 0;
            int k = childCount - 1;
            View localView;
            int width;
            int height;

            for (int N = 0; N < childCount; N++) {
                localView = getChildAt(N);
                width = localView.getMeasuredWidth();
                height = localView.getMeasuredHeight();

                if (N == 0) {
                    firstViewHeight = height;
                    intScrollY = firstViewHeight;
                    localView.layout(0, 0, width, height);
                }

                if (N == 1) {
                    localView.layout(0, firstViewHeight, width, firstViewHeight + height);
                    this.mMaxScoll = (int) (0.6D * height);
                    this.mClickSpringDistance = (int) (0.3*height);
                }
            }

            scrollTo(0, firstViewHeight);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (getScrollY() != 0)
            requestDisallowInterceptTouchEvent(true);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("DragMovingLayout", "ACTION_DOWN x=" + x + " y=" + y);
                stopAnimation();
                this.mGestureStartX = x;
                this.mGestureStartY = y;
                this.mGestureStartDragViewTop = getTop();
                return true;

            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mGestureStartY;
                /*if (deltaY > mDragDistance) {
                    deltaY = mDragDistance;
                }*/
                Log.d("DragMovingLayout", "ACTION_MOVE x=" + x + " y=" + y + " deltaY=" + deltaY);

                //滑动距离控制  越划越慢
//             int deltaY = (int)(this.mMaxScoll * (float)Math.sin(1.570796326794897D * (deltaY / this.mDragDistance)));

                if (getScrollY() - deltaY > intScrollY){
                    deltaY = getScrollY() - intScrollY;
                }else if (getScrollY() - deltaY < intScrollY - mMaxScoll){
                    deltaY = getScrollY() + mMaxScoll - intScrollY;
                }

                movingDragView(this.mDragView, 0, getScrollY() - deltaY);
                mGestureStartY = y;
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d("DragMovingLayout", "ACTION_UP x=" + x + " y=" + y);
                if (Math.abs(getScrollY() - intScrollY) > 5) {
                    handleDragViewMoveComplete(this.mDragView, x, y, 0);
                    return true;
                } else {
                    doClickAnimation(null, intScrollY + 0, intScrollY + 0);
                }
                return true;
        }

        return super.onTouchEvent(motionEvent);
    }

    void setHeader(View paramView) {
        LinearLayout localLinearLayout = new LinearLayout(getContext());
        localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        localLinearLayout.setGravity(Gravity.CENTER);
        localLinearLayout.addView(paramView);
        addView(localLinearLayout);
    }

    public void setOnTriggerListener(OnTriggerListener paramOnTriggerListener) {
        this.mOnTriggerListener = paramOnTriggerListener;
    }

    @TargetApi(11)
    private class DragLayoutViewWrap {
        private View mView;

        public DragLayoutViewWrap(View view) {
            this.mView = view;
        }

        public void setPosition(int paramInt) {
            PullUnlockView.this.movingDragView(this.mView, 0, paramInt);
        }
    }

    public static abstract interface OnTriggerListener {
        public abstract void onTrgger();
    }
}

