package br.com.sentinelapp.core.data.security

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gerencia a senha master durante a sessão do aplicativo.
 * A senha é mantida em memória apenas enquanto o app está ativo.
 */
@Singleton
class SessionManager @Inject constructor() {

    @Volatile
    private var masterPassword: String? = null

    /**
     * Define a senha master para a sessão atual.
     */
    fun setMasterPassword(password: String) {
        masterPassword = password
    }

    /**
     * Obtém a senha master da sessão atual.
     * @throws IllegalStateException se a senha não foi definida.
     */
    fun getMasterPassword(): String {
        return masterPassword ?: throw IllegalStateException(
            "Master password not set. User needs to authenticate first."
        )
    }

    /**
     * Verifica se há uma senha master definida na sessão.
     */
    fun isAuthenticated(): Boolean {
        return masterPassword != null
    }

    /**
     * Limpa a senha master da sessão (logout).
     */
    fun clearSession() {
        masterPassword = null
    }
}

