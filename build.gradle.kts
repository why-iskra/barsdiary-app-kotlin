
apply(plugin = Plugin.gradleVersionsPlugin)

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.1")

        classpath(Dependency.kotlinPlugin)
        classpath(Dependency.hiltPlugin)
        classpath(Dependency.googleServices)
        classpath(Dependency.firebaseCrashlyticsGradle)
        classpath(Dependency.gradleVersionsPlugin)
    }
}

allprojects {
    buildscript {
        repositories {
            google()
            mavenCentral()
        }
    }
}

tasks.register(name = "clean", type = Delete::class) {
    delete(rootProject.buildDir)
}