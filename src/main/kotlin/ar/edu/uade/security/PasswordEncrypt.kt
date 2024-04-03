package ar.edu.uade.security

import io.ktor.server.config.*
import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

var SECRET_KEY = ""
val ALGORITHM = "HmacSHA1"

val HASH_KEY = hex(SECRET_KEY)
val HMAC_KEY = SecretKeySpec(HASH_KEY, ALGORITHM)

fun getSecuritySecretKey(config: ApplicationConfig) {
    SECRET_KEY = config.property("encrypt.secret_key").getString()
}

fun hash(password: String): String {
    val hmac = Mac.getInstance(ALGORITHM)
    hmac.init(HMAC_KEY)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}