package ar.edu.uade.mappers

import kotlinx.serialization.Serializable

@Serializable
data class MapPublicacionImagen(val id: Int, val url: String, val idPublicacion: Int) {

}
