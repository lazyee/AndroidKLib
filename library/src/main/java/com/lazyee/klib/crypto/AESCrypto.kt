package com.lazyee.klib.crypto

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * ClassName: AesEncrypt
 * Description:
 * Date: 2025/4/11 11:56
 * @author Leeorz
 */
class AESCrypto : Crypto {
    private val encryptCipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    private val decryptCipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    private var iv: ByteArray = ByteArray(16)
    private var secretKey: String
    private var base64Flags: Int = Base64.NO_WRAP

    constructor(secretKey: String){
        this.secretKey = secretKey
        init()
    }

    constructor(secretKey: String, iv: String){
        this.secretKey = secretKey
        this.iv = iv.toByteArray(Charsets.UTF_8)
        init()
    }
    constructor(secretKey: String,iv: ByteArray){
        this.secretKey = secretKey
        this.iv = iv

        init()
    }

    private fun init() {
        val ivSpec = IvParameterSpec(iv)
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "AES")
        encryptCipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivSpec)
        decryptCipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ivSpec)
    }


    fun setBase64Flags(base64Flags: Int): AESCrypto {
        this.base64Flags = base64Flags
        return this
    }


    override fun encrypt(plainText: String): String? {
        try {
            val ciphertext  = encryptCipher.doFinal(plainText.toByteArray())
            val encodedCiphertext = Base64.encodeToString(ciphertext, base64Flags)
            return encodedCiphertext
        }catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }

    override fun decrypt(cipherText: String): String? {
        try {
            val decodedCiphertext = Base64.decode(cipherText, base64Flags)
            val plaintext  = decryptCipher.doFinal(decodedCiphertext)
            return String(plaintext)
        }catch (e: Exception){
            e.printStackTrace()
        }

        return null
    }
}