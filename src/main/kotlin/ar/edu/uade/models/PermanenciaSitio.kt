package ar.edu.uade.models

import java.io.Serializable
import org.jetbrains.exposed.sql.Table
import ar.edu.uade.models.Sitio.Sitios

data class PermanenciaSitio(
    val idSitio: Int,
    val permanente: Boolean
):Serializable{
    object PermanenciaSitios: Table(){
        val idSitio = integer("idSitio").uniqueIndex().references(Sitios.idSitio)
        val permanente = bool("permanente")
        override val primaryKey = PrimaryKey(Sitios.idSitio)

    }
}
