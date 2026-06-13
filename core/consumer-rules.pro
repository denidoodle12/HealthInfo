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
