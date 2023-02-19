package com.example.marvelcomicsapp.util

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

class Constants {

    companion object{
        const val BASE_URL = "https://gateway.marvel.com/v1/public/"
        const val PUBLIC_KEY = "615340260e5f8063dd68d61dd8b676e2"
        const val PRIVATE_KEY = "d288984e618e4e54177df9f44d52ed76f819157e"
        const val PAGE_SIZE = 20
        val ts = Timestamp(System.currentTimeMillis()).toString()

        fun hashMD5(): String {
            val input = "${ts}${PRIVATE_KEY}${PUBLIC_KEY}"
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32,'0')
        }

    }
}