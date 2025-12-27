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

    private val _loadedPassword = MutableLiveData<CurrentPasswordState?>()
    val loadedPassword: LiveData<CurrentPasswordState?> get() = _loadedPassword

    suspend fun loadPassword(passwordId: Int) {
        try {
            val password = repository.getPasswordById(passwordId)
            password.let {
                if (it == null) {
                    _loadedPassword.value = CurrentPasswordState.Error
                    return
                }else{
                    _loadedPassword.value = CurrentPasswordState.Success(password)
                }
            }
        } catch (e: Exception) {
            _loadedPassword.value = CurrentPasswordState.Error
        }
    }

    suspend fun delete(passwordId: Int){
        try{
            repository.removePassword(passwordId)
        }catch (e: Exception){
            throw e
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

sealed class CurrentPasswordState{
    object Loading: CurrentPasswordState()
    data class Success(val passwordEntity: PasswordEntity?): CurrentPasswordState()
    object Error: CurrentPasswordState()
}

sealed class NewPasswordCreateState {
    object Success: NewPasswordCreateState()
    data class Error(val message: String): NewPasswordCreateState()
}