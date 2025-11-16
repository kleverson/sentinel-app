package br.com.sentinelapp.data.mock

import br.com.sentinelapp.data.model.PasswordItens
import kotlin.random.Random

fun generateTestPasswords(count: Int = 100): List<PasswordItens> {
    val names = listOf(
        "kleverson", "joao", "maria", "ana", "carlos", "paula", "roberto", "aline",
        "victor", "bruna", "caio", "juliana", "marcos", "fernanda", "lucas", "laura",
        "ricardo", "sara", "felipe", "bianca"
    )

    val domains = listOf(
        "gmail.com", "outlook.com", "yahoo.com", "icloud.com",
        "hotmail.com", "protonmail.com"
    )

    val providers = listOf(
        "Gmail", "Instagram", "Facebook", "LinkedIn", "Twitter", "Outlook",
        "GitHub", "Discord", "Spotify", "Apple ID", "Amazon"
    )

    fun randomPassword(): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#\$%&*?"
        return (1..12).map { chars.random() }.joinToString("")
    }

    return List(count) {
        val name = names.random()
        PasswordItens(
            user = "$name${Random.nextInt(10, 99)}@${domains.random()}",
            password = randomPassword(),
            provider = providers.random()
        )
    }
}