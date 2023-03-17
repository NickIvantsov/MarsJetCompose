import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {

    //android ui
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"

    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    const val composeUI = "androidx.compose.ui:ui:${Versions.composeUiVersion}"
    const val composeUIToolingPreview =
        "androidx.compose.ui:ui-tooling-preview:${Versions.composeUiVersion}"
    const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.composeUiVersion}"

    const val composeMaterial = "androidx.compose.material:material:${Versions.composeMaterial}"

    //Glide
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glideCompiler}"
    const val glideOkhttp3Integration =
        "com.github.bumptech.glide:okhttp3-integration:${Versions.glideOkhttp3Integration}"

    //Retrofit
    const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.retrofit2}"

    // Moshi
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val retrofit2ConverterMoshi =
        "com.squareup.retrofit2:converter-moshi:${Versions.retrofit2ConverterMoshi}"

    //Coroutine
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"

    //Test Libs
    private val junit = "junit:junit:${Versions.junit}"
    private val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
    private val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    private val composeUITestJunit =
        "androidx.compose.ui:ui-test-junit4:${Versions.composeUiVersion}"

    //DEBUG
    val debugComposeUITooling = "androidx.compose.ui:ui-tooling:${Versions.composeUiVersion}"
    val debugComposeUITestManifest =
        "androidx.compose.ui:ui-test-manifest:${Versions.composeUiVersion}"

    //DI
    val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"

    // ViewModel
    val lifecycleViewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"

    // ViewModel utilities for Compose
    val lifecycleViewmodelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleVersion}"

    // LiveData
    val lifecycleLivedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}"

    // Lifecycles only (without ViewModel or LiveData)
    val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
    val lifecycleRuntimeCompose = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycleVersion}"

    // Saved state module for ViewModel
    val lifecycleViewmodelSavedstate =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycleVersion}"

    // Annotation processor
    val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycleVersion}"

    //Coil
    val coil = "io.coil-kt:coil-compose:${Versions.coil}"

    val appLibraries = arrayListOf<String>().apply {
        add(coreKtx)
        add(activityCompose)
        add(composeUI)
        add(composeUIToolingPreview)
        add(uiTooling)
        add(composeMaterial)
        add(glide)
        add(glideOkhttp3Integration)
        add(retrofit2)
        add(moshi)
        add(retrofit2ConverterMoshi)
        add(coroutines)
        add(coroutinesCore)
        add(hiltAndroid)
        add(lifecycleViewmodelKtx)
        add(lifecycleViewmodelCompose)
        add(lifecycleLivedataKtx)
        add(lifecycleRuntimeKtx)
        add(lifecycleViewmodelSavedstate)
        add(lifecycleRuntimeCompose)
        add(coil)
    }

    val annotationProcessorLibs = arrayListOf<String>().apply {
        add(glideCompiler)
    }
    val kaptLibs = arrayListOf<String>().apply {
        add(glideCompiler)
        add(hiltCompiler)
        add(lifecycleCompiler)
    }
    val androidTestLibraries = arrayListOf<String>().apply {
        add(extJUnit)
        add(espressoCore)
        add(composeUITestJunit)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(junit)
    }

    val debugComposeLibraries = arrayListOf<String>().apply {
        add(debugComposeUITooling)
        add(debugComposeUITestManifest)
    }

}

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}

fun DependencyHandler.annotationProcessor(list: List<String>) {
    list.forEach { dependency ->
        add("annotationProcessor", dependency)
    }
}

fun DependencyHandler.debugImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("annotationProcessor", dependency)
    }
}