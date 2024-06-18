package com.iscoding.cryptoexample

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import com.iscoding.cryptoexample.ui.theme.CryptoExampleTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.iscoding.cryptoexample.datastore.AppSettings
import com.iscoding.cryptoexample.datastore.AppSettingsSerializer
import com.iscoding.cryptoexample.datastore.Language
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

val Context.dataStore by dataStore("app-settings.json", AppSettingsSerializer)
class MainActivity : ComponentActivity() {
    private lateinit var dataStorePref: DataStore<Preferences>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStorePref = createDataStore(name = "settings")
        val cipherManager = CipherManager()
        // secure api key from git repos using  BuildConfig and local.properties
        val apiKey = BuildConfig.API_KEY
//        val tokenManager = TokenManager(this)
//
//// Encrypt and store the token
//        val token = "your_auth_token_here"
//        tokenManager.encryptToken(token)

// Decrypt the token
//        val decryptedToken = tokenManager.decryptToken()
//        println("Decrypted Token: $decryptedToken")
        setContent {
            CryptoExampleTheme {
                var messageToEncrypt by remember {
                    mutableStateOf("")
                }
                var messageToDecrypt by remember {
                    mutableStateOf("")
                }
                Column(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    TextField(
                        value = messageToEncrypt,
                        onValueChange = { messageToEncrypt = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(text = "Encrypt string") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(onClick = {
                            val bytes = messageToEncrypt.encodeToByteArray()
                            val file = File(filesDir, "secret.txt")
                            if (!file.exists()) {
                                file.createNewFile()
                            }
                            val fos = FileOutputStream(file)

                            messageToDecrypt = cipherManager.encrypt(
                                bytes = bytes,
                                outputStream = fos
                            ).decodeToString()
                        }) {
                            Text(text = "Encrypt")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = {
                            val file = File(filesDir, "secret.txt")
                            messageToEncrypt = cipherManager.decrypt(
                                inputStream = FileInputStream(file)
                            ).decodeToString()
                        }) {
                            Text(text = "Decrypt")
                        }
                    }
                    Text(text = messageToDecrypt)

                    val appSettings = dataStore.data.collectAsState(
                        initial = AppSettings()
                    ).value
                    val scope = rememberCoroutineScope()
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for(i in 0..2) {
                            val language = Language.values()[i]
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = language == appSettings.language,
                                    onClick = {
                                        scope.launch {
                                            setLanguage(language)
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = language.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    // use those to read and write to normal data store pref with key
    private suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStorePref.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStorePref.data.first()
        return preferences[dataStoreKey]
    }

    private suspend fun setLanguage(language: Language) {
        dataStore.updateData {
            it.copy(language = language)
        }
    }
}