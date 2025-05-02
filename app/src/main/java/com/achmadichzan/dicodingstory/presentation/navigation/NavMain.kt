package com.achmadichzan.dicodingstory.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.achmadichzan.dicodingstory.presentation.navigation.navgraph.AddStoryNavGraph
import com.achmadichzan.dicodingstory.presentation.navigation.navgraph.LoginNavGraph
import com.achmadichzan.dicodingstory.presentation.navigation.navgraph.MapsLocationNavGraph
import com.achmadichzan.dicodingstory.presentation.navigation.navgraph.RegisterNavGraph
import com.achmadichzan.dicodingstory.presentation.navigation.navgraph.StoryDetailNavGraph
import com.achmadichzan.dicodingstory.presentation.navigation.navgraph.StoryNavGraph

@Composable
fun NavMain(token: String?) {

    val navController: NavHostController = rememberNavController()
    val startDestination = if (token.isNullOrBlank()) Route.Login else Route.Story

    NavHost(navController = navController, startDestination = startDestination) {
        LoginNavGraph(navController = navController)

        RegisterNavGraph(navController = navController)

        StoryNavGraph(navController = navController)

        StoryDetailNavGraph(navController = navController)

        AddStoryNavGraph(navController = navController)

        MapsLocationNavGraph(navController = navController)
    }
}