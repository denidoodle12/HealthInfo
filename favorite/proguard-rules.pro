# ProGuard rules for :favorite dynamic feature module.
# Minification is handled by the base :app module.
# These rules ensure classes needed at runtime survive obfuscation.

# ========================
# Koin — Dependency Injection
# ========================
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# ========================
# Keep FavoriteActivity (referenced via reflection from base app)
# ========================
-keep class com.expert.healthinfo.favorite.FavoriteActivity { *; }
