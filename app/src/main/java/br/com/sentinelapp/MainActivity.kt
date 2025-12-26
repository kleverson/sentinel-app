package br.com.sentinelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.sentinelapp.composeable.SentinelAppTopBar
import br.com.sentinelapp.composeable.SentinelNavigationBottomBar
import br.com.sentinelapp.core.navigation.BottomBarItens
import br.com.sentinelapp.ui.generate.GenerateScreen
import br.com.sentinelapp.ui.home.HomeScreen
import br.com.sentinelapp.ui.settings.NewPassScreen
import br.com.sentinelapp.ui.settings.SettingsScreen
import br.com.sentinelapp.ui.theme.SentinelAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            SentinelAppTheme {
                Scaffold(
                    topBar = { SentinelAppTopBar(navController) },
                    bottomBar = {
                        Column {
                            HorizontalDivider(
                                thickness  = 1.dp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
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
                        startDestination = BottomBarItens.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(BottomBarItens.Home.route) {
                            HomeScreen()
                        }
                        composable(BottomBarItens.Generate.route) { GenerateScreen() }
                        composable(BottomBarItens.Settings.route) { SettingsScreen() }
                        composable(BottomBarItens.NewPass.route) { NewPassScreen() }
                    }
                }
            }
        }
    }
}