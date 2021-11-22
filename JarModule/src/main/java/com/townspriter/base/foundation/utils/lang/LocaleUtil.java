package com.townspriter.base.foundation.utils.lang;

import java.util.Locale;

/******************************************************************************
 * @path Foundation:LocaleUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class LocaleUtil
{
    public static String getSystemCountry()
    {
        return Locale.getDefault().getCountry();
    }
    
    public static String getSystemLanguage()
    {
        return Locale.getDefault().getLanguage();
    }
}
