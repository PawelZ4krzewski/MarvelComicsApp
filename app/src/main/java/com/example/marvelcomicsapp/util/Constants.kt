package com.example.marvelcomicsapp.util

import java.sql.Timestamp

class Constants {

    companion object{
        const val URL = "http://gateway.marvel.com/v1/public/"
        const val PUBLIC_KEY = "615340260e5f8063dd68d61dd8b676e2"
        const val PRIVATE_KEY = "d288984e618e4e54177df9f44d52ed76f819157e"
        val ts = Timestamp(System.currentTimeMillis()).toString()


    }
}