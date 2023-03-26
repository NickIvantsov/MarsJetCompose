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
    val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}" //kapt
    val hiltNavigationCompose =
        "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationCompose}"

    // ViewModel
    val lifecycleViewmodelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"

    // ViewModel utilities for Compose
    val lifecycleViewmodelCompose =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleVersion}"

    // LiveData
    val lifecycleLivedataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}"

    // Lifecycles only (without ViewModel or LiveData)
    val lifecycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
    val lifecycleRuntimeCompose =
        "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycleVersion}"

    // Saved state module for ViewModel
    val lifecycleViewmodelSavedstate =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycleVersion}"

    // Annotation processor
    val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycleVersion}"

    //Coil
    val coil = "io.coil-kt:coil-compose:${Versions.coil}"

    //Navigation
    val navigationCommon = "androidx.navigation:navigation-common:${Versions.navigationVersion}"
    val navigationCommonKtx =
        "androidx.navigation:navigation-common-ktx:${Versions.navigationVersion}"
    val navigationCompose = "androidx.navigation:navigation-compose:${Versions.navigationVersion}"
    val navigationRuntime = "androidx.navigation:navigation-runtime:${Versions.navigationVersion}"
    val navigationRuntimeKtx =
        "androidx.navigation:navigation-runtime-ktx:${Versions.navigationVersion}"

    //Paging
    val pagingRuntime = "androidx.paging:paging-runtime:3.1.1"
    val pagingCompose = "androidx.paging:paging-compose:1.0.0-alpha16"
    val pagingCommonKtx = "androidx.paging:paging-common-ktx:3.1.1"


    val appLibraries = listOf<String>(
        coreKtx,
        activityCompose,
        composeUI,
        composeUIToolingPreview,
        uiTooling,
        composeMaterial,
        retrofit2,
        moshi,
        retrofit2ConverterMoshi,
        coroutines,
        coroutinesCore,
        hiltAndroid,
        hiltNavigationCompose,
        lifecycleViewmodelKtx,
        lifecycleViewmodelCompose,
        lifecycleLivedataKtx,
        lifecycleRuntimeKtx,
        lifecycleViewmodelSavedstate,
        lifecycleRuntimeCompose,
        coil,
        navigationCommon,
        navigationCommonKtx,
        navigationCompose,
        navigationRuntime,
        navigationRuntimeKtx,
        pagingRuntime,
        pagingCompose,
        pagingCommonKtx
    )

    val kaptLibs = listOf<String>(hiltCompiler, lifecycleCompiler)
    val androidTestLibraries = listOf<String>(
        extJUnit,
        espressoCore,
        composeUITestJunit,
    )

    val testLibraries = listOf<String>(junit)

    val debugComposeLibraries = listOf<String>(debugComposeUITooling, debugComposeUITestManifest)

}

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add(/* configurationName = */ "kapt", /* dependencyNotation = */ dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add(/* configurationName = */ "implementation", /* dependencyNotation = */ dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add(/* configurationName = */ "androidTestImplementation", /* dependencyNotation = */
            dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add(/* configurationName = */ "testImplementation", /* dependencyNotation = */ dependency)
    }
}

fun DependencyHandler.annotationProcessor(list: List<String>) {
    list.forEach { dependency ->
        add(/* configurationName = */ "annotationProcessor", /* dependencyNotation = */ dependency)
    }
}

fun DependencyHandler.debugImplementation(list: List<String>) {
    list.forEach { dependency ->
        add(/* configurationName = */ "annotationProcessor", /* dependencyNotation = */ dependency)
    }
}