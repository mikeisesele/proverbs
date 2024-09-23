plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("com.diffplug.spotless")
    id("io.gitlab.arturbosch.detekt")
}

spotless {
    kotlin {
        ktlint("0.46.1")
            .editorConfigOverride(
                mapOf(
                    Pair("disabled_rules", "filename"),
                    Pair("ij_kotlin_allow_trailing_comma", true),
                    Pair("ij_kotlin_allow_trailing_comma_on_call_site", true)
                )
            )
        target("**/*.kt")
    }
    kotlinGradle {
        ktlint("0.46.1")
        target("*.gradle.kts")
    }
}

android {
    namespace = "com.michael.template"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.michael.template"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val rules = "proguard-rules.pro"
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), rules)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Compose
    val compose = "1.5.4"
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.compose.runtime:runtime:$compose")
    implementation("androidx.compose.foundation:foundation:$compose")
    implementation("androidx.compose.foundation:foundation-layout:$compose")
    implementation("androidx.compose.ui:ui-tooling:$compose")
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Material
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.compose.material3:material3:1.1.2")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-compiler:2.48.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // okhttp
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Room
    annotationProcessor("androidx.room:room-compiler:2.6.0")
    implementation("androidx.room:room-runtime:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")

    // Encrypted prefs

    implementation("androidx.security:security-crypto:1.0.0")

    // Detekt
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.21.0")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
}
