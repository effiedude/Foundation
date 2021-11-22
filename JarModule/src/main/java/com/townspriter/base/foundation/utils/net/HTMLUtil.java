package com.townspriter.base.foundation.utils.net;
/******************************************************************************
 * @path Foundation:HTMLUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class HTMLUtil
{
    public static String getBodyContent(String html)
    {
        String bodyContent=null;
        /** 对传入的参数做判空处理 */
        if(html!=null)
        {
            String bodyStartTag="<body>";
            String bodyEndTag="</body>";
            int pos1=html.indexOf(bodyStartTag);
            int pos2=html.indexOf(bodyEndTag);
            if(pos1>0&&pos2>0&&pos2>pos1)
            {
                bodyContent=html.substring(pos1+bodyStartTag.length(),pos2).trim();
            }
            else if(pos1>0&&pos2==-1)
            {
                bodyContent=html.substring(pos1+bodyStartTag.length()).trim();
            }
            else if(pos1==-1&&pos2>0)
            {
                bodyContent=html.substring(0,pos2).trim();
            }
        }
        return bodyContent;
    }
}
