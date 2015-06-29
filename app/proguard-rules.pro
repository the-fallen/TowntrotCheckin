# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Anil\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}




-dontpreverify
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable


-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.content.Context

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet, int);
}


-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class com.flurry.** { *; }
-dontwarn com.flurry.**
-keepattributes *Annotation*,EnclosingMethod

# Google Play Services library
-keep class * extends java.util.ListResourceBundle {
protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
@com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}


# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    public *;
}

-keep public enum com.sync.model$** {
    **[] $VALUES;
    public *;
}

# Application classes that will be serialized/deserialized over Gson
-keep class com.towntrot.checkin.models.** { *; }
-keep class com.towntrot.checkin.httputils.** { *; }


-keep class com.facebook.** {
   *;
}

-keep class com.androidplot.** { *; }

-keepclassmembers class * extends com.actionbarsherlock.ActionBarSherlock {
    <init>(android.app.Activity, int);
}

# Gson specific classes
#-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-keep class com.parse.**{ *; }
-dontwarn com.parse.**
-keep public class android.net.**{ *; }
-dontwarn android.net.**

-dontwarn org.apache.http.**
-keep class org.apache.http.** { *; }


-keep class com.localytics.android.** { *; }

# Required for attribution
-keep class com.google.android.gms.ads.** { *; }

# Required for Google Play Services (see http://developer.android.com/google/play-services/setup.html)
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
    }
-keep class com.google.android.gms.gcm.**{ *; }


-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.towntrot.checkin.about_event {*;}
-keep class com.towntrot.checkin.about_list {*;}
-keep class com.towntrot.checkin.bookings {*;}
-keep class com.towntrot.checkin.Event_details {*;}
-keep class com.towntrot.checkin.List_Details {*;}
-keep class com.towntrot.checkin.Post_check {*;}
-keep class com.towntrot.checkin.User_details {*;}

#adjust.com rules
-keep class com.adjust.sdk.** { *; }
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }
#adjust.com rules


#two way view
-keep class org.lucasr.twowayview.** { *; }
#two way view


#icepick
-dontwarn icepick.**
-keep class **$$Icicle { *; }
-keepnames class * { @icepick.Icicle *;}
-keepclasseswithmembernames class * {
    @icepick.* <fields>;
}
#icepick