
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Dependency.gradle)
        classpath(Dependency.kotlinPlugin)
        classpath(Dependency.hiltPlugin)
        classpath(Dependency.googleServices)
        classpath(Dependency.firebaseCrashlyticsGradle)
    }
}

plugins {
    Sugar.include(this, Plugin.ktlint)
    Sugar.include(this, Plugin.detekt)
}

allprojects {
    buildscript {
        repositories {
            google()
            mavenCentral()
        }
    }
}

subprojects {
    apply {
        plugin(Plugin.detekt.id)
        plugin(Plugin.ktlint.id)
    }

    ktlint {
        verbose.set(true)
        android.set(true)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)
    }

    detekt {
        config = rootProject.files("detekt-config.yml")
        parallel = true
    }
}

tasks.register(name = "clean", type = Delete::class) {
    delete(rootProject.buildDir)
}