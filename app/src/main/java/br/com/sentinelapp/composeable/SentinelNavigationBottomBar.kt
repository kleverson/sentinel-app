package br.com.sentinelapp.composeable

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.sentinelapp.core.navigation.BottomBarItens
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.sentinelapp.core.navigation.BottomBarIcon
import br.com.sentinelapp.ui.theme.InterFont

@Composable
fun SentinelNavigationBottomBar(navController: NavHostController) {
    val isDark = isSystemInDarkTheme()
    val screens = listOf(BottomBarItens.Home, BottomBarItens.Generate)

    NavigationBar(
        containerColor = when (isDark) {
            true -> Color(0xFF1A2633)
            false -> Color(0xFFFFFFFF)
            else -> Color(0xFFFFFFFF)
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A2633))
    ) {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination



        screens.forEach { screen ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = if (isDark) Color.White else Color(0xff91ADC9),
                    unselectedIconColor = Color(0xff91ADC9),
                    selectedTextColor = if (isDark) Color.White else Color(0xff91ADC9),
                    unselectedTextColor = Color(0xff91ADC9),
                    indicatorColor = Color.Transparent
                ),
                selected = currentDestination?.route == screen.route.toString(),
                onClick = {
                    navController.navigate(screen.route)
                },
                icon = {
                    when (val icon = screen.icon) {
                        is BottomBarIcon.Vector -> Icon(
                            imageVector = icon.icon,
                            contentDescription = null
                        )

                        is BottomBarIcon.Drawable -> Icon(
                            painter = painterResource(id = icon.resId),
                            contentDescription = null
                        )
                    }
                },
                label = {
                    Text(
                        stringResource(screen.title), style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = InterFont,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            )
        }
    }
}