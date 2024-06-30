package ar.edu.uade.models

import org.jetbrains.exposed.sql.Table
import java.io.Serializable
import ar.edu.uade.models.Comercio.Comercios

data class ComercioImagen (
    val idImagen: Int,
    val urlImagen: String,
    val idComercio: Int
): Serializable {
        object Comerciomagenes: Table(){
            val idImagen = integer("idImagen").autoIncrement()
            val urlImagen = varchar("urlImagen",255)
            val idComercio = integer("idComercio").references(Comercios.idComercio)
            override val primaryKey = PrimaryKey(urlImagen)
        }
    }
