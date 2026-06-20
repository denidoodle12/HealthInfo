# ProGuard rules for :core library module.
# Note: consumer-rules.pro is applied to consuming modules (app, favorite).
# This file applies only to the :core module itself during its own minification.

# ========================
# Gson — keep response models
# ========================
-keep class com.expert.healthinfo.core.data.source.remote.response.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# ========================
# Room — keep entity classes
# ========================
-keep @androidx.room.Entity class *
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# ========================
# Retrofit
# ========================
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepattributes Exceptions

# ========================
# OkHttp
# ========================
-dontwarn okhttp3.**
-dontwarn okio.**

# ========================
# Koin
# ========================
-keep class org.koin.** { *; }
-keepnames class org.koin.**
-dontwarn org.koin.**

# ========================
# Domain model (Parcelable)
# ========================
-keep class com.expert.healthinfo.core.domain.model.** { *; }

# ========================
# SQLCipher — Database Encryption
# ========================
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }
-dontwarn net.sqlcipher.**

# ========================
# AndroidX Security Crypto — EncryptedSharedPreferences
# ========================
-keep class androidx.security.crypto.** { *; }
-dontwarn androidx.security.crypto.**

# ========================
# Coroutines
# ========================
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-dontwarn kotlinx.coroutines.**

# ========================
# Kotlin
# ========================
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# Preserve stack trace line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile