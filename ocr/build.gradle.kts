plugins {
    alias(libs.plugins.receiptreader.android.library)
    alias(libs.plugins.receiptreader.android.library.compose)
    alias(libs.plugins.receiptreader.hilt)
//    id("com.google.gms.google-services")
}

android {
    namespace = "com.eunsong.ocr"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        manifestPlaceholders["mlkit_vision_dependencies"] = "ocr,ocr_chinese,ocr_devanagari,ocr_japanese,ocr_korean"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.firebase.functions)
    implementation(libs.gson)

    implementation(libs.mlkit.text.recognition)
//    implementation(libs.google.vision.ai)

    ksp(libs.hilt.android.compiler)

//    // To recognize Latin script
//    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.1")
//    // To recognize Chinese script
//    implementation("com.google.android.gms:play-services-mlkit-text-recognition-chinese:16.0.1")
//    // To recognize Devanagari script
//    implementation("com.google.android.gms:play-services-mlkit-text-recognition-devanagari:16.0.1")
//    // To recognize Japanese script
//    implementation("com.google.android.gms:play-services-mlkit-text-recognition-japanese:16.0.1")
//    // To recognize Korean script
//    implementation("com.google.android.gms:play-services-mlkit-text-recognition-korean:16.0.1")
    

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}