package com.expert.healthinfo.core.di

import android.content.Context
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.expert.healthinfo.core.BuildConfig
import com.expert.healthinfo.core.data.HealthRepository
import com.expert.healthinfo.core.data.source.local.LocalDataSource
import com.expert.healthinfo.core.data.source.local.room.HeadlinesDatabase
import com.expert.healthinfo.core.data.source.remote.RemoteDataSource
import com.expert.healthinfo.core.data.source.remote.network.ApiService
import com.expert.healthinfo.core.domain.repository.IheadlinesRepository
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

import android.provider.Settings

private const val DB_PASSPHRASE_KEY = "db_passphrase"
private const val PREFS_FILE_NAME = "health_info_secure_prefs"

/**
 * Membuat atau mengambil passphrase database dari EncryptedSharedPreferences.
 * Passphrase di-generate sekali dan disimpan secara aman menggunakan AES-256-GCM.
 *
 * Fallback: jika EncryptedSharedPreferences gagal (misal Keystore error pada device tertentu),
 * gunakan ANDROID_ID sebagai passphrase deterministik agar app tetap berjalan.
 */
private fun getOrCreatePassphrase(context: Context): CharArray {
    return try {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val encryptedPrefs = EncryptedSharedPreferences.create(
            context,
            PREFS_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        if (encryptedPrefs.contains(DB_PASSPHRASE_KEY)) {
            encryptedPrefs.getString(DB_PASSPHRASE_KEY, null)!!.toCharArray()
        } else {
            // Generate passphrase acak 32 karakter
            val charset = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            val passphrase = (1..32).map { charset.random() }.joinToString("")
            encryptedPrefs.edit().putString(DB_PASSPHRASE_KEY, passphrase).apply()
            passphrase.toCharArray()
        }
    } catch (e: Exception) {
        // Fallback: gunakan ANDROID_ID sebagai passphrase (deterministik per device)
        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "healthinfo_fallback_key"
        androidId.toCharArray()
    }
}

val databaseModule = module {
    factory {
        get<HeadlinesDatabase>().headlinesDao()
    }

    single {
        // Passphrase diambil dari EncryptedSharedPreferences
        val passphrase = getOrCreatePassphrase(androidContext())
        val factory = SupportFactory(SQLiteDatabase.getBytes(passphrase))

        Room.databaseBuilder(
            androidContext(),
            HeadlinesDatabase::class.java,
            // Nama baru "Headlines_secure.db" agar SQLCipher membuat
            // database terenkripsi baru, bukan mencoba membuka file
            // lama (unencrypted) yang menyebabkan crash.
            "Headlines_secure.db"
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }
}

val networkModule = module {
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        /**
         * Certificate Pinning untuk newsapi.org.
         *
         * Pin SHA-256 diperoleh dari SubjectPublicKeyInfo (SPKI) sertifikat aktual:
         *   - Leaf certificate    : newsapi.org (dikeluarkan oleh Google Trust Services WE1)
         *   - Intermediate CA pin : WE1 (Google Trust Services) — sebagai backup
         *
         * Sertifikat leaf berlaku hingga: 2026-08-16
         * Jika sertifikat diperbarui, update pin di sini menggunakan hash SPKI baru.
         */
        val certificatePinner = CertificatePinner.Builder()
            .add("newsapi.org", "sha256/E0r7F+QaAEvA6I0FW2dPlSwKcYWogdJyri+MYHIRMN4=") // Leaf
            .add("newsapi.org", "sha256/H7AMYAvicN2+UcFPBz3kJXCDmGrTItZh4ujUBK8hoWg=") // Intermediate (WE1)
            .build()

        OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repoModule = module {
    single {
        LocalDataSource(get())
    }

    single {
        RemoteDataSource(get())
    }

    single<IheadlinesRepository> {
        HealthRepository(get(), get())
    }
}