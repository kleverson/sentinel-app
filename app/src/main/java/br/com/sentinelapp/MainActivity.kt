package br.com.sentinelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.sentinelapp.composeable.AnimatedSnackbarHost
import br.com.sentinelapp.composeable.SentinelAppTopBar
import br.com.sentinelapp.composeable.SentinelNavigationBottomBar
import br.com.sentinelapp.core.manager.SnackBarManager
import br.com.sentinelapp.core.navigation.BottomBarItens
import br.com.sentinelapp.ui.generate.GenerateScreen
import br.com.sentinelapp.ui.home.HomeScreen
import br.com.sentinelapp.ui.newpass.NewPassScreen
import br.com.sentinelapp.ui.onboard.OnBoardScreen
import br.com.sentinelapp.ui.settings.SettingsScreen
import br.com.sentinelapp.ui.theme.SentinelAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()
            var snackBarManager = SnackBarManager.snackBarSetting.asSharedFlow()


            LaunchedEffect(snackBarManager) {
                snackBarManager.collect { data ->
                    data?.let {
                        snackbarHostState.showSnackbar(
                            message = it.message,
                            duration = SnackbarDuration.Short,
                            actionLabel = it.actionLabel
                        )

                        it.onDismiss?.invoke()
                        SnackBarManager.clear()
                    }
                }
            }


            val navController = rememberNavController()
            SentinelAppTheme {
                Scaffold(
                    snackbarHost = {
                        AnimatedSnackbarHost(hostState = snackbarHostState)
                    },
                    topBar = { SentinelAppTopBar(navController) },
                    bottomBar = {
                        Column {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.2f)
                            )
                            SentinelNavigationBottomBar(navController)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomBarItens.Onboarding.route,
                        enterTransition = {
                            fadeIn(tween(220)) + slideInHorizontally(
                                initialOffsetX = { it / 4 }
                            )
                        },
                        exitTransition = {
                            fadeOut(tween(220)) + slideOutHorizontally(
                                targetOffsetX = { -it / 4 }
                            )
                        },
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(BottomBarItens.Onboarding.route) {
                            OnBoardScreen(navController = navController)
                        }

                        composable(BottomBarItens.Home.route) {
                            HomeScreen(
                                navController = navController,
                            )
                        }
                        composable(BottomBarItens.Generate.route) { GenerateScreen() }
                        composable(BottomBarItens.Settings.route) { SettingsScreen() }
                        composable(
                            BottomBarItens.NewPass.route,
                            arguments = listOf(
                                navArgument("passwordId") {
                                    type = NavType.StringType
                                    nullable = true
                                }
                            )
                        ) { backStackEntry ->
                            val passwordId = backStackEntry.arguments?.getString("passwordId")
                            NewPassScreen(navController, passwordId)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

}

