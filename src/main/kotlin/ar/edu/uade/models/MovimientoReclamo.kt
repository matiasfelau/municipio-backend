package ar.edu.uade.models

import ar.edu.uade.utilities.BigDecimalComponentSerializer
import kotlinx.serialization.Serializable as KotlinSerializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time
import java.time.LocalTime
import java.io.Serializable as JavaSerializable
import ar.edu.uade.models.Reclamo.Reclamos
import kotlinx.serialization.Serializable
import ar.edu.uade.utilities.LocalDateTimeComponentSerializable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


data class MovimientoReclamo(
    val idMovimiento: Int,
    val responsable: String,
    val causa: String,
    @KotlinSerializable(with = LocalDateTimeComponentSerializable::class)
    val fecha: LocalDateTime,
    val idReclamo: Int
): JavaSerializable {
    public object MovimientosReclamo: Table() {
        val idMovimiento = integer("idMovimiento").autoIncrement()
        val responsable = varchar("responsable", 150)
        val causa = varchar("causa", 4000)
        val fecha = datetime("fecha")
        val idReclamo = integer("idReclamo").references(Reclamos.idReclamo)
        override val primaryKey = PrimaryKey(idMovimiento)
    }
}
