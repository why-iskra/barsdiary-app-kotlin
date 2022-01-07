plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 26
        targetSdk = 31

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        create("beta") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":sdk"))
    implementation(project(":domain"))

    implementation(Dependency.timber)

    debugImplementation(Dependency.chuckerDebug)
    releaseImplementation(Dependency.chuckerRelease)
    add("betaImplementation", Dependency.chuckerRelease)

    implementation(Dependency.androidXDatastoreCore)
    implementation(Dependency.androidXDatastore)

    implementation(Dependency.retrofit)

    implementation(Dependency.gson)

    implementation(Dependency.hilt)
    kapt(Dependency.hiltCompiler)
}