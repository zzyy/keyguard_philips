package com.sangfei.keyguard.unlock;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class Contact {
    private static final String TAG = "Contact";
    private static final int CONTACT_METHOD_ID_UNKNOWN = -1;
    public static final int CONTACT_METHOD_TYPE_EMAIL = 2;
    public static final int CONTACT_METHOD_TYPE_PHONE = 1;
    public static final int CONTACT_METHOD_TYPE_SELF = 3;
    private static final boolean DEBUG = false;
    private static ContactsCache sContactCache;
    private BitmapDrawable mAvatar;
    protected byte[] mAvatarData;
    private long mContactMethodId;
    private int mContactMethodType;
    private boolean mIsStale;
    private String mName;
    private String mNumber;
    public long mPersonId;

    public Contact(String paramString) {
        init(paramString, "");
    }

    public static Contact get(String number, boolean paramBoolean) {
        return sContactCache.get(number, paramBoolean);
    }

    public static void init(Context context) {
        sContactCache = new ContactsCache(context);
    }

    private void init(String phoneNum, String name) {
        this.mContactMethodId = -1L;
        this.mName = name;
        this.mNumber = phoneNum;
        this.mIsStale = true;
    }

    public Drawable getAvatar(Context paramContext) {
        if ((this.mAvatar == null) && (this.mAvatarData != null)) {
            Bitmap localBitmap = BitmapFactory.decodeByteArray(this.mAvatarData, 0, this.mAvatarData.length);
            this.mAvatar = new BitmapDrawable(paramContext.getResources(), localBitmap);
        }
        return this.mAvatar;
    }

    public String getName() {
        if (TextUtils.isEmpty(this.mName))
            return this.mNumber;
        return this.mName;
    }

    public String toString() {
        return "number:" + this.mNumber + " name:" + this.mName;
    }

    private static class ContactsCache {
        public static Context context;
        private final HashMap<String, Contact> mContactsMap = new HashMap();

        public ContactsCache(Context context) {
            this.context = context;
        }



        public static Contact queryContactByNumber(String phoneNumber){
            Contact contact = new Contact(phoneNumber);

            ContentResolver contentResolver = context.getContentResolver();
            Cursor c = null;
            c = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            int raw_contact_id = 0;
            while (c.moveToNext()){
                raw_contact_id = c.getInt( c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
                String display_name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if (PhoneNumberUtils.compare(number, phoneNumber)){
                    contact.mName = display_name;
                    break;
                }
            }
            c.close();
            Log.d(TAG, "queryContactByNumber: contact="+contact);
            return contact;
        }


        public Contact get(String number, boolean paramBoolean) {
            if (mContactsMap.containsKey(number)){
                return mContactsMap.get(number);
            }else {
                Contact contact = queryContactByNumber(number);
                mContactsMap.put(number, contact);
                return contact;
            }
        }
    }
}
