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
public class PullUnlockView extends ViewGroup
{
  static final String TAG = "PullUnlockView";
  static final Interpolator mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
  static final Interpolator mDecelerateInterpolator = new DecelerateInterpolator();
  private final int ANIMATION_DURATION = 300;

  @SuppressLint({"NewApi"})
  private AnimatorListenerAdapter animatorListen = new AnimatorListenerAdapter()
  {
    public void onAnimationEnd(Animator paramAnimator)
    {
      Log.d("PullUnlockView", "onAnimationEnd");
    }

    public void onAnimationStart(Animator paramAnimator)
    {
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

  public PullUnlockView(Context paramContext)
  {
    this(paramContext, null);
  }

  public PullUnlockView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramContext);
    this.mMinVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaxVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mMaxScoll = 0;
    this.mDragDistance = 0;
  }

  @TargetApi(11)
  private void doAnimation(int paramInt1, int paramInt2)
  {
    this.mAnimator = ObjectAnimator.ofInt(new DragLayoutViewWrap(), "Position", new int[] { paramInt1, paramInt2 });
    this.mAnimator.setInterpolator(mDecelerateInterpolator);
    this.mAnimator.setDuration(300L);
    this.mAnimator.addListener(this.animatorListen);
    this.mAnimator.start();
  }

  private void handleDragViewMoveComplete(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((isMoveDownEnough(paramView, paramInt1, paramInt2, paramInt3)) && (this.mOnTriggerListener != null))
    {
      Log.d("PullUnlockView", "handleDragViewMoveComplete onTrgger");
      this.mOnTriggerListener.onTrgger();
    }
    startAnimation(getScrollY(), this.mGestureStartDragViewTop);
  }

  private boolean isMoveDownEnough(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    return (Math.abs(paramInt3) > this.mMinVelocity) || (Math.abs(getScrollY()) >= this.mMaxScoll / 2);
  }

  private void movingDragView(int paramInt)
  {
    scrollTo(getScrollX(), paramInt);
  }

  private void startAnimation(int paramInt1, int paramInt2)
  {
    if (this.mAnimator != null)
      this.mAnimator.cancel();
    doAnimation(paramInt1, paramInt2);
  }

  private void stopAnimation()
  {
    if (this.mAnimator != null)
      this.mAnimator.cancel();
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    Log.d("PullUnlockView", "onInterceptTouchEvent ev=" + paramMotionEvent);
    Log.d("PullUnlockView", "onInterceptTouchEvent x=" + paramMotionEvent.getX() + " y=" + paramMotionEvent.getY());
    return super.onInterceptTouchEvent(paramMotionEvent);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (getChildCount() == 0)
      return;
    View localView = getChildAt(0);
    int i = localView.getMeasuredWidth();
    int j = localView.getMeasuredHeight();
    localView.layout(0, 0, i, j);
    this.mMaxScoll = j;
    this.mDragDistance = (j * 3);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    measureChildren(paramInt1, paramInt2);
    if (getChildCount() > 1)
      throw new IllegalArgumentException("PullUnlockView only can be add 1 child");
    View localView = getChildAt(0);
    setMeasuredDimension(localView.getMeasuredWidth(), localView.getMeasuredHeight());
  }


    /*
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    int j = (int)paramMotionEvent.getX();
    int k = (int)paramMotionEvent.getY();
    switch (i)
    {
    default:
    case 0:
    case 3:
    case 1:
    case 2:
    }
    while (true)
    {
      return true;
      Log.d("PullUnlockView", "ACTION_DOWN x=" + j + " y=" + k);
      stopAnimation();
      this.mGestureStartX = j;
      this.mGestureStartY = k;
      this.mGestureStartDragViewTop = getTop();
      continue;
      Log.d("PullUnlockView", "ACTION_CANCEL x=" + j + " y=" + k);
      if (getScrollY() == 0)
        continue;
      handleDragViewMoveComplete(this.mDragView, j, k, 0);
      continue;
      Log.d("PullUnlockView", "ACTION_UP x=" + j + " y=" + k);
      if (Math.abs(getScrollY()) <= 5)
        continue;
      handleDragViewMoveComplete(this.mDragView, j, k, 0);
      continue;
      float f = k - this.mGestureStartY;
      if (f < 0.0F)
        continue;
      if (f >= this.mDragDistance)
        f = this.mDragDistance;
      movingDragView(-(int)(this.mMaxScoll * (float)Math.sin(1.570796326794897D * (f / this.mDragDistance))));
      requestLayout();
    }
  }

*/


  public void setOnTriggerListener(OnTriggerListener paramOnTriggerListener)
  {
    this.mOnTriggerListener = paramOnTriggerListener;
  }

  @TargetApi(11)
  private class DragLayoutViewWrap
  {
    private DragLayoutViewWrap()
    {
    }

    public void setPosition(int paramInt)
    {
      PullUnlockView.this.movingDragView(paramInt);
    }
  }

  public static abstract interface OnTriggerListener
  {
    public abstract void onTrgger();
  }
}

/* Location:           D:\土豆ROM工具箱\classes_dex2jar.jar
 * Qualified Name:     com.sangfei.keyguard.unlock.PullUnlockView
 * JD-Core Version:    0.6.0
 */