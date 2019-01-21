package com.example.tutorapp

import kotlin.Exception

interface ListCallbackWithErrorHandler<in T: List<*>, in E : Exception> {

    fun onCallback(data: T)

    fun onErrorCallback(exception: E)

    fun onEmptyCallback()

}
