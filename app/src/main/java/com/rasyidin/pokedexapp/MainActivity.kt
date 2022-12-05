@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.rasyidin.pokedexapp

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rasyidin.pokedexapp.ui.navigation.Screen
import com.rasyidin.pokedexapp.ui.screen.detail.DetailScreen
import com.rasyidin.pokedexapp.ui.screen.home.HomeScreen
import com.rasyidin.pokedexapp.ui.screen.profile.ProfileScreen
import com.rasyidin.pokedexapp.ui.theme.LightGray
import com.rasyidin.pokedexapp.ui.theme.PokedexAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            PokedexAppTheme {
                // A surface container using the 'background' color from the theme
                PokedexAppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexAppScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetail = { pokemonName, dominantColor ->
                        navController.navigate(
                            Screen.Detail.createRoute(
                                pokemonName,
                                dominantColor.toArgb()
                            ),
                            navOptions = NavOptions.Builder()
                                .setEnterAnim(R.anim.slide_left)
                                .setExitAnim(R.anim.push_left)
                                .setPopEnterAnim(R.anim.push_right)
                                .setPopExitAnim(R.anim.slide_right)
                                .build()
                        )
                    },
                    navigateToProfile = {
                        navController.navigate(
                            Screen.Profile.route,
                            navOptions = NavOptions.Builder()
                                .setEnterAnim(R.anim.slide_left)
                                .setExitAnim(R.anim.push_left)
                                .setPopEnterAnim(R.anim.push_right)
                                .setPopExitAnim(R.anim.slide_right)
                                .build()
                        )

                    }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument(Screen.Detail.POKEMON_NAME) {
                        type = NavType.StringType
                    },
                    navArgument(Screen.Detail.POKEMON_DOMINANT_COLOR) {
                        type = NavType.IntType
                    }
                )
            ) {
                val pokemonName = it.arguments?.getString(Screen.Detail.POKEMON_NAME) ?: ""
                val dominantColor = remember {
                    val color = it.arguments?.getInt(Screen.Detail.POKEMON_DOMINANT_COLOR)
                    color?.let { colorValue ->
                        Color(colorValue)
                    } ?: LightGray
                }
                DetailScreen(
                    pokemonName = pokemonName,
                    dominantColor = dominantColor,
                    navigateBack = { navController.navigateUp() }
                )
            }
            composable(
                route = Screen.Profile.route,
            ) {
                ProfileScreen(
                    navigateBack = { navController.navigateUp() },
                    navigateToDetail = { pokemonName, dominantColor ->
                        navController.navigate(
                            Screen.Detail.createRoute(
                                pokemonName,
                                dominantColor.toArgb()
                            )
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PokedexAppTheme {
        PokedexAppScreen()
    }
}