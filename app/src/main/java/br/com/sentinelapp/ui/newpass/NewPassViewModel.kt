package br.com.sentinelapp.ui.newpass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.sentinelapp.core.data.dao.PasswordDao
import br.com.sentinelapp.core.data.entities.PasswordEntity
import br.com.sentinelapp.data.model.NewPasswordData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewPassViewModel @Inject constructor(
    private val repository: PasswordDao
): ViewModel() {

    private val _newpasswordState= MutableLiveData<NewPasswordCreateState>();
    val newpasswordState: LiveData<NewPasswordCreateState> get() = _newpasswordState

    private val _loadedPassword = MutableLiveData<PasswordEntity?>()
    val loadedPassword: LiveData<PasswordEntity?> get() = _loadedPassword

    suspend fun loadPassword(passwordId: Int) {
        try {
            val password = repository.getPasswordById(passwordId)
            _loadedPassword.value = password
        } catch (e: Exception) {
            _newpasswordState.value = NewPasswordCreateState.Error("Erro ao carregar senha: ${e.message}")
        }
    }

    suspend fun create(data: NewPasswordData){
        try{

            val passwordEntity = PasswordEntity(
                user = data.accoutName,
                password = data.password,
                provider = data.providerName
            )
            val new = repository.insertPassword(passwordEntity)

            if(new > 0){
                _newpasswordState.value = NewPasswordCreateState.Success
            } else {
                _newpasswordState.value = NewPasswordCreateState.Error("Erro ao salvar nova senha")
            }
        }catch (e: Exception){
            _newpasswordState.value = NewPasswordCreateState.Error("Erro ao salvar nova senha: ${e.message}")
            throw e
        }

    }
}

sealed class NewPasswordCreateState {
    object Success: NewPasswordCreateState()
    data class Error(val message: String): NewPasswordCreateState()
}