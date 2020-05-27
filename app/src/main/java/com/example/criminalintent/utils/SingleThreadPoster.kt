package com.example.criminalintent.utils

import java.util.concurrent.Executors

object SingleThreadPoster {

    private val _singleThreadExecutor = Executors.newSingleThreadExecutor()

    fun post(command: () -> Unit) {
        _singleThreadExecutor.execute(command)
    }
}