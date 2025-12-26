package br.com.sentinelapp.composeable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.sentinelapp.core.navigation.BottomBarItens
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.sentinelapp.ui.theme.InterFont

@Composable
fun SentinelNavigationBottomBar(navController: NavHostController) {
    val screens = listOf(BottomBarItens.Home, BottomBarItens.Generate, BottomBarItens.Settings)

    NavigationBar(
        containerColor = Color(0xFF1A2633),
        modifier = Modifier.fillMaxWidth().background(Color(0xFF1A2633))
    ) {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination


        screens.forEach { screen->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color(0xff91ADC9),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color(0xff91ADC9),
                    indicatorColor = Color.Transparent
                ),
                selected = currentDestination?.route == screen.route.toString(),
                onClick = {
                    navController.navigate(screen.route)
                },
                icon = { Icon(screen.icon, contentDescription = null)},
                label = {
                    Text(stringResource(screen.title), style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = InterFont,
                        fontWeight = FontWeight.Medium
                    ))
                }
            )
        }
    }
}