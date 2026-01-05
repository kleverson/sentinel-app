package br.com.sentinelapp.ui.onboard

import android.util.Log
import androidx.lifecycle.ViewModel
import br.com.sentinelapp.core.data.security.KeyStoreManager
import br.com.sentinelapp.core.data.security.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val keyStoreManager: KeyStoreManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val TAG = "OnBoardViewModel"

    suspend fun createMasterPassword(password: String) {
        Log.d(TAG, "=== createMasterPassword CALLED ===")
        Log.d(TAG, "Password length received: ${password.length}")
        try {
            Log.d(TAG, "Switching to IO dispatcher...")
            // Perform the potentially CPU/IO heavy crypto + prefs write off the main thread
            withContext(Dispatchers.IO) {
                Log.d(TAG, "On IO thread, calling keyStoreManager.setUserPassphrase...")
                keyStoreManager.setUserPassphrase(password)
                Log.d(TAG, "keyStoreManager.setUserPassphrase completed successfully")

                // Armazena a senha na sessão para uso imediato
                sessionManager.setMasterPassword(password)
                Log.d(TAG, "Password stored in session")
            }
            Log.d(TAG, "=== createMasterPassword SUCCESS ===")
        } catch (e: Exception) {
            Log.e(TAG, "=== createMasterPassword FAILED ===", e)
            Log.e(TAG, "Exception type: ${e.javaClass.simpleName}")
            Log.e(TAG, "Exception message: ${e.message}")
            throw e
        }
    }
}