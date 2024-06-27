package ar.edu.uade.models

import ar.edu.uade.utilities.BigDecimalComponentSerializer
import ar.edu.uade.utilities.LocalTimeComponentSerializer
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalTime

data class Comercio(
    val idComercio: Int,
    val nombre: String,
    @kotlinx.serialization.Serializable(with = LocalTimeComponentSerializer::class)
    val apertura: LocalTime?,
    @kotlinx.serialization.Serializable(with = LocalTimeComponentSerializer::class)
    val cierre: LocalTime?,
    val direccion: String?,
    val telefono: Int?,
    val descripcion: String?,
    @kotlinx.serialization.Serializable(with = BigDecimalComponentSerializer::class)
    val latitud: BigDecimal?,
    @kotlinx.serialization.Serializable(with = BigDecimalComponentSerializer::class)
    val longitud: BigDecimal?,
    val documento: String?
): Serializable{
    public object Comercios: Table(){
        val idComercio = integer("idComercio").autoIncrement()
        val nombre = varchar("nombre",250)
        val apertura = time("apertura").nullable()
        val cierre = time("cierre").nullable()
        val direccion = varchar("direccion",150).nullable()
        val telefono = integer("telefono").nullable()
        val descripcion = varchar("descripcion", 255).nullable()
        val latitud = decimal("latitud",9,5).nullable()
        val longitud = decimal("longitud",9,5).nullable()
        val documento = varchar("documento",20).nullable()
    }
}