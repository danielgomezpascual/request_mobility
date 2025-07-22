package com.personal.metricas.core.room

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val moduloDatabase = module {

    val DATABASE: String = "metricas"

    //Room
    single {
        Room.databaseBuilder(
            androidContext(), AppDatabase::class.java,
            DATABASE
        ).fallbackToDestructiveMigration().build()
    }


}
