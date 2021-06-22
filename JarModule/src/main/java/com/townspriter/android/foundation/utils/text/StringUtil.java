package com.townspriter.android.foundation.utils.text;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.townspriter.android.foundation.utils.lang.ArrayUtil;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path Foundation:StringUtil
 * @version 1.0.0.0
 * @describe 字符串的工具类
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-05
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public final class StringUtil
{
    private static final int NOTxFOUND=-1;
    private static final String EMPTYxSTRING="";
    
    /**
     * matcherSearchTitle
     * 搜索关键字标红
     *
     * @param title
     * 源文字内容
     * @param keyword
     * 高亮关键字
     * @return 添加关键字高亮之后的内容
     */
    public static String matcherSearchTitle(@NonNull String title,@NonNull String keyword)
    {
        String content=title;
        /** 用(?i)来忽略大小写 */
        String wordReg="(?i)"+keyword;
        StringBuffer stringBuffer=new StringBuffer();
        Matcher matcher=Pattern.compile(wordReg).matcher(content);
        while(matcher.find())
        {
            // 这样保证了原文的大小写没有发生变化
            matcher.appendReplacement(stringBuffer,"<font color=\"#FDB93F\"><b>"+matcher.group()+"</b></font>");
        }
        matcher.appendTail(stringBuffer);
        content=stringBuffer.toString();
        return content;
    }
    
    /**
     * getLastIndexString
     * 获取字串中最后指定字符之后的内容
     *
     * @param string
     * 源字符串
     * @param character
     * 标识字符
     * @return
     */
    public @Nullable static String getLastIndexString(@NonNull String string,@NonNull String character)
    {
        int characterIndex=string.lastIndexOf(character);
        int characterIndexMoveRight=characterIndex+1;
        if(characterIndexMoveRight>0&&characterIndexMoveRight<string.length())
        {
            return string.substring(characterIndexMoveRight);
        }
        return null;
    }
    
    /**
     * getLastIndexStringWithSelf
     * 获取字串中最后指定字符之后的内容(包含字符自身)
     *
     * @param string
     * 源字符串
     * @param character
     * 标识字符
     * @return
     */
    public @Nullable static String getLastIndexStringWithSelf(@NonNull String string,@NonNull String character)
    {
        int characterIndex=string.lastIndexOf(character);
        if(characterIndex>0&&characterIndex<string.length())
        {
            return string.substring(characterIndex);
        }
        return null;
    }
    
    /**
     * getIndexString
     * 获取字串中最先指定字符之后的内容
     *
     * @param string
     * 源字符串
     * @param character
     * 标识字符
     * @return
     */
    public @Nullable static String getIndexString(@NonNull String string,@NonNull String character)
    {
        int characterIndex=string.indexOf(character);
        int characterIndexMoveRight=characterIndex+1;
        if(characterIndexMoveRight>0&&characterIndexMoveRight<string.length())
        {
            return string.substring(characterIndexMoveRight);
        }
        return null;
    }
    
    /**
     * getIndexStringWithSelf
     * 获取字串中最先指定字符之后的内容(包含字符自身)
     *
     * @param string
     * 源字符串
     * @param character
     * 标识字符
     * @return
     */
    public @Nullable static String getIndexStringWithSelf(@NonNull String string,@NonNull String character)
    {
        int characterIndex=string.indexOf(character);
        if(characterIndex>0&&characterIndex<string.length())
        {
            return string.substring(characterIndex);
        }
        return null;
    }
    
    /**
     * 检查文本是是否为空或者空字符串.与{@link #isEmpty(String)}等价
     *
     * <pre>
     * StringUtil.isEmpty(null)      true
     * StringUtil.isEmpty("")        true
     * StringUtil.isEmpty(" ")       false
     * StringUtil.isEmpty("bob")     false
     * StringUtil.isEmpty("  bob  ") false
     * </pre>
     *
     * @param text
     * @return
     */
    public static boolean isEmpty(@Nullable String text)
    {
        return text==null||text.length()==0;
    }
    
    public static boolean isEmptyWithNull(String text)
    {
        return text==null||text.length()==0||"null".equalsIgnoreCase(text);
    }
    
    /**
     * 返回值与{@link #isEmpty(String)}相反
     *
     * @param text
     * @return
     */
    public static boolean isNotEmpty(String text)
    {
        return !isEmpty(text);
    }
    
    /**
     * <p>
     * 检查文本是否为空或空字符串.或{@link #trim(String)}后是字符串
     * </p>
     * <p>
     * 兼容性接口.<b>注意与{@link #isEmpty(String)}不等价.效率比它低.按需使用.</b>尽量用{@link #isEmpty(String)}
     * </p>
     *
     * <pre>
     * StringUtil.isEmpty(null)        true
     * StringUtil.isEmpty("")          true
     * StringUtil.isEmpty(" ")         true
     * StringUtil.isEmpty("bob")       false
     * StringUtil.isEmpty("  bob  ")   false
     * </pre>
     *
     * @param text
     * @return
     */
    public static boolean isEmptyWithTrim(String text)
    {
        if(isEmpty(text))
        {
            return true;
        }
        String trimText=text.trim();
        return isEmpty(trimText);
    }
    
    public static boolean isEmptyWithTrim(CharSequence charSequence)
    {
        boolean ret=true;
        if(null!=charSequence)
        {
            ret=isEmptyWithTrim(charSequence.toString());
        }
        return ret;
    }
    
    /**
     * 返回值与{@link #isEmptyWithTrim(String)}相反
     *
     * @param text
     * @return
     */
    public static boolean isNotEmptyWithTrim(String text)
    {
        return !isEmptyWithTrim(text);
    }
    
    /**
     * <p>
     * 判断StringBuffer是否为空
     * </p>
     * <b>修改历史</b>
     * <ol>
     * <li>张飞创建</li>
     * </ol>
     *
     * @param aStringBuffer
     * @return
     */
    public static boolean isEmpty(StringBuffer aStringBuffer)
    {
        return aStringBuffer==null||aStringBuffer.length()==0;
    }
    
    /**
     * 将任意个字符串合并成一个字符串
     *
     * @param mText
     * 字符串
     * @return
     */
    public static String merge(CharSequence...mText)
    {
        if(mText!=null)
        {
            int length=mText.length;
            StringBuilder mStringBuilder=new StringBuilder();
            for(int i=0;i<length;i++)
            {
                if(mText[i]!=null&&mText[i].length()>0&&!"null".equals(mText[i].toString()))
                {
                    mStringBuilder.append(mText[i]);
                }
            }
            return mStringBuilder.toString();
        }
        return null;
    }
    
    public static String[] split(String text,String separator)
    {
        return split(text,separator,true);
    }
    
    public static String[] split(String text,String separator,boolean withEmptyString)
    {
        if(isEmpty(text))
        {
            return new String[0];
        }
        if(separator==null||separator.length()==0)
        {
            return new String[]{text};
        }
        String[] sTarget;
        int sTargetLength=0;
        int sLength=text.length();
        int sStartIndex=0;
        int sEndIndex=0;
        /** 扫描字符串.确定目标字符串数组的长度 */
        for(sEndIndex=text.indexOf(separator);sEndIndex!=-1&&sEndIndex<sLength;sEndIndex=text.indexOf(separator,sEndIndex))
        {
            sTargetLength+=(withEmptyString||sStartIndex!=sEndIndex)?1:0;
            sStartIndex=sEndIndex+=sEndIndex>=0?separator.length():0;
        }
        /** 如果最后一个标记的位置非字符串的结尾.则需要处理结束串 */
        sTargetLength+=withEmptyString||sStartIndex!=sLength?1:0;
        /** 重置变量值.根据标记拆分字符串 */
        sTarget=new String[sTargetLength];
        int sIndex=0;
        for(sIndex=0,sEndIndex=text.indexOf(separator),sStartIndex=0;sEndIndex!=-1&&sEndIndex<sLength;sEndIndex=text.indexOf(separator,sEndIndex))
        {
            if(withEmptyString||sStartIndex!=sEndIndex)
            {
                sTarget[sIndex]=text.substring(sStartIndex,sEndIndex);
                ++sIndex;
            }
            sStartIndex=sEndIndex+=sEndIndex>=0?separator.length():0;
        }
        /** 取结束的子串 */
        if(withEmptyString||sStartIndex!=sLength)
        {
            sTarget[sTargetLength-1]=text.substring(sStartIndex);
        }
        return sTarget;
    }
    
    public static String[] splitAndTrim(String value,String expr)
    {
        value=value.replace(" ","");
        return value.split(expr);
    }
    
    /**
     * 比较两个字符串(大小写敏感)
     *
     * <pre>
     * StringUtil.equals(null,null)   true
     * StringUtil.equals(null,"abc")  false
     * StringUtil.equals("abc",null)  false
     * StringUtil.equals("abc","abc") true
     * StringUtil.equals("abc","ABC") false
     * </pre>
     *
     * @param left
     * 要比较的字符串
     * @param right
     * 要比较的字符串
     * @return 如果两个字符串相同.或者都是<code>NULL</code>.否则返回<code>true</code>
     */
    public static boolean equals(String left,String right)
    {
        if(left==null)
        {
            return right==null;
        }
        return left.equals(right);
    }
    
    /**
     * <p>
     * 字符串比较不考虑大小写.如果两个字符串的长度相同并且其中的相应字符都相等(忽略大小写).则认为这两个字符串是相等的
     * </p>
     *
     * <pre>
     * StringUtil.equalsIgnoreCase(null,null)   true
     * StringUtil.equalsIgnoreCase(null,"abc")  false
     * StringUtil.equalsIgnoreCase("abc",null)  false
     * StringUtil.equalsIgnoreCase("abc","abc") true
     * StringUtil.equalsIgnoreCase("abc","ABC") true
     * </pre>
     *
     * @param left
     * 需要被对比的字符串
     * @param right
     * 需要对比的字符串
     * @return 相等(忽略大小写)则返回<code>true</code>.否则返回<code>false</code>
     */
    public static boolean equalsIgnoreCase(String left,String right)
    {
        if(left==null)
        {
            return right==null;
        }
        return left.equalsIgnoreCase(right);
    }
    
    /**
     * <p>
     * 测试此字符串是否以指定的前缀开始.<b>注:不区分大小写</b>
     * </p>
     *
     * @param str
     * 测试的字符串
     * @param prefix
     * 指定的前缀
     * @return 如果参数表示的字符序列是此字符串表示的字符序列的前缀.则返回<code>true</code>.否则返回<code>false</code>
     */
    public static boolean startsWithIgnoreCase(String str,String prefix)
    {
        if(str==null||prefix==null)
        {
            return str==null&&prefix==null;
        }
        if(prefix.length()>str.length())
        {
            return false;
        }
        return str.regionMatches(true,0,prefix,0,prefix.length());
    }
    
    public static int indexOfIgnoreCase(String str,String searchStr)
    {
        return indexOfIgnoreCase(str,searchStr,0);
    }
    
    /**
     * <p>
     * 返回指定子字符串在此字符串中第一次出现处的索引.从指定的索引开始
     * </p>
     *
     * @param str
     * 计算的字符串
     * @param searchStr
     * 指定的子字符串
     * @param startIndex
     * 指定的索引
     * @return 如果字符串参数作为一个子字符串在此对象中出现一次或多次.则返回最后一个这种子字符串的第一个字符.如果它不作为一个子字符串出现.则返回-1
     */
    public static int indexOfIgnoreCase(String str,String searchStr,int startIndex)
    {
        if(str==null||searchStr==null)
        {
            return NOTxFOUND;
        }
        if(startIndex<0)
        {
            startIndex=0;
        }
        int endLimit=str.length()-searchStr.length()+1;
        if(startIndex>endLimit)
        {
            return NOTxFOUND;
        }
        if(searchStr.length()==0)
        {
            return startIndex;
        }
        for(int i=startIndex;i<endLimit;i++)
        {
            if(str.regionMatches(true,i,searchStr,0,searchStr.length()))
            {
                return i;
            }
        }
        return NOTxFOUND;
    }
    
    /**
     * 以分隔符链接字符串
     *
     * @param list
     * @param seperator
     * @return
     */
    public static String join(List<String> list,String seperator)
    {
        if(null==seperator)
        {
            return "";
        }
        if(null==list||list.isEmpty())
        {
            return "";
        }
        int listSize=list.size();
        StringBuilder sb=new StringBuilder();
        if(listSize>0)
        {
            sb.append(list.get(0));
            for(int i=1;i<listSize;i++)
            {
                sb.append(seperator);
                sb.append(list.get(i));
            }
        }
        return sb.toString();
    }
    
    /**
     * @see {@link #join(List,String)}}
     * @param str
     * @param seperator
     * @return
     */
    public static List<String> unJoin(String str,String seperator)
    {
        if(isEmpty(str)||null==seperator)
        {
            return new ArrayList<>();
        }
        String[] result=str.split(seperator);
        if(null==result)
        {
            return new ArrayList<>();
        }
        ArrayList<String> list=new ArrayList<>(result.length);
        for(String item:result)
        {
            list.add(item);
        }
        return list;
    }
    
    /** 替换字符串中的<code>&</code> <code>nbsp</code> <code> </code>为半角空格 */
    public static String replaceNBSPSpace(String str)
    {
        if(str==null||str.length()==0)
        {
            return str;
        }
        return str.replace('\u00A0','\u0020');
    }
    
    public static boolean parseBoolean(String value,boolean aDefault)
    {
        if(value==null)
        {
            return aDefault;
        }
        return "1".equals(value)||"true".equalsIgnoreCase(value);
    }
    
    public static boolean parseBoolean(String value)
    {
        return parseBoolean(value,false);
    }
    
    /**
     * <p>
     * 从字符串解析成整数
     * </p>
     * <b>修改历史</b>
     * <li>创建初始版本</li>
     * <li>支持转换十六进制</li>
     *
     * @param aValue
     * @param aDefault
     * @return
     */
    public static int parseInt(String aValue,int aDefault)
    {
        if(aValue==null||aValue.length()==0)
        {
            return aDefault;
        }
        int result=aDefault;
        boolean isHex;
        if(isHex=aValue.startsWith("0x"))
        {
            aValue=aValue.substring(2);
        }
        try
        {
            if(!isHex)
            {
                result=Integer.parseInt(aValue);
            }
            else
            {
                result=(int)Long.parseLong(aValue,16);
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return result;
    }
    
    /**
     * <p>
     * 从字符串解析成整数
     * </p>
     * <b>修改历史</b>
     * <ol>
     * <li>创建初始版本</li>
     * </ol>
     *
     * @param aValue
     * @return
     */
    public static int parseInt(String aValue)
    {
        return parseInt(aValue,0);
    }
    
    /**
     * <p>
     * 从字符串解析成整数
     * </p>
     * <b>修改历史</b>
     * <li>创建初始版本</li>
     *
     * @param aValue
     * @param aDefault
     * @return
     */
    public static long parseLong(String aValue,long aDefault)
    {
        if(aValue==null||aValue.length()==0)
        {
            return aDefault;
        }
        long result=aDefault;
        boolean isHex;
        if(isHex=aValue.startsWith("0x"))
        {
            aValue=aValue.substring(2);
        }
        try
        {
            if(!isHex)
            {
                result=Long.parseLong(aValue);
            }
            else
            {
                result=Long.parseLong(aValue,16);
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return result;
    }
    
    /**
     * <p>
     * 从字符串解析成整数
     * </p>
     * <b>修改历史</b>
     * <li>创建初始版本</li>
     *
     * @param aValue
     * @return
     */
    public static long parseLong(String aValue)
    {
        return parseLong(aValue,0);
    }
    
    public static byte[] getBytesUTF8(String value)
    {
        if(value==null)
        {
            return null;
        }
        byte[] data;
        data=value.getBytes(StandardCharsets.UTF_8);
        return data;
    }
    
    public static String newStringUTF8(byte[] bytes)
    {
        if(ArrayUtil.isEmpty(bytes))
        {
            return "";
        }
        return new String(bytes,StandardCharsets.UTF_8);
    }
    
    public static String newStringUTF8(byte[] bytes,int offset,int length)
    {
        if(ArrayUtil.isEmpty(bytes))
        {
            return "";
        }
        return new String(bytes,offset,length,StandardCharsets.UTF_8);
    }
    
    /**
     * <p>
     * 使用给定的<code>replacement</code>替换此字符串所有匹配给定的匹配的子字符串
     * </p>
     * <b>修改历史</b>
     * <li>创建初始版本</li>
     *
     * @param srcString
     * 源字符串
     * @param matchingString
     * 匹配的字符串
     * @param replacement
     * 替换的字符串
     * @param supportReplacementEmpty
     * 替换的字符串是否可以为<code>""</code>或者<code>NULL</code>.NULL会转换成""
     * @return 返回替换操作后的字符串
     */
    public static String replaceAll(String srcString,String matchingString,String replacement,boolean supportReplacementEmpty)
    {
        boolean flag=isEmpty(replacement);
        if(supportReplacementEmpty)
        {
            if(replacement==null)
            {
                replacement="";
            }
            flag=false;
        }
        if(isEmpty(srcString)||isEmpty(matchingString)||flag)
        {
            return null;
        }
        StringBuffer sResult=new StringBuffer();
        int sIndex=0;
        int sMaxIndex=srcString.length()-1;
        while((sIndex=srcString.indexOf(matchingString))!=-1)
        {
            String sPreStr=srcString.substring(0,sIndex);
            sResult.append(sPreStr).append(replacement);
            srcString=(sIndex<sMaxIndex)?srcString.substring(sIndex+matchingString.length()):"";
        }
        sResult.append(srcString);
        return sResult.toString();
    }
    
    public static String replaceAll(String srcString,String matchingString,String replacement)
    {
        return replaceAll(srcString,matchingString,replacement,false);
    }
    
    public static String ensureObjectToStringNotNull(Object object)
    {
        if(object==null)
        {
            return "";
        }
        else
        {
            return object.toString();
        }
    }
    
    /**
     * 去除字符串两端的空格
     *
     * @param originalString
     * 原始字符串
     * @return 返回去除两端的空格后的字符串
     */
    public static String trim(String originalString)
    {
        if(originalString==null)
        {
            return null;
        }
        return originalString.trim();
    }
    
    /**
     * <p>
     * 获取字符串的前缀.超过最大长度则截断
     * </p>
     *
     * @param str
     * 原始字符串
     * @param len
     * 获取前缀的最大长度
     * @return 字符串前缀
     */
    public static String left(String str,int len)
    {
        if(str==null)
        {
            return null;
        }
        if(len<0)
        {
            return EMPTYxSTRING;
        }
        if(str.length()<=len)
        {
            return str;
        }
        return str.substring(0,len);
    }
    
    public static String right(String str,int len)
    {
        if(str==null)
        {
            return null;
        }
        if(len<0)
        {
            return EMPTYxSTRING;
        }
        if(str.length()<=len)
        {
            return str;
        }
        return str.substring(str.length()-len);
    }
    
    public static String removeStart(String str,String remove)
    {
        if(isEmpty(str)||isEmpty(remove))
        {
            return str;
        }
        if(str.startsWith(remove))
        {
            return str.substring(remove.length());
        }
        return str;
    }
    
    public static String removeStartIgnoreCase(String str,String remove)
    {
        if(isEmpty(str)||isEmpty(remove))
        {
            return str;
        }
        if(startsWithIgnoreCase(str,remove))
        {
            return str.substring(remove.length());
        }
        return str;
    }
    
    /**
     * @param str
     * @param start
     * 负数等同于传零
     * @return
     */
    public static String substring(String str,int start)
    {
        if(str==null)
        {
            return null;
        }
        if(start<0)
        {
            start=0;
        }
        if(start>str.length())
        {
            return EMPTYxSTRING;
        }
        return str.substring(start);
    }
    
    /**
     * 判断是否由ASCII码组成的字符串
     *
     * @param text
     * @return
     */
    public static boolean isMadeUpOfAscii(String text)
    {
        if(isEmpty(text))
        {
            return false;
        }
        int len=text.length();
        for(int i=0;i<len;i++)
        {
            char c=text.charAt(i);
            if(c>127)
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取指定区间的字符串
     *
     * @param str
     * @param beginStr
     * @param endStr
     * @return
     */
    public static @Nullable String stringBetween(String str,String beginStr,String endStr)
    {
        if(str==null||beginStr==null||endStr==null)
        {
            return null;
        }
        int start=str.indexOf(beginStr);
        if(start!=NOTxFOUND)
        {
            int end=str.indexOf(endStr,start+beginStr.length());
            if(end!=NOTxFOUND)
            {
                return str.substring(start+beginStr.length(),end);
            }
        }
        return null;
    }
    
    public static String toUppercase(String input)
    {
        String ret="";
        if(isNotEmpty(input))
        {
            ret=input.toUpperCase(Locale.getDefault());
        }
        return ret;
    }
    
    /**
     * 判断一个字符是否为ASCII码
     *
     * @param characters
     * @return
     */
    public static boolean isAscii(char characters)
    {
        return characters<='\u007f';
    }
    
    public static boolean detectGB2312(byte[] data)
    {
        if(null==data||0==data.length)
        {
            return false;
        }
        int indexStart=0;
        int indexEnd=data.length-1;
        while(true)
        {
            int srcCharHigh=0xFF&data[indexStart];
            int srcCharLow;
            /** 0x00~0x7F ASCII码 */
            if(srcCharHigh<0x80)
            {
                if(indexStart>=indexEnd)
                {
                    break;
                }
                indexStart++;
            }
            /** 0x80~0xA0 空码 */
            else if(srcCharHigh<0xA1)
            {
                return false;
            }
            /** 0xA1~0xA9 字符部分 */
            else if(srcCharHigh<0xAA)
            {
                if(indexStart>=indexEnd)
                {
                    return false;
                }
                srcCharLow=0xFF&data[indexStart+1];
                /** 低字节范围 0xA1~0xFE */
                if(srcCharLow<0xA1||srcCharLow>0xFE)
                {
                    return false;
                }
                if(indexStart>=indexEnd-1)
                {
                    break;
                }
                indexStart+=2;
            }
            /** 0xAA~0xAF 空码 */
            else if(srcCharHigh<0xB0)
            {
                return false;
            }
            /** 0xB0~0xF7 GB2312常见汉字表 */
            else if(srcCharHigh<0xF8)
            {
                if(indexStart>=indexEnd)
                {
                    return false;
                }
                srcCharLow=0xFF&data[indexStart+1];
                /** 低字节范围 0xA1~0xFE */
                if(srcCharLow<0xA1||srcCharLow>0xFE)
                {
                    return false;
                }
                if(indexStart>=indexEnd-1)
                {
                    break;
                }
                indexStart+=2;
            }
            /** 0xF8~0xFF 空码 */
            else
            {
                return false;
            }
        }
        return true;
    }
    
    public static boolean detectUtf8(byte[] data)
    {
        if(null==data||0==data.length)
        {
            return false;
        }
        int checkLen=0;
        int seqLen=0;
        int index=0;
        int oldIndex=0;
        int checkChar=0;
        int srcChar=0;
        while(true)
        {
            srcChar=0xFF&data[index];
            if((srcChar&0x80)==0)
            {
                seqLen=1;
            }
            else if((srcChar&0xC0)!=0xC0)
            {
                seqLen=0;
            }
            else if((srcChar&0xE0)==0xC0)
            {
                seqLen=2;
            }
            else if((srcChar&0xF0)==0xE0)
            {
                seqLen=3;
            }
            else if((srcChar&0xF8)==0xF0)
            {
                seqLen=4;
            }
            else if((srcChar&0xFC)==0xF8)
            {
                seqLen=5;
            }
            else if((srcChar&0xFE)==0xFC)
            {
                seqLen=6;
            }
            if(0==seqLen)
            {
                return false;
            }
            checkLen=seqLen;
            oldIndex=index;
            checkChar=0;
            /** 检查UTF格式 */
            index+=seqLen;
            if(index>data.length)
            {
                return false;
            }
            /** 六字节 */
            if(checkLen==6)
            {
                checkChar=0xFF&data[oldIndex+5];
                if(checkChar<0x80||checkChar>0xBF)
                {
                    return false;
                }
                checkLen--;
            }
            /** 五字节 */
            if(checkLen==5)
            {
                checkChar=0xFF&data[oldIndex+4];
                if(checkChar<0x80||checkChar>0xBF)
                {
                    return false;
                }
                checkLen--;
            }
            /** 四字节 */
            if(checkLen==4)
            {
                checkChar=0xFF&data[oldIndex+3];
                if(checkChar<0x80||checkChar>0xBF)
                {
                    return false;
                }
                checkLen--;
            }
            /** 三字节 */
            if(checkLen==3)
            {
                checkChar=0xFF&data[oldIndex+2];
                if(checkChar<0x80||checkChar>0xBF)
                {
                    return false;
                }
                checkLen--;
            }
            /** 二字节 */
            if(checkLen==2)
            {
                checkChar=0xFF&data[oldIndex+1];
                if(checkChar>0xBF)
                {
                    return false;
                }
                switch(srcChar)
                {
                    // case 0xE0: if (checkChar < 0xA0) return false;
                    // case 0xED: if (checkChar > 0x9F) return false;
                    // case 0xF0: if (checkChar < 0x90) return false;
                    // case 0xF4: if (checkChar > 0x8F) return false;
                    default:
                        if(checkChar<0x80)
                        {
                            return false;
                        }
                }
                checkLen--;
            }
            /** 一字节 */
            if(checkLen==1)
            {
                if(srcChar>=0x80&&srcChar<0xC2)
                {
                    return false;
                }
            }
            if(index==data.length)
            {
                return true;
            }
        }
    }
    
    /**
     * 检测是否是中文字符串
     *
     * @param chineseStr
     * 被检测字符串
     * @return
     */
    public static boolean isChineseCharacter(String chineseStr)
    {
        char[] charArray=chineseStr.toCharArray();
        for(int i=0;i<charArray.length;i++)
        {
            /** 是否是Unicode编码.除了<code>?</code>这个字符.这个字符要另外处理 */
            if((charArray[i]>='\u0000'&&charArray[i]<'\uFFFD')||((charArray[i]>'\uFFFD'&&charArray[i]<'\uFFFF')))
            {
                continue;
            }
            else
            {
                return false;
            }
        }
        return true;
    }
    
    public static boolean startsWith(String text,String prefix)
    {
        if(text==null||prefix==null)
        {
            return text==null&&prefix==null;
        }
        if(prefix.length()>text.length())
        {
            return false;
        }
        return text.startsWith(prefix);
    }
    
    public static String bytesToString(byte[] bytes)
    {
        if(bytes==null||bytes.length==0)
        {
            return null;
        }
        StringBuilder buf=new StringBuilder();
        for(byte b:bytes)
        {
            buf.append(String.format("%02X:",b));
        }
        if(buf.length()>0)
        {
            buf.deleteCharAt(buf.length()-1);
        }
        return buf.toString();
    }
    
    public static String getNotNullString(String text)
    {
        if(text==null)
        {
            return "";
        }
        return text;
    }
    
    /** 替换下划线并按小驼峰格式化 */
    public static @Nullable String replaceUnderLineAndHump(@Nullable String key)
    {
        if(key==null||key.isEmpty())
        {
            return key;
        }
        String[] segments=key.split("_");
        if(segments==null||segments.length==0)
        {
            if(key.contains("_"))
            {
                return "";
            }
            return key;
        }
        StringBuilder sb=new StringBuilder();
        if(!segments[0].isEmpty())
        {
            String segment=segments[0].trim();
            if(!segment.isEmpty())
            {
                sb.append(segment);
            }
        }
        for(int i=1;i<segments.length;++i)
        {
            if(segments[i].isEmpty()||segments[i].trim().isEmpty())
            {
                continue;
            }
            String first=segments[i].substring(0,1);
            if(sb.length()>0)
            {
                first=first.toUpperCase();
            }
            sb.append(first).append(segments[i].substring(1));
        }
        return sb.toString();
    }
    
    public static boolean startWidthIgnoreCase(String oriString,String startString)
    {
        if(oriString==null||startString==null)
        {
            return false;
        }
        int length=startString.length();
        if(length>oriString.length())
        {
            return false;
        }
        return startString.equalsIgnoreCase(oriString.substring(0,length));
    }
}
