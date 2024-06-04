package ar.edu.uade.models

import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class ReclamoImagen(
    val urlImagen: String,
    val idReclamo: Int
): Serializable{
    object ReclamoImagenes: Table(){
        val urlImagen = varchar("urlImagen",255)
        val idReclamo = integer("idReclamo").references(Reclamo.Reclamos.idReclamo)
        override val primaryKey = PrimaryKey(urlImagen)
    }
}
