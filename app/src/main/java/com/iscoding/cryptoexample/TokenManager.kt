package com.iscoding.cryptoexample

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher

    class TokenManager(private val context: Context) {

        private val alias = "my_key_alias"

        private fun generateMasterKeyIfNeeded() {
            if (!keyExists(alias)) {
                val masterKey = MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()

                // Dummy write to trigger key creation
                val sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "secure_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )

                sharedPreferences.edit().apply {
                    putString("dummy_key", "dummy_value")
                    apply()
                }
            }
        }

        private fun keyExists(alias: String): Boolean {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            return sharedPreferences.contains(alias)
        }

        fun encryptToken(token: String): ByteArray {
            generateMasterKeyIfNeeded()

            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            val editor = sharedPreferences.edit()
            println("encryptToken Token: $token")

            editor.putString("encrypted_auth_token", token)
            editor.apply()

            return token.toByteArray(StandardCharsets.UTF_8)
        }

        fun decryptToken(): String? {
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            return sharedPreferences.getString("encrypted_auth_token", null)
        }


    }

////usage
//val tokenManager = TokenManager(context)
//
//// Encrypt and store the token
//val token = "your_auth_token_here"
//tokenManager.encryptToken(token)
//
//// Decrypt the token
//val decryptedToken = tokenManager.decryptToken()
//println("Decrypted Token: $decryptedToken")