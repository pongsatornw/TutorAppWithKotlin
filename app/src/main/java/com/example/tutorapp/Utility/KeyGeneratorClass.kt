package com.example.user.tutorapp.Utility

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.*
import javax.crypto.Cipher

class KeyGeneratorClass: AppCompatActivity(){

    private lateinit var pub_Key: PublicKey
    private lateinit var pri_key: PrivateKey

    fun createKey(){
        val keyPairGenerator = KeyPairGenerator.getInstance(
                "RSA")
        val secureRandom = SecureRandom.getInstance("RSA")
        keyPairGenerator.initialize(1024, secureRandom)
        //val messageDigest = MessageDigest.getInstance("SHA256")

        //val digest = messageDigest.digest("string".toByteArray(StandardCharsets.UTF_8))

        keyPairGenerator.initialize(1024)
        val keyPairr = keyPairGenerator.genKeyPair()

        pri_key = keyPairr.private
        pub_Key = keyPairr.public

        val pri_key_encoded = keyPairr.private.encoded
        val pub_key_encoded = keyPairr.public.encoded

        val pri_key_str = Base64.encodeToString(pri_key_encoded, Base64.DEFAULT)
        val pub_key_str = Base64.encodeToString(pub_key_encoded, Base64.DEFAULT)


        Log.d("PublicKey: ", "$pub_key_str")
        Log.d("PrivateKey: ", "$pri_key_str")

    }

    private fun encryptData(privateKey: PrivateKey, plaintext: String): ByteArray {
        val cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, privateKey)

        val cipherResult = cipher.doFinal(plaintext.toByteArray())
        Log.d("Encrypt", String(cipherResult, Charsets.UTF_8))

        return cipherResult
    }

    private fun decryptData(publicKey: PublicKey, cipherByteArray: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING")
        cipher.init(Cipher.DECRYPT_MODE, publicKey)

        val plainResult = cipher.doFinal(cipherByteArray)
        Log.d("Decrypt", String(plainResult, Charsets.UTF_8))

        return plainResult
    }

}