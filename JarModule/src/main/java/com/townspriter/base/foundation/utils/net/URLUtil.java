package com.townspriter.base.foundation.utils.net;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLStreamHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.townspriter.base.foundation.utils.lang.AssertUtil;
import com.townspriter.base.foundation.utils.lang.NumberUtil;
import com.townspriter.base.foundation.utils.net.mime.MimeUtil;
import com.townspriter.base.foundation.utils.text.StringUtil;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path URLUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public final class URLUtil
{
    private static final String ASSETxBASE="file:///android_asset/";
    private static final String FILExBASE="file://";
    private static final String PROXYxBASE="file:///cookieless_proxy/";
    private static final String LOCALxFILExURLxPREFIX="file:///data/data/";
    private static final String HTTPxPREFIX="http://";
    private static final String HTTPSxPREFIX="https://";
    private static final String PATTERNxEND="(:\\d{1,5})?(/|\\?|$)";
    private static final Pattern URLxWITHxPROTOCOLxPATTERN=Pattern.compile("[a-zA-Z0-9]{2,}://[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9\\-]+)*"+PATTERNxEND);
    private static final Pattern URLxWITHOUTxPROTOCOLxPATTERN=Pattern.compile("([a-zA-Z0-9\\-]+\\.)+[a-zA-Z0-9\\-]{2,}"+PATTERNxEND);
    private static final Pattern URIxPROTOCOLxPATTERN=Pattern.compile("^[\\w+.-]{1,16}+://");
    private static final String[] DEFAULTxQSxKEYS={"attachment","filename"};
    private static final String DEFAULTxNAME="index.html";
    private static final Pattern NAMExINxPATHxPATTERN=Pattern.compile("/([^/]++)/*+$");
    private static final URLStreamHandler sIgnoreUnknownProtocolHandler=new URLStreamHandler()
    {
        @Override
        protected URLConnection openConnection(URL u) throws IOException
        {
            return null;
        }
    };
    
    public static boolean isFileUrl(String url)
    {
        return (!StringUtil.isEmptyWithTrim(url))&&(url.startsWith(FILExBASE)&&!url.startsWith(ASSETxBASE)&&!url.startsWith(PROXYxBASE));
    }
    
    public static boolean isHttpUrl(String url)
    {
        return (!StringUtil.isEmptyWithTrim(url))&&(url.length()>6)&&HTTPxPREFIX.equalsIgnoreCase(url.substring(0,7));
    }
    
    public static boolean isHttpsUrl(String url)
    {
        return (!StringUtil.isEmptyWithTrim(url))&&(url.length()>7)&&HTTPSxPREFIX.equalsIgnoreCase(url.substring(0,8));
    }
    
    public static boolean isNetworkUrl(String url)
    {
        if(StringUtil.isEmptyWithTrim(url))
        {
            return false;
        }
        return isHttpUrl(url)||isHttpsUrl(url);
    }
    
    public static String stripAnchor(String url)
    {
        int anchorIndex=url.indexOf('#');
        if(anchorIndex!=-1)
        {
            return url.substring(0,anchorIndex);
        }
        return url;
    }
    
    public static String getHostFromUrl(String url)
    {
        if(StringUtil.isEmpty(url))
        {
            return "";
        }
        int index=url.indexOf("://");
        if(index<0)
        {
            url="http://"+url;
        }
        try
        {
            URL uri=new URL(url);
            return uri.getHost();
        }
        catch(Throwable throwable)
        {
            return "";
        }
    }
    
    public static int getPortFromHttpAndHttpsUrl(String url)
    {
        if(null==url||url.length()==0)
        {
            return -1;
        }
        int port=80;
        int nextSearchStart=0;
        String protocol="http";
        int index1=url.indexOf("://");
        if(index1>0)
        {
            protocol=url.substring(0,index1);
            nextSearchStart=index1+3;
        }
        if("https".equalsIgnoreCase(protocol))
        {
            port=443;
        }
        else if(!"http".equalsIgnoreCase(protocol))
        {
            return -1;
        }
        int index2=url.indexOf(":",nextSearchStart);
        if(index2>0)
        {
            int index3=url.indexOf('/',index2+1);
            if(index3<0)
            {
                port=NumberUtil.toInt(url.substring(index2+1),port);
            }
            else
            {
                port=NumberUtil.toInt(url.substring(index2+1,index3),port);
            }
        }
        return port;
    }
    
    public static String getSchemaFromUrl(String url)
    {
        if(null==url||url.length()==0)
        {
            return "";
        }
        int index=url.indexOf("://");
        if(index>0)
        {
            return url.substring(0,index);
        }
        return "";
    }
    
    public static String getRootHostFromUrl(String url)
    {
        String host=getHostFromUrl(url);
        if(StringUtil.isNotEmptyWithTrim(host))
        {
            String[] hostItem=host.split("\\.");
            if(hostItem!=null&&hostItem.length>=2)
            {
                return StringUtil.merge(hostItem[hostItem.length-2],".",hostItem[hostItem.length-1]);
            }
        }
        return host;
    }
    
    /** 从请求链接中提取文件名.若路径无法获取合适的文件名将通过DEFAULTxQSxKEYS列表的值进行匹配 */
    public static String getFileNameFromUrl(String url)
    {
        return getFileNameFromUrl(url,true,DEFAULTxQSxKEYS,DEFAULTxNAME);
    }
    
    public static String getFileNameFromUrl(String inputUrl,boolean parseQueryString,String[] queryStringKeys,@NonNull String defaultName)
    {
        if(inputUrl==null)
        {
            return defaultName;
        }
        inputUrl=inputUrl.trim();
        if(inputUrl.length()==0)
        {
            return defaultName;
        }
        boolean urlDecoded=false;
        if(!inputUrl.contains("/")&&inputUrl.contains("%"))
        {
            try
            {
                inputUrl=URLDecoder.decode(inputUrl);
                urlDecoded=true;
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        if(!URIxPROTOCOLxPATTERN.matcher(inputUrl).find())
        {
            inputUrl="http://"+inputUrl;
        }
        URL uri;
        try
        {
            uri=new URL(null,inputUrl,sIgnoreUnknownProtocolHandler);
        }
        catch(Exception exception)
        {
            return defaultName;
        }
        String fileName=defaultName;
        String path=uri.getPath();
        if(path!=null)
        {
            String name=findFileNameInUrlPath(path);
            if(!TextUtils.isEmpty(name))
            {
                fileName=name;
            }
        }
        String extension=MimeUtil.getFileExtensionFromFileName(fileName);
        boolean isInvalidName=TextUtils.isEmpty(fileName)||fileName.equals(defaultName);
        if((TextUtils.isEmpty(extension)||isInvalidName)&&parseQueryString&&queryStringKeys!=null&&queryStringKeys.length>0)
        {
            String queryString=uri.getQuery();
            if(!TextUtils.isEmpty(queryString))
            {
                NameFilter filter=new NameFilter()
                {
                    @Override
                    public boolean accept(String newName)
                    {
                        String extension=MimeUtil.getFileExtensionFromFileName(newName);
                        return !TextUtils.isEmpty(extension);
                    }
                };
                fileName=findNameInQueryString(queryStringKeys,queryString,filter);
            }
        }
        if(TextUtils.isEmpty(fileName))
        {
            return defaultName;
        }
        if(!urlDecoded&&!fileName.equals(defaultName))
        {
            try
            {
                fileName=URLDecoder.decode(fileName);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return fileName;
    }
    
    private static String findFileNameInUrlPath(String path)
    {
        if(TextUtils.isEmpty(path)||!path.contains("/"))
        {
            return path;
        }
        Matcher matcher=NAMExINxPATHxPATTERN.matcher(path);
        return matcher.find()?matcher.group(1):"";
    }
    
    public static String findNameInQueryString(String[] keys,String queryString,NameFilter filter)
    {
        if(keys==null||keys.length==0||TextUtils.isEmpty(queryString))
        {
            return "";
        }
        queryString=queryString.toLowerCase();
        if(!queryString.startsWith("&"))
        {
            queryString="&"+queryString;
        }
        for(String key:keys)
        {
            String name=getValueFromQueryString(key,queryString);
            if(TextUtils.isEmpty(name))
            {
                continue;
            }
            name=findFileNameInUrlPath(name);
            if(filter!=null&&!filter.accept(name))
            {
                continue;
            }
            return name;
        }
        return "";
    }
    
    private static String getValueFromQueryString(String key,String queryString)
    {
        int keyIndex=queryString.indexOf("&"+key+"=");
        if(keyIndex>=0)
        {
            int keyLen=key.length()+2;
            int endIndex=queryString.indexOf("&",keyIndex+keyLen);
            if(endIndex>0)
            {
                return queryString.substring(keyIndex+keyLen,endIndex);
            }
            else
            {
                return queryString.substring(keyIndex+keyLen);
            }
        }
        return "";
    }
    
    public static boolean isWapSite(String host)
    {
        if(null==host||0==host.length())
        {
            return false;
        }
        String tmpHost=host.toLowerCase(Locale.ENGLISH);
        String[] wapPrefix={"wap.","3g","m.","mobi."};
        for(int i=0;i<wapPrefix.length;i++)
        {
            int pos=tmpHost.indexOf(wapPrefix[i]);
            if(pos==0)
            {
                return true;
            }
            else if(pos>0)
            {
                if(".".equals(tmpHost.substring(pos-1,pos)))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Nullable
    @Deprecated
    public static String getParamFromUrl(String url,String key)
    {
        if(url!=null)
        {
            url=url.substring(url.indexOf('?')+1);
            String[] paramaters=url.split("&");
            for(String param:paramaters)
            {
                String[] values=param.split("=");
                if(values.length==2)
                {
                    if(key.equalsIgnoreCase(values[0]))
                    {
                        return values[1];
                    }
                }
            }
        }
        return null;
    }
    
    public static String getQueryParameter(String url,String key)
    {
        String param="";
        if(TextUtils.isEmpty(url)||TextUtils.isEmpty(key))
        {
            return param;
        }
        Uri uri=Uri.parse(url);
        try
        {
            param=uri.getQueryParameter(key);
        }
        catch(UnsupportedOperationException e)
        {
            AssertUtil.fail(e);
        }
        return param;
    }
    
    @Nullable
    public static String removeParamFromUrl(String url,String key)
    {
        if(url!=null)
        {
            String value=getQueryParameter(url,key);
            if(value!=null)
            {
                String param=key+"="+value;
                url=url.replace("&"+param,"");
                url=url.replace("?"+param,"?");
            }
        }
        return url;
    }
    
    /** 根据请求地址参数的键.如果有相应键则值不改变.如果没有则什么都不做 */
    public static String replaceParamFromUrl(String url,String key,String value)
    {
        String result=url;
        if(hasParam(url,key))
        {
            result=removeParamFromUrl(url,key);
            result=addParamsToUrl(result,key,value);
        }
        return result;
    }
    
    /** 根据请求地址参数的键.如果有相应键则值不改变.如果没有相应键则新加键 */
    public static String replaceAddParamFromUrl(String url,String key,String value)
    {
        String result=url;
        if(!hasParam(url,key))
        {
            result=addParamsToUrl(url,key,value);
        }
        return result;
    }
    
    /** 强制使用新的值 */
    public static String addParamFromUrlForce(String url,String key,String value)
    {
        String result=url;
        if(hasParam(url,key))
        {
            result=replaceParamFromUrl(url,key,value);
        }
        else
        {
            result=addParamsToUrl(url,key,value);
        }
        return result;
    }
    
    /** 检测请求地址中是否有键对应的值 */
    public static boolean hasParam(String url,String key)
    {
        if(StringUtil.isNotEmpty(url))
        {
            url=url.substring(url.indexOf('?')+1);
            String[] paramaters=url.split("&");
            for(String param:paramaters)
            {
                String[] values=param.split("=");
                if(values.length>0&&key.equalsIgnoreCase(values[0]))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    /** 清理网址中存在的重复点 */
    public static String cleanRepeatDot(String input)
    {
        StringBuilder output=new StringBuilder(input);
        char dot='.';
        int currIndex=output.indexOf(String.valueOf(dot));
        int length=output.length();
        while(currIndex>=0&&currIndex<length-1)
        {
            if(currIndex>=0&&output.charAt(currIndex)==dot&&output.charAt(currIndex)==output.charAt(currIndex+1))
            {
                output.deleteCharAt(currIndex);
            }
            else
            {
                currIndex+=1;
            }
            currIndex=output.indexOf(String.valueOf(dot),currIndex);
            length=output.length();
        }
        return output.toString();
    }
    
    /**
     * 往请求地址中添加参数
     * 注意
     * 1.键值对如果需要编码.由使用者外部编码之后再传入
     * 2.此函数不做键值对的重复性检查
     *
     * @param url
     * @param key
     * @param value
     * @return 新组装后的url
     */
    public static String addParamsToUrl(String url,String key,String value)
    {
        if(StringUtil.isEmptyWithTrim(url)||StringUtil.isEmptyWithTrim(key))
        {
            return url;
        }
        String newUrl;
        String tempUrl=url;
        String hashUrl=null;
        boolean hashFlag=false;
        int hashPos=url.indexOf("#");
        if(hashPos>0)
        {
            hashFlag=true;
            tempUrl=url.substring(0,hashPos);
            hashUrl=url.substring(hashPos);
        }
        int pos=tempUrl.indexOf("?");
        if(pos<0)
        {
            newUrl=tempUrl+"?"+key+"="+(null!=value?value:"");
        }
        else
        {
            newUrl=tempUrl+"&"+key+"="+(null!=value?value:"");
        }
        if(hashFlag)
        {
            newUrl+=hashUrl;
        }
        return newUrl;
    }
    
    public static boolean isContainQueMark(String url)
    {
        if(StringUtil.isEmptyWithTrim(url))
        {
            return false;
        }
        int pos=url.indexOf("?");
        return pos>=0;
    }
    
    public static boolean isValideIP4Address(String str)
    {
        if(str==null)
        {
            return false;
        }
        str=str.trim();
        if(str.length()==0)
        {
            return false;
        }
        boolean isIP4Address=false;
        try
        {
            if(str.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))
            {
                String[] s=str.split("\\.");
                if(Integer.parseInt(s[0])<256)
                {
                    if(Integer.parseInt(s[1])<256)
                    {
                        if(Integer.parseInt(s[2])<256)
                        {
                            if(Integer.parseInt(s[3])<256)
                            {
                                isIP4Address=true;
                            }
                        }
                    }
                }
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return isIP4Address;
    }
    
    public static boolean isBasicallyValidURI(CharSequence uri)
    {
        if(null==uri)
        {
            return false;
        }
        Matcher m=URLxWITHxPROTOCOLxPATTERN.matcher(uri);
        if(m.find()&&m.start()==0)
        {
            return true;
        }
        m=URLxWITHOUTxPROTOCOLxPATTERN.matcher(uri);
        return m.find()&&m.start()==0;
    }
    
    public static boolean isLocalFileURI(String uri)
    {
        return uri!=null&&uri.startsWith(LOCALxFILExURLxPREFIX);
    }
    
    public static String cleanRepeatDotAndTrim(String url)
    {
        if(TextUtils.isEmpty(url))
        {
            return "";
        }
        return cleanRepeatDot(url).trim();
    }
    
    public static String getFragmentFromUrl(String url)
    {
        if(null==url||url.length()==0)
        {
            return "";
        }
        int index=url.indexOf("#");
        if(index>0)
        {
            return url.substring(index+1);
        }
        return "";
    }
    
    public static String getSecondLevelDomain(String url)
    {
        if(TextUtils.isEmpty(url))
        {
            return url;
        }
        Uri uri=Uri.parse(url);
        String host=uri.getHost();
        if(TextUtils.isEmpty(host))
        {
            return url;
        }
        String[] domains=TextUtils.split(host,"\\.");
        if((null==domains)||(domains.length==0))
        {
            return url;
        }
        int len=domains.length;
        String secondLevelDomain=null;
        int SECONDxLEVELxDOMAINxLEN=3;
        if(len>SECONDxLEVELxDOMAINxLEN)
        {
            for(int i=0;i<SECONDxLEVELxDOMAINxLEN;i++)
            {
                String tmp=domains[len-i-1];
                if(secondLevelDomain!=null)
                {
                    secondLevelDomain=tmp+"."+secondLevelDomain;
                }
                else
                {
                    secondLevelDomain=tmp;
                }
            }
            return secondLevelDomain;
        }
        else
        {
            return host;
        }
    }
    
    public static String removeHttpsAndHttpPrefix(String url)
    {
        if(StringUtil.isEmptyWithTrim(url))
        {
            return url;
        }
        if(url.startsWith("https://"))
        {
            url=url.substring("https://".length());
        }
        else if(url.startsWith("http://"))
        {
            url=url.substring("http://".length());
        }
        return url;
    }
    
    public static boolean isMarketURL(String aUrl)
    {
        return StringUtil.isNotEmptyWithTrim(aUrl)&&aUrl.toLowerCase().startsWith("market://");
    }
    
    public static Map<String,String> getQueryParams(String query)
    {
        if(TextUtils.isEmpty(query))
        {
            return Collections.emptyMap();
        }
        String[] queryArray=query.split("&");
        if(queryArray.length==0)
        {
            return Collections.emptyMap();
        }
        Map<String,String> params=new HashMap<>(queryArray.length);
        try
        {
            for(String kv:queryArray)
            {
                if(kv==null||!kv.contains("="))
                {
                    continue;
                }
                String[] entry=kv.split("=");
                if(entry.length==2)
                {
                    String k=entry[0];
                    String v=entry[1];
                    params.put(k,v);
                }
            }
        }
        catch(PatternSyntaxException patternSyntaxException)
        {
            AssertUtil.fail(patternSyntaxException);
        }
        return params;
    }
    
    public interface NameFilter
    {
        boolean accept(String newName);
    }
}
