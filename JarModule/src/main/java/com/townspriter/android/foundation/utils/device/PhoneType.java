package com.townspriter.android.foundation.utils.device;

import java.io.IOException;
import com.townspriter.android.foundation.utils.device.PhoneTypeUtil.BuildProperties;
import android.text.TextUtils;

/******************************************************************************
 * @Path Foundation:PhoneType
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
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
                BuildProperties prop=BuildProperties.getInstance();
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
