package br.com.sentinelapp.data.model

enum class SnackBarType {
    DEFAULT,
    ERROR,
    SUCCESS,
    WARNING
}

data class SnackBarData(
    val message: String,
    val actionLabel: String? = null,
    val type: SnackBarType = SnackBarType.DEFAULT,
    val onAction: (() -> Unit)? = null,
    val onDismiss: (() -> Unit)? = null
)
