package com.achmadichzan.dicodingstory.presentation.navigation

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