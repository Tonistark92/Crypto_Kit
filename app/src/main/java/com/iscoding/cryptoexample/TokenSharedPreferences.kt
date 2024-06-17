package com.iscoding.cryptoexample

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

// i added them in the token manager class
fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    return EncryptedSharedPreferences.create(
        "secure_prefs_dataa",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

fun storeToken(context: Context, token: String) {
    val sharedPreferences = getEncryptedSharedPreferences(context)
    val editor = sharedPreferences.edit()
    editor.putString("auth_token", token)
    editor.apply()
}

fun getToken(context: Context): String? {
    val sharedPreferences = getEncryptedSharedPreferences(context)
    return sharedPreferences.getString("auth_token", null)
}