package com.easyback.manager.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;

import java.util.List;
import java.util.Map;

/**
 * 构建请求的Plist
 *
 * @Author: zhuangqingdian
 * @Date:2023/8/2
 */
public class ReqPlistBuilder {

    /**
     * 获取访问限制APP PLIST
     *
     * @return
     */
    public static String getRestrictApp(List<String> allowedAppBundleIds, List<String> disAllowedAppBundleIds) {
        //白名单应用
        StringBuffer whiteList = new StringBuffer();
        if (CollectionUtil.isNotEmpty(allowedAppBundleIds)) {
            whiteList.append("<key>whitelistedAppBundleIDs</key>");
            whiteList.append("<array>");
            for (String bundleIds : allowedAppBundleIds) {
                whiteList.append("<string>" + bundleIds + "</string>");
            }
            whiteList.append("</array>");
        }
        //黑名单应用
        StringBuffer blackList = new StringBuffer();
        if (CollectionUtil.isNotEmpty(disAllowedAppBundleIds)) {
            blackList.append("<key>blacklistedAppBundleIDs</key>");
            blackList.append("<array>");
            for (String bundleIds : disAllowedAppBundleIds) {
                blackList.append("<string>" + bundleIds + "</string>");
            }
            blackList.append("</array>");
        }

        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        sb.append("<plist version=\"1.0\">");
        sb.append("<dict>");
        sb.append("<key>PayloadContent</key>");
        sb.append("<array>");
        sb.append("<dict>");
        sb.append("<key>PayloadDescription</key>");
        sb.append("<string>APP访问限制</string>");
        sb.append("<key>PayloadDisplayName</key>");
        sb.append("<string>APP访问限制</string>");
        sb.append("<key>PayloadIdentifier</key>");
        sb.append("<string>com.apple.applicationaccess.5AE0808B-282C-4F4D-90AD-F288589FFD3D</string>");
        sb.append("<key>PayloadType</key>");
        sb.append("<string>com.apple.applicationaccess</string>");
        sb.append("<key>PayloadUUID</key>");
        sb.append("<string>5AE0808B-282C-4F4D-90AD-F288589FFD3D</string>");
        sb.append("<key>PayloadVersion</key>");
        sb.append("<integer>1</integer>");

        sb.append(whiteList);
        sb.append(blackList);

        sb.append("</dict>");
        sb.append("</array>");
        sb.append("<key>PayloadDisplayName</key>");
        sb.append("<string>APP限制</string>");
        sb.append("<key>PayloadIdentifier</key>");
        sb.append("<string>zqdfounddeMacBook-Pro.FD6A1F73-18B5-4A10-BD77-06ED33C9194C</string>");
        sb.append("<key>PayloadRemovalDisallowed</key>");
        sb.append("<true/>");//不允许用户删除
        sb.append("<key>PayloadType</key>");
        sb.append("<string>Configuration</string>");
        sb.append("<key>PayloadUUID</key>");
        sb.append("<string>6A8A605C-4BC5-40ED-83FB-C572E8235594</string>");
        sb.append("<key>PayloadVersion</key>");
        sb.append("<integer>1</integer>");
        sb.append("</dict>");
        sb.append("</plist>");
        return sb.toString();
    }

    /**
     * 安装描述文件 PLIST
     *
     * @param commandUUID
     * @param xml
     * @return
     */
    public static String getInstallProfile(String commandUUID, String xml) {
        //base64 编码
        String encode = Base64.encode(xml);
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        sb.append("<plist version=\"1.0\">");
        sb.append("<dict>");
        sb.append("<key>Command</key>");
        sb.append("<dict>");
        sb.append("<key>Payload</key>");
        sb.append("<data>");
        sb.append(encode);
        sb.append("</data>");
        sb.append("<key>RequestType</key>");
        sb.append("<string>InstallProfile</string>");
        sb.append("</dict>");
        sb.append("<key>CommandUUID</key>");
        sb.append("<string>" + commandUUID + "</string>");
        sb.append("</dict>");
        sb.append("</plist>");
        return sb.toString();
    }

    /**
     * 获取设备信息PList
     *
     * @param commandUUID
     * @return
     */
    public static String getDeviceInfoPList(String commandUUID) {
        StringBuffer backString = new StringBuffer();
        backString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        backString.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        backString.append("<plist version=\"1.0\">");
        backString.append("<dict>");
        backString.append("<key>Command</key>");
        backString.append("<dict>");
        backString.append("<key>Queries</key>");
        backString.append("<array>");
        backString.append("<string>UDID</string>");
//        backString.append("<string>Languages</string>");
//        backString.append("<string>Locales</string>");
//        backString.append("<string>DeviceID</string>");
//        backString.append("<string>OrganizationInfo</string>");
//        backString.append("<string>LastCloudBackupDate</string>");
//        backString.append("<string>AwaitingConfiguration</string>");
//        backString.append("<string>MDMOptions</string>");
//        backString.append("<string>iTunesStoreAccountIsActive</string>");
//        backString.append("<string>iTunesStoreAccountHash</string>");
//        backString.append("<string>DeviceName</string>");
        backString.append("<string>OSVersion</string>");
//        backString.append("<string>BuildVersion</string>");
//        backString.append("<string>ModelName</string>");
//        backString.append("<string>Model</string>");
//        backString.append("<string>ProductName</string>");
//        backString.append("<string>SerialNumber</string>");
//        backString.append("<string>DeviceCapacity</string>");
//        backString.append("<string>AvailableDeviceCapacity</string>");
//        backString.append("<string>BatteryLevel</string>");
//        backString.append("<string>CellularTechnology</string>");
//        backString.append("<string>ICCID</string>");
//        backString.append("<string>BluetoothMAC</string>");
//        backString.append("<string>WiFiMAC</string>");
//        backString.append("<string>EthernetMACs</string>");
//        backString.append("<string>CurrentCarrierNetwork</string>");
//        backString.append("<string>SubscriberCarrierNetwork</string>");
//        backString.append("<string>CurrentMCC</string>");
//        backString.append("<string>CurrentMNC</string>");
//        backString.append("<string>SubscriberMCC</string>");
//        backString.append("<string>SubscriberMNC</string>");
//        backString.append("<string>SIMMCC</string>");
//        backString.append("<string>SIMMNC</string>");
//        backString.append("<string>SIMCarrierNetwork</string>");
//        backString.append("<string>CarrierSettingsVersion</string>");
//        backString.append("<string>PhoneNumber</string>");
//        backString.append("<string>DataRoamingEnabled</string>");
//        backString.append("<string>VoiceRoamingEnabled</string>");
//        backString.append("<string>PersonalHotspotEnabled</string>");
//        backString.append("<string>IsRoaming</string>");
        backString.append("<string>IMEI</string>");
        backString.append("<string>MEID</string>");
//        backString.append("<string>ModemFirmwareVersion</string>");
//        backString.append("<string>IsSupervised</string>");
//        backString.append("<string>IsDeviceLocatorServiceEnabled</string>");
//        backString.append("<string>IsActivationLockEnabled</string>");
//        backString.append("<string>IsDoNotDisturbInEffect</string>");
//        backString.append("<string>EASDeviceIdentifier</string>");
//        backString.append("<string>IsCloudBackupEnabled</string>");
//        backString.append("<string>OSUpdateSettings</string>");
//        backString.append("<string>LocalHostName</string>");
//        backString.append("<string>HostName</string>");
//        backString.append("<string>CatalogURL</string>");
//        backString.append("<string>IsDefaultCatalog</string>");
//        backString.append("<string>PreviousScanDate</string>");
//        backString.append("<string>PreviousScanResult</string>");
//        backString.append("<string>PerformPeriodicCheck</string>");
//        backString.append("<string>AutomaticCheckEnabled</string>");
//        backString.append("<string>BackgroundDownloadEnabled</string>");
//        backString.append("<string>AutomaticAppInstallationEnabled</string>");
//        backString.append("<string>AutomaticOSInstallationEnabled</string>");
//        backString.append("<string>AutomaticSecurityUpdatesEnabled</string>");
//        backString.append("<string>OSUpdateSettings</string>");
//        backString.append("<string>LocalHostName</string>");
//        backString.append("<string>HostName</string>");
//        backString.append("<string>IsMultiUser</string>");
//        backString.append("<string>IsMDMLostModeEnabled</string>");
//        backString.append("<string>MaximumResidentUsers</string>");
//        backString.append("<string>PushToken</string>");
//        backString.append("<string>DiagnosticSubmissionEnabled</string>");
//        backString.append("<string>AppAnalyticsEnabled</string>");
//        backString.append("<string>IsNetworkTethered</string>");
//        backString.append("<string>ServiceSubscriptions</string>");
        backString.append("</array>");
        backString.append("<key>RequestType</key>");
        backString.append("<string>DeviceInformation</string>");
        backString.append("</dict>");
        backString.append("<key>CommandUUID</key>");
        backString.append("<string>" + commandUUID + "</string>");
        backString.append("</dict>");
        backString.append("</plist>");
        return backString.toString();
    }

    /**
     * 发送Setting的pList格式的模板文件
     * 示例：壁纸https://developer.apple.com/documentation/devicemanagement/settingscommand/command/settings/wallpaper?language=objc
     *
     * @return
     */
    public static String getSettingPList(String commandUUID, StringBuffer settingStr) {
        StringBuffer paramString = new StringBuffer();
        StringBuffer backString = new StringBuffer();
        backString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        backString.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        backString.append("<plist version=\"1.0\">");
        backString.append("<dict>");
        backString.append("<key>Command</key>");
        backString.append("<dict>");
        backString.append(paramString);
        backString.append("<key>RequestType</key>");
        backString.append("<string>Settings</string>");
        backString.append("<key>Settings</key>");
        backString.append("<array>");
        backString.append("<dict>");
        backString.append(settingStr);
        backString.append("</dict>");
        backString.append("</array>");
        backString.append("</dict>");
        backString.append("<key>CommandUUID</key>");
        backString.append("<string>");
        backString.append(commandUUID);
        backString.append("</string>");
        backString.append("</dict>");
        backString.append("</plist>");
        return backString.toString();
    }

    /**
     * 发送命令的pList格式的模板文件
     *
     * @return
     */
    public static String getCommandPList(String command, String commandUUID, Map<String, String> paramMap) {
        StringBuffer paramString = new StringBuffer();
        if (CollectionUtil.isNotEmpty(paramMap)) {
            for (Map.Entry<String, String> h : paramMap.entrySet()) {
                paramString.append("<key>");
                paramString.append(h.getKey());
                paramString.append("</key>");
                paramString.append("<string>");
                paramString.append(h.getValue());
                paramString.append("</string>");
            }
        }
        StringBuffer backString = new StringBuffer();
        backString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        backString.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        backString.append("<plist version=\"1.0\">");
        backString.append("<dict>");
        backString.append("<key>Command</key>");
        backString.append("<dict>");
        backString.append(paramString);
        backString.append("<key>RequestType</key>");
        backString.append("<string>");
        backString.append(command);
        backString.append("</string>");
        backString.append("</dict>");
        backString.append("<key>CommandUUID</key>");
        backString.append("<string>");
        backString.append(commandUUID);
        backString.append("</string>");
        backString.append("</dict>");
        backString.append("</plist>");
        return backString.toString();
    }

    /**
     * 获取已安装app列表
     *
     * @param commandUUID
     * @return
     */
    public static String getInstalledApp(String commandUUID) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        sb.append("<plist version=\"1.0\">");
        sb.append("<dict>");
        sb.append("<key>Command</key>");
        sb.append("<dict>");
        sb.append("<key>ManagedAppsOnly</key>");
        sb.append(" <false/>");
        sb.append(" <key>RequestType</key>");
        sb.append("<string>InstalledApplicationList</string>");
        sb.append(" </dict>");
        sb.append("<key>CommandUUID</key>");
        sb.append("<string>"+commandUUID+"</string>");
        sb.append(" </dict>");
        sb.append("</plist>");
        return sb.toString();
    }
}
