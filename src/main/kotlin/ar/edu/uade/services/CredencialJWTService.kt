package ar.edu.uade.services

import ar.edu.uade.mappers.requests.CredencialRequest
import ar.edu.uade.models.Credencial
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import java.util.*

class CredencialJWTService (private val application: Application, private val credencialService: CredencialService) {
    private val secret = getConfigProperty("jwt.secret")
    private val audience = getConfigProperty("jwt.audience")
    private val issuer = getConfigProperty("jwt.issuer")
    val realm = getConfigProperty("jwt.realm")
    val jwtVerifier: JWTVerifier = JWT.require(Algorithm.HMAC256(secret)).withAudience(audience).withIssuer(issuer).build()

    suspend fun createJwtToken(credencialRequest: CredencialRequest): String? {
        val foundCredencial: Credencial? = credencialService.find(credencialRequest.documento)
        return if (foundCredencial != null && credencialRequest.password == foundCredencial.password)
            JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("documento", credencialRequest.documento)
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC256(secret))
        else
            null
    }
    suspend fun customValidator(credential: JWTCredential, credencialJWTService: CredencialJWTService): JWTPrincipal? {
        val documento: String? = extractUsername(credential)
        val foundCredencial: Credencial? = documento?.let { credencialService.find(it) }
        return foundCredencial?.let {
            if (audienceMatches(credential))
                JWTPrincipal(credential.payload)
            else
                null
        }
    }
    private fun audienceMatches(credential: JWTCredential, ): Boolean = credential.payload.audience.contains(audience)
    private fun getConfigProperty(path: String) = application.environment.config.property(path).getString()
    private fun extractUsername(credential: JWTCredential): String? = credential.payload.getClaim("username").asString()
}