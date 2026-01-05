package com.lazyee.klib.util

import android.util.Base64
import com.lazyee.klib.extension.hexToBytes
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec




/**
 * ClassName: EncryptUtils
 * Description:加密工具
 * Date: 2025/4/11 11:29
 * @author Leeorz
 */
object EncryptUtils {

    fun encrypt(content: String,secretKey: String): String {

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        //        val ivSpec = IvParameterSpec("348cae7c4b02b3d1".toByteArray())
        val ivSpec = IvParameterSpec(ByteArray(16))
        // 将密钥字符串转换为字节数组
        val keyBytes: ByteArray = secretKey.toByteArray()
        val key = SecretKeySpec(keyBytes, "AES")
        cipher.init(Cipher.ENCRYPT_MODE,key,ivSpec)
        val ciphertext  = cipher.doFinal(content.toByteArray(Charsets.UTF_8))
        val encodedCiphertext = Base64.encodeToString(ciphertext,Base64.NO_WRAP)
        println("加密后的字符串为: $encodedCiphertext")
        return encodedCiphertext
    }

    fun decrypt(content: String,secretKey: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val ivSpec = IvParameterSpec("348cae7c4b02b3d1".toByteArray())
//        val ivSpec = IvParameterSpec(ByteArray(16))
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES"), ivSpec)
        val decodedCiphertext = Base64.decode(content, Base64.NO_WRAP)
        val plaintext  = cipher.doFinal(decodedCiphertext)
        println("解密后的字符串为: ${String(plaintext)}")
        return String(plaintext)
    }

}