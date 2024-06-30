package ar.edu.uade.models

import ar.edu.uade.utilities.LocalDateTimeComponentSerializable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

data class MovimientoDenuncia(
    val idMovimiento: Int,
    val responsable: String,
    val causa: String,
    @Serializable(with = LocalDateTimeComponentSerializable::class)
    val fecha: LocalDateTime,
    val idDenuncia: Int
): java.io.Serializable {
    public object MovimientosDenuncia: Table() {
        val idMovimiento = integer("idMovimiento").autoIncrement()
        val responsable = varchar("responsable", 150)
        val causa = varchar("causa", 4000)
        val fecha = datetime("fecha")
        val idDenuncia = integer("idDenuncia").references(Denuncia.Denuncias.idDenuncia)
        override val primaryKey = PrimaryKey(idMovimiento)
    }
}