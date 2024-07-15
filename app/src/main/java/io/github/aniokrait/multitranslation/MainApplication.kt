package io.github.aniokrait.multitranslation

import android.app.Application
import com.google.android.gms.ads.MobileAds
import io.github.aniokrait.multitranslation.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)

        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}
