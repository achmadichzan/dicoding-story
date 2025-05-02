package com.achmadichzan.dicodingstory.presentation.navigation.navgraph

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.achmadichzan.dicodingstory.presentation.intent.AddStoryIntent
import com.achmadichzan.dicodingstory.presentation.navigation.Route
import com.achmadichzan.dicodingstory.presentation.navigation.navigateBack
import com.achmadichzan.dicodingstory.presentation.ui.screen.addstory.AddStoryScreen
import com.achmadichzan.dicodingstory.presentation.viewmodel.UploadViewModel
import com.google.android.gms.location.LocationServices
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

fun NavGraphBuilder.AddStoryNavGraph(navController: NavHostController) {
    composable<Route.AddStory> {
        val viewModel = koinViewModel<UploadViewModel>()
        val state = viewModel.state
        val context = getKoin().get<Context>()

        LaunchedEffect(Unit) {
            viewModel.navigationEvent.collect { intent ->
                when (intent) {
                    AddStoryIntent.GoBack -> navController.navigateBack()
                    else -> Unit
                }
            }
        }

        @SuppressLint("MissingPermission")
        AddStoryScreen(
            state = state,
            onUpload = { file, desc ->
                if (state.isLocationEnabled) {
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        val lat = location?.latitude
                        val lon = location?.longitude
                        viewModel.uploadStory(file, desc, lat, lon)
                    }
                } else {
                    viewModel.uploadStory(file, desc, null, null)
                }
            },
            onIntent = viewModel::onIntent
        )
    }
}