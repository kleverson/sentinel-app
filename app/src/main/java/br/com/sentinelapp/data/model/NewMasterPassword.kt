package br.com.sentinelapp.data.model

data class NewMasterPassword(
    val password: String,
    val confirmPassword: String
) {
    fun isValid(minLength: Int = 8): Boolean {
        return password.isNotBlank()
                && confirmPassword.isNotBlank()
                && password == confirmPassword
                && hasUppercase()
                && hasNumber()
                && hasSpecialChar()
                && hasMinLength(minLength)
    }

    fun isEqual(): Boolean = password == confirmPassword


    fun hasUppercase(): Boolean  =  password.any { it.isUpperCase() }

    fun hasNumber(): Boolean  =   password.any { it.isDigit() }

    fun hasSpecialChar(): Boolean{
        val specialChars = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~"
        return password.any { it in specialChars }
    }


    fun hasMinLength(minLength: Int = 8): Boolean  = password.length >= minLength

}
