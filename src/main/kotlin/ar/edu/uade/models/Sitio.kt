package ar.edu.uade.models

import ar.edu.uade.utilities.LocalDateTimeComponentSerializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time
import java.io.Serializable
import java.time.LocalDateTime

data class Sitio (
    val idSitio: Int,
    val latitud: Float,
    val longitud: Float,
    val calle: String?,
    val numero: Int,
    val entreCalleA: String?,
    val entreCalleB: String?,
    val descripcion: String,
    val aCargoDe: String,
    @kotlinx.serialization.Serializable(with = LocalDateTimeComponentSerializable::class)
    val apertura: LocalDateTime,
    @kotlinx.serialization.Serializable(with = LocalDateTimeComponentSerializable::class)
    val cierre: LocalDateTime,
    val comentarios: String

): Serializable {
    public object Sitios: Table() {
        val idSitio = integer("idSitio").autoIncrement()
        val latitud = decimal("latitud", 9, 5)
        val longitud = decimal("longitud", 9, 5)
        val calle = varchar("calle", 150)
        val numero = integer("numero")
        val entreCalleA = varchar("entreCalleA",150)
        val entreCalleB = varchar("entreCalleB",150)
        val descripcion = varchar("descripcion", 300)
        val aCargoDe = varchar("aCargoDe", 200)
        val apertura = time("apertura")
        val cierre = time("cierre")
        val comentarios = text("comentarios")
    }
}
