# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-injars libs
-keepattributes *Annotation*, InnerClasses, Exceptions, Signature
-keepattributes EnclosingMethod
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Keep Elasticode SDK libs
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-keep class com.squareup.picasso.** { *; }
-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.LruCache { *; }
-dontwarn com.squareup.picasso.LruCache

-dontwarn com.google.gson.**
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-dontwarn okio.**
-keep class okio.** { *; }
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

-dontwarn com.jakewharton.picasso.**
-keep class com.jakewharton.picasso.** { *; }

# Keep SDK
-keep class com.elasticode.network.** { *; }
-dontwarn com.elasticode.network.**
-keep class com.elasticode.provider.** { *; }
-dontwarn com.elasticode.provider.**
-keep class com.elasticode.model.** { *; }
-dontwarn com.elasticode.model.**
-keep class com.elasticode.utils.** { *; }
-dontwarn com.elasticode.utils.**
-keep class com.elasticode.view.** { *; }
-dontwarn com.elasticode.view.**