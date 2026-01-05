package br.com.sentinelapp.core.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import br.com.sentinelapp.R

sealed class BottomBarItens(
    var route: String,
    var title: Int,
    var icon: BottomBarIcon
) {
    object Onboarding : BottomBarItens(
        route = "onboarding",
        title = R.string.onboarding_label_nav,
        icon = BottomBarIcon.Vector(Icons.Default.Add)
    )

    object Home : BottomBarItens(
        route = "home",
        title = R.string.password_label_nav,
        icon = BottomBarIcon.Vector(Icons.Default.Key)
    )

    object Generate: BottomBarItens(
        route = "generate",
        title = R.string.generate_label_nav,
        icon = BottomBarIcon.Drawable(R.drawable.magic)
    )

    object Settings : BottomBarItens(
        route = "settings",
        title = R.string.settings_label_nav,
        icon = BottomBarIcon.Vector(Icons.Default.Key)
    )

    object NewPass : BottomBarItens(
        route = "setting_detail?passwordId={passwordId}",
        title = R.string.new_pass_label_nav,
        icon = BottomBarIcon.Vector(Icons.Default.Key)
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

sealed class BottomBarIcon {
    data class Vector(val icon: ImageVector) : BottomBarIcon()
    data class Drawable(@DrawableRes val resId: Int) : BottomBarIcon()
}