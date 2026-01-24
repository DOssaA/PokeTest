package com.darioossa.poketest.data.local

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Encrypts and decrypts small payloads using an app-scoped AES/GCM key
 * stored in Android Keystore.
 *
 * The encrypted payload format is `base64(iv):base64(cipherText)`.
 */
class CryptoManager {
    private val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

    /**
     * Encrypts [plainText] and returns a Base64-encoded payload.
     *
     * @return payload in the form `base64(iv):base64(cipherText)`
     */
    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey())
        val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        val iv = cipher.iv
        val ivEncoded = Base64.encodeToString(iv, Base64.NO_WRAP)
        val cipherEncoded = Base64.encodeToString(cipherText, Base64.NO_WRAP)
        return "$ivEncoded:$cipherEncoded"
    }

    /**
     * Decrypts a payload produced by [encrypt].
     *
     * @return plaintext if the payload is valid and decryptable; otherwise null.
     */
    fun decrypt(payload: String): String? {
        val parts = payload.split(":")
        if (parts.size != 2) return null
        val iv = Base64.decode(parts[0], Base64.NO_WRAP)
        val cipherText = Base64.decode(parts[1], Base64.NO_WRAP)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(), spec)
        return runCatching {
            String(cipher.doFinal(cipherText), Charsets.UTF_8)
        }.getOrNull()
    }

    /**
     * Returns the AES key from Keystore, creating it if missing.
     */
    private fun getOrCreateSecretKey(): SecretKey {
        val existing = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        if (existing != null) return existing

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val parameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .build()
        keyGenerator.init(parameterSpec)
        return keyGenerator.generateKey()
    }

    private companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val KEY_ALIAS = "poketest_favorites_key"
        private const val KEY_SIZE = 256
        private const val GCM_TAG_LENGTH = 128
    }
}
