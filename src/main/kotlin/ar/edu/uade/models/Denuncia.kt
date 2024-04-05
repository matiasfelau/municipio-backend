package ar.edu.uade.models

import ar.edu.uade.models.Barrio.Barrios.integer
import ar.edu.uade.models.Empleado.Personal.references
import org.jetbrains.exposed.sql.Table
import java.io.Serializable
import ar.edu.uade.models.Sitio.*

data class Denuncia(
    val idDenuncias: Int,
    val documento: String,
    val idSitio: Int,
    val descripcion:String,
    val estado:String,
    val aceptaResponsabilidad: Boolean
) : Serializable{
    public object Denuncias : Table(){
        val idDenuncias = integer("idDenuncias").autoIncrement()
        val documento = varchar("documento",40)
        val idSitio = integer("idSitio").uniqueIndex().references(Sitios.idSitio)
        val descripcion = varchar("descripcion",150)
        val estado = varchar("estado",40)
        val aceptaResponsabilidad = bool("aceptaResponsabilidad")
    }
}
