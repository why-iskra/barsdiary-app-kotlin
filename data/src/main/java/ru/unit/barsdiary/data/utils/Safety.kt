package ru.unit.barsdiary.data.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class Safety @Inject constructor() {

    companion object {
        private const val ALIAS_KEY = "ru.unit.barsdiary"
    }

    init {
        if (!keyIsExists(ALIAS_KEY)) {
            generateKey(ALIAS_KEY)
        }
    }

    private fun generateKey(name: String) {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec
            .Builder(name, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun keyIsExists(alias: String): Boolean {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val aliases = keyStore.aliases()
        var result = false
        while (aliases.hasMoreElements()) {
            if(aliases.nextElement() == alias) {
                result = true
                break
            }
        }

        return result
    }

    private fun getKey(alias: String): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val secretKeyEntry = keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    private fun encrypt(data: String, keyAlias: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getKey(keyAlias))

        val ivBytes = cipher.iv.copyOf()
        val result = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

        return Pair(ivBytes, result)
    }

    private fun decrypt(ivBytes: ByteArray, data: ByteArray, keyAlias: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, getKey(keyAlias), GCMParameterSpec(128, ivBytes))

        return cipher.doFinal(data).toString(Charsets.UTF_8)
    }

    fun encrypt(data: String): String {
        val result = encrypt(data, ALIAS_KEY)

        return Base64.encodeToString(result.first, Base64.NO_WRAP) + ":" + Base64.encodeToString(result.second, Base64.NO_WRAP)
    }

    fun decrypt(encryptOut: String): String {
        val split = encryptOut.split(":")
        val ivBytes = Base64.decode(split.first(), Base64.NO_WRAP)
        val data = Base64.decode(split.last(), Base64.NO_WRAP)

        return decrypt(ivBytes, data, ALIAS_KEY)
    }
}