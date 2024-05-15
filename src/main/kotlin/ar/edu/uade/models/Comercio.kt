package ar.edu.uade.models

import ar.edu.uade.mappers.LocalTimeComponentSerializer
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time
import java.io.Serializable
import java.time.LocalTime
//TODO CUANDO ES VACIO?
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
    val latitud: Float?,
    val longitud: Float?,
    val documento: String

):Serializable{
    public object Comercios: Table(){
        val idComercio = integer("idComercio").autoIncrement()
        val nombre = varchar("nombre",250)
        val apertura = time("apertura")
        val cierre = time("cierre")
        val direccion = varchar("direccion",250)
        val telefono = integer("telefono")
        val descripcion = varchar("descripcion",150)
        val latitud = decimal("latitud",9,5)
        val longitud = decimal("longitud",9,5)
        val documento = varchar("documento",20).uniqueIndex().references(Vecino.Vecinos.documento)
        override val primaryKey = PrimaryKey(idComercio)
    }
}