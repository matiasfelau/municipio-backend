package ar.edu.uade.models

import ar.edu.uade.models.Denuncia.Denuncias
import ar.edu.uade.models.Denuncia.Denuncias.references
import ar.edu.uade.models.Desperfecto.Desperfectos.references
import ar.edu.uade.models.Vecino.Vecinos
import org.jetbrains.exposed.sql.Table
import java.io.Serializable


data class VecinoDenunciado(
    val id: Int,
    val idDenuncia: Int,
    val documento: String?,
    val direccion: String,
    val nombre: String,
    val apellido: String
): Serializable{
    public object VecinosDenunciados: Table(){
        val id = integer("id").autoIncrement()
        val idDenuncia = integer("id_denuncia").references(Denuncia.Denuncias.idDenuncia)
        val documento = varchar("documento", 20).references(Vecinos.documento)
        val direccion = varchar("direccion", 150)
        val nombre = varchar("nombre", 150)
        val apellido = varchar("apellido", 150)
        override val primaryKey = PrimaryKey(id)
    }
}