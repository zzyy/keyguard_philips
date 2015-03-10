package com.sangfei.keyguard.unlock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

@SuppressLint({"NewApi"})
public class PullUnlockView extends ViewGroup {
    static final String TAG = "PullUnlockView";
    static final Interpolator mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
    static final Interpolator mDecelerateInterpolator = new DecelerateInterpolator();
    private final int ANIMATION_DURATION = 300;

    @SuppressLint({"NewApi"})
    private AnimatorListenerAdapter animatorListen = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator paramAnimator) {
            Log.d("PullUnlockView", "onAnimationEnd");
        }

        public void onAnimationStart(Animator paramAnimator) {
            Log.d("PullUnlockView", "onAnimationStart");
        }
    };
    private ObjectAnimator mAnimator;
    private int mDragDistance;
    private View mDragView;
    private int mGestureStartDragViewTop;
    private int mGestureStartX;
    private int mGestureStartY;
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
        this.mDragDistance = 0;
    }

    @TargetApi(11)
    private void doAnimation(int paramInt1, int paramInt2) {
        this.mAnimator = ObjectAnimator.ofInt(new DragLayoutViewWrap(), "Position", new int[]{paramInt1, paramInt2});
        this.mAnimator.setInterpolator(mDecelerateInterpolator);
        this.mAnimator.setDuration(300L);
        this.mAnimator.addListener(this.animatorListen);
        this.mAnimator.start();
    }

    private void handleDragViewMoveComplete(View paramView, int x, int y, int velocityY) {
        if ((isMoveDownEnough(paramView, x, y, velocityY)) && (this.mOnTriggerListener != null)) {
            Log.d("PullUnlockView", "handleDragViewMoveComplete onTrgger");
            this.mOnTriggerListener.onTrgger();
        }
        startAnimation(getScrollY(), this.mGestureStartDragViewTop);
    }

    //判断是移动距离 是否足够, 达到要求后 调用回调函数 触发事件
    private boolean isMoveDownEnough(View paramView, int paramInt1, int paramInt2, int paramInt3) {
        return (Math.abs(paramInt3) > this.mMinVelocity) || (Math.abs(getScrollY()) >= this.mMaxScoll / 2);
    }

    private void movingDragView(int paramInt) {
        scrollTo(getScrollX(), paramInt);
    }

    private void startAnimation(int paramInt1, int paramInt2) {
        if (this.mAnimator != null)
            this.mAnimator.cancel();
        doAnimation(paramInt1, paramInt2);
    }

    private void stopAnimation() {
        if (this.mAnimator != null)
            this.mAnimator.cancel();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 1)
            throw new IllegalArgumentException("PullUnlockView only can be add 1 child");
        View localView = getChildAt(0);
        setMeasuredDimension(localView.getMeasuredWidth(), localView.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0)
            return;
        View localView = getChildAt(0);
        int width = localView.getMeasuredWidth();
        int height = localView.getMeasuredHeight();
        localView.layout(0, 0, width, height);
        this.mMaxScoll = height;
        this.mDragDistance = (height * 3);
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        Log.d("PullUnlockView", "onInterceptTouchEvent ev=" + paramMotionEvent);
        Log.d("PullUnlockView", "onInterceptTouchEvent x=" + paramMotionEvent.getX() + " y=" + paramMotionEvent.getY());
        return super.onInterceptTouchEvent(paramMotionEvent);
    }


    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("PullUnlockView", "ACTION_DOWN x=" + x + " y=" + y);
                this.mGestureStartX = x;
                this.mGestureStartY = y;
                this.mGestureStartDragViewTop = getTop();
                stopAnimation();
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d("PullUnlockView", "ACTION_MOVE x=" + x + " y=" + y);
                float deltaY = y - this.mGestureStartY;
                movingDragView(-(int) (this.mMaxScoll * (float) Math.sin(1.570796326794897D * (deltaY / this.mDragDistance))));
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d("PullUnlockView", "ACTION_UP x=" + x + " y=" + y);
                handleDragViewMoveComplete(this.mDragView, x, y, 0);
                return true;
        }
        return super.onTouchEvent(ev);
    }


    public void setOnTriggerListener(OnTriggerListener paramOnTriggerListener) {
        this.mOnTriggerListener = paramOnTriggerListener;
    }

    @TargetApi(11)
    private class DragLayoutViewWrap {
        private DragLayoutViewWrap() {
        }

        public void setPosition(int paramInt) {
            PullUnlockView.this.movingDragView(paramInt);
        }
    }

    public static abstract interface OnTriggerListener {
        public abstract void onTrgger();
    }
}