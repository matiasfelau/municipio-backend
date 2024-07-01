package ar.edu.uade

import ar.edu.uade.plugins.*
import io.ktor.server.application.*
import ar.edu.uade.databases.MySQLSingleton
import ar.edu.uade.services.*
import ar.edu.uade.utilities.CloudinaryConfig
import ar.edu.uade.utilities.Delay
import ar.edu.uade.utilities.sendNotificationToDevice
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.FileInputStream

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    MySQLSingleton.init(environment.config)
    val credencialService = CredencialService()
    val comercioService = ComercioService(environment.config)
    val credencialJWTService = CredencialJWTService(this, credencialService)
    val empleadoService = EmpleadoService(environment.config)
    val empleadoJWTService = EmpleadoJWTService(this, empleadoService)
    val vecinoService = VecinoService(environment.config)
    val jwtService = JWTService(this)
    val reclamoService = ReclamoService(environment.config)
    val desperfectoService = DesperfectoService(environment.config)
    val rubroService = RubroService(environment.config)
    val sitioService = SitioService(environment.config)
    val movimientoReclamoService = MovimientoReclamoService()
    val cloudinary = CloudinaryConfig(this)
    val documentoToken = DocumentoTokenService()
    val denunciaService = DenunciaService(environment.config)
    val profesionalService = ProfesionalService()
    val movimientoDenunciaService = MovimientoDenunciaService()
    configureSecurity(credencialJWTService, empleadoJWTService)
    configureRouting(
        denunciaService,
        comercioService,
        credencialService,
        credencialJWTService,
        empleadoService,
        empleadoJWTService,
        vecinoService,
        jwtService,
        reclamoService,
        desperfectoService,
        sitioService,
        rubroService,
        cloudinary,
        documentoToken,
        movimientoReclamoService,
        movimientoDenunciaService,
        profesionalService
        )
    val serviceAccount = FileInputStream("src/main/resources/firebase/serviceAccountKey.json")
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(options)
    launch {
        llamarCheckChanges(movimientoReclamoService,reclamoService,documentoToken,movimientoDenunciaService,denunciaService)
    }
}

suspend fun llamarCheckChanges(movimientoReclamoService: MovimientoReclamoService, reclamoService: ReclamoService, documentoToken: DocumentoTokenService, movimientoDenunciaService: MovimientoDenunciaService, denunciaService: DenunciaService){
    while (true) {
        delay(Delay.DELAY)
        checkDenunciasChanges(movimientoDenunciaService,denunciaService,documentoToken)
        checkReclamoChanges(movimientoReclamoService,reclamoService,documentoToken)
    }
}

suspend fun checkDenunciasChanges(movimientoDenunciaService: MovimientoDenunciaService, denunciaService: DenunciaService, documentoToken: DocumentoTokenService) {
        println("INICIO DEL DENUNCIA CHANGES...")
        val changes = movimientoDenunciaService.getByDelay()
        if (changes.isNotEmpty()) {
            for (change in changes) {
                val denuncia = denunciaService.getDenunciaById(change.idDenuncia)

                if (denuncia != null) {
                    println(denuncia.documento)
                    val token = documentoToken.getToken(denuncia.documento)?.token
                    if (token != null) {
                        println("ENVIANDO MENSAJE DE FIREBASE...")
                        sendNotificationToDevice(
                            token,
                            "Se actualizó una denuncia.",
                            "La denuncia ${denuncia.idDenuncia} cambió su estado a ${denuncia.estado}")
                    }
                }
            }
        }
}

suspend fun checkReclamoChanges(movimientoReclamoService: MovimientoReclamoService, reclamoService: ReclamoService, documentoTokenService: DocumentoTokenService) {
        val changes = movimientoReclamoService.getByDelay()
        if (changes.isNotEmpty()) {
            for (change in changes) {
                val reclamo = reclamoService.getReclamoById(change.idReclamo)
                if (reclamo != null) {
                    println(reclamo.documento)
                    val token = documentoTokenService.getToken(reclamo.documento)?.token
                    if (token != null) {
                        sendNotificationToDevice(
                            token,
                            "Se actualizó un reclamo.",
                            "El reclamo ${reclamo.idReclamo} cambió su estado a ${reclamo.estado}")
                    }
                }
            }
        }
}
