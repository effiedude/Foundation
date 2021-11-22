package com.townspriter.base.foundation.utils.app;
/******************************************************************************
 * @path Foundation:ConfigManager
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:06:37
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class ConfigManager
{
    private static final String TAG="ConfigManager";
    private static final String NORMAL_STUDENT_ID="1266";
    private static final String NORMAL_STUDENT_NAME="布鲁克";
    private String mStudentID;
    /** 测试账号信息 */
    private String mStudentName;
    /** 是否使用三方底层接口 */
    private boolean mThirdPartyAPISwitch;
    /** 是否开启账号服务 */
    private boolean mStudentSwitch;
    /** 是否开启语音服务 */
    private boolean mVoiceSwitch;
    /** 是否开启推送服务 */
    private boolean mPushSwitch;
    /** 是否显示桌面精灵 */
    private boolean mDesktopSpriteSwitch;
    /** 是否开启内存检测服务 */
    private boolean mLeakCanarySwitch;
    
    private ConfigManager()
    {
        mStudentID=NORMAL_STUDENT_ID;
        mStudentName=NORMAL_STUDENT_NAME;
    }
    
    public static ConfigManager getInstance()
    {
        return Holder.configManager;
    }
    
    public void parse(String config)
    {}
    
    public boolean isPushSwitch()
    {
        return mPushSwitch;
    }
    
    public void setPushSwitch(boolean pushSwitch)
    {
        mPushSwitch=pushSwitch;
    }
    
    public boolean isVoiceSwitch()
    {
        return mVoiceSwitch;
    }
    
    public void setVoiceSwitch(boolean voiceSwitch)
    {
        mVoiceSwitch=voiceSwitch;
    }
    
    public boolean isStudentSwitch()
    {
        return mStudentSwitch;
    }
    
    public void setStudentSwitch(boolean studentSwitch)
    {
        mStudentSwitch=studentSwitch;
    }
    
    public boolean isThirdPartyAPISwitch()
    {
        return mThirdPartyAPISwitch;
    }
    
    public void setThirdPartyAPISwitch(boolean thirdPartyAPISwitch)
    {
        mThirdPartyAPISwitch=thirdPartyAPISwitch;
    }
    
    public boolean isDesktopSpriteSwitch()
    {
        return mDesktopSpriteSwitch;
    }
    
    public void setDesktopSpriteSwitch(boolean desktopSpriteSwitch)
    {
        mDesktopSpriteSwitch=desktopSpriteSwitch;
    }
    
    public boolean isLeakCanarySwitch()
    {
        return mLeakCanarySwitch;
    }
    
    public void setLeakCanarySwitch(boolean leakCanarySwitch)
    {
        this.mLeakCanarySwitch=leakCanarySwitch;
    }
    
    public String getStudentName()
    {
        return mStudentName;
    }
    
    public void setStudentName(String studentName)
    {
        mStudentName=studentName;
    }
    
    public String getStudentID()
    {
        return mStudentID;
    }
    
    public void setStudentID(String studentID)
    {
        mStudentID=studentID;
    }
    
    private static class Holder
    {
        private static final ConfigManager configManager=new ConfigManager();
    }
}
