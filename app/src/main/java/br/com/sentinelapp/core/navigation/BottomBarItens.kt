package br.com.sentinelapp.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import br.com.sentinelapp.R

sealed class BottomBarItens(
    var route: String,
    var title: Int,
    var icon: ImageVector
) {
    object Home : BottomBarItens(
        route = "home",
        title = R.string.password_label_nav,
        icon = Icons.Default.Key
    )

    object Generate: BottomBarItens(
        route = "generate",
        title = R.string.generate_label_nav,
        icon = Icons.Default.Add
    )

    object Settings : BottomBarItens(
        route = "settings",
        title = R.string.settings_label_nav,
        icon = Icons.Default.Settings
    )

    object NewPass : BottomBarItens(
        route = "setting_detail?passwordId={passwordId}",
        title = R.string.new_pass_label_nav,
        icon = Icons.Rounded.Add
    ) {
        fun createRoute(passwordId: Int? = null): String {
            return if (passwordId != null) {
                "setting_detail?passwordId=$passwordId"
            } else {
                "setting_detail"
            }
        }
    }

}