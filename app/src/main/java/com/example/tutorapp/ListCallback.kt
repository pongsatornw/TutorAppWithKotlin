package com.example.tutorapp

interface ListCallback<in T: List<*>> {

    fun onCallback(data: T)

}
