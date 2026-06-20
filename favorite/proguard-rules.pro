# ProGuard rules for :favorite dynamic feature module.
# Minification is handled by the base :app module.
# These rules ensure classes needed at runtime survive obfuscation.

# ========================
# Keep all classes in this module
# ========================
-keep class com.expert.healthinfo.favorite.** { *; }
-keepnames class com.expert.healthinfo.favorite.**

# ========================
# Koin — Dependency Injection
# ========================
-keep class org.koin.** { *; }
-keepnames class org.koin.**
-dontwarn org.koin.**

# ========================
# ViewModels — kept for Koin viewModel injection
# ========================
-keep class * extends androidx.lifecycle.ViewModel { *; }
