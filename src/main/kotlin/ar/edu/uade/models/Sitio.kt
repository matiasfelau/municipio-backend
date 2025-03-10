package ar.edu.uade.models


import ar.edu.uade.utilities.BigDecimalComponentSerializer
import ar.edu.uade.utilities.LocalDateTimeComponentSerializable
import ar.edu.uade.utilities.LocalTimeComponentSerializer
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalTime

data class Sitio (
    val idSitio: Int?,
    @kotlinx.serialization.Serializable(with = BigDecimalComponentSerializer::class)
    val latitud: BigDecimal,
    @kotlinx.serialization.Serializable(with = BigDecimalComponentSerializer::class)
    val longitud: BigDecimal,
    val calle: String?,
    val numero: Int,
    val entreCalleA: String?,
    val entreCalleB: String?,
    val descripcion: String,
    val aCargoDe: String,
    @kotlinx.serialization.Serializable(with = LocalTimeComponentSerializer::class)
    val apertura: LocalTime,
    @kotlinx.serialization.Serializable(with = LocalTimeComponentSerializer::class)
    val cierre: LocalTime,
    val comentarios: String

): Serializable {
    object Sitios: Table() {
        val idSitio = integer("idSitio").autoIncrement()
        val latitud = decimal("latitud", 9, 5)
        val longitud = decimal("longitud", 9, 5)
        val calle = varchar("calle", 150).nullable()
        val numero = integer("numero")
        val entreCalleA = varchar("entreCalleA",150).nullable()
        val entreCalleB = varchar("entreCalleB",150).nullable()
        val descripcion = varchar("descripcion", 300)
        val aCargoDe = varchar("aCargoDe", 200)
        val apertura = time("apertura")
        val cierre = time("cierre")
        val comentarios = text("comentarios")
        override val primaryKey = PrimaryKey(idSitio)
    }
}
