package com.townspriter.android.foundation.utils.lang;

import java.util.Locale;

/******************************************************************************
 * @Path Foundation:LocaleUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
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
