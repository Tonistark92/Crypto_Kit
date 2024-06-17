# Encryption and Token Management in Android

## Overview

This repository showcases secure encryption and token management techniques for Android applications using Kotlin. It includes implementations for both data encryption and token encryption using Android's cryptographic APIs.

## Features

- **Encryption:** Utilizes AES encryption with CBC block mode and PKCS7 padding via `CipherManager`.
- **Token Management:** Securely manages tokens using AES256 encryption with `TokenManager`.
- **Key Management:** Uses Android Keystore for secure key storage and retrieval.
- **Chunked Processing:** Supports large data encryption and decryption in chunks to avoid memory issues.
- **Saving Api Key :** Using BuildConfig in the gradle.
  
## Encryption

### CipherManager Class

The `CipherManager` class provides methods for encrypting and decrypting data using AES encryption with CBC block mode and PKCS7 padding. Key management is handled securely using Android Keystore.

### Constants

- `CHUNK_SIZE`: Size of chunks used for processing data.
- `KEY_SIZE`: Size of the encryption key in bytes.
- `ALIAS`: Alias used for the encryption key in the Keystore.
- `ALGORITHM`, `BLOCK_MODE`, `PADDING`, `TRANSFORMATION`: Encryption algorithm and parameters.

## Token Management

### TokenManager Class

The `TokenManager` class securely manages tokens using Android's EncryptedSharedPreferences with AES256 encryption.

## Usage

### Encrypting Data

To encrypt data using `CipherManager`:

```kotlin
val cipherManager = CipherManager()
val encryptedData = cipherManager.encrypt(dataBytes, outputStream)
```
### Decrypting Data
To decrypt data using CipherManager:
```kotlin
val cipherManager = CipherManager()
val decryptedData = cipherManager.decrypt(inputStream)
```
### Encrypting Tokens
```kotlin
To encrypt tokens using TokenManager:


val tokenManager = TokenManager(context)
val encryptedToken = tokenManager.encryptToken(token)
```
### Decrypting Tokens
```kotlin
To decrypt tokens using TokenManager:

val tokenManager = TokenManager(context)
val decryptedToken = tokenManager.decryptToken()
```
