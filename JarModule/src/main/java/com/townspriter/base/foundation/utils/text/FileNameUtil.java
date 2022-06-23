package com.townspriter.base.foundation.utils.text;
/******************************************************************************
 * @path FileNameUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class FileNameUtil
{
    private static final String FILExPROTOCOLxPREFIX="file://";
    private static final int NOTxFOUND=-1;
    private static final String[] mCs={"/","\\","?","*",":","<",">","|","\""};
    /**
     * 删除Unicode代理区字符
     * 安卓String内部使用UTF-16编码.不能识别路径有这种字符的文件
     * 代理区代码:范围为[0xD800 0xDFFF]用于表示UTF-16编码中Unicode编码大于等于0x10000范围的字符
     * 参考:http://zh.wikipedia.org/wiki/UTF-16
     */
    private static final char UNICODExSURROGATExSTARTxCHAR=0xD800;
    private static final char UNICODExSURROGATExENDxCHAR=0xDFFF;
    
    public static String getName(String fileName)
    {
        if(fileName==null)
        {
            return null;
        }
        int index=indexOfLastSeparator(fileName);
        return fileName.substring(index+1);
    }
    
    public static String getPath(String fileName)
    {
        return doGetPath(fileName,true);
    }
    
    public static String getPathNoEndSeparator(String fileName)
    {
        return doGetPath(fileName,false);
    }
    
    private static String doGetPath(String fileName,boolean withEndSeparator)
    {
        String path=null;
        if(fileName!=null)
        {
            int index=indexOfLastSeparator(fileName);
            if(index==NOTxFOUND)
            {
                path="";
            }
            else
            {
                path=fileName.substring(0,index+(withEndSeparator?1:0));
            }
        }
        return path;
    }
    
    public static int indexOfLastSeparator(String fileName)
    {
        int index=NOTxFOUND;
        if(!StringUtil.isEmpty(fileName))
        {
            int unixSlash=fileName.lastIndexOf('/');
            int windowsSlash=fileName.lastIndexOf('\\');
            index=Math.max(unixSlash,windowsSlash);
        }
        return index;
    }
    
    public static String removeFileProtocolPrefix(String uri)
    {
        if(!StringUtil.isEmpty(uri))
        {
            if(uri.length()>FILExPROTOCOLxPREFIX.length())
            {
                if(uri.startsWith(FILExPROTOCOLxPREFIX))
                {
                    uri=uri.substring(FILExPROTOCOLxPREFIX.length());
                }
            }
        }
        return uri;
    }
    
    public static String addFileProtocolPrefix(String fileName)
    {
        if(!fileName.startsWith(FILExPROTOCOLxPREFIX))
        {
            fileName=StringUtil.merge(FILExPROTOCOLxPREFIX,fileName);
        }
        return fileName;
    }
    
    public static boolean isFilenameValid(String fileName)
    {
        if(StringUtil.isEmpty(fileName))
        {
            return false;
        }
        fileName=fileName.trim();
        if(fileName.length()==0)
        {
            return false;
        }
        for(String c:mCs)
        {
            if(fileName.contains(c))
            {
                return false;
            }
        }
        return !containsSurrogateChar(fileName);
    }
    
    /**
     * 把文件名中不合法的字符删掉
     *
     * @param fileName
     * @return
     */
    public static String fixFileName(String fileName)
    {
        if(null==fileName)
        {
            return null;
        }
        String result=fileName;
        for(String c:mCs)
        {
            result=result.replace(c,"");
        }
        if(containsSurrogateChar(result))
        {
            result=removeSurrogateChars(result);
        }
        return result;
    }
    
    private static String removeSurrogateChars(String string)
    {
        if(StringUtil.isEmpty(string))
        {
            return string;
        }
        int length=string.length();
        StringBuilder stringBuilder=new StringBuilder(length);
        for(int i=0;i<length;i++)
        {
            char c=string.charAt(i);
            if(c<UNICODExSURROGATExSTARTxCHAR||c>UNICODExSURROGATExENDxCHAR)
            {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
    
    private static boolean containsSurrogateChar(String string)
    {
        if(StringUtil.isEmpty(string))
        {
            return false;
        }
        int length=string.length();
        boolean hasSurrogateChar=false;
        for(int i=0;i<length;i++)
        {
            char c=string.charAt(i);
            if(UNICODExSURROGATExSTARTxCHAR<=c&&c<=UNICODExSURROGATExENDxCHAR)
            {
                hasSurrogateChar=true;
                break;
            }
        }
        return hasSurrogateChar;
    }
    
    public static String clipFilename(String fileName,int maxLen)
    {
        if(null==fileName||fileName.length()<maxLen)
        {
            return fileName;
        }
        if(maxLen<=0)
        {
            return fileName;
        }
        String extension=getExtension(fileName);
        /** 没有后缀名直接从文件名中截取 */
        if(StringUtil.isEmpty(extension))
        {
            return fileName.substring(0,maxLen);
        }
        int nameEndIndex=maxLen-extension.length()-1;
        if(nameEndIndex<0)
        {
            return fileName.substring(0,maxLen);
        }
        return fileName.substring(0,nameEndIndex)+"."+extension;
    }
    
    /**
     * 返回文件名后缀
     * 
     * <pre>
     * getExtension("a/b/c.jpg") jpg
     * getExtension("a/b.txt/c") ""
     * getExtension("a/b/c")     ""
     * getExtension("a/b/.jpg")  jpg
     * getExtension("a/b/c.")    ""
     * getExtension(null)        null
     * </pre>
     *
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName)
    {
        String extension=null;
        if(fileName!=null)
        {
            int dotIndex=indexOfExtension(fileName);
            if(dotIndex==NOTxFOUND)
            {
                extension="";
            }
            else
            {
                extension=fileName.substring(dotIndex+1);
            }
        }
        return extension;
    }
    
    public static int indexOfExtension(String fileName)
    {
        if(fileName==null)
        {
            return NOTxFOUND;
        }
        int extensionPos=fileName.lastIndexOf('.');
        int lastSeparator=indexOfLastSeparator(fileName);
        return lastSeparator>extensionPos?NOTxFOUND:extensionPos;
    }
    
    /**
     * <pre>
     * a/b/c.txt c
     * a.txt     a
     * a/b/c     c
     * a/b/c/    ""
     * </pre>
     *
     * @param fileName
     * @return
     */
    public static String getBaseName(String fileName)
    {
        return removeExtension(getName(fileName));
    }
    
    /**
     * 移除扩展名
     *
     * <pre>
     * foo.txt   foo
     * a\b\c.jpg a\b\c
     * a\b\c     a\b\c
     * a.b\c     a.b\c
     * </pre>
     *
     * @param fileName
     * @return
     */
    public static String removeExtension(String fileName)
    {
        String result=null;
        if(fileName!=null)
        {
            int index=indexOfExtension(fileName);
            if(index==NOTxFOUND)
            {
                result=fileName;
            }
            else
            {
                result=fileName.substring(0,index);
            }
        }
        return result;
    }
}
