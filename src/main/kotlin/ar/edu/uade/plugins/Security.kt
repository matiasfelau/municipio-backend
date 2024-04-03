package ar.edu.uade.plugins

import ar.edu.uade.services.EmpleadoJWTService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
fun Application.configureSecurity(empleadoJwtService: EmpleadoJWTService) {
    authentication {
        jwt {
            realm = empleadoJwtService.realm
            verifier(empleadoJwtService.jwtVerifier)
            validate { credential -> empleadoJwtService.customValidator(credential, empleadoJwtService)
            }
        }
    }
}