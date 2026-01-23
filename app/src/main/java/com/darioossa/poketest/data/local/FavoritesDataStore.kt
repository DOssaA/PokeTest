package com.darioossa.poketest.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.favoritesDataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")

class FavoritesDataStore(
    private val context: Context,
    private val cryptoManager: CryptoManager
) {
    private val favoritesKey = stringPreferencesKey("favorites_encrypted")

    val favoritesFlow: Flow<Set<Int>> = context.favoritesDataStore.data
        .map { prefs ->
            val encrypted = prefs[favoritesKey].orEmpty()
            parseFavorites(encrypted)
        }

    suspend fun getFavorites(): Set<Int> {
        return favoritesFlow.first()
    }

    suspend fun toggleFavorite(pokemonId: Int): Set<Int> {
        var updated: Set<Int> = emptySet()
        context.favoritesDataStore.edit { prefs ->
            val current = parseFavorites(prefs[favoritesKey].orEmpty()).toMutableSet()
            if (current.contains(pokemonId)) {
                current.remove(pokemonId)
            } else {
                current.add(pokemonId)
            }
            updated = current.toSet()
            prefs[favoritesKey] = encodeFavorites(updated)
        }
        return updated
    }

    private fun parseFavorites(encrypted: String): Set<Int> {
        if (encrypted.isBlank()) return emptySet()
        val decrypted = cryptoManager.decrypt(encrypted).orEmpty()
        if (decrypted.isBlank()) return emptySet()
        return decrypted.split(',')
            .mapNotNull { it.trim().toIntOrNull() }
            .toSet()
    }

    private fun encodeFavorites(favorites: Set<Int>): String {
        if (favorites.isEmpty()) return ""
        val payload = favorites.joinToString(",")
        return cryptoManager.encrypt(payload)
    }
}
