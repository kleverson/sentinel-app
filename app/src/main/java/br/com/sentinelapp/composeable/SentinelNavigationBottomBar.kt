package br.com.sentinelapp.composeable

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import br.com.sentinelapp.core.navigation.BottomBarItens
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun SentinelNavigationBottomBar(navController: NavHostController) {
    val screens = listOf(BottomBarItens.Home, BottomBarItens.Generate, BottomBarItens.Settings)

    NavigationBar {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination


        screens.forEach { screen->
            NavigationBarItem(
                selected = currentDestination?.route == screen.route.toString(),
                onClick = {
                    navController.navigate(screen.route)
                },
                icon = { Icon(screen.icon, contentDescription = null)},
                label = {
                    Text(stringResource(screen.title))
                }
            )
        }
    }
}