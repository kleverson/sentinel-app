package br.com.sentinelapp.core.manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object HasMasterPass {
    private val _hasMaterPassDefined = MutableStateFlow(false)
    var hasMasterPassDefined: StateFlow<Boolean> = _hasMaterPassDefined

    fun setHasMasterPassDefined(hasMasterPass: Boolean) {
        _hasMaterPassDefined.value = hasMasterPass
    }

    fun getMasterPassDefined(): Boolean {
        return _hasMaterPassDefined.value
    }
}