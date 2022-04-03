plugins {
    Sugar.include(this, Plugin.androidApplication)
    Sugar.include(this, Plugin.kotlinAndroid)
    Sugar.include(this, Plugin.kotlinKapt)
    Sugar.include(this, Plugin.hilt)
    Sugar.include(this, Plugin.googleServices)
    Sugar.include(this, Plugin.firebaseCrashlytics)
}

android {
    compileSdk = 31

    val findTaskTag = listOf("assemble", "bundle")
    val typeVersion = findTaskTag.map { tag ->
        gradle.startParameter.taskRequests.first()
            .args.find { it.contains(tag) }?.let {
                it.substring(it.indexOf(tag) + tag.length).toLowerCase()
            }
    }.find { it != null }

    val assembleVersionName = if(typeVersion == null) {
        project.logger.warn("Unknown build type")
        "${ApplicationInfo.Version.MAJOR}.${ApplicationInfo.Version.MINOR}.${ApplicationInfo.Version.PATCH}"
    } else {
        "${ApplicationInfo.Version.MAJOR}.${ApplicationInfo.Version.MINOR}.${ApplicationInfo.Version.PATCH}-$typeVersion"
    }

    println("${27.toChar()}[30;46mApp Version: $assembleVersionName${27.toChar()}[0m")

    defaultConfig {
        applicationId = "ru.unit.barsdiary"
        minSdk = 26
        targetSdk = 31
        versionCode = ApplicationInfo.Version.CODE
        versionName = assembleVersionName
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
        getByName("debug") { }
    }

    flavorDimensions.add("version")
    productFlavors {
        create("publish") {
            dimension = "version"
            resValue("string", "app_name", "@string/app_name_publish")
        }
        create("developing") {
            dimension = "version"
            resValue("string", "app_name", "@string/app_name_developing")
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
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
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(platform(Dependency.firebaseBom))

    implementation(Dependency.firebaseCore)
    implementation(Dependency.firebaseCrashlytics)
    implementation(Dependency.firebaseAnalytics)

    implementation(Dependency.googlePlayCore)

    implementation(Dependency.hilt)
    kapt(Dependency.hiltCompiler)

    implementation(Dependency.konfetti)
    implementation(Dependency.shimmer)
    implementation(Dependency.picasso)
    implementation(Dependency.picassoTransformations)

    implementation(Dependency.timber)

    implementation(Dependency.androidXCore)
    implementation(Dependency.androidXAppCompat)
    implementation(Dependency.androidXConstraintLayout)
    implementation(Dependency.androidXLifecycle)
    implementation(Dependency.androidXLifecycleViewModel)
    implementation(Dependency.androidXFragment)
    implementation(Dependency.androidXNavigation)
    implementation(Dependency.androidXNavigationFragment)
    implementation(Dependency.androidXLegacy)
    implementation(Dependency.androidXPaging)
    implementation(Dependency.androidXDatastore)
    implementation(Dependency.androidXViewPager2)

    implementation(Dependency.material)
}