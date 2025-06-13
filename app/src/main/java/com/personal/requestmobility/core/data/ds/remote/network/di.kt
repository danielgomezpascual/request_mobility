package com.personal.requestmobility.core.data.ds.remote.network


import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val moduloNetwork = module {
    single { provideHttpLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
}

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BASIC
    return logging
}

fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

    val dispatcher = Dispatcher().apply {
        // Aumentamos el número máximo de peticiones totales.
        // Pongámoslo en 500 para que coincida con tu necesidad.
        maxRequests = 500

        // Aumentamos el número máximo de peticiones por host.
        // Si todas van al mismo dominio, este es el límite más importante.
        maxRequestsPerHost = 500
    }

    val httpClient : OkHttpClient = OkHttpClient.Builder()
        .dispatcher(dispatcher)
        .addInterceptor(loggingInterceptor)
        .readTimeout(200L, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .connectTimeout(200L, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(100,3, TimeUnit.MINUTES))
        //.connectionSpecs()
        .build()


    return  httpClient
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val BASE_URL = "https://FB6A6494573A4A9187C8537186167BA0.mobile.ocp.oraclecloud.com:443/mobile/custom/ApiMaxamWS_DEV/"

    return Retrofit.Builder()
        .baseUrl(BASE_URL) // Reemplaza con tu URL base
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())

        //.addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}
