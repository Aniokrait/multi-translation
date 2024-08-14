import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "io.github.aniokrait.multitranslation"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.aniokrait.multitranslation"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val prop =
            Properties().apply {
                load(FileInputStream(File(rootProject.rootDir, "local.properties")))
            }
        buildConfigField(
            type = "String",
            name = "SLACK_API_KEY",
            value = prop.getProperty("slack_api_key"),
        )
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            ndkVersion = "27.0.12077973"
            ndk {
                debugSymbolLevel = "FULL"
            }
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // collectAsStateWithLifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)

    // navigation-compose
    implementation(libs.androidx.navigation.compose)

    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    // MLKit Translate
    implementation(libs.mlkit.translate)
    // kotlinx-coroutines-play-services
    implementation(libs.kotlinx.coroutines.play.services)

    // mockk
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk)

    // kotlinx-coroutines-test
    testImplementation(libs.kotlinx.coroutines.test)

    // Data store
    implementation(libs.androidx.datastore.preferences)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.androidx.compose)

    // Lottie
    implementation(libs.lottie.compose)

    // Firebase
    implementation(platform(libs.firebase.bom))
    // Analytics
    implementation(libs.firebase.analytics)
    // Admob
    implementation(libs.play.services.ads)

    // Timber
    implementation(libs.timber)

    // Ktor
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)
}
