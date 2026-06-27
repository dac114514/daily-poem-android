plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.claude.poem"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.claude.poem"
        minSdk = 24
        targetSdk = 36
        versionCode = 4
        versionName = "1.3"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Only force the ARM64 AAPT2 classifier when actually building on Linux ARM64
// (Termux/Proot). On x86_64 hosts (incl. GitHub Actions Ubuntu runners) and
// Windows/macOS, leave the default classifier in place.
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "com.android.tools.build" && requested.name == "aapt2") {
            val osArch = System.getProperty("os.arch")
            val osName = System.getProperty("os.name").orEmpty().lowercase()
            if (osArch == "aarch64" && osName.contains("linux")) {
                useTarget("com.android.tools.build:aapt2:${'$'}{requested.version}:linux-aarch64")
            }
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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.gson)
    debugImplementation(libs.androidx.ui.tooling)
}
