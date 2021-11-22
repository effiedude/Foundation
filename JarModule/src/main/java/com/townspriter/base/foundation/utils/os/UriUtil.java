package com.townspriter.base.foundation.utils.os;

import java.io.InputStream;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.io.IOUtil;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;

/******************************************************************************
 * @path Foundation:UriUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class UriUtil
{
    /**
     * 通过Uri获取图片真正路径
     * 例如
     * content://media/external/images/media/70020
     *
     * @param contentUri
     * @return
     */
    public static String getImageRealPathFromUri(Uri contentUri)
    {
        Cursor cursor=null;
        try
        {
            String[] proj={Media.DATA};
            cursor=Foundation.getApplication().getContentResolver().query(contentUri,proj,null,null,null);
            if(cursor!=null)
            {
                int column_index=cursor.getColumnIndexOrThrow(Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            IOUtil.safeClose(cursor);
        }
        return "";
    }
    
    /**
     * 通过Uri获取彩信图片
     * 例如
     * content://mms/part/4
     *
     * @param partURI
     * @return
     */
    public static Bitmap getMMSImageBitmap(Uri partURI)
    {
        InputStream in=null;
        Bitmap bitmap=null;
        try
        {
            in=Foundation.getApplication().getContentResolver().openInputStream(partURI);
            bitmap=BitmapFactory.decodeStream(in);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        finally
        {
            IOUtil.safeClose(in);
        }
        return bitmap;
    }
}
