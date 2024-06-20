package ar.edu.uade.utilities

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message

fun sendNotificationToDevice(token: String, title: String, body: String) {
    val message = Message.builder()
        .putData("title", title)
        .putData("body", body)
        .setToken(token)
        .build()

    try {
        val response = FirebaseMessaging.getInstance().send(message)
        println("Successfully sent message: $response")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}