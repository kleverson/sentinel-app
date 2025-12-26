package br.com.sentinelapp.data.model

data class NewPasswordData(
    val providerName: String = "",
    val accoutName: String = "",
    val user: String = "",
    val password: String = ""
){
    fun isValid(): Boolean{
        return providerName.isNotEmpty() &&  user.isNotBlank() && password.isNotBlank()
    }
}
