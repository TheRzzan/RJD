package com.morozov.rjd

import android.app.Application
import com.morozov.rjd.di.data.DaggerDataComponent
import com.morozov.rjd.di.data.DataComponent
import com.morozov.rjd.di.data.DataModule

class DefaultApplication: Application() {

    companion object {
        lateinit var dataComponent: DataComponent
    }

    override fun onCreate() {
        super.onCreate()

        dataComponent = DaggerDataComponent
                        .builder()
                        .dataModule(DataModule(applicationContext))
                        .build()
    }
}