package com.example.guoxiaolin.testview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class DragView extends LinearLayout {
    /**
     * ???????
     */
    private int mScreenHeight;
    /**
     * ??????????getScrollY
     */
    private int mScrollStart;
    /**
     * ?????????getScrollY
     */
    private int mScrollEnd;
    /**
     * ?????????Y
     */
    private int mLastY;
    /**
     * ???????????
     */
    private Scroller mScroller;
    /**
     * ??????????
     */
    private boolean isScrolling;
    /**
     * ???????
     */
    private VelocityTracker mVelocityTracker;
    /**
     * ???????
     */
    private int currentPage = 0;

    private OnPageChangeListener mOnPageChangeListener;

    public DragView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**
         * ??????????
         */
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels / 4;

        // ?????
        mScroller = new Scroller(context);
        mScroller.startScroll(0, 0, 0, mScreenHeight);// ?????????
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub

        super.onDraw(canvas);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // ???????????????????????onTouchEvent
        if (isScrolling)
            return super.onTouchEvent(event);

        int action = event.getAction();
        int y = (int) event.getY();

        obtainVelocity(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mScrollStart = getScrollY();
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                int dy = mLastY - y;
                // ???????
                int scrollY = getScrollY();
                // ????????????????????????????????
                if (dy < 0 && scrollY + dy < 0) {
                    dy = -scrollY;
                }
                // ?????????????????????????????????
                if (dy > 0 && scrollY + dy > getHeight() - mScreenHeight) {
                    dy = getHeight() - mScreenHeight - scrollY;
                }

                scrollBy(0, dy);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:

                mScrollEnd = getScrollY();

                int dScrollY = mScrollEnd - mScrollStart;

                if (wantScrollToNext())// ???????
                {
                    if (shouldScrollToNext()) {
                        mScroller.startScroll(0, getScrollY(), 0, mScreenHeight
                                - dScrollY);

                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }

                }

                if (wantScrollToPre())// ???????
                {
                    if (shouldScrollToPre()) {
                        // ??????????????????app
                        if (mOnPageChangeListener != null) {

                            mOnPageChangeListener.onPageChange();
                        }
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                        // mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight
                        // - dScrollY);

                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }
                }
                isScrolling = true;
                postInvalidate();
                recycleVelocity();
                break;
        }

        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        // TODO Auto-generated method stub
        if (changed) {
            int childCount = getChildCount();
            // ?????????????
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            lp.height = mScreenHeight * childCount;
            setLayoutParams(lp);

            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != View.GONE) {
                    child.layout(left, i * mScreenHeight, right, (i + 1)
                            * mScreenHeight);// ?????????????layout
                }
            }

        }
//        int cCount = getChildCount();
//        int cWidth = 0;
//        int cHeight = 0;
//        int cWidth1 = 0;
//        int cHeight1 = 0;
//        MarginLayoutParams cParams = null;
//        /**
//         * 遍历所有childView根据其宽和高，以及margin进行布局
//         */
//        for (int i = 0; i < cCount; i++)
//        {
//            View childView = getChildAt(i);
//            cWidth = childView.getMeasuredWidth();
//            cHeight = childView.getMeasuredHeight();
//            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
//			lp.height = mScreenHeight + cHeight1;
//            setLayoutParams(lp);
//            int cl = 0, ct = 0, cr = 0, cb = 0;
//
//            switch (i)
//            {
//                case 0:
//                    cWidth1=cWidth;
//                    cHeight1=cHeight;
//                    cl =0;
//                    ct = 0;
//                    break;
//                case 1:
//                    cl =0;
//
//                    ct =cHeight1;
//                    break;
//
//
//            }
//            cr = cl + cWidth;
//            cb = cHeight + ct;
//            childView.layout(cl, ct, cr, cb);
//        }

    }

    void setScreenHeight(int height) {
        mScreenHeight = height;

    }

    void setHeader(int background) {

//		RelativeLayout localRelativeLayout = (RelativeLayout) findViewById(R.id.headlayout);
        ImageView mbutton1 = new ImageView(getContext());
//        RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(MarginLayoutParams.WRAP_CONTENT,MarginLayoutParams.WRAP_CONTENT);        mbutton1.setBackgroundResource(background);
//        localRelativeLayout.addView(mbutton1,localLayoutParams);
//       // localRelativeLayout.setBackgroundResource(background);
addView(mbutton1);
    }

    void setHeader(View paramView) {
//        RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
//        RelativeLayout localRelativeLayout = (RelativeLayout) findViewById(R.id.headlayout);
//        localRelativeLayout.addView(paramView,localLayoutParams);
        addView(paramView);
    }


    void setContent(View paramView) {
        //	RelativeLayout localRelativeLayout = (RelativeLayout) findViewById(R.id.contentlayout);

        // btn1 位于父 View 的顶部，在父 View 中水平居中

        //localRelativeLayout.setBackgroundColor(Color.RED);
        //    localRelativeLayout.addView(paramView );
        addView(paramView);
    }

    /**
     * ????????????ж?????????????????
     *
     * @return
     */
    private boolean shouldScrollToNext() {
        return mScrollEnd - mScrollStart > mScreenHeight / 2
                || Math.abs(getVelocity()) > 600;
    }

    /**
     * ??????????????ж???????????????????????
     *
     * @return
     */
    private boolean wantScrollToNext() {
        return mScrollEnd > mScrollStart;
    }

    /**
     * ????????????ж?????????????????
     *
     * @return
     */
    private boolean shouldScrollToPre() {

        return -mScrollEnd + mScrollStart > mScreenHeight / 2
                || Math.abs(getVelocity()) > 600;
    }

    /**
     * ??????????????ж???????????????????????
     *
     * @return
     */
    private boolean wantScrollToPre() {
        return mScrollEnd < mScrollStart;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        } else {

            int position = getScrollY() / mScreenHeight;

            Log.e("xxx", position + "," + currentPage);
            if (position != currentPage) {
            /*	if (mOnPageChangeListener != null) {
					currentPage = position;
					mOnPageChangeListener.onPageChange(currentPage);
				}*/
            }

            isScrolling = false;
        }

    }

    /**
     * ???y?????????
     *
     * @return
     */
    private int getVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        return (int) mVelocityTracker.getYVelocity();
    }

    /**
     * ??????
     */
    private void recycleVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * ??????????????
     *
     * @param event
     */
    private void obtainVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * ?????????
     *
     * @param onPageChangeListener
     */
    public void setOnPageChangeListener(
            OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    /**
     * ??????
     */
    public interface OnPageChangeListener {
        void onPageChange();
    }
}
