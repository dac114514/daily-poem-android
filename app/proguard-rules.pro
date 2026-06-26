# ProGuard / R8 rules for 每日诗文

# Keep line numbers for crash logs
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.flow.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Gson (no reflection-based adapters used directly, but keep generic signature info)
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class com.claude.poem.data.repository.** { *; }

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Material Icons Extended - keep all extended icon classes referenced via reflection in some build configs
-keep class androidx.compose.material.icons.** { *; }

# Keep our Application/Activity entry points
-keep class com.claude.poem.MainActivity { *; }
-keep class com.claude.poem.data.model.** { *; }
-keep class com.claude.poem.data.local.** { *; }

# Avoid stripping data classes used by Gson via @SerializedName
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
