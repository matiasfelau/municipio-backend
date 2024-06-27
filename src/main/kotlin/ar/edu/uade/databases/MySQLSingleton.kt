package ar.edu.uade.databases

import ar.edu.uade.models.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object MySQLSingleton {
    fun init(config: ApplicationConfig) {
        val database = Database.connect(
            createHikariDataSource(
                config.property("storage.jdbcURL").getString(),
                config.property("storage.driverClassName").getString(),
                config.property("storage.user").getString(),
                config.property("storage.password").getString()
            )
        )
        transaction(database) {
            SchemaUtils.create(Credencial.Credenciales)
            SchemaUtils.create(Empleado.Personal)
            SchemaUtils.create(Reclamo.Reclamos)
            SchemaUtils.create(PermanenciaSitio.PermanenciaSitios)
            SchemaUtils.create(ReclamoImagen.ReclamoImagenes)
            SchemaUtils.create(MovimientoReclamo.MovimientosReclamo)
            SchemaUtils.create(DocumentoToken.TDocumentoToken)
            SchemaUtils.create(Denuncia.Denuncias)
            SchemaUtils.create(DenunciaImagen.DenunciaImagenes)
            SchemaUtils.create(VecinoDenunciado.VecinosDenunciados)
            SchemaUtils.create(ComercioDenunciado.ComerciosDenunciados)
            SchemaUtils.create(Comercio.Comercios)
            //TODO IMPLEMENTAR MOVIMIENTO DENUNCIAS
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun createHikariDataSource(
        url: String,
        driver: String,
        user: String,
        pw: String
    ) = HikariDataSource(HikariConfig().apply {
        jdbcUrl = url
        driverClassName = driver
        username = user
        password = pw
        maximumPoolSize = 10
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })
}
