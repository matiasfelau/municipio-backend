package ar.edu.uade.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalTime

data class Profesional(
    val idProfesional: Int,
    val nombre: String,
    val rubro: String,
    val descripcion: String?,
    val direccion: String?,
    val telefono: Int?,
    val email: String?,
    val latitud: BigDecimal?,
    val longitud: BigDecimal?,
    val inicioJornada: LocalTime?,
    val finJornada: LocalTime?,
    val documento: String,
    val autorizado: Boolean
): Serializable {
    public object Profesionales: Table() {
        val idProfesional = integer("idProfesional").autoIncrement()
        val nombre = varchar("nombre", 150)
        val rubro = varchar("rubro", 40)
        val descripcion = text("descripcion").nullable()
        val direccion = varchar("direccion", 150).nullable()
        val telefono = integer("telefono").nullable()
        val email = varchar("email", 40).nullable()
        val latitud = decimal("latitud", 9, 5).nullable()
        val longitud = decimal("longitud", 9, 5).nullable()
        val inicioJornada = time("inicioJornada").nullable()
        val finJornada = time("finJornada").nullable()
        val documento = varchar("documento", 20)
        val autorizado = bool("autorizado").default(false)
        override val primaryKey = PrimaryKey(idProfesional)
    }
}
