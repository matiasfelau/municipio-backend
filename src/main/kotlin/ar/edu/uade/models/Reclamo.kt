package ar.edu.uade.models

import ar.edu.uade.models.Vecino.Vecinos
import ar.edu.uade.models.Sitio.*
import java.io.Serializable
import org.jetbrains.exposed.sql.Table
import ar.edu.uade.models.Desperfecto.*

data class Reclamo(
    val idReclamo: Int,
    val descripcion: String?,
    val estado: String,
    val documento: String,
    val idSitio: Int,
    val idDesperfecto: Int,
    val idReclamoUnif: Int?

): Serializable{
    public object Reclamos : Table() {
        val idReclamo = integer("idReclamo").autoIncrement()
        val descripcion = varchar("descripcion",1000)
        val estado = varchar("estado",3)
        val documento = varchar("documento",20).uniqueIndex().references(Vecinos.documento)
        val idSitio = integer("idSitio").uniqueIndex().references(Sitios.idSitio)
        val idDesperfecto = integer("idDesperfecto").uniqueIndex().references(Desperfectos.idDesperfecto)
        val idReclamoUnif = integer("idReclamoUnif").uniqueIndex().references(Reclamos.idReclamo)
        override val primaryKey = PrimaryKey(idReclamo)
    }
}
