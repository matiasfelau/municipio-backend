package ar.edu.uade.models

import ar.edu.uade.models.Vecino.Vecinos
import ar.edu.uade.models.Sitio.*
import java.io.Serializable
import org.jetbrains.exposed.sql.Table
import ar.edu.uade.models.Desperfecto.*
import ar.edu.uade.models.Empleado.Personal

data class Reclamo(
    val idReclamo: Int?,
    val descripcion: String?,
    val estado: String,
    val documento: String,
    val idSitio: Int,
    val idDesperfecto: Int,
    val idReclamoUnif: Int?

): Serializable{
    public object Reclamos : Table() {
        val idReclamo = integer("idReclamo").autoIncrement()
        val descripcion = varchar("descripcion",1000).nullable()
        val estado = varchar("estado",30)
        val documento = varchar("documento",40).references(Vecinos.documento).references(Personal.documento)
        val idSitio = integer("idSitio").references(Sitios.idSitio)
        val idDesperfecto = integer("idDesperfecto").references(Desperfectos.idDesperfecto)
        val idReclamoUnif = integer("idReclamoUnif").references(Reclamos.idReclamo).nullable()
        override val primaryKey = PrimaryKey(idReclamo)
    }
}
