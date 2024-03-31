package ar.edu.uade

import ar.edu.uade.models.Rubro.*
import ar.edu.uade.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect(url = "jdbc:mysql://localhost:3306/municipio", driver = "com.mysql.cj.jdbc.Driver", user = "root", password = "1234")
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
