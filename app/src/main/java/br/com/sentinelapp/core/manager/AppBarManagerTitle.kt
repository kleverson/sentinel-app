package br.com.sentinelapp.core.manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AppBarManagerTitle {

    private val _barTitle = MutableStateFlow("")
    var BarTitle: StateFlow<String> = _barTitle

    fun setTitle(title: String) {
        _barTitle.value = title
    }

    fun getTitle(): String {
        return _barTitle.value
    }
}