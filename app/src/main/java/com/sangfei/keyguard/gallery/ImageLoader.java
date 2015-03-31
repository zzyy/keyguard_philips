package com.sangfei.keyguard.gallery;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class ImageLoader extends AsyncTask<Void, Void, Boolean> {
    private static Bitmap mCachedBitmap;
    private static String mCachedString;
    private String imageFile;
    private Context mContext;
    private ImageView view;

    public ImageLoader(ImageView paramImageView, String paramString, Context paramContext) {
        this.view = paramImageView;
        this.imageFile = paramString;
        this.mContext = paramContext;
    }

    private Bitmap getBitmap(String paramString) {
        Options localOptions = new Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(paramString, localOptions);
        int i = getDisplayMetricsWidth() / 3;
        int j = getDisplayMetricsHeigth() / 3;
        int k = (int) Math.ceil(localOptions.outWidth / i);
        int m = (int) Math.ceil(localOptions.outHeight / j);

        if ((k > 1) || (m > 1)) {
            if (k < m) {
                localOptions.inSampleSize = k;
            } else {
                localOptions.inSampleSize = m;
            }
        }

        localOptions.inJustDecodeBounds = false;
        mCachedBitmap = BitmapFactory.decodeFile(paramString, localOptions);
        return mCachedBitmap;
    }

    private int getDisplayMetricsHeigth() {
        return this.mContext.getResources().getDisplayMetrics().heightPixels;
    }

    private int getDisplayMetricsWidth() {
        return this.mContext.getResources().getDisplayMetrics().widthPixels;
    }

    protected Boolean doInBackground(Void[] paramArrayOfVoid) {
        if (TextUtils.isEmpty(this.imageFile))
            return Boolean.valueOf(false);
        if ((!this.imageFile.equals(mCachedString)) || (mCachedBitmap == null))
            mCachedBitmap = getBitmap(this.imageFile);
        if (mCachedBitmap != null) {
            mCachedString = this.imageFile;
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    protected void onPostExecute(Boolean paramBoolean) {
        if ((paramBoolean.booleanValue()) && (this.view != null))
            this.view.setImageBitmap(mCachedBitmap);
    }
}
