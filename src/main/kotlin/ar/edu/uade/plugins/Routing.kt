package ar.edu.uade.plugins

import ar.edu.uade.routes.*
import ar.edu.uade.services.*
import ar.edu.uade.utilities.CloudinaryConfig
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    denunciaService: DenunciaService,
    credencialService: CredencialService,
    credencialJWTService: CredencialJWTService,
    empleadoService: EmpleadoService,
    empleadoJWTService: EmpleadoJWTService,
    vecinoService: VecinoService,
    jwtService: JWTService,
    reclamoService: ReclamoService,
    desperfectoService: DesperfectoService,
    sitioService: SitioService,
    rubroService: RubroService,
    cloudinaryConfig: CloudinaryConfig,
    documentoTokenService: DocumentoTokenService,
    movimientoReclamoService: MovimientoReclamoService,
    profesionalService: ProfesionalService
    ) {
    routing {
        get("/") {
            call.respondText("")
        }
        credencialRouting(credencialService, credencialJWTService)
        empleadoRouting(empleadoService, empleadoJWTService)
        vecinoRouting(vecinoService, credencialService)
        reclamoRouting(jwtService, reclamoService, movimientoReclamoService, cloudinaryConfig)
        desperfectoRouting(jwtService,desperfectoService)
        rubroRouting(jwtService,rubroService)
        sitioRouting(jwtService,sitioService)
        documentoTokenRouting(documentoTokenService)
        denunciaRouting(jwtService,denunciaService,vecinoService,cloudinaryConfig)
        profesionalRouting(profesionalService, jwtService)
    }
}
