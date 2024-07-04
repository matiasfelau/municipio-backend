package ar.edu.uade.models

import ar.edu.uade.mappers.responses.PublicacionResponse
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class PublicacionImagen (
    val id: Int?,
    val url: String,
    val idPublicacion: Int
): Serializable{
    object PublicacionImagenes: Table(){
        val id = integer("id").autoIncrement();
        val url = varchar("url", 255);
        val idPublicacion = integer("idPublicacion").references((Publicacion.Publicaciones.id));
        override val primaryKey = PrimaryKey(id);
    }
}