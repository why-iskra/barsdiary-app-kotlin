plugins {
    Sugar.include(this, Plugin.javaLibrary)
    Sugar.include(this, Plugin.kotlin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(Dependency.dagger)
    annotationProcessor(Dependency.daggerCompiler)
    implementation(Dependency.kotlinCoroutines)
}