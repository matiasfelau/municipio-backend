package ar.edu.uade.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import io.ktor.server.application.*

class JWTService(application: Application) {
    private val secret = application.environment.config.property("jwt.secret").getString()

    fun validateToken(token: String): Boolean {
        return try {
            val algorithm = Algorithm.HMAC256(secret)
            val jwtVerifier: JWTVerifier = JWT.require(algorithm).build()
            val decodedJWT: DecodedJWT = jwtVerifier.verify(token)
            true
        } catch (jwtVerificationException: JWTVerificationException) {
            false
        }
    }
}
