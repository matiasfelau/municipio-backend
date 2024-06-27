package ar.edu.uade.models

import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class DenunciaImagen(
    val urlImagen: String,
    val idDenuncia: Int
): Serializable {
    object DenunciaImagenes: Table(){
        val urlImagen = varchar("urlImagen",255)
        val idDenuncia = integer("idDenuncia").references(Denuncia.Denuncias.idDenuncia)
        override val primaryKey = PrimaryKey(urlImagen)
    }
}
