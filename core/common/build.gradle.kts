plugins {
    alias(libs.plugins.receiptreader.android.library)
//    alias(libs.plugins.receiptreader.jvm.library)
//    alias(libs.plugins.receiptreader.hilt)
}

android {
    namespace = "com.eunsong.receiptreader.core.common"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(libs.timber)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

//    prodImplementation(platform(libs.firebase.bom))
//    prodImplementation(libs.firebase.analytics)
}