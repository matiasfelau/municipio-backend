package ar.edu.uade.services

import ar.edu.uade.mappers.EmpleadoLoginRequest
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import ar.edu.uade.models.*
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import java.util.*

class EmpleadoJWTService (private val application: Application, private val empleadoService: EmpleadoService) {
    private val secret = getConfigProperty("jwt.secret")
    private val audience = getConfigProperty("jwt.audience")
    private val issuer = getConfigProperty("jwt.issuer")
    val realm = getConfigProperty("jwt.realm")
    val jwtVerifier: JWTVerifier = JWT.require(Algorithm.HMAC256(secret)).withAudience(audience).withIssuer(issuer).build()

    suspend fun createJwtToken(empleadoLoginRequest: EmpleadoLoginRequest): String? {
        val foundEmpleado: Empleado? = empleadoService.findEmpleadoByLegajo(empleadoLoginRequest.legajo)
        return if (foundEmpleado != null && empleadoLoginRequest.password == foundEmpleado.password)
            JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("legajo", empleadoLoginRequest.legajo)
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC256(secret))
        else
            null
    }
    suspend fun customValidator(credential: JWTCredential, empleadoJwtService: EmpleadoJWTService): JWTPrincipal? {
        val legajo: Int? = extractUsername(credential)
        val foundEmpleado: Empleado? = legajo?.let { empleadoService.findEmpleadoByLegajo(it) }
        return foundEmpleado?.let {
            if (audienceMatches(credential))
                JWTPrincipal(credential.payload)
            else
                null
        }
    }
    private fun audienceMatches(credential: JWTCredential, ): Boolean = credential.payload.audience.contains(audience)
    private fun getConfigProperty(path: String) = application.environment.config.property(path).getString()
    private fun extractUsername(credential: JWTCredential): Int? = credential.payload.getClaim("username").asInt()
}