package ar.edu.uade.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time
import java.util.Date

data class Sitio(
    val idSitio: Int,
    val latitud: Float,
    val longitud: Float,
    val calle: String?,
    val numero: Int,
    val entreCalleA: String?,
    val entreCalleB: String?,
    val descripcion: String,
    val aCargoDe: String,
    val apertura: Date,
    val cierre: Date,
    val comentarios: String
) {
    public object Sitios : Table() {
        val idSitio = integer("idSitio").autoIncrement()
        val latitud = decimal("latitud", 9, 5)
        val longitud = decimal("longitud", 9, 5)
        val calle = varchar("calle", 150)
        val numero = integer("numero")
        val entreCalleA = varchar("entreCalleA", 150)
        val entreCalleB = varchar("entreCalleB", 150)
        val descripcion = varchar("descripcion", 300)
        val aCargoDe = varchar("aCargoDe", 200)
        val apertura = time("apertura")
        val cierre = time("cierre")
        val comentarios = text("comentarios")
        override val primaryKey = PrimaryKey(idSitio)
    }
}
