package com.personal.requestmobility.core.room

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val moduloDatabase = module {

    val DATABASE: String = "requestmobility"

    //Room
    single {
        Room.databaseBuilder(
            androidContext(), AppDatabase::class.java,
            DATABASE
        ).fallbackToDestructiveMigration().build()
    }


}
