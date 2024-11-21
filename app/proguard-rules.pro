# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-ignorewarnings
-dontobfuscate
-dontoptimize
-dontpreverify             #忽略与dex编译器和Dalvik VM不相干的预校验
-verbose            #开启混淆记录日志，指定映射文件的名称
-printmapping proguardMapping.txt
-keepattributes *Annotation*   #保留注释，RemoteViews通常需要annotations
-keepattributes Signature      #保持泛型
-dontusemixedcaseclassnames  # 混淆时不使用大小写混合，混淆后的类名为小写
-dontskipnonpubliclibraryclasses       # 指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclassmembers  # 指定不去忽略非公共的库的类的成员
#-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends androidx.appcompat.*{*;}
#如果引用了v4或者v7包
-dontwarn android.support.*
# 混淆算法
-optimizations !code/simplification/cast,!field/*,!class/merging/*
#基本规则
#四大组件(AndroidManifest.xml清单文件中内容)等不混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.app.AppComponentFactory
-keep public class * extends androidx.core.app.CoreComponentFactory
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep class android.app.**{*;}
-keep class android.content.**{*;}
-keep class android.os.**{*;}
#自定义控件不混淆
-keep public class * extends android.view.View {
   public <init>(android.content.Context);
   public <init>(android.content.Context, android.util.AttributeSet);
   public <init>(android.content.Context, android.util.AttributeSet, int);
   public void set*(...);
}
#包含特殊参数的构造方法及其类不混淆
-keepclasseswithmembers class * {
   public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#所有的Onclick操作不混淆
-keepclassmembers class * extends android.content.Context {
  public void *(android.view.View);
  public void *(android.view.MenuItem);
}
#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
# 对于R（资源）类中的静态方法不能被混淆
-keepclassmembers class **.R$* {
 public static <fields>;
}
#Javascript接口中的方法不能混淆
-keepclassmembers class * {
  @android.webkit.JavascriptInterface <methods>;
}
# 枚举类
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#w
#序列化不能被混淆
-keep public class * implements java.io.Serializable {*;}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class com.dolphin.localsocket.bean.** {*;}
-keep class com.dolphin.localsocket.cmd.** {*;}
-keep class com.jtun.router.http.** {*;}
-keep class com.jtun.router.room.** {*;}
-keep class com.jtun.router.sms.** {*;}
-keep class com.jtun.router.net.** {*;}
-keep class kotlin.text.**{*;}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}


-dontwarn javax.annotation.**
-dontwarn javax.inject.**

# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
# RxJava RxAndroid
-keep class io.reactivex.rxjava3.**{*;}
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Gson
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

##---------------End: proguard configuration for Gson  ----------

# for DexGuard only
#-keep resourcexmlelements manifest/application/meta-data@value=GlideModule
