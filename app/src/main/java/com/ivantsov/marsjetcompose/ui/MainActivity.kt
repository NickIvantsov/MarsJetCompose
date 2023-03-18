package com.ivantsov.marsjetcompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ivantsov.marsjetcompose.data.reprository.PhotosRepository
import com.ivantsov.marsjetcompose.di.IoDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var photosRepository: PhotosRepository

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarsJetComposeApp(
                photosRepository = photosRepository,
                ioDispatcher = ioDispatcher
            )
        }
    }
}