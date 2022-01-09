object Dependency {
    val gradle by lazy { "com.android.tools.build:gradle:${DependencyVersion.GRADLE}" }
    val kotlinPlugin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${DependencyVersion.KOTLIN}" }
    val hiltPlugin by lazy { "com.google.dagger:hilt-android-gradle-plugin:${DependencyVersion.HILT}" }
    val googleServices by lazy { "com.google.gms:google-services:${DependencyVersion.GOOGLE_SERVICES}" }
    val firebaseCrashlyticsGradle by lazy { "com.google.firebase:firebase-crashlytics-gradle:${DependencyVersion.FIREBASE_CRASHLYTICS}" }

    val kotlinCoroutines by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${DependencyVersion.KOTLIN_COROUTINES}" }

    val googlePlayCore by lazy { "com.google.android.play:core-ktx:${DependencyVersion.GOOGLE_PLAY_CORE}" }

    val dagger by lazy { "com.google.dagger:dagger:${DependencyVersion.HILT}" }
    val daggerCompiler by lazy { "com.google.dagger:dagger-compiler:${DependencyVersion.HILT}" }

    val hilt by lazy { "com.google.dagger:hilt-android:${DependencyVersion.HILT}" }
    val hiltCompiler by lazy { "com.google.dagger:hilt-android-compiler:${DependencyVersion.HILT}" }

    val gson by lazy { "com.google.code.gson:gson:${DependencyVersion.GSON}" }
    val retrofit by lazy { "com.squareup.retrofit2:retrofit:${DependencyVersion.RETROFIT}" }
    val retrofitConverterGson by lazy { "com.squareup.retrofit2:converter-gson:${DependencyVersion.RETROFIT}" }
    val picasso by lazy { "com.squareup.picasso:picasso:${DependencyVersion.PICASSO}" }
    val picassoTransformations by lazy { "jp.wasabeef:picasso-transformations:${DependencyVersion.PICASSO_TRANSFORMATIONS}" }

    val timber by lazy { "com.jakewharton.timber:timber:${DependencyVersion.TIMBER}" }
    val chuckerDebug by lazy { "com.github.chuckerteam.chucker:library:${DependencyVersion.CHUCKER}" }
    val chuckerRelease by lazy { "com.github.chuckerteam.chucker:library-no-op:${DependencyVersion.CHUCKER}" }

    val konfetti by lazy { "nl.dionsegijn:konfetti:${DependencyVersion.KONFETTI}" }
    val shimmer by lazy { "com.facebook.shimmer:shimmer:${DependencyVersion.SHIMMER}" }

    val firebaseBom by lazy { "com.google.firebase:firebase-bom:${DependencyVersion.FIREBASE_BOM}" }
    val firebaseCore by lazy { "com.google.firebase:firebase-core" }
    val firebaseCrashlytics by lazy { "com.google.firebase:firebase-crashlytics-ktx" }
    val firebaseAnalytics by lazy { "com.google.firebase:firebase-analytics-ktx" }

    val androidXDatastoreCore by lazy { "androidx.datastore:datastore-preferences-core:${DependencyVersion.ANDROIDX_DATASTORE}" }
    val androidXDatastore by lazy { "androidx.datastore:datastore-preferences:${DependencyVersion.ANDROIDX_DATASTORE}" }
    val androidXCore by lazy { "androidx.core:core-ktx:${DependencyVersion.ANDROIDX_CORE}" }
    val androidXAppCompat by lazy { "androidx.appcompat:appcompat:${DependencyVersion.ANDROIDX_APPCOMPAT}" }
    val androidXConstraintLayout by lazy { "androidx.constraintlayout:constraintlayout:${DependencyVersion.ANDROIDX_CONSTRAINTLAYOUT}" }
    val androidXLifecycle by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:${DependencyVersion.ANDROIDX_LIFECYCLE}" }
    val androidXLifecycleViewModel by lazy { "androidx.lifecycle:lifecycle-viewmodel-ktx:${DependencyVersion.ANDROIDX_LIFECYCLE}" }
    val androidXFragment by lazy { "androidx.fragment:fragment-ktx:${DependencyVersion.ANDROIDX_FRAGMENT}" }
    val androidXNavigationFragment by lazy { "androidx.navigation:navigation-fragment-ktx:${DependencyVersion.ANDROIDX_NAVIGATION}" }
    val androidXNavigation by lazy { "androidx.navigation:navigation-ui-ktx:${DependencyVersion.ANDROIDX_NAVIGATION}" }
    val androidXLegacy by lazy { "androidx.legacy:legacy-support-v4:${DependencyVersion.ANDROIDX_LEGACY}" }
    val androidXPaging by lazy { "androidx.paging:paging-runtime-ktx:${DependencyVersion.ANDROIDX_PAGING}" }
    val androidXViewPager2 by lazy { "androidx.viewpager2:viewpager2:${DependencyVersion.ANDROIDX_VIEWPAGER_2}" }

    val material by lazy { "com.google.android.material:material:${DependencyVersion.MATERIAL}" }
}