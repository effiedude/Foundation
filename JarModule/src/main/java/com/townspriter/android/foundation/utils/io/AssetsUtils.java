package com.townspriter.android.foundation.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import com.townspriter.android.foundation.Foundation;
import com.townspriter.android.foundation.utils.log.Logger;
import android.content.res.AssetManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path Foundation:AssetsUtils
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class AssetsUtils
{
    private static final String TAG="AssetsUtils";
    private static final boolean DEBUG=false;
    
    public static @Nullable String readFileToString(@NonNull AssetManager assetManager,@NonNull String filePath) throws IOException
    {
        String result;
        /** 获取输入流 */
        InputStream inputStream=assetManager.open(filePath);
        /** 获取文件的字节数 */
        int available=inputStream.available();
        byte[] buffer=new byte[available];
        /** 将文件中的数据写入到字节数组中 */
        inputStream.read(buffer);
        inputStream.close();
        result=new String(buffer);
        return result;
    }
    
    public static String[] readFileToStringList(@NonNull AssetManager assetManager,@NonNull String path)
    {
        ArrayList<String> lines=new ArrayList<>();
        BufferedReader reader=null;
        try
        {
            InputStream is=assetManager.open(path);
            reader=new BufferedReader(new InputStreamReader((is)));
            String line;
            while((line=reader.readLine())!=null)
            {
                lines.add(line);
            }
        }
        catch(IOException ioException)
        {
            if(DEBUG)
            {
                Logger.e(TAG,ioException.getMessage());
            }
        }
        finally
        {
            IOUtil.safeClose(reader);
        }
        return lines.toArray(new String[lines.size()]);
    }
    
    /**
     * 拷贝Asset目录下文件到指定文件
     *
     * @param relativeAssetsFile
     * 相对路径
     * @param destFile
     * 绝对路径
     */
    public static boolean copyAssetFile(String relativeAssetsFile,String destFile)
    {
        boolean ret=true;
        InputStream in=null;
        OutputStream out=null;
        File parent=new File(destFile).getParentFile();
        if(!parent.exists())
        {
            parent.mkdirs();
        }
        try
        {
            AssetManager mAssetMgr=Foundation.getApplication().getAssets();
            in=mAssetMgr.open(relativeAssetsFile);
            out=new FileOutputStream(destFile);
            byte[] buffer=new byte[8096];
            int read;
            while((read=in.read(buffer))!=-1)
            {
                out.write(buffer,0,read);
            }
        }
        catch(Exception exception)
        {
            ret=false;
        }
        finally
        {
            IOUtil.safeClose(in);
            IOUtil.safeClose(out);
        }
        return ret;
    }
}
