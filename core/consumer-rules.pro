# Consumer ProGuard rules for the :core library module.
# These rules are applied to any module that depends on :core.

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

# ========================
# Koin
# ========================
-keep class org.koin.** { *; }
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
# Core classes referenced by app module (fix Missing classes R8 error)
# ========================
# Result sealed class + subclasses
-keep class com.expert.healthinfo.core.data.Result { *; }
-keep class com.expert.healthinfo.core.data.Result$* { *; }

# Koin DI module (accessed by name at runtime)
-keep class com.expert.healthinfo.core.di.CoreModuleKt { *; }
-keep class com.expert.healthinfo.core.di.** { *; }

# Domain layer
-keep interface com.expert.healthinfo.core.domain.repository.** { *; }
-keep class com.expert.healthinfo.core.domain.usecase.** { *; }

# UI adapter
-keep class com.expert.healthinfo.core.ui.HealthAdapter { *; }
-keep class com.expert.healthinfo.core.ui.** { *; }

# ========================
# ViewBinding
# ========================
-keep class androidx.viewbinding.** { *; }
-keep interface androidx.viewbinding.** { *; }

-dontwarn java.lang.invoke.StringConcatFactory
