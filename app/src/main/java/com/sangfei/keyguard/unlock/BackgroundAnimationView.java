package com.sangfei.keyguard.unlock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sangfei.keyguard.R;

public class BackgroundAnimationView extends View
{
  static final String TAG = "BackgroundAnimationView";
  private int mControlX;
  private int mControlY;
  private Paint mPaint = new Paint();
  private Path mPath;
  int mScreenHeight;
  int mScreenWidth;
  private int mTargetX;
  private int mViewBottom;
  private int mViewBottomHeight;
  private int mViewBottomHeightDefault;
  private int mViewCornerHeight;
  private int mViewHeight;
  private int mViewLeft;
  private int mViewRight;
  private int mViewTop;

  public BackgroundAnimationView(Context paramContext)
  {
    this(paramContext, null);
  }

  public BackgroundAnimationView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mPaint.setColor(-1);
    this.mPaint.setStyle(Style.FILL);
    this.mPaint.setAntiAlias(true);
    this.mPath = new Path();
    this.mViewBottomHeightDefault = (int)getContext().getResources().getDimension(R.dimen.kg_unlock_drag_layout_height);
    this.mViewBottomHeight = this.mViewBottomHeightDefault;
    this.mViewCornerHeight = (int)getContext().getResources().getDimension(R.dimen.kg_background_animation_view_corner_height);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    this.mPath.reset();
    this.mPath.moveTo(this.mViewLeft, this.mViewBottom);
    this.mPath.lineTo(this.mViewRight, this.mViewBottom);
    this.mPath.lineTo(this.mViewRight, this.mViewTop);
    this.mPath.quadTo(this.mControlX, this.mControlY, this.mTargetX, this.mControlY);
    this.mPath.lineTo(this.mViewLeft, this.mControlY);
    this.mPath.close();
    paramCanvas.drawPath(this.mPath, this.mPaint);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    this.mScreenWidth = MeasureSpec.getSize(paramInt1);
    this.mScreenHeight = MeasureSpec.getSize(paramInt2);
    this.mViewLeft = 0;
    this.mViewRight = this.mScreenWidth;
    this.mViewHeight = (this.mViewBottomHeight + this.mViewCornerHeight);
    this.mViewTop = (this.mScreenHeight - this.mViewHeight);
    this.mViewBottom = this.mScreenHeight;
    this.mControlX = this.mScreenWidth;
    this.mControlY = (this.mScreenHeight - this.mViewBottomHeight);
    this.mTargetX = (int)((this.mControlY - this.mViewTop) / 0.42D);
  }

  public void setBottomHeight(int paramInt)
  {
    this.mViewBottomHeight = paramInt;
    Log.d("BackgroundAnimationView", "setBottomHeight height=" + paramInt);
    requestLayout();
  }

  public void setPosition(int paramInt)
  {
    this.mControlY = paramInt;
    this.mTargetX = (int)((this.mControlY - this.mViewTop) / 0.42D);
    invalidate();
  }
}
