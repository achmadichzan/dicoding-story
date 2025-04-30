package com.achmadichzan.dicodingstory.presentation.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

@Serializable
object Route {

    @Serializable
    object Login

    @Serializable
    object Register

    @Serializable
    object Story

    @Serializable
    data class StoryDetail(val id: String)

    @Serializable
    object AddStory

    @Serializable
    object MapsLocation
}

val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

fun NavHostController.navigateBack() {
    if (canGoBack) {
        popBackStack()
    }
}