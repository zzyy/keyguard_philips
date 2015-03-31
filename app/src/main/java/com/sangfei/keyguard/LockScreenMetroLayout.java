package com.sangfei.keyguard;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sangfei.keyguard.gallery.GalleryWidget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LockScreenMetroLayout extends ViewGroup {
    private static final float GOLDEN_SECTION = 0.618F;
    private Rect[] mChildrenRect = new Rect[5];
    private int mHalfSpace = 1;
    private List<LockScreenWidget> mWidgetList = new ArrayList();

    public LockScreenMetroLayout(Context paramContext) {
        this(paramContext, null);
    }

    public LockScreenMetroLayout(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public LockScreenMetroLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        setMotionEventSplittingEnabled(false);
    }

    private void computeChildLayout(int width, int height) {
        int i = Math.round(0.618F * width);
        int j = width - i;
        int k = height - i;
        int m = k / 2;
        this.mChildrenRect[0] = new Rect(0, 0, i - this.mHalfSpace, k - this.mHalfSpace);
        this.mChildrenRect[1] = new Rect(i + this.mHalfSpace, 0, width, m - this.mHalfSpace);
        this.mChildrenRect[2] = new Rect(i + this.mHalfSpace, m + this.mHalfSpace, width, k - this.mHalfSpace);
        this.mChildrenRect[3] = new Rect(0, k + this.mHalfSpace, j - this.mHalfSpace, height);
        this.mChildrenRect[4] = new Rect(j + this.mHalfSpace, k + this.mHalfSpace, width, height);
    }

    private int getChildMeasureSpec(float paramFloat) {
        return MeasureSpec.makeMeasureSpec((int) paramFloat, 1073741824);
    }

    //添加一个块的内容
    public void addWidget(LockScreenWidget paramLockScreenWidget) {
        this.mWidgetList.add(paramLockScreenWidget);
        addView(paramLockScreenWidget.createPullView());
    }

    public Rect getChildRegion(int paramInt) {
        return this.mChildrenRect[paramInt];
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Iterator localIterator = this.mWidgetList.iterator();
        while (localIterator.hasNext())
            ((LockScreenWidget) localIterator.next()).onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Iterator localIterator = this.mWidgetList.iterator();
        while (localIterator.hasNext())
            ((LockScreenWidget) localIterator.next()).onDetachedFromWindow();
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i = getChildCount();
        for (int j = 0; j < i; j++) {
            View localView = getChildAt(j);
            Rect localRect = this.mChildrenRect[j];
            localView.layout(localRect.left, localRect.top, localRect.right, localRect.bottom);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        computeChildLayout(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        int i = getChildCount();
        for (int j = 0; j < i; j++) {
            View localView = getChildAt(j);
            Rect localRect = this.mChildrenRect[j];
            localView.measure(getChildMeasureSpec(localRect.width()), getChildMeasureSpec(localRect.height()));
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                this.addWidget(new GalleryWidget(this.getContext()));
            }
            if (i == 1) {
                this.addWidget(new GalleryWidget(this.getContext()));
            }
            if (i == 2) {
                this.addWidget(new GalleryWidget(this.getContext()));
            }
            if (i == 3) {
                this.addWidget(new GalleryWidget(this.getContext()));
                continue;
            }
            if (i == 4) {
                this.addWidget(new GalleryWidget(this.getContext()));
            }
        }
    }
}
