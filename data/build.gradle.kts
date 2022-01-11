plugins {
    Sugar.include(this, Plugin.androidLibrary)
    Sugar.include(this, Plugin.kotlinAndroid)
    Sugar.include(this, Plugin.kotlinKapt)
    Sugar.include(this, Plugin.hilt)
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

    flavorDimensions.add("version")
    productFlavors {
        create("publish") {
            dimension = "version"
        }
        create("developing") {
            dimension = "version"
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