package ar.edu.uade.mappers

import ar.edu.uade.models.PublicacionImagen
import kotlinx.serialization.Serializable

@Serializable
data class MapPublicacion(
    val titulo: String,
    val descripcion: String,
    val autor: String,
    val fecha: String,
    val fotos: List<String>
)