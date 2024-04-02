package ar.edu.uade.plugins

import kotlinx.coroutines.*
import java.io.*
import ar.edu.uade.repository.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val rubroDao: RubroDAOFacade = RubroDAOFacadeCacheImpl(
        RubroDAOFacadeImpl(),
        File(environment.config.property("storage.ehcacheFilePath").getString())
    ).apply { }
    routing {
        get("/") {
            call.respondText(rubroDao.allRubros().first().descripcion.toString())
        }
    }
}
