package com.sangfei.keyguard.unlock;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract.Data;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.util.HashMap;

public class Contact
{
    /*
  private static final int CONTACT_METHOD_ID_UNKNOWN = -1;
  public static final int CONTACT_METHOD_TYPE_EMAIL = 2;
  public static final int CONTACT_METHOD_TYPE_PHONE = 1;
  public static final int CONTACT_METHOD_TYPE_SELF = 3;
  private static final boolean DEBUG = false;
  private static final String TAG = "Contact";
  private static ContactsCache sContactCache;
  private BitmapDrawable mAvatar;
  protected byte[] mAvatarData;
  private long mContactMethodId;
  private int mContactMethodType;
  private boolean mIsStale;
  private String mName;
  private String mNumber;
  public long mPersonId;

  public Contact(String paramString)
  {
    init(paramString, "");
  }

  public static Contact get(String paramString, boolean paramBoolean)
  {
    return sContactCache.get(paramString, paramBoolean);
  }

  public static void init(Context paramContext)
  {
    sContactCache = new ContactsCache(paramContext, null);
  }

  private void init(String paramString1, String paramString2)
  {
    this.mContactMethodId = -1L;
    this.mName = paramString2;
    this.mNumber = paramString1;
    this.mIsStale = true;
  }

  public Drawable getAvatar(Context paramContext)
  {
    if ((this.mAvatar == null) && (this.mAvatarData != null))
    {
      Bitmap localBitmap = BitmapFactory.decodeByteArray(this.mAvatarData, 0, this.mAvatarData.length);
      this.mAvatar = new BitmapDrawable(paramContext.getResources(), localBitmap);
    }
    return this.mAvatar;
  }

  public String getName()
  {
    if (TextUtils.isEmpty(this.mName))
      return this.mNumber;
    return this.mName;
  }

  public String toString()
  {
    return "number:" + this.mNumber + " name:" + this.mName;
  }

  private static class ContactsCache
  {
    private static final String[] CALLER_ID_PROJECTION;
    private static final String CALLER_ID_SELECTION = " Data._ID IN  (SELECT DISTINCT lookup.data_id  FROM  (SELECT data_id, normalized_number, length(normalized_number) as len  FROM phone_lookup  WHERE min_match = ?) AS lookup )";
    private static final String CALLER_ID_SELECTION_EXACT_MATCH = " Data._ID IN  (SELECT DISTINCT lookup.data_id  FROM  (SELECT data_id, normalized_number, length(normalized_number) as len  FROM phone_lookup  WHERE normalized_number = ?) AS lookup  WHERE  (lookup.len <= ? AND  substr(?, ? - lookup.len + 1) = lookup.normalized_number))";
    private static final int CONTACT_ID_COLUMN = 4;
    private static final int CONTACT_NAME_COLUMN = 3;
    private static final int CONTACT_PRESENCE_COLUMN = 5;
    private static final int CONTACT_STATUS_COLUMN = 6;
    private static final Uri PHONES_WITH_PRESENCE_URI = ContactsContract.Data.CONTENT_URI;
    private static final int PHONE_ID_COLUMN = 0;
    private static final int PHONE_LABEL_COLUMN = 2;
    private static final int PHONE_NORMALIZED_NUMBER = 7;
    private static final int PHONE_NUMBER_COLUMN = 1;
    private static final int SEND_TO_VOICEMAIL = 8;
    static final int STATIC_KEY_BUFFER_MAXIMUM_LENGTH = 10;
    private final HashMap<String, Contact> mContactsHash = new HashMap();
    private final Context mContext;

    static
    {
      CALLER_ID_PROJECTION = new String[] { "_id", "data1", "data3", "display_name", "contact_id", "contact_presence", "contact_status", "data4", "send_to_voicemail" };
    }

    private ContactsCache(Context paramContext)
    {
      this.mContext = paramContext;
    }

    // ERROR //
    private void fillPhoneTypeContact(Contact paramContact, Cursor paramCursor)
    {
      // Byte code:
      //   0: aload_1
      //   1: monitorenter
      //   2: aload_1
      //   3: iconst_1
      //   4: invokestatic 100	com/sangfei/keyguard/unlock/Contact:access$602	(Lcom/sangfei/keyguard/unlock/Contact;I)I
      //   7: pop
      //   8: aload_1
      //   9: aload_2
      //   10: iconst_0
      //   11: invokeinterface 106 2 0
      //   16: invokestatic 110	com/sangfei/keyguard/unlock/Contact:access$502	(Lcom/sangfei/keyguard/unlock/Contact;J)J
      //   19: pop2
      //   20: aload_1
      //   21: aload_2
      //   22: iconst_3
      //   23: invokeinterface 114 2 0
      //   28: invokestatic 118	com/sangfei/keyguard/unlock/Contact:access$702	(Lcom/sangfei/keyguard/unlock/Contact;Ljava/lang/String;)Ljava/lang/String;
      //   31: pop
      //   32: aload_1
      //   33: aload_2
      //   34: iconst_4
      //   35: invokeinterface 106 2 0
      //   40: putfield 122	com/sangfei/keyguard/unlock/Contact:mPersonId	J
      //   43: aload_1
      //   44: monitorexit
      //   45: aload_0
      //   46: aload_1
      //   47: invokespecial 126	com/sangfei/keyguard/unlock/Contact$ContactsCache:loadAvatarData	(Lcom/sangfei/keyguard/unlock/Contact;)[B
      //   50: astore 8
      //   52: aload_1
      //   53: monitorenter
      //   54: aload_1
      //   55: aload 8
      //   57: putfield 130	com/sangfei/keyguard/unlock/Contact:mAvatarData	[B
      //   60: aload_1
      //   61: monitorexit
      //   62: return
      //   63: astore_3
      //   64: aload_1
      //   65: monitorexit
      //   66: aload_3
      //   67: athrow
      //   68: astore 9
      //   70: aload_1
      //   71: monitorexit
      //   72: aload 9
      //   74: athrow
      //
      // Exception table:
      //   from	to	target	type
      //   2	45	63	finally
      //   64	66	63	finally
      //   54	62	68	finally
      //   70	72	68	finally
    }

    private Contact get(String paramString, boolean paramBoolean)
    {
      new Object();
      if (TextUtils.isEmpty(paramString))
        paramString = "";
      Contact localContact = internalGet(paramString);
      monitorenter;
      if (paramBoolean);
      try
      {
        Contact.access$202(localContact, true);
        if (localContact.mIsStale)
        {
          Contact.access$202(localContact, false);
          updateContact(localContact);
        }
        return localContact;
      }
      finally
      {
        monitorexit;
      }
      throw localObject;
    }

    private Contact getContactInfo(Contact paramContact)
    {
      return getContactInfoForPhoneNumber(paramContact.mNumber);
    }

    private Contact getContactInfoForPhoneNumber(String paramString)
    {
      Contact localContact = new Contact(paramString);
      Contact.access$602(localContact, 1);
      String str1 = normalizeNumber(paramString);
      String str2 = PhoneNumberUtils.toCallerIDMinMatch(str1);
      Cursor localCursor;
      if ((!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str2)))
      {
        String str3 = String.valueOf(str1.length());
        String[] arrayOfString = { str1, str3, str1, str3 };
        localCursor = this.mContext.getContentResolver().query(PHONES_WITH_PRESENCE_URI, CALLER_ID_PROJECTION, " Data._ID IN  (SELECT DISTINCT lookup.data_id  FROM  (SELECT data_id, normalized_number, length(normalized_number) as len  FROM phone_lookup  WHERE normalized_number = ?) AS lookup  WHERE  (lookup.len <= ? AND  substr(?, ? - lookup.len + 1) = lookup.normalized_number))", arrayOfString, null);
        if ((localCursor == null) || ((localCursor != null) && (localCursor.getCount() == 0)))
        {
          if (localCursor != null)
            localCursor.close();
          localCursor = this.mContext.getContentResolver().query(PHONES_WITH_PRESENCE_URI, CALLER_ID_PROJECTION, " Data._ID IN  (SELECT DISTINCT lookup.data_id  FROM  (SELECT data_id, normalized_number, length(normalized_number) as len  FROM phone_lookup  WHERE min_match = ?) AS lookup )", new String[] { str2 }, null);
        }
        if (localCursor == null)
          Log.w("Contact", "queryContactInfoByNumber(" + paramString + ") returned NULL cursor!" + " contact uri used " + PHONES_WITH_PRESENCE_URI);
      }
      else
      {
        return localContact;
      }
      try
      {
        if (localCursor.moveToFirst())
        {
          Log.d("Contact", "getContactInfoForPhoneNumber(): ready to fill contact with query result. number=" + str1);
          fillPhoneTypeContact(localContact, localCursor);
        }
        return localContact;
      }
      finally
      {
        localCursor.close();
      }
      throw localObject;
    }

    private Contact internalGet(String paramString)
    {
      CharBuffer localCharBuffer = CharBuffer.allocate(10);
      synchronized (???.key(paramString.replaceAll(" ", "").replaceAll("-", ""), localCharBuffer))
      {
        String str;
        Object localObject4 = (Contact)???.mContactsHash.get(str);
        Contact localContact;
        if (localObject4 == null)
          localContact = new Contact(paramString);
        try
        {
          ???.mContactsHash.put(str, localContact);
          localObject4 = localContact;
          return localObject4;
          Object localObject1;
          throw localObject1;
        }
        finally
        {
        }
      }
    }

    private String key(String paramString, CharBuffer paramCharBuffer)
    {
      paramCharBuffer.clear();
      paramCharBuffer.mark();
      int i = paramString.length();
      int j = 0;
      do
      {
        i--;
        if (i < 0)
          break;
        paramCharBuffer.put(paramString.charAt(i));
        j++;
      }
      while (j != 10);
      paramCharBuffer.reset();
      if (j > 0)
        paramString = paramCharBuffer.toString();
      return paramString;
    }

    // ERROR //
    private byte[] loadAvatarData(Contact paramContact)
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore_2
      //   2: aload_1
      //   3: invokestatic 290	com/sangfei/keyguard/unlock/Contact:access$400	(Lcom/sangfei/keyguard/unlock/Contact;)Landroid/graphics/drawable/BitmapDrawable;
      //   6: ifnull +5 -> 11
      //   9: aconst_null
      //   10: areturn
      //   11: getstatic 293	android/provider/ContactsContract$Contacts:CONTENT_URI	Landroid/net/Uri;
      //   14: aload_1
      //   15: getfield 122	com/sangfei/keyguard/unlock/Contact:mPersonId	J
      //   18: invokestatic 299	android/content/ContentUris:withAppendedId	(Landroid/net/Uri;J)Landroid/net/Uri;
      //   21: astore_3
      //   22: ldc 202
      //   24: new 204	java/lang/StringBuilder
      //   27: dup
      //   28: invokespecial 205	java/lang/StringBuilder:<init>	()V
      //   31: ldc_w 301
      //   34: invokevirtual 211	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   37: aload_3
      //   38: invokevirtual 218	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   41: invokevirtual 222	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   44: invokestatic 237	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   47: pop
      //   48: aload_0
      //   49: getfield 83	com/sangfei/keyguard/unlock/Contact$ContactsCache:mContext	Landroid/content/Context;
      //   52: invokevirtual 188	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
      //   55: aload_3
      //   56: invokestatic 305	android/provider/ContactsContract$Contacts:openContactPhotoInputStream	(Landroid/content/ContentResolver;Landroid/net/Uri;)Ljava/io/InputStream;
      //   59: astore 5
      //   61: new 204	java/lang/StringBuilder
      //   64: dup
      //   65: invokespecial 205	java/lang/StringBuilder:<init>	()V
      //   68: ldc_w 307
      //   71: invokevirtual 211	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   74: astore 11
      //   76: iconst_0
      //   77: istore 12
      //   79: aload 5
      //   81: ifnonnull +6 -> 87
      //   84: iconst_1
      //   85: istore 12
      //   87: ldc 202
      //   89: aload 11
      //   91: iload 12
      //   93: invokevirtual 310	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
      //   96: invokevirtual 222	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   99: invokestatic 237	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   102: pop
      //   103: aconst_null
      //   104: astore_2
      //   105: aload 5
      //   107: ifnull +21 -> 128
      //   110: aload 5
      //   112: invokevirtual 315	java/io/InputStream:available	()I
      //   115: newarray byte
      //   117: astore_2
      //   118: aload 5
      //   120: aload_2
      //   121: iconst_0
      //   122: aload_2
      //   123: arraylength
      //   124: invokevirtual 319	java/io/InputStream:read	([BII)I
      //   127: pop
      //   128: aload 5
      //   130: ifnull +8 -> 138
      //   133: aload 5
      //   135: invokevirtual 320	java/io/InputStream:close	()V
      //   138: aload_2
      //   139: areturn
      //   140: astore 8
      //   142: ldc 202
      //   144: ldc_w 322
      //   147: invokestatic 228	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   150: pop
      //   151: aload 5
      //   153: ifnull -15 -> 138
      //   156: aload 5
      //   158: invokevirtual 320	java/io/InputStream:close	()V
      //   161: goto -23 -> 138
      //   164: astore 10
      //   166: goto -28 -> 138
      //   169: astore 6
      //   171: aload 5
      //   173: ifnull +8 -> 181
      //   176: aload 5
      //   178: invokevirtual 320	java/io/InputStream:close	()V
      //   181: aload 6
      //   183: athrow
      //   184: astore 15
      //   186: goto -48 -> 138
      //   189: astore 7
      //   191: goto -10 -> 181
      //
      // Exception table:
      //   from	to	target	type
      //   61	76	140	java/io/IOException
      //   87	103	140	java/io/IOException
      //   110	128	140	java/io/IOException
      //   156	161	164	java/io/IOException
      //   61	76	169	finally
      //   87	103	169	finally
      //   110	128	169	finally
      //   142	151	169	finally
      //   133	138	184	java/io/IOException
      //   176	181	189	java/io/IOException
    }

    public static String normalizeNumber(String paramString)
    {
      if (paramString == null)
        return null;
      StringBuilder localStringBuilder = new StringBuilder();
      int i = paramString.length();
      int j = 0;
      if (j < i)
      {
        char c = paramString.charAt(j);
        int k = Character.digit(c, 10);
        if (k != -1)
          localStringBuilder.append(k);
        label82: 
        do
          while (true)
          {
            j++;
            break;
            if ((j != 0) || (c != '+'))
              break label82;
            localStringBuilder.append(c);
          }
        while (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')));
        return normalizeNumber(PhoneNumberUtils.convertKeypadLettersToDigits(paramString));
      }
      return localStringBuilder.toString();
    }

    private void updateContact(Contact paramContact)
    {
      if (paramContact == null)
      {
        Log.d("Contact", "Contact.updateContact(): Contact for query is null.");
        return;
      }
      Contact localContact = getContactInfo(paramContact);
      monitorenter;
      try
      {
        Contact.access$302(paramContact, localContact.mNumber);
        paramContact.mPersonId = localContact.mPersonId;
        paramContact.mAvatarData = localContact.mAvatarData;
        Contact.access$402(paramContact, localContact.mAvatar);
        Contact.access$502(paramContact, localContact.mContactMethodId);
        Contact.access$602(paramContact, localContact.mContactMethodType);
        Contact.access$702(paramContact, localContact.mName);
        return;
      }
      finally
      {
        monitorexit;
      }
      throw localObject;
    }
  }
  */
}

/* Location:           D:\土豆ROM工具箱\classes_dex2jar.jar
 * Qualified Name:     com.sangfei.keyguard.unlock.Contact
 * JD-Core Version:    0.6.0
 */