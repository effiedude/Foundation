package com.townspriter.base.foundation.utils.device;

import java.io.IOException;
import android.text.TextUtils;

/******************************************************************************
 * @path Foundation:PhoneType
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public enum PhoneType
{
    XIAOMI("xiaomi"),HUAWEI("huawei"),HONOR("honor"),SAMSUNG("samsung"),MEIZU("meizu"),VIVO("vivo"),COOLPAD("coolpad"),OPPO("oppo"),MOTO("moto"),NUBIA("nubia"),UNKNOWN("unknown");
    
    private final String mPhoneTypeName;
    private String mVersionName;
    
    PhoneType(String name)
    {
        mPhoneTypeName=name.toLowerCase();
    }
    
    public static PhoneType getPhoneTypeByBrand(String brand)
    {
        PhoneType result=null;
        if(TextUtils.isEmpty(brand))
        {
            return UNKNOWN;
        }
        brand=brand.toLowerCase();
        for(PhoneType phoneType:PhoneType.values())
        {
            String phoneTypeName=phoneType.getPhoneTypeName();
            if(brand.contains(phoneTypeName))
            {
                result=phoneType;
                break;
            }
        }
        if(result==XIAOMI)
        {
            try
            {
                String KEYxMIUIxVERSIONxNAME="ro.miui.ui.version.name";
                PhoneTypeUtil.BuildProperties prop=PhoneTypeUtil.BuildProperties.getInstance();
                String name=prop.getProperty(KEYxMIUIxVERSIONxNAME,"");
                if(!TextUtils.isEmpty(name))
                {
                    result=XIAOMI;
                    result.setVersionName(name);
                }
            }
            catch(IOException ioException)
            {
                result=UNKNOWN;
                ioException.printStackTrace();
            }
        }
        if(result==null)
        {
            result=UNKNOWN;
        }
        return result;
    }
    
    public String getPhoneTypeName()
    {
        return mPhoneTypeName;
    }
    
    public String getVersionName()
    {
        return mVersionName;
    }
    
    public void setVersionName(String versionName)
    {
        mVersionName=versionName;
    }
}
