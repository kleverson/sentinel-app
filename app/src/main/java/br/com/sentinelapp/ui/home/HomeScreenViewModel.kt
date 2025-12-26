package br.com.sentinelapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sentinelapp.core.data.dao.PasswordDao
import br.com.sentinelapp.core.data.entities.PasswordEntity
import br.com.sentinelapp.ui.newpass.NewPasswordCreateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel  @Inject constructor(
    val passwordDao: PasswordDao
): ViewModel() {

    private val _passwordState= MutableLiveData<PasswordListState>();
    val passwordState: LiveData<PasswordListState> get() = _passwordState


    init {
        _passwordState.value = PasswordListState.Loading
        loadPasswords()
    }

    fun loadPasswords(){
        viewModelScope.launch {
            try {
                _passwordState.value = PasswordListState.Loading
                val passwords = passwordDao.getAllPasswords()
                _passwordState.value = PasswordListState.Success(passwords)
            }catch (Exception: Exception){
                _passwordState.postValue(PasswordListState.Error(Exception.message ?: "Erro desconhecido"))
            }
        }
    }

    suspend fun removeItem(passwordEntity: PasswordEntity){
        try {
            passwordDao.removePassword(passwordEntity.id)
            loadPasswords()
        }catch (Exception: Exception){
            _passwordState.postValue(PasswordListState.Error(Exception.message ?: "Erro desconhecido"))
        }
    }

    suspend fun searchPassword(term: String){
        try {
            val passwords = passwordDao.getPasswordByTerm(term)

            passwords.let {
                if (it == null || it.isEmpty()){
                    _passwordState.value = PasswordListState.Error("Nenhum resultado encontrado")
                    return
                }
                _passwordState.value = PasswordListState.Success(passwords)
            }

        }catch (Exception: Exception){
            _passwordState.postValue(PasswordListState.Error(Exception.message ?: "Erro desconhecido"))
        }
    }
}

sealed class PasswordListState{
    object Loading: PasswordListState()
    data class Success(val data: List<PasswordEntity>): PasswordListState()
    data class Error(val message: String): PasswordListState()
}