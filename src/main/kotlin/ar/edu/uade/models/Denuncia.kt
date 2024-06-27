package ar.edu.uade.models
import ar.edu.uade.models.Vecino.Vecinos
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class Denuncia (
    val idDenuncia: Int,
    val descripcion: String?,
    val estado: String?,
    val aceptarResponsabilidad: Boolean,
    val documento: String
    ): Serializable {
    public object Denuncias : Table() {
        val idDenuncia = integer("idDenuncia").autoIncrement()
        val descripcion = varchar("descripcion",1000).nullable()
        val estado = varchar("estado",30).nullable()
        val aceptarResponsabilidad =  bool("aceptarResponsabilidad")
        val documento = varchar("documento",40).references(Vecinos.documento)
        override val primaryKey = PrimaryKey(idDenuncia)
    }
    }