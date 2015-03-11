package com.sangfei.keyguard.unlock;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class NewEvent implements Comparable<NewEvent> {
    public static final int TYPE_CALL = 1;
    public static final int TYPE_MESSAGE_SMS = 2;
    public static final int TYPE_MESSAGE_MMS = 3;
    Contact contact;
    int count;
    long date;
    String ids;
    String message;
    String number;
    int type;

    public NewEvent() {
    }

    //id 对应数据库_id字段
    public NewEvent(int type, String number, long date, String msg, int id) {
        this.type = type;
        this.number = number;
        this.date = date;
        this.message = msg;
        this.ids = (id + "");
        this.count = 1;
    }

    public static NewEvent fromCall(Cursor paramCursor) {
        if (paramCursor == null)
            return null;
        NewEvent localNewEvent = new NewEvent();
        localNewEvent.ids = (paramCursor.getInt(0) + "");
        localNewEvent.date = paramCursor.getLong(2);
        localNewEvent.number = paramCursor.getString(3);
        localNewEvent.type = 1;
        localNewEvent.count = 1;
        return localNewEvent;
    }

    public void addId(String paramString) {
        this.ids = (this.ids + "," + paramString);
    }

    public int compareTo(NewEvent paramNewEvent) {
        return (int) (paramNewEvent.date - this.date);
    }

    //获取头像
    public Drawable getAvatar(Context context) {
//        if (this.contact != null)
//            return this.contact.getAvatar(context);
        return null;
    }

    public String getMessage() {
        return this.message;
    }

    public String getName() {
        if (this.contact != null)
            return this.contact.getName();
        return null;
    }

    public String getNumber() {
        return this.number;
    }

    public String toString() {
        return "[ number:" + this.number + " message:" + this.message + " date:" + this.date + " Contact:" + this.contact + " Name: "+ this.getName() +" ]";
    }

    public void updateContact() {
        if (!TextUtils.isEmpty(this.number))
            this.contact = Contact.get(this.number, false);
    }
}

/* Location:           D:\土豆ROM工具箱\classes_dex2jar.jar
 * Qualified Name:     com.sangfei.keyguard.unlock.NewEvent
 * JD-Core Version:    0.6.0
 */