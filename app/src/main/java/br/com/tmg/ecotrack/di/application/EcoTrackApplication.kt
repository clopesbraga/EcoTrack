package br.com.tmg.ecotrack.di.application

import android.app.Application
import br.com.tmg.ecotrack.di.module.appmodule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class EcoTrackApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@EcoTrackApplication)
            modules(appmodule)
        }

    }

}
