package ar.edu.uade.plugins

import ar.edu.uade.services.CredencialJWTService
import ar.edu.uade.services.EmpleadoJWTService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
fun Application.configureSecurity(credencialJWTService: CredencialJWTService, empleadoJWTService: EmpleadoJWTService) {
    authentication {
        jwt {
            realm = credencialJWTService.realm
            verifier(credencialJWTService.jwtVerifier)
            validate { credential -> credencialJWTService.customValidator(credential, credencialJWTService) }
            realm = empleadoJWTService.realm
            verifier(empleadoJWTService.jwtVerifier)
            validate { credential -> empleadoJWTService.customValidator(credential, empleadoJWTService) }
        }
    }
}