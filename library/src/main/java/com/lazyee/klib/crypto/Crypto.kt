package com.lazyee.klib.crypto

/**
 * ClassName: Encrypt
 * Description:
 * Date: 2025/4/11 11:55
 * @author Leeorz
 */
interface Crypto {
    fun decrypt(cipherText: String): String?
    fun encrypt(plainText: String): String?
}