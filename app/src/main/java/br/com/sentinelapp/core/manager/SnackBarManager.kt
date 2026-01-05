package br.com.sentinelapp.core.manager

import br.com.sentinelapp.data.model.SnackBarData
import br.com.sentinelapp.data.model.SnackBarType
import kotlinx.coroutines.flow.MutableStateFlow

object SnackBarManager {

    private val _snackBarSetting = MutableStateFlow<SnackBarData?>(null)
    var snackBarSetting = _snackBarSetting


    fun show(
        message: String,
        actionLabel: String? = null,
        type: SnackBarType = SnackBarType.DEFAULT,
        onAction: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null
    ){
        _snackBarSetting.value = SnackBarData(
            message = message,
            type = type,
            actionLabel = actionLabel,
            onAction = onAction,
            onDismiss = onDismiss
        )
    }

    fun clear() {
        _snackBarSetting.value = null
    }

}