package ar.edu.uade.models

import ar.edu.uade.models.DenunciaImagen.DenunciaImagenes.references
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

class ComercioImagen (
    val urlImagen: String,
    val idComercio: Int
    ): Serializable {
        object Comerciomagenes: Table(){
            val urlImagen = varchar("urlImagen",255)
            val idComercio = integer("idComercio").references(Comercio.Comercios.idComercio)
            override val primaryKey = PrimaryKey(urlImagen)
        }
    }
