# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/wangdd/Android/Sdk/android-sdk-linux/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# Umeng
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class org.wdd.app.android.seedoctor.R$*{
    public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep class com.umeng.analytics.**{*;}
-keep class u.aly.**{*;}

#iflytek
-dontwarn com.iflytek.**
-keepattributes Signature
-keep class com.iflytek.**{*;}

# 广点通联盟
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.app.NotificationCompat**{
    public *;
}

-keep class com.alibaba.sdk.android.httpdns.**{*;}
-keep class cn.bmob.v3.**{*;}

-dontwarn org.apache.http.**
-dontwarn rx.internal.util.unsafe.**
-dontwarn okio.**
-dontwarn com.androidquery.auth.**
-dontwarn com.alibaba.fastjson.**
-dontwarn cn.bmob.v3.helper.**

# 高的地图混淆
# 3D地图
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
-keep class com.amap.api.trace.**{*;}

# 定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

# 搜索
-keep class com.amap.api.services.**{*;}

# 导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}