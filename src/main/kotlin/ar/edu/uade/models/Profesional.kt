package ar.edu.uade.models

import ar.edu.uade.mappers.LocalTimeComponentSerializer
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time
import java.io.Serializable
import java.time.LocalTime
//TODO CUANDO ES VACIO?
data class Profesional (
    val idProfesional: Int,
    val marca: String,
    val direccion: String?,
    val telefono: Int?,
    val email: String?,
    val latitud: Float?,
    val longitud: Float?,
    @kotlinx.serialization.Serializable(with = LocalTimeComponentSerializer::class)
    val inicioJornada: LocalTime?,
    @kotlinx.serialization.Serializable(with = LocalTimeComponentSerializer::class)
    val finJornada: LocalTime?,
    val documento: String?
):Serializable{
    public object Profesionales: Table() {
        val idProfesional = integer("idProfesional").autoIncrement()
        val marca = varchar("marca", 150)
        val direccion = varchar("direccion", 150)
        val telefono = integer("telefono")
        val email = varchar("email", 40)
        val latitud = decimal("latitud", 9, 5)
        val longitud = decimal("longitud", 9, 5)
        val inicioJornada = time("inicioJornada")
        val finJornada = time("finJornada")
        val documento = varchar("documento", 20).uniqueIndex().references(Vecino.Vecinos.documento)
        override val primaryKey = PrimaryKey(idProfesional)
    }
}