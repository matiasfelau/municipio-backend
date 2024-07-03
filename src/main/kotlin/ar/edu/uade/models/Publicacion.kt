package ar.edu.uade.models

import ar.edu.uade.utilities.LocalTimeComponentSerializer
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class Publicacion(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val autor: String,
    @kotlinx.serialization.Serializable(with = LocalTimeComponentSerializer::class)
    val fecha: String,
    val aprobado: Boolean
) : Serializable {
    public object Publicaciones : Table() {
        val id = integer("id").autoIncrement()
        val titulo = varchar("titulo", 255)
        val descripcion = text("descripcion")
        val autor = varchar("autor", 255)
        val fecha = varchar("fecha", 255)
        val aprobado = bool("aprobado")
        override val primaryKey = PrimaryKey(id)
    }
}