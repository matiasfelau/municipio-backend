package ar.edu.uade.models

import ar.edu.uade.models.Vecino.Vecinos
import ar.edu.uade.models.VecinoDenunciado.VecinosDenunciados.autoIncrement
import ar.edu.uade.models.VecinoDenunciado.VecinosDenunciados.references
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class ComercioDenunciado(
    val id: Int,
    val idComercio: Int,
    val idDenuncia: Int,
    val nombre: String,
    val direccion: String
): Serializable {
    public object ComerciosDenunciados: Table(){
        val id = integer("id").autoIncrement()
        val idComercio = integer("idComercio").references(Comercio.Comercios.idComercio)
        val idDenuncia = integer("idDenuncia").references(Denuncia.Denuncias.idDenuncia)
        val nombre = varchar("nombre", 150)
        val direccion = varchar("direccion", 150)
        override val primaryKey = PrimaryKey(id)
    }
}