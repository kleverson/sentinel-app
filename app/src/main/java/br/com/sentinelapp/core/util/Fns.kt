package br.com.sentinelapp.core.util

fun generatePassword(
    length: Int,
    includeUppercase: Boolean,
    includeNumbers: Boolean,
    includeSpecialChars: Boolean,
    mustContain: String = ""
): String{
    val lowerCase = ('a'..'z')
    val upperCase = if(includeUppercase) ('A'..'Z') else emptyList()
    val numbers = if(includeNumbers) ('0'..'9') else emptyList()
    val symbols = if (includeSpecialChars) "!@#$%^&*()_-+=<>?".toList() else emptyList()

    val allChars = (lowerCase + upperCase + numbers + symbols).shuffled()

    var password = mustContain + (1..length - mustContain.length).map {
        allChars.random()
    }.joinToString ("")

    return password.toList().shuffled().joinToString("")
}