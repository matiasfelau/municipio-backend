package ar.edu.uade.models

import org.jetbrains.exposed.sql.Table
import java.io.Serializable
import ar.edu.uade.models.Profesional.Profesionales

data class ImagenProfesional(
    val idImagen: Int,
    val urlImagen: String,
    val idProfesional: Int
): Serializable {
    object ImagenesProfesional: Table() {
        val idImagen = integer("idImagen").autoIncrement()
        val urlImagen = text("urlImagen")
        val idProfesional = integer("idProfesional").references(Profesionales.idProfesional)
        override val primaryKey = PrimaryKey(idImagen)
    }
}
