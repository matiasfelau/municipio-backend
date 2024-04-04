package ar.edu.uade.models
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class Reclamo(
    val idReclamo: Int,
    val documento: String,
    val idSitio: Int,
    val idDesperfecto: Int?,
    val descripcion: String?,
    val estado: String,
    val idReclamoUnificado: Int?
): Serializable {
    object Reclamos : Table() {
        val idReclamo = integer("idReclamo").autoIncrement()
        val documento = varchar("documento",20)
        val idSitio = integer("idSitio").uniqueIndex().references(Sitio.Sitios.idSitio)
        val idDesperfecto = integer("idDesperfecto").uniqueIndex().references(Desperfecto.Desperfectos.idDesperfecto)
        val descripcion = varchar("descripcion",1000)
        val estado = varchar("estado",30)
        val idReclamoUnificado = integer("idReclamoUnificado")
        override val primaryKey = PrimaryKey(idReclamo)
    }
}

