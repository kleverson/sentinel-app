package br.com.sentinelapp.core.manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/*
* package br.com.sentinelapp.core.manager

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
* */

object PasswordGenerated {
    private val _password = MutableStateFlow("")
    var password: StateFlow<String> = _password

    fun setPassword(password: String) {
        _password.value = password
    }

    fun clearPassowrd() {
        _password.value = ""
    }

    fun getPassword(): String {
        return _password.value
    }
}