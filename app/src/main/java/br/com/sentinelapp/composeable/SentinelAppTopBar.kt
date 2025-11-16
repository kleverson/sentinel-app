package br.com.sentinelapp.composeable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.sentinelapp.core.manager.AppBarManagerTitle
import br.com.sentinelapp.core.navigation.BottomBarItens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelAppTopBar(navController: NavController) {
    val barTitle = AppBarManagerTitle.BarTitle.collectAsState()

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = barTitle.value ?: "SentinelApp",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            if(currentDestination?.route != BottomBarItens.Home.route) {
                IconButton(
                    onClick = {
                        navController.navigate(BottomBarItens.Home.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Voltar para Home"
                    )
                }
            }
        },
        actions = {
            if(currentDestination?.route == BottomBarItens.Home.route) {
                IconButton(
                    onClick = {
                        navController.navigate(BottomBarItens.NewPass.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Ação"
                    )
                }
            }
        }
    )
}