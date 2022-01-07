
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