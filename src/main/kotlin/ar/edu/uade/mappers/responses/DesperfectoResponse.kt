package ar.edu.uade.mappers.responses

import kotlinx.serialization.Serializable

@Serializable
data class DesperfectoResponse(
    val idDesperfecto: Int,
    val descripcion: String,
    val idRubro: Int?
)
