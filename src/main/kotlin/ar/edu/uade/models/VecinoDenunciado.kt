package ar.edu.uade.models

import ar.edu.uade.models.Vecino.Vecinos
import org.jetbrains.exposed.sql.Table
import java.io.Serializable


data class VecinoDenunciado(
    var idDenuncia: Int?,
    var documento: String?,
    val direccion: String,
    val nombre: String,
    val apellido: String
): Serializable{
    public object VecinosDenunciados: Table(){
        val id = integer("id").autoIncrement()
        val idDenuncia = integer("id_denuncia").references(Denuncia.Denuncias.idDenuncia)
        val documento = varchar("documento", 40).references(Vecinos.documento).nullable()
        val direccion = varchar("direccion", 150)
        val nombre = varchar("nombre", 150)
        val apellido = varchar("apellido", 150)
        override val primaryKey = PrimaryKey(id)
    }
}