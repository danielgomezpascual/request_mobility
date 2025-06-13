plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsKotlinSerialization)

    /*id("kotlin-kapt")*/
    id("com.google.devtools.ksp")
    id("androidx.room")
}

android {
    namespace = "com.personal.requestmobility"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.personal.requestmobility"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
    packagingOptions {
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE-notice.md")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE-notice")
    }



}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)


    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)




    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.20") // Reemplaza con tu versión de Kotlin


    //===========================================================
    // ========== ICONOS ANDROID ============
    implementation("androidx.compose.material:material-icons-core:1.7.8") // O la última versión estable
    implementation("androidx.compose.material:material-icons-extended:1.7.8") // O la última versión estable



    //===========================================================
    // ========== KOIN ============
    var koin_version = "3.5.0"
    implementation("io.insert-koin:koin-android:$koin_version") //Koin for Android
    //Koin Componse  y view model....
    implementation("io.insert-koin:koin-androidx-compose:$koin_version")
    implementation("io.insert-koin:koin-androidx-compose-navigation:$koin_version")

    //===========================================================
    // ========== RETROFIT ============
    var retroft_version = "2.10.0"
    implementation("com.squareup.retrofit2:retrofit:$retroft_version")
    implementation("com.squareup.retrofit2:converter-gson:$retroft_version")

    // OkHttp (para cliente HTTP, interceptores, logging, etc.)
    var okhttp_version = "4.12.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")


    // Coroutines (si planeas usarlas con Retrofit)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    //===========================================================
    // ========== ROOM ============
    val room_version = "2.6.1"
    val coroutines_version_kotlinx = "1.7.3"

/*    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    androidTestImplementation("androidx.room:room-testing:2.5.0")

*/

    implementation("androidx.room:room-runtime:$room_version")

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp("androidx.room:room-compiler:$room_version")

    // If this project only uses Java source, use the Java annotationProcessor
    // No additional plugins are necessary
    //annotationProcessor("androidx.room:room-compiler:$room_version")


    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version_kotlinx")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version_kotlinx")


    //===========================================================
    // ========== GSON ============
    var gson_version = "2.11.0"
    implementation("com.google.code.gson:gson:$gson_version")


    //===========================================================
    // ========== XML ============
    implementation("org.jsoup:jsoup:1.17.2")

    //===========================================================
    // ========== COIL ============
    implementation("io.coil-kt:coil-compose:2.6.0")


    //===========================================================
    // ========== GRAFICAS ============
    implementation("com.himanshoe:charty:2.1.0-beta03.4")

}