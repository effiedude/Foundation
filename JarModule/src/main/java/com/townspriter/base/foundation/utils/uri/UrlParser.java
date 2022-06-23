package com.townspriter.base.foundation.utils.uri;

import static android.util.Patterns.GOOD_IRI_CHAR;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.text.TextUtils;

/******************************************************************************
 * @path UrlParser
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:37:51
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class UrlParser
{
    public static final String TOPxLEVELxDOMAINxSTRxSIMPLIFIED="(([a-zA-Z]{2,}+)"+"|(\u0434\u0435\u0442\u0438|\u043c\u043e\u043d|\u043c\u043e\u0441\u043a\u0432\u0430|\u043e\u043d\u043b\u0430\u0439\u043d|\u043e\u0440\u0433|\u0440\u0444|\u0441\u0430\u0439\u0442|\u0441\u0440\u0431|\u0443\u043a\u0440|\u049b\u0430\u0437|\u0627\u0644\u0627\u0631\u062f\u0646|\u0627\u0644\u062c\u0632\u0627\u0626\u0631|\u0627\u0644\u0633\u0639\u0648\u062f\u064a\u0629|\u0627\u0644\u0645\u063a\u0631\u0628|\u0627\u0645\u0627\u0631\u0627\u062a|\u0627\u06cc\u0631\u0627\u0646|\u0628\u0627\u0632\u0627\u0631|\u0628\u06be\u0627\u0631\u062a|\u062a\u0648\u0646\u0633|\u0633\u0648\u0631\u064a\u0629|\u0634\u0628\u0643\u0629|\u0639\u0645\u0627\u0646|\u0641\u0644\u0633\u0637\u064a\u0646|\u0642\u0637\u0631|\u0645\u0635\u0631|\u0645\u0644\u064a\u0633\u064a\u0627|\u0645\u0648\u0642\u0639|\u092d\u093e\u0930\u0924|\u0938\u0902\u0917\u0920\u0928|\u09ad\u09be\u09b0\u09a4|\u0a2d\u0a3e\u0a30\u0a24|\u0aad\u0abe\u0ab0\u0aa4|\u0b87\u0ba8\u0bcd\u0ba4\u0bbf\u0baf\u0bbe|\u0b87\u0bb2\u0b99\u0bcd\u0b95\u0bc8|\u0b9a\u0bbf\u0b99\u0bcd\u0b95\u0baa\u0bcd\u0baa\u0bc2\u0bb0\u0bcd|\u0c2d\u0c3e\u0c30\u0c24\u0c4d|\u0dbd\u0d82\u0d9a\u0dcf|\u0e44\u0e17\u0e22|\u307f\u3093\u306a|\u4e16\u754c|\u4e2d\u4fe1|\u4e2d\u56fd|\u4e2d\u570b|\u4e2d\u6587\u7f51|\u4f01\u4e1a|\u4f5b\u5c71|\u516c\u53f8|\u516c\u76ca|\u53f0\u6e7e|\u53f0\u7063|\u5546\u57ce|\u5546\u6807|\u5728\u7ebf|\u5e7f\u4e1c|\u6211\u7231\u4f60|\u624b\u673a|\u653f\u52a1|\u65b0\u52a0\u5761|\u673a\u6784|\u6e38\u620f|\u79fb\u52a8|\u7ec4\u7ec7\u673a\u6784|\u7f51\u5740|\u7f51\u7edc|\u96c6\u56e2|\u9999\u6e2f|\uc0bc\uc131|\ud55c\uad6d|xn\\-\\-1qqw23a|xn\\-\\-3bst00m|xn\\-\\-3ds443g|xn\\-\\-3e0b707e|xn\\-\\-45brj9c|xn\\-\\-4gbrim|xn\\-\\-55qw42g|xn\\-\\-55qx5d|xn\\-\\-6frz82g|xn\\-\\-6qq986b3xl|xn\\-\\-80adxhks|xn\\-\\-80ao21a|xn\\-\\-80asehdb|xn\\-\\-80aswg|xn\\-\\-90a3ac|xn\\-\\-c1avg|xn\\-\\-cg4bki|xn\\-\\-clchc0ea0b2g2a9gcd|xn\\-\\-czr694b|xn\\-\\-czru2d|xn\\-\\-d1acj3b|xn\\-\\-fiq228c5hs|xn\\-\\-fiq64b|xn\\-\\-fiqs8s|xn\\-\\-fiqz9s|xn\\-\\-fpcrj9c3d|xn\\-\\-fzc2c9e2c|xn\\-\\-gecrj9c|xn\\-\\-h2brj9c|xn\\-\\-i1b6b1a6a2e|xn\\-\\-io0a7i|xn\\-\\-j1amh|xn\\-\\-j6w193g|xn\\-\\-kprw13d|xn\\-\\-kpry57d|xn\\-\\-kput3i|xn\\-\\-l1acc|xn\\-\\-lgbbat1ad8j|xn\\-\\-mgb9awbf|xn\\-\\-mgba3a4f16a|xn\\-\\-mgbaam7a8h|xn\\-\\-mgbab2bd|xn\\-\\-mgbayh7gpa|xn\\-\\-mgbbh1a71e|xn\\-\\-mgbc0a9azcg|xn\\-\\-mgberp4a5d4ar|xn\\-\\-mgbx4cd0ab|xn\\-\\-ngbc5azd|xn\\-\\-nqv7f|xn\\-\\-nqv7fs00ema|xn\\-\\-o3cw4h|xn\\-\\-ogbpf8fl|xn\\-\\-p1ai|xn\\-\\-pgbs0dh|xn\\-\\-q9jyb4c|xn\\-\\-rhqv96g|xn\\-\\-s9brj9c|xn\\-\\-ses554g|xn\\-\\-unup4y|xn\\-\\-vhquv|xn\\-\\-wgbh1c|xn\\-\\-wgbl6a|xn\\-\\-xhq521b|xn\\-\\-xkc2al3hye2a|xn\\-\\-xkc2dl3a5ee0h|xn\\-\\-yfro4i67o|xn\\-\\-ygbi2ammx|xn\\-\\-zfr164b)"+")";
    /** Patterns.DOMAINxNAME的写法会在输入过长时响应巨慢(如abc.asdfasdfasdfasdfasdf)使用固化子模式可以避免回溯.大大提高匹配效率 */
    public static final Pattern DOMAINxNAME=Pattern.compile(String.format("^((?:(?!-)[%1$s-]++(?<!-)|[%1$s]++)\\.)++%2$s$",GOOD_IRI_CHAR,TOPxLEVELxDOMAINxSTRxSIMPLIFIED));
    /** Patterns.IPxADDRESS的写法少了必须匹配文本开始和结束的要求.类似256.2.2.256也会被识别为合法IP(56.2.2.25).修改了一下 */
    public static final Pattern IPxADDRESSxV4=Pattern.compile("^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"+"[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"+"[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"+"|[1-9][0-9]|[0-9]))$");
    public static final Pattern SPECIALxSCHEME=Pattern.compile("^((qk)\\:\\/\\/)",Pattern.CASE_INSENSITIVE);
    public static final Pattern SPECIALxSCHEMExWITHOUTxSLASH=Pattern.compile("^((sms|smsto|mms|mmsto|tel|about|ucd|ext)\\:)",Pattern.CASE_INSENSITIVE);
    public static final Pattern URL=Pattern.compile(/* scheme */"(?:(http|https|file|.*)\\:\\/\\/)?"+
    /* authority */"(?:([-A-Za-z0-9$_.+!*'(),;?&=]+(?:\\:[-A-Za-z0-9$_.+!*'(),;?&=]+)?)@)?"+
    /* host */"(["+GOOD_IRI_CHAR+"%_-]["+GOOD_IRI_CHAR+"%_\\.-]*|\\[[0-9a-fA-F:\\.]+\\])?"+
    /* port */"(?:\\:([0-9]*))?"+
    /* path */"(\\/?[^#]*)?"+
    /* anchor */".*",Pattern.CASE_INSENSITIVE);
    private static final int MATCHxGROUPxSCHEME=1;
    private static final int MATCHxGROUPxAUTHORITY=2;
    private static final int MATCHxGROUPxHOST=3;
    private static final int MATCHxGROUPxPORT=4;
    private static final int MATCHxGROUPxPATH=5;
    private boolean mIsSpecialScheme;
    private String mOriginalUrl;
    private String mScheme;
    private String mHost;
    private int mPort;
    private String mPath;
    private String mAuthInfo;
    
    public UrlParser(String url) throws IllegalArgumentException
    {
        if(url==null)
        {
            throw new NullPointerException();
        }
        url=url.replace('。','.');
        url=url.replaceAll("\\.{2,}","\\.");
        mScheme="";
        mHost="";
        mPort=-1;
        mPath="/";
        mAuthInfo="";
        if(isSpecialScheme(url))
        {
            mIsSpecialScheme=true;
            mOriginalUrl=url;
        }
        Matcher m=URL.matcher(url);
        String t;
        if(m.matches())
        {
            t=m.group(MATCHxGROUPxSCHEME);
            if(t!=null)
            {
                mScheme=t.toLowerCase();
            }
            t=m.group(MATCHxGROUPxAUTHORITY);
            if(t!=null)
            {
                mAuthInfo=t;
            }
            t=m.group(MATCHxGROUPxHOST);
            if(t!=null)
            {
                mHost=t;
            }
            t=m.group(MATCHxGROUPxPORT);
            if(t!=null&&t.length()>0)
            {
                try
                {
                    mPort=Integer.parseInt(t);
                }
                catch(NumberFormatException ex)
                {
                    throw new IllegalArgumentException("无效端口");
                }
                if(mPort<0)
                {
                    throw new IllegalArgumentException("无效端口");
                }
            }
            t=m.group(MATCHxGROUPxPATH);
            if(t!=null&&t.length()>0)
            {
                if(t.charAt(0)=='/')
                {
                    mPath=t;
                }
                else
                {
                    mPath="/"+t;
                }
            }
        }
        else
        {
            throw new IllegalArgumentException("无效地址");
        }
        if(mPort==443&&"".equals(mScheme))
        {
            mScheme="https";
        }
        else if(mPort==-1)
        {
            if("https".equals(mScheme))
            {
                mPort=443;
            }
            else
            {
                mPort=80;
            }
        }
        if("".equals(mScheme))
        {
            mScheme="http";
        }
    }
    
    public static boolean isValidIP(String host)
    {
        try
        {
            Pattern p=IPxADDRESSxV4;
            Matcher m=p.matcher(host);
            if(m.find())
            {
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean isValidDomain(String host)
    {
        try
        {
            Pattern p=DOMAINxNAME;
            Matcher m=p.matcher(host);
            if(m.find())
            {
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean isSpecialScheme(String url)
    {
        try
        {
            Pattern p=SPECIALxSCHEME;
            Matcher m=p.matcher(url);
            if(m.find())
            {
                return true;
            }
            p=SPECIALxSCHEMExWITHOUTxSLASH;
            m=p.matcher(url);
            if(m.find())
            {
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    private static String merge(String scheme,String authInfo,String host,int port,String path)
    {
        String portStr="";
        if((port!=443&&"https".equals(scheme))||(port!=80&&"http".equals(scheme)))
        {
            portStr=":"+port;
        }
        if(authInfo.length()>0)
        {
            authInfo=authInfo+"@";
        }
        return scheme+"://"+authInfo+host+portStr+path;
    }
    
    public boolean isDomainWellFormed()
    {
        if(mIsSpecialScheme)
        {
            return true;
        }
        if(TextUtils.isEmpty(mHost)||!mHost.contains("."))
        {
            return false;
        }
        if(isValidDomain(mHost))
        {
            return true;
        }
        return isValidIP(mHost);
    }
    
    @Override
    public String toString()
    {
        if(mIsSpecialScheme)
        {
            return mOriginalUrl;
        }
        else
        {
            return merge(mScheme,mAuthInfo,mHost,mPort,mPath);
        }
    }
    
    public String getScheme()
    {
        return mScheme;
    }
    
    public void setScheme(String scheme)
    {
        mScheme=scheme;
    }
    
    public String getHost()
    {
        return mHost;
    }
    
    public void setHost(String host)
    {
        mHost=host;
    }
    
    public int getPort()
    {
        return mPort;
    }
    
    public void setPort(int port)
    {
        mPort=port;
    }
    
    public String getPath()
    {
        return mPath;
    }
    
    public void setPath(String path)
    {
        mPath=path;
    }
    
    public String getAuthInfo()
    {
        return mAuthInfo;
    }
    
    public void setAuthInfo(String authInfo)
    {
        mAuthInfo=authInfo;
    }
}
